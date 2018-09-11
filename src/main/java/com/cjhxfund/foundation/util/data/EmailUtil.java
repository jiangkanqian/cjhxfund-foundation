package com.cjhxfund.foundation.util.data;


import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
 
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;
 
/**
 * Created by liguoqing on 2016/4/1.
 */
public class EmailUtil {
 
    @Test
    public void sendTxtMail() {
 
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        // 设置邮件服务器
        javaMailSender.setHost("smtp.aliyun.com");
        javaMailSender.setUsername("lgq@gmail.com");
        javaMailSender.setPassword("mima");
        javaMailSender.setDefaultEncoding("UTF-8");
 
        //
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人
        simpleMailMessage.setTo("lgq0218@foxmail.com"); // 也可以传一个数组 new String[] {"user1@gmail.com","user2@gmail.com"}
        simpleMailMessage.setFrom("lgq0218@aliyun.com");
        simpleMailMessage.setSubject("测试java邮件-简单文本的发送");
        simpleMailMessage.setText("这里是一段简单文本。");
 
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");  // 验证用户名和密码
        properties.put("mail.smtp.timeout","25000");
 
        javaMailSender.setJavaMailProperties(properties);
        // 发送邮件
        javaMailSender.send(simpleMailMessage);
 
        System.out.println("邮件已发送");
 
    }
 
 
 
    @Test
    public void sendHtmlMail() throws Exception {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        // 设置邮件服务器
        javaMailSender.setHost("smtp.aliyun.com");
        javaMailSender.setUsername("lgq@gmail.com");
        javaMailSender.setPassword("mima");
        javaMailSender.setDefaultEncoding("UTF-8");
 
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo("lgq0218@foxmail.com");
        mimeMessageHelper.setFrom("lgq0218@aliyun.com");
        mimeMessageHelper.setSubject("测试java邮件-html邮件的发送");
        StringBuilder str = new StringBuilder("<html><head></head><body><h1>");
        str.append("hello!this is spring mail test。<br/>");
        str.append("spring 邮件测试。<br/>");
        str.append("</h1></body></html>");
        // true 表示启用html
        mimeMessageHelper.setText(str.toString(),true);
 
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");  // 验证用户名和密码
        properties.put("mail.smtp.timeout","25000");
 
        javaMailSender.setJavaMailProperties(properties);
        // 发送邮件
        javaMailSender.send(mimeMessage);
 
        System.out.println("邮件已发送");
 
    }
 
 
    @Test
    public void sendAttachedImageMail() throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        // 设置邮件服务器
        javaMailSender.setHost("smtp.aliyun.com");
        javaMailSender.setUsername("lgq@gmail.com");
        javaMailSender.setPassword("mima");
        javaMailSender.setDefaultEncoding("UTF-8");
 
 
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // multipart模式
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setTo("lgq0218@foxmail.com");
        mimeMessageHelper.setFrom("lgq0218@aliyun.com");
        mimeMessageHelper.setSubject("测试java邮件-邮件包含图片的发送");
        StringBuilder str = new StringBuilder("<html><head></head><body><h1>");
        str.append("spring 邮件测试。<br/>");
        str.append("这个邮件中包含图片<br/>");
        str.append("<img src=\"cid:imageId\"/><br/>");  // cid为固定写法，imageId指定一个标识
        str.append("</h1></body></html>");
        // 启用html
        mimeMessageHelper.setText(str.toString(),true);
 
        FileSystemResource img = new FileSystemResource(new File("F:/test.jpg"));
        mimeMessageHelper.addInline("imageId", img);
 
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");  // 验证用户名和密码
        properties.put("mail.smtp.timeout","25000");
 
        javaMailSender.setJavaMailProperties(properties);
        // 发送邮件
        javaMailSender.send(mimeMessage);
 
        System.out.println("邮件已发送");
    }
 
 
    @Test
    public void sendAttendedFileMail() throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.aliyun.com");
        javaMailSender.setUsername("lgq@gmail.com");
        javaMailSender.setPassword("mima");
        javaMailSender.setDefaultEncoding("UTF-8");
 
 
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // multipart模式
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
        mimeMessageHelper.setTo("lgq0218@foxmail.com");
        mimeMessageHelper.setFrom("lgq0218@aliyun.com");
        mimeMessageHelper.setSubject("测试java邮件-邮件包含文件的发送");
        StringBuilder str = new StringBuilder("<html><head></head><body><h1>");
        str.append("spring 邮件测试。<br/>");
        str.append("这个邮件附件中包含文件<br/>");
        str.append("</h1></body></html>");
        mimeMessageHelper.setText(str.toString(),true);
 
        FileSystemResource file = new FileSystemResource(new File("F:/test.rar"));
        mimeMessageHelper.addAttachment("test.rar",file);
 
//        FileSystemResource file = new FileSystemResource(new File("F:/test.pdf"));
//        mimeMessageHelper.addAttachment("test.pdf",file);
 
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true"); // 验证用户名和密码
        properties.put("mail.smtp.timeout","25000");
 
        javaMailSender.setJavaMailProperties(properties);
        // 发送邮件
        javaMailSender.send(mimeMessage);
 
        System.out.println("邮件已发送");
 
    }
}
