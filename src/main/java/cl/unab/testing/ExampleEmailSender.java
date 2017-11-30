package cl.unab.testing;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

class ExampleEmailSender {
    void sendEmail(Email email) throws EmailException {
        email.send();
    }
}
