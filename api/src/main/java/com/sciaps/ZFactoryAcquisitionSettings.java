package com.sciaps;

public class ZFactoryAcquisitionSettings {
    public int numLocations;                // Number of raster locations
    public int numDataPulses;               // Number of data pulses per location
    public int numCleaningPulses;           // Number of cleaning pulses per location
    public boolean argonEnabled;            // Argon on/off
    public int preFlushTimeMs;              // Argon pre-purge time in milliseconds
    public boolean lowArgonWarningEnabled;  // Low pressure warning of/off
    public int lowPressureThreshold;        // Low pressure warning threshold (PSI)
    public boolean detectorGatingEnabled;   // Detector gating on/off
    public int integrationDelay;            // Integration delay (units??)
    public int integrationPeriod;           // Integration time (units??)
    public int laserPulsePeriod;            // Laser pulse period
    public int numPulsesToAvg;              // Spectra averaging
    public int preBurnType;                 // 0 = off, 1 = short, 2 = long
    public int numPreBurnPulses;            // Number of pre burn pulses
    public int rasterPattern;               // 0 = typewriter, 1 = s-pattern
}
