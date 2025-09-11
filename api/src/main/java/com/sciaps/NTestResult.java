package com.sciaps;

import java.util.List;

public class NTestResult {
    public String status;
    public boolean abortedByUser;
    public int errorCode;
    public List<NSpectrum> spectra;
    public List<NAcquisitionMetadata> metadata;     // One entry per spectrometer
    public List<NMineralMatch> mineralMatches;
    public List<NScalar> scalars;
    public AnalysisResult testData;
}
