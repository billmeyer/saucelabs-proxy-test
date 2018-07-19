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

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    private Authenticator proxyAuthenticator;
    private Proxy proxy;
    private OkHttpClient httpClient;

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
        createProxy();
        createHttpClient();

        checkURL("https://ondemand.saucelabs.com", "OK,ondemand alive");
        checkURL("https://us1.appium.testobject.com/wd/hub/status", null);
        checkURL("https://eu1.appium.testobject.com/wd/hub/status", null);
        checkURL("https://app.testobject.com/api/rest/releaseVersion", null);
        checkURL("https://saucelabs.com/rest/v1/info/status", null);
        getDNSInfo();
    }

    private void createHttpClient()
    {
        // @formatter:off
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .proxy(proxy)
                .proxyAuthenticator(proxyAuthenticator)
                .build();
        // @formatter:on
    }

    private void createProxy()
    {
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

        proxyAuthenticator = new Authenticator()
        {
            @Override
            public Request authenticate(Route route, Response response)
            throws IOException
            {
                String credential = Credentials.basic(proxyUser, proxyPassword);
                return response.request().newBuilder().header("Proxy-Authorization", credential).build();
            }
        };

        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }

    private void checkURL(String url, String expectedResponseBody)
    {
        /////////////////////////////////////////////////////////////////////////////////
        // Test access to https://ondemand.saucelabs.com:443...

        System.out.printf("Testing connection to %s:\n", url);
        Request request = new Request.Builder().url(url).build();
        Response response = null;

        try
        {
            response = httpClient.newCall(request).execute();
        }
        catch (IOException e)
        {
            System.err.printf("Failed to execute call to %s, err=%s\n", url, e.getMessage());
            return;
        }

        System.out.printf("===========================================\n");
        System.out.printf("Response Code: %d - %s\n", response.code(), response.message());
        assertTrue(response.code() == 200);

        String line = null;
        try
        {
            line = response.body().string();
            System.out.printf("Response Body: [%s]\n", line);
            if (expectedResponseBody != null)
            {
                assertTrue(line.equals(expectedResponseBody));
            }
        }
        catch (IOException e)
        {
            System.err.printf("Failed to read response body: %s\n", e.getMessage());
        }

        System.out.println();
    }

    private static void getDNSInfo()
    {
        Hashtable<String, String> env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");

        System.out.printf("DNS Servers:\n");
        System.out.printf("===========================================\n");

        try
        {
            DirContext context = new InitialDirContext(env);
            String dnsServers = (String)context.getEnvironment().get("java.naming.provider.url");
            System.out.printf("\t%s\n", dnsServers);
        }
        catch (NamingException e)
        {
        }
    }
}
