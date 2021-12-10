package com.sciaps;

public class InstrumentStatus {
    public boolean isWlCalibrationNeeded;   // is wavelength calibration required
    public float argonPSI;                  // argon pressure or -1 if not argon capable
    public float laserTemp;                 // laser temp in degrees C
    public float processorTemp;             // processor temp in degrees C
}
