package cl.unab.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

class MountebankHandler {

    private static final String MOUNTEBACK_SCHEME = "http";
    private static final String MOUNTEBACK_IMPOSTERS_PATH = "/imposters";
    private static final String MOUNTEBANK_TEST_PATH = "/test";
    private static final int MOUNTEBACK_PORT = 2525;

    private CloseableHttpClient mountebackHttpClient;
    private ObjectMapper mapper;
    private String mountebankHost = null;

    MountebankHandler(String mountebankHost) {
        this.mountebankHost = mountebankHost;
        mountebackHttpClient = HttpClients.createDefault();
        mapper = new ObjectMapper();
    }

    void deleteImposter(int port) throws Exception {
        URI uri = prepareImposterURIWithPort(port);
        HttpDelete httpDelete = new HttpDelete(uri);
        mountebackHttpClient.execute(httpDelete);
    }

    MountebankImposter getImposter(int port) throws Exception {
        URI uri = prepareImposterURIWithPort(port);
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = mountebackHttpClient.execute(httpGet);
        return parseImposterFromResponse(response);
    }

    private MountebankImposter parseImposterFromResponse(HttpResponse response)
            throws IOException {
        return mapper.readValue(EntityUtils.toString(response.getEntity()),
                MountebankImposter.class);
    }

    private URI prepareImposterURIWithPort(int port) {
        try {
            return new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                    .setPath(MOUNTEBACK_IMPOSTERS_PATH + "/" + port).setPort(MOUNTEBACK_PORT).build();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    URI prepareImposterTestURI(int port){
        try {
            return new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                    .setPath(MOUNTEBANK_TEST_PATH + "/" + port).setPort(port).build();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    void postImposter(MountebankImposter imposter) throws Exception {
        URI uri = prepareImposterURI();
        HttpEntity imposterEntity = preparePostEntity(imposter);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(imposterEntity);
        mountebackHttpClient.execute(httpPost);
    }

    private HttpEntity preparePostEntity(MountebankImposter imposter)
            throws UnsupportedEncodingException, JsonProcessingException {
        StringEntity imposterEntity = new StringEntity(mapper.writeValueAsString(imposter));
        imposterEntity.setContentType("application/json");
        return imposterEntity;
    }

    MountebankImposter parseImposterFromConfigFile(String imposterDefinitionFilename)
            throws IOException {
        return this.mapper.readValue(ClassLoader.getSystemResource(imposterDefinitionFilename),
                MountebankImposter.class);
    }

    private URI prepareImposterURI() {
        try {
            return new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                    .setPath(MOUNTEBACK_IMPOSTERS_PATH).setPort(MOUNTEBACK_PORT).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    void close() throws IOException {
        if (mountebackHttpClient != null) {
            mountebackHttpClient.close();
        }
    }
}
