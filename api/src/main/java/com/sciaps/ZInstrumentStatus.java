package com.sciaps;

public class ZInstrumentStatus {
    public float batteryLevel;              // Battery charge level in percent
    public boolean isCharging;              // True is battery charged is plugged in
    public String user;                     // Current user
    public String wifiSSID;                 // Wifi SSID
    public int wifiLevel;                   // Wifi signal strength 0-3
    public double latitude;                 // Current latitude of device
    public double longitude;                // Current longitude of device
    public boolean isWlCalNeeded;           // is wavelength calibration required
    public float argonPSI;                  // argon pressure or -1 if not argon capable
    public float laserTemp;                 // laser temp in degrees C
    public float processorTemp;             // processor temp in degrees C
}
