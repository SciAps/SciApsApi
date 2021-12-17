package com.sciaps;

public class XInstrumentStatus {
    public float batteryLevel;              // Battery charge level in percent
    public boolean isCharging;              // True is battery charged is plugged in
    public String user;                     // Current user
    public String wifiSSID;                 // Wifi SSID
    public int wifiLevel;                   // Wifi signal strength 0-3
    public double latitude;                 // Current latitude of device
    public double longitude;                // Current longitude of device
    public boolean isECalNeeded;            // is energy calibration required
    public float detectorTemp;              // detector temp in degrees C
    public float tubeTemp;                  // tube temp in degrees C
}
