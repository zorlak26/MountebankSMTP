package cl.unab.testing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;


public class ExampleHttpClientTest {

    private static final String BASIC_SMTP_IMPOSTER_FILENAME = "HTTPImposter.json";
    private static final String MOUNTEBANK_HOST = "localhost";

    private MountebankHandler mountebankHandler;

    @Before
    public void init() throws Exception{
        mountebankHandler = new MountebankHandler(MOUNTEBANK_HOST);
    }

    @Test
    public void givenValidURIthenGetResource() throws Exception{

        MountebankImposter imposter;
        MountebankImposter imposterResponse = null;
        try {
            imposter = mountebankHandler.parseImposterFromConfigFile(BASIC_SMTP_IMPOSTER_FILENAME);
            mountebankHandler.postImposter(imposter);

            ExampleHttpClient exampleHttpClient = new ExampleHttpClient();
            URI httpUri = mountebankHandler.prepareImposterTestURI(imposter.getPort());
            Assert.assertEquals("OK", exampleHttpClient.getResource(httpUri));

            imposterResponse = mountebankHandler.getImposter(imposter.getPort());

            Assert.assertNotNull(imposterResponse.getRequests());

        } finally{
            if (imposterResponse != null){
                mountebankHandler.deleteImposter(imposterResponse.getPort());
            }
        }
    }

    @After
    public void destroy() throws Exception{
        mountebankHandler.close();
    }
}