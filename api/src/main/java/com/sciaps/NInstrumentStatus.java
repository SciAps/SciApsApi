package com.sciaps;

public class NInstrumentStatus {
    public float batteryLevel;              // Battery charge level in percent
    public boolean isCharging;              // True is battery charged is plugged in
    public String user;                     // Current user
    public String wifiSSID;                 // Wifi SSID
    public int wifiLevel;                   // Wifi signal strength 0-3
    public double latitude;                 // Current latitude of device
    public double longitude;                // Current longitude of device
    public boolean isWhiteRefCalNeeded;     // is energy calibration required
}
