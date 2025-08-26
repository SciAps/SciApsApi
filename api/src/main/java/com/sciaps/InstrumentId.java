package com.sciaps;

import java.util.List;

public class InstrumentId {
    public String family;                   // XRF, LIBS, RAMAN
    public String id;                       // Instrument Id - model number + serial number
    public String model;                    // Instrument model
    public String partNumber;               // Instrument part number
    public String swVersion;                // Software version number
    public String homeVersion;              // Home app version number
    public String serviceVersion;           // Analyzer Service version number
    public String picVersion;               // PIC Firmware version number
    public String osVersion;                // OS Release version number
    public List<String> apps;               // Available application modes
    public List<AnalyticalModel> models;    // Available analytical models
    public List<Library> libraries;         // Available libraries
}
