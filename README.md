# SciAps Remote Control API

The SciAps Remote Control API allows clients to query and apply acquisition settings, and initiate calibrations, tests and spectrum acquisitions.

| URL | METHOD | BODY | RESPONSE | DESCRIPTION |
| --- | --- | --- | --- | --- |
| /api/v2/id | GET | - | InstrumentId | Returns identifying info |
| /api/v2/config | GET | - | ZInstrumentConfig | Returns configuration info |
| /api/v2/status | GET | - | ZInstrumentStatus | Returns status info |
| /api/v2/wlcalibration | GET | - | ZCalibration | Return calibration coefficients |
| /api/v2/wlcalibration | POST | - | - | Run a wavelength calibration |
| /api/v2/acquisitionParams/user?mode=[mode] | GET | - | ZAcquisitionSettings | Returns user acquisition settings for the given mode |
| /api/v2/acquisitionParams/user?mode=[mode] | PUT | ZAcquisitionSettings | - | Applies user acquisition settings for the given mode |
| /api/v2/acquisitionParams/user?mode=[mode] | POST | - | - | Reset user acquisition settings for the given mode to factory defaults |
| /api/v2/test/{spectra}?mode=[mode] | POST | ZAcquisitionSettings | ZTestResult | Runs a test using the given user acquisition settings |
| /api/v2/acquisitionParams/factory?mode=[mode] | GET | - | ZFactoryAcquisitionSettings | Returns factory acquisition settings for the given mode |
| /api/v2/acquisitionParams/factory?mode=[mode] | PUT | ZFactoryAcquisitionSettings | - | Applies factory acquisition settings for the given mode |
| /api/v2/acquisitionParams/factory?mode=[mode] | POST | - | - |Reset factory acquisition settings for the given mode to factory defaults |
| /api/v2/acquire/{spectra}?mode=[mode] | POST | ZFactoryAcquisitionSettings | ZAcquisitionResult | Acquire spectra using the given factory acquisition settings |
| /api/v2/abort | POST | - | - | Aborts the currently running operation |

## Analyzer Information
These commands return identifying information required for further communications with the analyzer as well as 
configuration and current status.

### /api/v2/id
Clients should query the **id** endpoint to determine the device family which identifies the analyzer as XRF or LIBS.
Also, the list of licensed applications id returned, which define the valid values for the mode parameter of the other 
endpoints.  Details of the InstrumentId object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/InstrumentId.java)

### /api/v2/config
Details of the ZInstrumentConfig object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentConfig.java)

### /api/v2/status
Details of the ZInstrumentStatus object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentStatus.java)

## Wavelength Calibration
These commands return the current calibration coefficients and recalculates them.

### /api/v2/wlcalibration
Details of the ZCalibration object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZCalibration.java)

## Tests
These commands relate to running tests which return chemistry information for the sample along with spectra. 

### /api/v2/acquisitionParams/user
This endpoint is used to retrieve, apply or reset user acquisition settings.  User acquisition settings are a subset
of the factory acquisition settings.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the ZAcquisitionSettings object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZAcquisitionSettings.java)

### /api/v2/test
This endpoint is used to initiate a test and return spectra and chemistry results.  Individual spectra will be returned
if **all** is passed as the last segment of the URL.  Only the final, averaged spectra will be returned if **final**
is passed as the last segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the
ZTestResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZTestResult.java)

## Raw Spectra Acquisition
These commands relate to acquiring spectra only.

### /api/v2/acquisitionParams/factory
This endpoint is used to retrieve, apply or reset factory acquisition settings.  This endpoint requires a mode to be 
passed which can be obtained from the **apps** field of the InstrumentId object.  Details of the 
ZFactoryAcquisitionSettings object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZFactoryAcquisitionSettings.java)

### /api/v2/acquire
This endpoint is used to initiate raw spectra acquisition.  Individual spectra will be returned if **all** is passed 
as the last segment of the URL.  Only the final, averaged spectra will be returned if **final**is passed as the last 
segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the
ZAcquisitionResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZAcquisitionResult.java)

### /api/v2/abort
This endpoint aborts the currently running command