package root.app.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSenderUtil {

    private static final String MAIL_SYSTEM = "dohongkien2003@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom(MAIL_SYSTEM);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
    }
}
