package com.sciaps;

public class InstrumentConfig {
    public String id;                       // Instrument Id - model number + serial number
    public String model;                    // Instrument model
    public String partNumber;               // Instrument part number
    public String swVersion;                // Software version number
    public String[] apps;                   // Available application modes
    public String spectrometers;            // Installed spectrometers
    public boolean isArgonCapable;          // is device equipped with argon tank
    public boolean isAirPumpCapable;        // is device equipped with air pump
}
