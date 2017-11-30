package cl.unab.testing;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

class ExampleHttpClient {

    private CloseableHttpClient httpClient;

    ExampleHttpClient(){
        httpClient = HttpClients.createDefault();
    }

    String getResource(URI uri) {
        try {
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
