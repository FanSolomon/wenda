package com.company.wenda.util;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freemarker;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String templates, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("我勒个去");
            InternetAddress from = new InternetAddress(nick + "<1041826928@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//            String result = VelocityEngineUtils
//                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            Template template = freemarker.getConfiguration().getTemplate(templates);
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            //mimeMessageHelper.setText("dfasdkfldf");
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //邮件发送服务器
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("1041826928@qq.com");
        mailSender.setPassword("xxxx");
        mailSender.setHost("smtp.qq.com");
        //mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");

        //加认证机制
        Properties javaMailProperties = new Properties();
        //javaMailProperties.put("mail.smtp.ssl.enable", true);
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.smtp.timeout", "25000");
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
