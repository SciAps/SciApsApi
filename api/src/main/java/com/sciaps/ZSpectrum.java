package com.sciaps;

public class ZSpectrum {
    public int index;                       // Spectrum index
    public boolean isAvg;                   // True if this spectrum is the final, averaged spectrum
    public ZCalibration calibration;        // Calibration coefficients
    public double[] knots;                  // Endpoints in nanometers of each spectrometer
    public double[][] pixels;               // Raw pixel data without the wavelength calibration applied
}
