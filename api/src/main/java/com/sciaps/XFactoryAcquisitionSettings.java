package com.sciaps;

import java.util.List;

public class XFactoryAcquisitionSettings {
    public int testType;                // For Alloy based modes: Auto Beam (1), Single Beam (2), Two Beams (4)
                                        // For Empirical based modes the value will be 0
                                        // For Rohs mode: Auto MatrixID (0), Alloy (1), Polymer (2)
    public List<XBeamSettings> beams;
}
