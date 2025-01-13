package com.sciaps;

import java.util.List;

public class  XAcquisitionSettings {
    public int testType;                    // For Alloy based modes: Auto Beam (1), Single Beam (2), Two Beams (4)
                                            // For Empirical based modes the value will be 0
    public String modelName;                // Empty for Alloy modes, for Rohs mode: Auto, Alloy, Polymer
    public List<Boolean> beamEnabledFlags;  // Beam enabled flags, empty for Alloy modes
    public List<Long> beamTimes;            // Beam test time in milliseconds
}
