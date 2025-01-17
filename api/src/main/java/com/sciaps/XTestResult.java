package com.sciaps;

import java.util.List;

public class XTestResult {
    public int status;               // One of the values in XResultCodes
    public boolean abortedByUser;
    public int errorCode;
    public List<XSpectrum> spectra;
    public AnalysisResult testData;
}
