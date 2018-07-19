# saucelabs-proxy-test

## Background

A simple Java application to attempt connecting to Sauce Labs cloud via Java's proxy settings.  Several of the URLs used by Sauce Labs are tested.

## Test Arguments

There are five properties you can set to specify the proxy that will be used by the __Java HTTP/HTTPS Protocol Handler__:

* `https.proxyHost`: the host name of the proxy server,
* `https.proxyPort`: the port number, the default value being 443.
* `https.proxyUser`: the name of the user to authenticate to the proxy server.
* `https.proxyPassword`: the password of the user to authenticate to the proxy server.

These values should be specified as environment variables or passed on the Java Command Line using the __-D__ option.

## Test Execution

To execute the test, use the __-D__ command line option to supply the above arguments to the test.

Example:

    $ mvn -Dhttps.proxyHost=squid.example.com -Dhttps.proxyPort=3128 -Dhttps.proxyUser=myproxyuser -Dhttps.proxyPassword=password test
    ...
    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running com.saucelabs.billmeyer.AppTest
    Proxy Settings:
    ===========================================
    https.proxyHost: squid.example.com
    https.proxyPort: 3128
    https.proxyUser: myproxyuser
    https.proxyPassword: password
    
    Testing connection to https://ondemand.saucelabs.com:
    ===========================================
    Response Code: 200 - OK
    Response Body: [OK,ondemand alive]
    
    Testing connection to https://us1.appium.testobject.com/wd/hub/status:
    ===========================================
    Response Code: 200 - OK
    Response Body: []
    
    Testing connection to https://eu1.appium.testobject.com/wd/hub/status:
    ===========================================
    Response Code: 200 - OK
    Response Body: []
    
    Testing connection to https://app.testobject.com/api/rest/releaseVersion:
    ===========================================
    Response Code: 200 - OK
    Response Body: [2018.07.17.18.08]
    
    Testing connection to https://saucelabs.com/rest/v1/info/status:
    ===========================================
    Response Code: 200 - OK
    Response Body: [{"wait_time": 0.5774264610599974, "service_operational": true, "status_message": "Basic service status checks passed."}]
    
    DNS Servers:
    ===========================================
        dns://172.16.32.10
        dns://172.17.32.10
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.644 sec
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 4.043 s
    [INFO] Finished at: 2018-07-19T14:48:52-05:00
    [INFO] Final Memory: 17M/386M
    [INFO] ------------------------------------------------------------------------
