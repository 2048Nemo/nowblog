package top.rabbitbyte.nowblog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content){
        try {
            org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }
        // 异步发送邮件
        // mailSender.send(message);

    }

    public void  sendMailWithHTML(String to, String subject, String content){

        Context context = new org.thymeleaf.context.Context();
        context.setVariable("username","sunday");

        String emailContent = templateEngine.process("mail/demo", context);
        System.out.println(emailContent);
        sendMail(to,subject,emailContent);
    }

 }
