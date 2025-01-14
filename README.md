# SciAps Remote Control API

The SciAps Remote Control API allows clients to query and apply acquisition settings, and initiate calibrations, tests and spectrum acquisitions.

| URL | METHOD | BODY                                         | RESPONSE                                                   | DESCRIPTION                                                               |
| --- | --- |----------------------------------------------|------------------------------------------------------------|---------------------------------------------------------------------------|
| /api/v2/id | GET | -                                            | InstrumentId                                               | Returns identifying info                                                  |
| /api/v2/config | GET | -                                            | ZInstrumentConfig or XInstrumentConfig                     | Returns configuration info                                                |
| /api/v2/status | GET | -                                            | ZInstrumentStatus or XInstrumentStatus                     | Returns status info                                                       |
| /api/v2/wlcalibration | GET | -                                            | ZCalibration                                               | Return calibration coefficients                                           |
| /api/v2/wlcalibration?mode=[mode] | POST | -                                            | -                                                          | Run a wavelength calibration                                              |
| /api/v2/energyCal | GET | -                                            | XCalibration                                               | Return calibration coefficients                                           |
| /api/v2/energyCal | POST | -                                            | -                                                          | Run a energy calibration                                                  |
| /api/v2/acquisitionParams/user?mode=[mode] | GET | -                                            | ZAcquisitionSettings or XAcquisitionSettings               | Returns user acquisition settings for the given mode                      |
| /api/v2/acquisitionParams/user?mode=[mode] | PUT | ZAcquisitionSettings or XAcquisitionSettings | -                                                          | Applies user acquisition settings for the given mode                      |
| /api/v2/acquisitionParams/user?mode=[mode] | POST | -                                            | -                                                          | Reset user acquisition settings for the given mode to factory defaults    |
| /api/v2/test/{spectra}?mode=[mode]&modelName=[modelName] | POST | ZAcquisitionSettings or XAcquisitionSettings | ZTestResult or XTestResult                                 | Runs a test using the given user acquisition settings                     |
| /api/v2/acquisitionParams/factory?mode=[mode] | GET | -                                            | ZFactoryAcquisitionSettings or XFactoryAcquisitionSettings | Returns factory acquisition settings for the given mode                   |
| /api/v2/acquisitionParams/factory?mode=[mode] | PUT | ZFactoryAcquisitionSettings or XFactoryAcquisitionSettings                 | -                                                          | Applies factory acquisition settings for the given mode                   |
| /api/v2/acquisitionParams/factory?mode=[mode] | POST | -                                            | -                                                          | Reset factory acquisition settings for the given mode to factory defaults |
| /api/v2/acquire/{spectra}?mode=[mode] | POST | ZFactoryAcquisitionSettings or XFactoryAcquisitionSettings                 | ZAcquisitionResult or XAcquisitionResult                   | Acquire spectra using the given factory acquisition settings              |
| /api/v2/abort | POST | -                                            | -                                                          | Aborts the currently running operation                                    |
| /api/v2/photo?cameraId=[cameraId] | GET | -                                             | Byte array                                                 | Takes a high resolution image from the specified camera                   |

## Analyzer Information
These commands return identifying information required for further communications with the analyzer as well as 
configuration and current status.

### /api/v2/id
Clients should query the **id** endpoint to determine the device family which identifies the analyzer as XRF or LIBS.
Also, the list of licensed applications id returned, which define the valid values for the mode parameter of the other 
endpoints.  Details of the InstrumentId object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/InstrumentId.java)

#### Example usage LIBS:
```
$ curl http://192.168.42.129:8080/api/v2/id
{"apps":["Alloy","Geochem"],
"family":"LIBS","homeVersion":"ngl-v2.0.6-0-g0c6f38b","id":"Z901-00000","model":"Z-901 Dual-Burn",
"models":[{"mode":"Geochem","modelName":"Lithium-Clay"},{"mode":"Geochem","modelName":"Lithium-Mica-Schist"},{"mode":"Geochem","modelName":"Lithium-Pegmatite"}],
"osVersion":"3.10.49-gebcaa3c","partNumber":"910-500088","picVersion":"5.0.1.0","serviceVersion":"ngl-v2.0.6-0-g8f20647","swVersion":"ngl-v2.0.6-7-g9c5d66a3"}
```

#### Example usage XRF:
```
$ curl http://192.168.42.129:8080/api/v2/id
{"apps":["Alloy","Aluminum","Soil","PreciousMetals","Mining","SulfidicCorrosion","X505Alloy","Residuals","Empirical","Ree","Rohs","LeadPaint","CarCats","Turnings","Coatings","DetectOre"],
"family":"XRF","homeVersion":"ngl-v2.0.3-0-gfbcfdd2","id":"X550-00121","model":"X505_Rh","models":[{"mode":"Mining","modelName":"Mining"},{"mode":"Mining","modelName":"MiningAuOriginal"},
{"mode":"CarCats","modelName":"CarCats"},{"mode":"RohsPolymer","modelName":"RohsPolymer"},{"mode":"LeadPaint","modelName":"LeadPaint"},{"mode":"Empirical","modelName":"Coatings"},
{"mode":"Empirical","modelName":"EmpiricalAppTest"},{"mode":"Empirical","modelName":"Coatings-Ag"},{"mode":"Empirical","modelName":"Coatings-Au"},{"mode":"Soil","modelName":"Soil"},
{"mode":"Mining","modelName":"MiningDeriv"},{"mode":"Mining","modelName":"Mining2Deriv"},{"mode":"Mining","modelName":"MiningAuDeriv"}],"osVersion":"3.10.49-g1be460e",
"partNumber":"","picVersion":"2.6.0.0","serviceVersion":"ngx-v2.1-1-0-gc29a29f","swVersion":"ngx-v2.2.1-2-131-g59406f679"}
```

## LIBS Analyzers

### /api/v2/config
Details of the ZInstrumentConfig object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentConfig.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/config
{"isAirPumpCapable":false,"isArgonCapable":true,"spectrometers":["K","L","P"]}
```

### /api/v2/status
Details of the ZInstrumentStatus object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZInstrumentStatus.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/status
{"argonPSI":12.997406,"batteryLevel":100.0,"isCharging":false,"isWlCalibrationNeeded":true,"laserTemp":20.375,"latitute":0.0,"longitude":0.0,"processorTemp":20.0,"user":"","wifiLevel":0,"wifiSSID":""}
```

## Wavelength Calibration
These commands return the current calibration coefficients and recalculates them.

### /api/v2/wlcalibration
Details of the ZCalibration object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZCalibration.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/wlcalibration
{"coefficients":[[369.19634957579086,-0.0785188815993957,-5.796697950455971E-6,4.533061070459962E-10],[625.6182269991748,-0.11134386477793423,-9.534416965576989E-6,6.856738216864445E-10],[949.2019021133968,-0.15742079497045272,-1.3855774884084319E-5,9.016855223288858E-10]]}
$ curl -X POST http://192.168.42.129:8080/api/v2/wlcalibration?mode=Alloy
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
if **all** is passed as the last segment of the URL.  Only the final, averaged spectra will be returned if **final** is
passed as the last segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object. Passing a model name works similar to force base on the analyzer and is optional for most application types.
Details of the ZTestResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/ZTestResult.java)

#### Example usage:
```
$ curl -X POST -H "Content-Type: application/json" -d '{}' --output output.json http://192.168.42.129:8080/api/v2/test/all?mode=Alloy
$ curl -X POST -H "Content-Type: application/json" -d '{}' --output output.json http://192.168.42.129:8080/api/v2/test/all?mode=Geochem&modelName=Lithium-Clay
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
as the last segment of the URL.  Only the final, averaged spectra will be returned if **final** is passed as the last 
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

## XRF Analyzers

### /api/v2/config
Details of the XInstrumentConfig object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XInstrumentConfig.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/config
{"detectorType":"Ketek","dppVersion":"DXP PIC v32.4.14, DSP v12.5.133","isFilterWheelInstalled":true,"isShutterInstalled":false,"tubeType":"Rh"}
```

### /api/v2/status
Details of the XInstrumentStatus object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XInstrumentStatus.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/status
{"batteryLevel":80.0,"detectorTemp":-25.055584,"isCharging":true,"isECalNeeded":false,"latitude":0.0,"longitude":0.0,"tubeTemp":38.280247,"user":"","wifiLevel":0,"wifiSSID":""}
```

## Energy Calibration
These commands return the current calibration coefficients and recalculates them.

### /api/v2/energyCal
Details of the XCalibration object can be found 
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XCalibration.java)

#### Example usage:
```
$ curl http://192.168.42.129:8080/api/v2/energyCal
{"offset":-17.460930552816535,"slope":20.038459361730006}
$ curl -X POST http://192.168.42.129:8080/api/v2/energyCal
{"status":"CODE_SUCCESS","abortedByUser":"false","errorCode":0}
```

## Tests
These commands relate to running tests which return chemistry information for the sample along with spectra.

### /api/v2/acquisitionParams/user
This endpoint is used to retrieve or apply user acquisition settings.  User acquisition settings are a subset
of the factory acquisition settings.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the XAcquisitionSettings object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XAcquisitionSettings.java)

#### Example usage:
```
$ curl --output alloy-user-settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
$ curl -X PUT -H "Content-Type: application/json" -d @alloy-user-settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
$ curl -X PUT -H "Content-Type: application/json" -d '{"testType":4}' http://192.168.42.129:8080/api/v2/acquisitionParams/user?mode=Alloy
```

### /api/v2/test
This endpoint is used to initiate a test and return spectra and chemistry results.  Individual spectra will be returned
if **all** is passed as the last segment of the URL.  Only the final spectra will be returned if **final**
is passed as the last segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the XTestResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XTestResult.java)

#### Example usage:
```
# Perform an Alloy test with default settings
$ curl -X POST -H "Content-Type: application/json" -d '{}' --output output.json http://192.168.42.129:8080/api/v2/test/all?mode=Alloy
# Perform an Alloy test with specified settings
$ curl -X POST -H "Content-Type: application/json" -d @alloy-user-settings.json --output output.json http://192.168.42.129:8080/api/v2/test/final?mode=Alloy
# Perform an Empirical test with specified model and settings - notice that & must be escaped for the shell
$ curl -X POST --output empirical-test.json -H "Content-Type: application/json" -d @empirical-user-settings.json http://192.168.42.129:8080/api/v2/test?mode=Empirical\&modelName=AgCoatingOnCu
```

## Raw Spectra Acquisition
These commands relate to acquiring spectra only.

### /api/v2/acquisitionParams/factory
This endpoint is used to retrieve or apply factory acquisition settings.  This endpoint requires a mode to be
passed which can be obtained from the **apps** field of the InstrumentId object.  Details of the
XFactoryAcquisitionSettings object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XFactoryAcquisitionSettings.java)

#### Example usage:
```
$ curl --output settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/factory?mode=Alloy
$ curl -X PUT -H "Content-Type: application/json" -d @settings.json http://192.168.42.129:8080/api/v2/acquisitionParams/factory?mode=Alloy
```

### /api/v2/acquire
This endpoint is used to initiate raw spectra acquisition.  Individual spectra will be returned if **all** is passed
as the last segment of the URL.  Only the final spectra will be returned if **final**is passed as the last
segment of the URL.  This endpoint requires a mode to be passed which can be obtained from the **apps**
field of the InstrumentId object.  Details of the
XAcquisitionResult object can be found
[here](https://github.com/SciAps/SciApsApi/tree/master/api/src/main/java/com/sciaps/XAcquisitionResult.java)

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

### /api/v2/photo
This endpoint is used to get high resolution camera image. The endpoint requires a camera type.

#### Example usage:
```
$ curl -v --output image.png http://127.0.0.1:8080/api/v2/photo?cameraId=fullview
$ curl -v --output image.png http://127.0.0.1:8080/api/v2/photo?cameraId=sample
```
