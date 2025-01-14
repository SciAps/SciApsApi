package com.sciaps;

import java.util.List;

public class ZTestResult {
    public String status;       // A string indicating the status of the test, one of:
                                //    "CODE_SUCCESS",
                                //    "CODE_LASER_NOT_READY",
                                //    "CODE_LASER_OVERHEATING",
                                //    "CODE_LASER_NEEDS_WARM_UP",
                                //    "CODE_NO_SAMPLE_DETECTED",
                                //    "CODE_LOW_ARGON",
                                //    "CODE_TIMEOUT",
                                //    "CODE_ABORT",
                                //    "CODE_INVALID_CONFIG",
                                //    "CODE_AIRSHOT_AUTO_ABORT",
                                //    "CODE_WRONG_STAGE"
    public boolean abortedByUser;
    public int errorCode;       // 0 for success, -150 for WL Calibration failure
    public List<ZSpectrum> spectra;
    public ZAcquisitionMetadata metadata;
    public AnalysisResult testData;
}
