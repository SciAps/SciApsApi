package com.sciaps;

public class NSpectrum {
    public String type;     // The following values are supported:
                            // Refl - Reflectance
                            // Absorb - Absorbance
                            // W - WhiteRef
                            // W2 - WhiteRef2
                            // WE - ExtWhiteRef
                            // D - DarkRef
                            // S - Sample
                            // C - CalRef
    public double[] x;      // wavelength
    public double[] y;
}
