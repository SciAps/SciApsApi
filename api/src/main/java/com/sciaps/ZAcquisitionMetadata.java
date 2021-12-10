package com.sciaps;

public class ZAcquisitionMetadata {
    public boolean argonEnabled;        // True if argon was enabled for this acquisition
    public float argonPSI;              // Argon pressure at the time of this acquisition
    public float maxLaserTemp;          // Max laser temperature in degrees C during this acquisition
    public int maxLaserPumpTime;        // Max laser pump time in microseconds for this acquisition
    public String errorMsg;             // Error message
}
