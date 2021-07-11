package br.com.labestudo.api.service;

import br.com.labestudo.api.model.dto.EmailDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmailService implements EmailSender {

    private final JavaMailSender javaMailSender;

    private final Configuration freemarkerConfiguration;

    @Override
    public void send(EmailDto emailDto) {
        try {
            var corpo = processarModelo(emailDto);
            var mimeMessage = javaMailSender.createMimeMessage();
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom("Lab Estudo <labestudo2021@gmail.com>");
            mimeMessageHelper.setTo(emailDto.getRecipients().toArray(new String[0]));
            mimeMessageHelper.setSubject(emailDto.getSubject());
            mimeMessageHelper.setText(corpo, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception ignored) {

        }
    }

    public String processarModelo(EmailDto message) throws IOException, TemplateException {
        var template = freemarkerConfiguration.getTemplate(message.getTemplate());
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getValues());
    }
}
