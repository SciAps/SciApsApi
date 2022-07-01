package com.sciaps;

import java.util.List;

public class XAcquisitionResult {
   public int status;
   public boolean abortedByUser;
   public int errorCode;
   public List<XSpectrum> spectra;
}
