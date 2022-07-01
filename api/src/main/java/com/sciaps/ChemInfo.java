package com.sciaps;

public class ChemInfo {
    public int atomicNumber;
    public double percent;
    public double uncertainty;
    public int flags;

    // Flags is a bitmask with the following bits defined:
    public static final int TYPE_ERROR      = (1 << 0); // 1
    public static final int TYPE_NOMINAL    = (1 << 1); // 2
    public static final int TYPE_LESS_LOD   = (1 << 3); // 8
    public static final int TYPE_LE         = (1 << 5); // 32
    public static final int TYPE_OCR_MINUS  = (1 << 6); // 64
    public static final int TYPE_OCR_PLUS   = (1 << 7); // 128
}
