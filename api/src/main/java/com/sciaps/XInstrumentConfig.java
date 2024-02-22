package com.sciaps;

public class XInstrumentConfig {
    public boolean isShutterInstalled;          // is device equipped a shutter
    public boolean isFilterWheelInstalled;      // is device equipped with a filter wheel
    public String detectorType;                 // DPP type: Amptek / Ketek
    public String tubeAnodeType;                // Tube anode type: Rh / Ag / Au / W / Ta
    public int tubeType;                        // Tube type: 1: 4W, 2: 5W, 3: Turbo, 4: 55KV, 5: 80KV
    public String dppVersion;                   // DPP firmware version
}
