package com.sciaps;

public class XBeamSettings {
    public String name;                 // Beam name
    public double voltage;              // Voltage in KeV
    public double current;              // Current in uA
    public int filterPosition;          // 0-5 are valid values
    public double setCountRate;
    public double lowCountRate;
    public double maxCountRate;
    public double beamTimeMs;
    public boolean enabled;
}
