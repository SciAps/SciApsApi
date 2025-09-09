package com.sciaps;

import java.util.List;

public class NAcquisitionResult {
    public String status;
    public boolean abortedByUser;
    public int errorCode;
    public List<NSpectrum> spectra;
    public List<NAcquisitionMetadata> metadata;     // One entry per spectrometer
}
