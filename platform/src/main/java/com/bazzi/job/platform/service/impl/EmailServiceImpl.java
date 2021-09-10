package com.bazzi.job.platform.service.impl;

import com.bazzi.job.platform.config.DefinitionProperties;
import com.bazzi.job.platform.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    @Resource
    private DefinitionProperties definitionProperties;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private Configuration configuration;

    public void sendTemplateMail(String[] sendTo, String title, String template, Map<String, Object> content) throws Exception {
        sendTemplateMail(sendTo, title, template, content, null);
    }

    public void sendTemplateMail(String[] sendTo, String title, String template, Map<String, Object> content,
                                 List<Pair<String, byte[]>> attachments) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        helper.setFrom(definitionProperties.getEmailFrom());
        helper.setTo(sendTo);
        helper.setSubject(title);

        Template t = configuration.getTemplate(template, StandardCharsets.UTF_8.name()); // freeMarker template
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, content);
        helper.setText(text, true);

        if (attachments != null) {
            for (Pair<String, byte[]> pair : attachments) {
                helper.addAttachment(pair.getFirst(), new ByteArrayResource(pair.getSecond()));
            }
        }
        mailSender.send(mimeMessage);

    }
}
