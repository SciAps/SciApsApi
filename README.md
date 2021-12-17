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

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/id
{"apps":["Alloy","Geochem"],"family":"LIBS","homeVersion":"ngl-v1.1-0-gc76429c","id":"Z903-00003","model":"Z-903 Geo","partNumber":"910-500080","swVersion":"ngl-v1.1-1-28-gd5f5857c"}
```

## LIBS Analyzers

### /api/v2/config
Details of the ZInstrumentConfig object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentConfig.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/config
{"isAirPumpCapable":false,"isArgonCapable":true,"spectrometers":["K","L","P"]}
```

### /api/v2/status
Details of the ZInstrumentStatus object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentStatus.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/status
{"argonPSI":12.997406,"batteryLevel":100.0,"isCharging":false,"isWlCalibrationNeeded":true,"laserTemp":20.375,"latitute":0.0,"longitude":0.0,"processorTemp":20.0,"user":"","wifiLevel":0,"wifiSSID":""}
```

## Wavelength Calibration
These commands return the current calibration coefficients and recalculates them.

### /api/v2/wlcalibration
Details of the ZCalibration object can be found [here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZCalibration.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/wlcalibration
{"coefficients":[[369.19634957579086,-0.0785188815993957,-5.796697950455971E-6,4.533061070459962E-10],[625.6182269991748,-0.11134386477793423,-9.534416965576989E-6,6.856738216864445E-10],[949.2019021133968,-0.15742079497045272,-1.3855774884084319E-5,9.016855223288858E-10]]}
$ curl -X POST http://192.168.42.129:8080/api/v2/wlcalibration
{"status":"CODE_SUCCESS","abortedByUser":"false","errorCode":0}
```

## Tests
These commands relate to running tests which return chemistry information for the sample along with spectra. 

### /api/v2/acquisitionParams/user
This endpoint is used to retrieve, apply or reset user acquisition settings.  User acquisition settings are a subset
of the factory acquisition settings.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the ZAcquisitionSettings object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZAcquisitionSettings.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
{"numPreBurnPulses":20,"preBurnType":0}
$ curl -X PUT -H "Content-Type: application/json" -d '{"numPreBurnPulses":10,"preBurnType":2}' http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
$ curl -X POST http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
```

### /api/v2/test
This endpoint is used to initiate a test and return spectra and chemistry results.  Individual spectra will be returned
if **all** is passed as the last segment of the URL.  Only the final, averaged spectra will be returned if **final**
is passed as the last segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the
ZTestResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZTestResult.java)

#### Example usage:
```
$ curl -X POST -H "Content-Type: application/json" -d '{}' --output output.json http://192.168.42.129:8080/api/v2/test/all?mode=Alloy
```

## Raw Spectra Acquisition
These commands relate to acquiring spectra only.

### /api/v2/acquisitionParams/factory
This endpoint is used to retrieve, apply or reset factory acquisition settings.  This endpoint requires a mode to be 
passed which can be obtained from the **apps** field of the InstrumentId object.  Details of the 
ZFactoryAcquisitionSettings object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZFactoryAcquisitionSettings.java)

#### Example usage:
```
$ curl --output settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/factory?mode=Alloy
$ curl -X PUT -H "Content-Type: application/json" -d @settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/factory?mode=Alloy
$ curl -X POST http://192.168.42.129:8080/api/v2/acquisitionParams/factory?mode=Alloy
```

### /api/v2/acquire
This endpoint is used to initiate raw spectra acquisition.  Individual spectra will be returned if **all** is passed 
as the last segment of the URL.  Only the final, averaged spectra will be returned if **final**is passed as the last 
segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the
ZAcquisitionResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZAcquisitionResult.java)

#### Example usage:
```
$ curl -X POST -H "Content-Type: application/json" -d @settings.json --output output.json http://192.168.42.129:8080/api/v2/acquire/all?mode=Alloy
```

### /api/v2/abort
This endpoint aborts the currently running command

#### Example usage:
```
$ curl -X POST http://192.168.42.129:8080/api/v2/abort
```
