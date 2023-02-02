package com.sciaps;

import java.util.List;

public class ZTestResult {
    public String status;
    public boolean abortedByUser;
    public int errorCode;
    public List<ZSpectrum> spectra;
    public ZAcquisitionMetadata metadata;
    public AnalysisResult testData;
}
