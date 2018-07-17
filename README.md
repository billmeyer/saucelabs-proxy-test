# saucelabs-proxy-test

## Background

A simple Java application to attempt connecting to Sauce Labs cloud via Java's proxy settings.  Two URLs are tested:

* https://ondemand.saucelabs.com:443
* https://us1.appium.testobject.com/wd/hub/status

The first URL confirms connectivity to Sauce Labs' desktop VMs and the second URL confirms connectivity to Sauce Labs' mobile cloud.

## Test Arguments

There are five properties you can set to specify the proxy that will be used by the __Java HTTP/HTTPS Protocol Handler__:

* `https.proxyHost`: the host name of the proxy server,
* `https.proxyPort`: the port number, the default value being 443.
* `https.proxyUser`: the name of the user to authenticate to the proxy server.
* `https.proxyPassword`: the password of the user to authenticate to the proxy server.

These values should be specified as environment variables or passed on the Java Command Line using the __-D__ option.

## Test Execution

Let's look at a few examples assuming we're trying to execute the main method of the GetURL class:

    $ mvn -Dhttps.proxyHost=webcache.example.com test

All http connections will go through the proxy server at `webcache.example.com` listening on port `80` (we didn't specify any port, so the default one is used).

    $ mvn -Dhttps.proxyHost=webcache.example.com -Dhttps.proxyPort=3128 -Dhttps.proxyUser=myproxyuser -Dhttps.proxyPassword=password test
    ...
    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running com.saucelabs.billmeyer.AppTest
    Proxy Settings:
    ===========================================
    https.proxyHost: myproxyhost.acme.com
    https.proxyPort: 3128
    https.proxyUser: myproxyuser
    https.proxyPassword: somepassword
    
    Testing connection to saucelabs.com:
    ===========================================
    Response Code: 200 - OK
    Content: [OK,ondemand alive]
    
    Testing connection to testobject.com:
    ===========================================
    Response Code: 200 - OK
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.971 sec
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 1.786 s
    [INFO] Finished at: 2018-07-17T15:47:14-05:00
    [INFO] Final Memory: 13M/773M
    [INFO] ------------------------------------------------------------------------
