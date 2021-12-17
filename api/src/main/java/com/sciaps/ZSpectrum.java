package com.sciaps;

public class ZSpectrum {
    public int index;                       // scectrum index
    public boolean isAvg;                   // true if this spectrum is the final, averaged spectrum
    public ZCalibration calibration;        // Calibration coefficients
    public double[] knots;                  // Endpoints in nm of each spectrometer
    public double[][] pixels;               // raw pixel data
}
