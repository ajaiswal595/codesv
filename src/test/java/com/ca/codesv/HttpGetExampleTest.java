package com.ca.codesv;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.*;
import com.ca.codesv.engine.junit4.VirtualServerRule;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpGetExampleTest {

    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private static final String URL = "http://www.ca.com/portfolio";
    private static final String RESPONSE_BODY_GET = "Response body from virtualized service.";
    private static final int CUSTOM_STATUS_CODE = 258;

    @Test
    public void testHttpGetCallService() throws IOException{
        HttpGet httpGet = new HttpGet(URL);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        BufferedReader reader = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
        StringBuffer response = new StringBuffer();
        String inputLine;

        while ((inputLine = reader.readLine()) != null){
            response.append(inputLine);
        }

        reader.close();
        Assert.assertEquals(CUSTOM_STATUS_CODE,httpResponse.getStatusLine().getStatusCode());

    }

    @Test
    public void testHttpGetCallWithVirtualizedService() throws IOException{
        forGet(URL).doReturn(
                aMessage(CUSTOM_STATUS_CODE)
                    .withStringBody(RESPONSE_BODY_GET)
        );


        HttpGet httpGet = new HttpGet(URL);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        BufferedReader reader = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
        StringBuffer response = new StringBuffer();
        String inputLine;

        while ((inputLine = reader.readLine()) != null){
            response.append(inputLine);
        }

        reader.close();

        System.out.println("Response code : "+ httpResponse.getStatusLine().getStatusCode());
        System.out.println("Response body: "+response.toString());

        Assert.assertEquals(CUSTOM_STATUS_CODE,httpResponse.getStatusLine().getStatusCode());

    }

}

