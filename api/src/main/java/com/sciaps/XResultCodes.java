package com.sciaps;

public class XResultCodes {

    /**
     * Non-error result codes [0..100]
     */
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_USERABORT = 1;
    public static final int CODE_INTERNALABORT = 2;
    public static final int CODE_INTERNALABORT_END_SESSION = 3;
    public static final int CODE_LIVE_UPDATE = 4;

    /**
     * Acquisition Session error codes [101..199]
     */
    public static final int ERROR_ACQ_UNKNOWN = 101;
    public static final int ERROR_ACQ_DETECTOR_TEMP = 102;
    public static final int ERROR_ACQ_TUBE_POWER = 103;
    public static final int ERROR_ACQ_TUBE_TEMP = 104;
    public static final int ERROR_ACQ_LOW_COUNT_RATE = 105;
    public static final int ERROR_ACQ_TESTSTAND_INTERLOCK_OPEN = 106;
    public static final int ERROR_ACQ_SHUTTER_NOT_OPEN = 107;
    public static final int ERROR_ACQ_CURRENT_NOT_SET = 108;
    public static final int ERROR_ACQ_FILTER_WHEEL_TIMEOUT = 109;

    /**
     * Analysis Engine error codes [201..299]
     */
    public static final int ERROR_AENG_MISSING_CONFIG_FILES = 201;
    public static final int ERROR_AENG_EMPTY_GRADE_FOLDER = 202;
    public static final int ERROR_AENG_MISSING_GRADE_LIB_FILE = 203;
    public static final int ERROR_AENG_THRESHOLD_FILE_CORRUPTED = 204;
    public static final int ERROR_AENG_INVALID_TEST_TYPE = 205;
    public static final int ERROR_AENG_CALIBRATION_MODEL_UNDEFINED = 206;
    public static final int ERROR_AENG_CALIBRATION_MODEL_PARAMS = 207;
    public static final int ERROR_AENG_CALIBRATION_MODEL_NOT_FOUND = 208;
    public static final int ERROR_AENG_CALIBRATION_MODEL_NO_CURVES = 209;
    public static final int ERROR_AENG_ACQUISITION_PARAMS = 210;
    public static final int ERROR_AENG_HQI_SAMPLE_CONFIG = 211;
    public static final int ERROR_AENG_INVALID_CALIBRATION_IMD = 212;
    public static final int ERROR_AENG_ACQUISITION_SESSION_CONFIG = 213;
    public static final int ERROR_AENG_FPCOMPUTE_QUANT_ERROR = 214;
    public static final int ERROR_AENG_NOT_AL_ALLOY = 215;
    public static final int ERROR_AENG_IMD_INIT_FAILED_BASE = 220;
    public static final int ERROR_AENG_IMD_INIT_FAILED_READ_FILE = 221;
    public static final int ERROR_AENG_IMD_INIT_FAILED_INIT_IMD = 222;
    public static final int ERROR_AENG_CORRUPTED_GRADE_LIB_FILE = 223;

    /**
     * Energy Calibration error codes [301..399]
     */
    public static final int ERROR_ECAL_BASE = 300;
    public static final int ERROR_ECAL_PEAKS_NOT_FOUND = 301;
    public static final int ERROR_ECAL_COUNT_RATE_DEVIATION = 302;
    public static final int ERROR_ECAL_FWHM_DEVIATION = 303;
    public static final int ERROR_ECAL_SLOPE_DEVIATION = 304;
    public static final int ERROR_ECAL_OFFSET_DEVIATION = 305;
    public static final int ERROR_ECAL_LOGFILE_WRITE_FAILED = 310;
    public static final int ERROR_ECAL_TIMEOUT = 311;

    /**
     * Remote Service error codes [401..499]
     */
    public static final int ERROR_RMT_TOO_MANY_REQUESTS = 401;
    public static final int ERROR_RMT_SERVICE_IS_OFF = 402;
    public static final int ERROR_RMT_INVALID_CONFIGURATION = 403;
}
