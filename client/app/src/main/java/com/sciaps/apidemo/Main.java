package com.sciaps.apidemo;

import javax.swing.*;

import com.sciaps.InstrumentId;
import com.sciaps.ZAcquisitionMetadata;
import com.sciaps.ZAcquisitionResult;
import com.sciaps.ZAcquisitionSettings;
import com.sciaps.ZAnalysisResult;
import com.sciaps.ZCalibration;
import com.sciaps.ZChemInfo;
import com.sciaps.ZFactoryAcquisitionSettings;
import com.sciaps.ZInstrumentConfig;
import com.sciaps.ZInstrumentStatus;
import com.sciaps.ZTestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;

public class Main {

    public static Logger LOGGER = LoggerFactory.getLogger("ApiDemo");

    private static final String DEFAULT_IP_ADDRESS = "192.168.42.129:8080";

    public static void main(String[] args) {
        String defaultAddress = args.length >= 1 ? args[0] : DEFAULT_IP_ADDRESS;
        String command = args.length >= 2 ? args[1] : "";

        boolean isAbort = command.equalsIgnoreCase("--abort");
        boolean promptUser = !isAbort && !command.equalsIgnoreCase("--go");

        String ipAddress = defaultAddress;
        if (promptUser) {
            ipAddress = JOptionPane.showInputDialog("Enter Analyzer IP Address", ipAddress);
        }

        if (ipAddress == null || ipAddress.trim().isEmpty()) {
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
        LOGGER.info("Available apps: {}", id.apps);

        ZInstrumentConfig config = client.getZInstrumentConfig();
        LOGGER.info("Installed spectrometers: {}", config.spectrometers);
        LOGGER.info("isArgonCapable: {}", config.isArgonCapable);
        LOGGER.info("isAirPumpCapable: {}", config.isAirPumpCapable);
        int numSpectrometers = config.spectrometers.size();

        ZInstrumentStatus status = client.getZInstrumentStatus();
        LOGGER.info("Battery level: {}%, is charging: {}", status.batteryLevel, status.isCharging);
        LOGGER.info("Wifi SSID: {}, signal level: {}", status.wifiSSID, status.wifiLevel);
        LOGGER.info("Location: {}, {}", status.latitude, status.longitude);
        LOGGER.info("Wl Calibration needed: {}", status.isWlCalNeeded);
        LOGGER.info("Argon pressure: {} psi", status.argonPSI);
        LOGGER.info("Laser temp: {} degC", status.laserTemp);
        LOGGER.info("Processor temp: {} degC", status.processorTemp);

        LOGGER.info(" -- Running WL Calibration -- ");
        client.runWlCalibration();
        ZCalibration calibration = client.getZCalibration();
        for (int i = 0; i < numSpectrometers; i++) {
            LOGGER.info("Calibration coefficients for spectrometer {}: {}", i, Arrays.toString(calibration.coefficients[i]));
        }

        String mode = id.apps.get(0);
        ZAcquisitionSettings userSettings = client.getZAcquisitionSettings(mode);
        LOGGER.info(" -- UserSettings -- ");
        LOGGER.info("preBurnType: {}, numPreBurnPulses: {}", userSettings.preBurnType, userSettings.numPreBurnPulses);

        userSettings.preBurnType = 0;
        client.setZAcquisitionSettings(mode, userSettings);

        LOGGER.info(" -- Running {} Test -- ", mode);
        ZTestResult testResult = client.runTest(mode, userSettings, true);
        LOGGER.info("Test completed, status: {}, errorCode: {}, abortedByUser: {}",
                testResult.status, testResult.errorCode, testResult.abortedByUser);
        ZAcquisitionMetadata testMd = testResult.metadata;
        LOGGER.info("argonEnabled: {}, argonPSI: {}, maxLaserTemp: {}, maxLaserPumpTime: {}, errorMsg: {}",
                testMd.argonEnabled, testMd.argonPSI, testMd.maxLaserTemp, testMd.maxLaserPumpTime, testMd.errorMsg);
        LOGGER.info("Num spectra collected: {}", testResult.spectra.size());
        ZAnalysisResult analysisResult = testResult.testData;
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
        for (ZChemInfo chemInfo : analysisResult.chemistry) {
            LOGGER.info("    {} {} +/- {}", chemInfo.atomicNumber, chemInfo.percent, chemInfo.uncertainty);
        }

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

        ZAcquisitionResult acqResult = client.runAcquisition(mode, factorySettings, true);
        LOGGER.info("Acquisition completed, status: {}, errorCode: {}, abortedByUser: {}",
                acqResult.status, acqResult.errorCode, acqResult.abortedByUser);
        ZAcquisitionMetadata acqMd = acqResult.metadata;
        LOGGER.info("argonEnabled: {}, argonPSI: {}, maxLaserTemp: {}, maxLaserPumpTime: {}, errorMsg: {}",
                acqMd.argonEnabled, acqMd.argonPSI, acqMd.maxLaserTemp, acqMd.maxLaserPumpTime, acqMd.errorMsg);
        LOGGER.info("Num spectra collected: {}", acqResult.spectra.size());
    }
}