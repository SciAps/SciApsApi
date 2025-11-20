package com.sciaps;

public class XSpectrum {
    public int index;                         // Spectrum index
    public String beamName;                   // Beam name
    public double energySlope;                // Energy calibration slope
    public double energyOffset;               // Energy calibration offset
    public double liveTime;                   // Livetime
    public double realTime;                   // Realtime
    public double liveTimeMultiplier;
    public int outputEvents;
    public int fastPeaks;
    public double keV;
    public double uA;
    public int filterPos;
    public double tubeTemp;
    public double detectorTemp;
    public int numBins;
    public double[] data;       // Either data or x and y will be populated. Data are the channel intensity values without the energy calibration applied
    public double[] x;          // Energy in kiloelectron volts calculated using the energy calibration
    public double[] y;          // Intensity in counts per second
}
