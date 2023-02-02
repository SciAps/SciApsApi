package com.sciaps;

public class ChemInfo {
    public int atomicNumber;
    public double percent;
    public double uncertainty;
    public int flags;

    /**
     * Flags is a bitmask which may contain a combination of the following bits.
     * The meaning of the flags is as follows:
     *   - TYPE_ERROR indicates either the percent or uncertainty calculation resulted in a value of Infinity.
     *   - TYPE_NOMINAL indicates this element was not actually measured in the sample but was inferred due to the
     *         alloy grade matched.
     *   - TYPE_LESS_LOD indicated the value in the percent field is below the Level Of Detection
     *   - TYPE_LE indicates the value in the percent field represents the Light Elements
     *   - TYPE_OCR_MINUS indicates the value in the percent field is Outside the Calibration Range (below)
     *   - TYPE_OCR_PLUS indicates the value in the percent field is Outside the Calibration Range (above)
     **/
    public static final int TYPE_ERROR      = (1 << 0); // 1
    public static final int TYPE_NOMINAL    = (1 << 1); // 2
    public static final int TYPE_LESS_LOD   = (1 << 3); // 8
    public static final int TYPE_LE         = (1 << 5); // 32
    public static final int TYPE_OCR_MINUS  = (1 << 6); // 64
    public static final int TYPE_OCR_PLUS   = (1 << 7); // 128
}
