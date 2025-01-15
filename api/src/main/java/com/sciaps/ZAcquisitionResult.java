package com.sciaps;

import java.util.List;

public class ZAcquisitionResult {
    public String status;
    public boolean abortedByUser;
    public int errorCode;
    public List<ZSpectrum> spectra;     // Only one of spectra or spectraInterpolated will be populated
    public List<ZSpectrumInterpolated> spectraInterpolated;
    public ZAcquisitionMetadata metadata;
}
