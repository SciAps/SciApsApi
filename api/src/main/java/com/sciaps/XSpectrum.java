package com.sciaps;

public class XSpectrum {
   public int index;                         // spectrum index
   public double energySlope;                // Energy calibration slope
   public double energyOffset;               // Energy calibration offset
   public double liveTime;                   // livetime
   public double realTime;                   // realtime
   public double liveTimeMultiplier;
   public int outputEvents;
   public int fastPeaks;
   public double keV;
   public double uA;
   public int filterPos;
   public double tubeTemp;
   public double detectorTemp;
   public int numBins;
   public double[] data;
}
