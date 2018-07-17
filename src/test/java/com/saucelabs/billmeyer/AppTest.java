package com.saucelabs.billmeyer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    throws IOException
    {
        Request request;
        Response response;

        final int proxyPort = Integer.parseInt(System.getProperty("https.proxyPort"));
        final String proxyHost = System.getProperty("https.proxyHost");
        final String proxyUser = System.getProperty("https.proxyUser");
        final String proxyPassword = System.getProperty("https.proxyPassword");

        System.out.printf("Proxy Settings:\n");
        System.out.printf("===========================================\n");
        System.out.printf("https.proxyHost: %s\n", proxyHost);
        System.out.printf("https.proxyPort: %s\n", proxyPort);
        System.out.printf("https.proxyUser: %s\n", proxyUser);
        System.out.printf("https.proxyPassword: %s\n", proxyPassword);
        System.out.println();

        Authenticator proxyAuthenticator = new Authenticator()
        {
            @Override
            public Request authenticate(Route route, Response response)
            throws IOException
            {
                String credential = Credentials.basic(proxyUser, proxyPassword);
                return response.request().newBuilder().header("Proxy-Authorization", credential).build();
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .proxy(proxy)
                .proxyAuthenticator(proxyAuthenticator)
                .build();

        /////////////////////////////////////////////////////////////////////////////////
        // Test access to https://ondemand.saucelabs.com:443...

        System.out.printf("Testing connection to saucelabs.com:\n");
        request = new Request.Builder().url("https://ondemand.saucelabs.com:443").build();
        response = client.newCall(request).execute();

        System.out.printf("===========================================\n");
        System.out.printf("Response Code: %d - %s\n", response.code(), response.message());
        assertTrue(response.code() == 200);

        String line = response.body().string();
        System.out.printf("Content: [%s]\n", line);
        assertTrue(line.equals("OK,ondemand alive"));
        System.out.println();

        /////////////////////////////////////////////////////////////////////////////////
        // Test access to https://us1.appium.testobject.com/wd/hub/status...

        System.out.printf("Testing connection to testobject.com:\n");
        request = new Request.Builder().url("https://us1.appium.testobject.com/wd/hub/status").build();
        response = client.newCall(request).execute();

        System.out.printf("===========================================\n");
        System.out.printf("Response Code: %d - %s\n", response.code(), response.message());
        System.out.println();
        assertTrue(response.code() == 200);

        // No content on the testobject side!
    }
}
