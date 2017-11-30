package cl.unab.testing;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExampleEmailSenderTest {

    private static final String MOUNTEBANK_HOST = "localhost";
    private static final String EMAIL_FROM = "mark.shuckerberg@facebook.com";
    private static final String EMAIL_TO = "carlos.rios.stange@gmail.com";
    private static final String EMAIL_SUBJECT = "Te has ganado el terrible premio!";
    private static final String EMAIL_CONTENT = "Eshtimado ususrio, debido a que usted es un fiel seguidor... ";
    private static final String BASIC_SMTP_IMPOSTER_FILENAME = "SMTPImposter.json";

    private MountebankHandler mountebankHandler;

    @Before
    public void init(){
        mountebankHandler = new MountebankHandler(MOUNTEBANK_HOST);
    }

    @Test
    public void givenValidEmailthenSendEmail() throws Exception {

        ExampleEmailSender exampleEmailSender = new ExampleEmailSender();
        MountebankImposter imposter = null;
        try {
            imposter = mountebankHandler.parseImposterFromConfigFile(BASIC_SMTP_IMPOSTER_FILENAME);
            mountebankHandler.postImposter(imposter);
            exampleEmailSender.sendEmail(createValidTestEmail(imposter.getPort()));
            MountebankImposter imposterResponse = mountebankHandler.getImposter(imposter.getPort());

            Assert.assertNotNull(imposterResponse.getRequests());

        } finally{
            if (imposter != null){
                mountebankHandler.deleteImposter(imposter.getPort());
            }
        }
    }

    @After
    public void destroy() throws Exception{
        mountebankHandler.close();
    }

    private Email createValidTestEmail(int port){
        Email email = new SimpleEmail();
        email.setHostName(MOUNTEBANK_HOST);
        email.setSmtpPort(port);
        email.setSubject(EMAIL_SUBJECT);
        try {
            email.setFrom(EMAIL_FROM);
            email.setMsg(EMAIL_CONTENT);
            email.addTo(EMAIL_TO);
        } catch (EmailException e) {
            Assert.fail();
        }
        return email;
    }
}