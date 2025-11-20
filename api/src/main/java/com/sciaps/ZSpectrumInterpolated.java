package com.sciaps;

import java.util.List;

public class ZSpectrumInterpolated {
    public int index;                       // Spectrum index
    public boolean isAvg;                   // True if this spectrum is the final, averaged spectrum
    public double[] x;                      // Wavelength in nanometers calculated using the wavelength calibration
    public double[] y;                      // Intensity in total counts
}
