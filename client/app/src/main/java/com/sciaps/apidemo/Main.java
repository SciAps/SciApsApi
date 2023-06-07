package com.sciaps.apidemo;

import javax.swing.*;

import com.sciaps.AnalysisResult;
import com.sciaps.AnalyticalModel;
import com.sciaps.ChemInfo;
import com.sciaps.InstrumentId;
import com.sciaps.XAcquisitionResult;
import com.sciaps.XAcquisitionSettings;
import com.sciaps.XBeamSettings;
import com.sciaps.XCalibration;
import com.sciaps.XFactoryAcquisitionSettings;
import com.sciaps.XInstrumentConfig;
import com.sciaps.XInstrumentStatus;
import com.sciaps.XSpectrum;
import com.sciaps.XTestResult;
import com.sciaps.ZAcquisitionMetadata;
import com.sciaps.ZAcquisitionResult;
import com.sciaps.ZAcquisitionSettings;
import com.sciaps.ZCalibration;
import com.sciaps.ZFactoryAcquisitionSettings;
import com.sciaps.ZInstrumentConfig;
import com.sciaps.ZInstrumentStatus;
import com.sciaps.ZTestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {

    public static Logger LOGGER = LoggerFactory.getLogger("ApiDemo");

    private static final String DEFAULT_IP_ADDRESS = "192.168.42.129:8080";

    public static void main(String[] args) {
        String defaultAddress = args.length >= 1 ? args[0] : DEFAULT_IP_ADDRESS;
        String command = args.length >= 2 ? args[1] : "";

        boolean isAbort = command.equalsIgnoreCase("--abort");
        String[] tokens = command.split(":");
        String mode = tokens.length > 0 ? tokens[0] : null;
        String modelName = tokens.length > 1 ? tokens[1] : null;

        String ipAddress = defaultAddress;
        boolean promptUser = !isAbort && !(command.equalsIgnoreCase("--go") || !isEmptyString(mode));
        if (promptUser) {
            ipAddress = JOptionPane.showInputDialog("Enter Analyzer IP Address", ipAddress);
        }

        if (isEmptyString(ipAddress)) {
            LOGGER.error("Must specify Analyzer IP Address");
            return;
        }

        SciApsClient client = new SciApsClient(ipAddress);
        if (isAbort) {
            LOGGER.info("Aborting command");
            client.abort();
            return;
        }

        InstrumentId id = client.getInstrumentId();
        LOGGER.info("Found {} instrument {} at {}", id.family, id.id, ipAddress);
        LOGGER.info("Model number: {}", id.model);
        LOGGER.info("Part number: {}", id.partNumber);
        LOGGER.info("software version: {}", id.swVersion);
        LOGGER.info("home version: {}", id.homeVersion);
        LOGGER.info("service version: {}", id.serviceVersion);
        LOGGER.info("PIC version: {}", id.picVersion);
        LOGGER.info("OS version: {}", id.osVersion);
        LOGGER.info("Available apps: {}", id.apps);
        LOGGER.info("Available models: ");
        for (AnalyticalModel model : id.models) {
            LOGGER.info("  {}:{}", model.mode, model.modelName);
        }

        boolean isXDevice = id.family.equalsIgnoreCase("XRF");
        if (isXDevice) {
            XInstrumentConfig config = client.getXInstrumentConfig();
            LOGGER.info("Detector Type: {}", config.detectorType);
            LOGGER.info("Tube Type: {}", config.tubeType);
            LOGGER.info("DPP Firmware: {}", config.dppVersion);
            LOGGER.info("Shutter Installed: {}", config.isShutterInstalled);
            LOGGER.info("Filter Wheel Installed: {}", config.isFilterWheelInstalled);

            XInstrumentStatus status = client.getXInstrumentStatus();
            LOGGER.info("Battery level: {}%, is charging: {}", status.batteryLevel, status.isCharging);
            LOGGER.info("Wifi SSID: {}, signal level: {}", status.wifiSSID, status.wifiLevel);
            LOGGER.info("Location: {}, {}", status.latitude, status.longitude);
            LOGGER.info("Energy Calibration needed: {}", status.isECalNeeded);
            LOGGER.info("Detector temp: {} degC", status.detectorTemp);
            LOGGER.info("Tube temp: {} degC", status.tubeTemp);

            if (!isEmptyString(mode)) {
                if (!isEmptyString(modelName)) {
                    for (AnalyticalModel model : id.models) {
                        if (model.mode.equals(mode) && modelName.equals(modelName)) {
                            LOGGER.info(" -- Running {} Test with model {} -- ", mode, modelName);
                            XTestResult testResult = client.runXTest(mode, modelName, null, false);
                            LOGGER.info("Test completed, status: {}, errorCode: {}, abortedByUser: {}",
                                        testResult.status, testResult.errorCode, testResult.abortedByUser);
                            printTestResult(testResult.testData);
                            printXSpectra(testResult.spectra);
                            return;
                        }
                    }
                } else {
                    for (String app : id.apps) {
                        if (app.equals(mode)) {
                            LOGGER.info(" -- Running {} Test -- ", mode);
                            XTestResult testResult = client.runXTest(mode, null, null, false);
                            LOGGER.info("Test completed, status: {}, errorCode: {}, abortedByUser: {}",
                                        testResult.status, testResult.errorCode, testResult.abortedByUser);
                            printTestResult(testResult.testData);
                            printXSpectra(testResult.spectra);
                            return;
                        }
                    }
                }
            }

            if (status.isECalNeeded) {
                LOGGER.info(" -- Running Energy Calibration -- ");
                client.runEnergyCalibration();
            }
            XCalibration calibration = client.getXCalibration();
            LOGGER.info("Energy calibration slope: {}, offset: {}", calibration.slope, calibration.offset);

            mode = id.apps.get(0);
            XAcquisitionSettings userSettings = client.getXAcquisitionSettings(mode);
            LOGGER.info(" -- UserSettings -- for {}", mode);
            LOGGER.info("testType: {}, enabledBeams: {}, beamTimes: {}",
                    userSettings.testType, userSettings.beamEnabledFlags, userSettings.beamTimes);

            userSettings.testType = 4;  // Two Beam Test
            client.setXAcquisitionSettings(mode, userSettings);

            LOGGER.info(" -- Running {} Test -- ", mode);
            XTestResult testResult = client.runXTest(mode, null, userSettings, true);
            LOGGER.info("Test completed, status: {}, errorCode: {}, abortedByUser: {}",
                    testResult.status, testResult.errorCode, testResult.abortedByUser);
            printTestResult(testResult.testData);
            printXSpectra(testResult.spectra);

            XFactoryAcquisitionSettings factorySettings = client.getXFactoryAcquisitionSettings(mode);
            LOGGER.info(" -- FactorySettings -- ");
            LOGGER.info("testType: {}", factorySettings.testType);
            for (XBeamSettings beam : factorySettings.beams) {
                LOGGER.info("Beam name: {}, time: {}ms voltage: {}keV, current: {}uA, filterPosition: {}, rates(set/low/max): {}/{}/{}",
                        beam.name, beam.beamTimeMs, beam.voltage, beam.current, beam.filterPosition,
                        beam.setCountRate, beam.lowCountRate, beam.maxCountRate);
            }

            factorySettings.beams.get(0).beamTimeMs = 3000;
            factorySettings.beams.get(1).beamTimeMs = 6000;
            factorySettings.beams.get(2).beamTimeMs = 6000;
            client.setXFactoryAcquisitionSettings(mode, factorySettings);

            XAcquisitionResult acqResult = client.runXAcquisition(mode, factorySettings, true);
            LOGGER.info("Acquisition completed, status: {}, errorCode: {}, abortedByUser: {}",
                    acqResult.status, acqResult.errorCode, acqResult.abortedByUser);
            printXSpectra(acqResult.spectra);
        } else {
            ZInstrumentConfig config = client.getZInstrumentConfig();
            LOGGER.info("Installed spectrometers: {}", config.spectrometers);
            LOGGER.info("isArgonCapable: {}", config.isArgonCapable);
            LOGGER.info("isAirPumpCapable: {}", config.isAirPumpCapable);
            int numSpectrometers = config.spectrometers.length();

            ZInstrumentStatus status = client.getZInstrumentStatus();
            LOGGER.info("Battery level: {}%, is charging: {}", status.batteryLevel, status.isCharging);
            LOGGER.info("Wifi SSID: {}, signal level: {}", status.wifiSSID, status.wifiLevel);
            LOGGER.info("Location: {}, {}", status.latitude, status.longitude);
            LOGGER.info("Wl Calibration needed: {}", status.isWlCalNeeded);
            LOGGER.info("Argon pressure: {} psi", status.argonPSI);
            LOGGER.info("Laser temp: {} degC", status.laserTemp);
            LOGGER.info("Processor temp: {} degC", status.processorTemp);

            if (status.isWlCalNeeded) {
                LOGGER.info(" -- Running WL Calibration -- ");
                client.runWlCalibration();
            }
            ZCalibration calibration = client.getZCalibration();
            for (int i = 0; i < numSpectrometers; i++) {
                LOGGER.info("Calibration coefficients for spectrometer {}: {}", i, Arrays.toString(calibration.coefficients[i]));
            }

            if (isEmptyString(mode)) {
                mode = id.apps.get(0);
            }
            ZAcquisitionSettings userSettings = client.getZAcquisitionSettings(mode);
            LOGGER.info(" -- UserSettings -- for {}", mode);
            LOGGER.info("preBurnType: {}, numPreBurnPulses: {}", userSettings.preBurnType, userSettings.numPreBurnPulses);

            userSettings.preBurnType = 0;
            client.setZAcquisitionSettings(mode, userSettings);

            LOGGER.info(" -- Running {} Test -- ", mode);
            ZTestResult testResult = client.runZTest(mode, userSettings, true);
            LOGGER.info("Test completed, status: {}, errorCode: {}, abortedByUser: {}",
                    testResult.status, testResult.errorCode, testResult.abortedByUser);
            ZAcquisitionMetadata testMd = testResult.metadata;
            LOGGER.info("argonEnabled: {}, argonPSI: {}, maxLaserTemp: {}, maxLaserPumpTime: {}, errorMsg: {}",
                    testMd.argonEnabled, testMd.argonPSI, testMd.maxLaserTemp, testMd.maxLaserPumpTime, testMd.errorMsg);
            LOGGER.info("Num spectra collected: {}", testResult.spectra.size());
            printTestResult(testResult.testData);

            ZFactoryAcquisitionSettings factorySettings = client.getZFactoryAcquisitionSettings(mode);
            LOGGER.info(" -- FactorySettings -- ");
            LOGGER.info("numLocations: {}, numDataPulses: {}, numCleaningPulses: {}",
                    factorySettings.numLocations, factorySettings.numDataPulses, factorySettings.numCleaningPulses);
            LOGGER.info("argonEnabled: {}, preFlushTimeMs: {}, lowArgonWarningEnabled: {}, lowPressureThreshold: {}",
                    factorySettings.argonEnabled, factorySettings.preFlushTimeMs,
                    factorySettings.lowArgonWarningEnabled, factorySettings.lowPressureThreshold);
            LOGGER.info("detectorGatingEnabled: {}, integrationDelay: {}, integrationPeriod: {}",
                    factorySettings.detectorGatingEnabled, factorySettings.integrationDelay, factorySettings.integrationPeriod);
            LOGGER.info("laserPulsePeriod: {}, numPulsesToAvg: {}", factorySettings.laserPulsePeriod, factorySettings.numPulsesToAvg);
            LOGGER.info("preBurnType: {}, numPreBurnPulses: {}", factorySettings.preBurnType, factorySettings.numPreBurnPulses);

            factorySettings.numDataPulses = 32;
            client.setZFactoryAcquisitionSettings(mode, factorySettings);

            ZAcquisitionResult acqResult = client.runZAcquisition(mode, factorySettings, true);
            LOGGER.info("Acquisition completed, status: {}, errorCode: {}, abortedByUser: {}",
                    acqResult.status, acqResult.errorCode, acqResult.abortedByUser);
            ZAcquisitionMetadata acqMd = acqResult.metadata;
            LOGGER.info("argonEnabled: {}, argonPSI: {}, maxLaserTemp: {}, maxLaserPumpTime: {}, errorMsg: {}",
                    acqMd.argonEnabled, acqMd.argonPSI, acqMd.maxLaserTemp, acqMd.maxLaserPumpTime, acqMd.errorMsg);
            LOGGER.info("Num spectra collected: {}", acqResult.spectra.size());
        }
    }

    public static boolean isEmptyString(String str) {
        return !(str != null && str.trim().length() > 0);
    }

    static void printTestResult(AnalysisResult analysisResult) {
        if (analysisResult != null) {
            LOGGER.info("Mode: {}, timestamp: {}, duration: {}ms", analysisResult.mode,
                        new Date(analysisResult.timestamp), analysisResult.durationMs);
            LOGGER.info("Location: {}, {}", analysisResult.latitude, analysisResult.longitude);
            LOGGER.info("Base: {}, Grade Library: {}, modelName: {}",
                        analysisResult.base, analysisResult.gradeLibraryName, analysisResult.modelName);
            LOGGER.info("Grade matches: {} ({}), {} ({}), {} ({})",
                        analysisResult.firstGradeMatch, analysisResult.firstGradeMatchScore,
                        analysisResult.secondGradeMatch, analysisResult.secondGradeMatchScore,
                        analysisResult.thirdGradeMatch, analysisResult.thirdGradeMatchScore);
            LOGGER.info("Chemistry: {} Elements found", analysisResult.chemistry.size());
            for (ChemInfo chemInfo : analysisResult.chemistry) {
                LOGGER.info("    {} {} +/- {}", chemInfo.atomicNumber, chemInfo.percent, chemInfo.uncertainty);
            }
        }
    }

    static void printXSpectra(List<XSpectrum> spectra) {
        LOGGER.info("Num spectra collected: {}", spectra.size());
        for (XSpectrum s : spectra) {
            LOGGER.info("index: {}, beam Name: {}, {}keV/{}uA, filter: {}, livetime: {} realtime: {}",
                    s.index, s.beamName, s.keV, s.uA, s.filterPos, s.liveTime, s.realTime);
        }
    }
}
