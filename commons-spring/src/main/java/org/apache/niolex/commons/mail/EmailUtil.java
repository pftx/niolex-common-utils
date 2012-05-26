package org.apache.niolex.commons.mail;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * EmailUtil是一个利用Spring Email框架进行同步发送邮件的工具类
 * 
 * 目前提供的功能如下：
 * 
 * 1. public static void sendMail(
 *              String from, 
 *              List tos, 
 *              String title, 
 *              String text, 
 *              List<File> attachments, 
 *              String priority ,
 *              boolean isHtml, 
 *              String encoding
 * );
 * 同步发送指定参数的邮件
 * 
 * @category niolex-common-utils -> 公共库 -> 邮件处理
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2010-11-18$
 */
public class EmailUtil {

    /**
     * 邮件发送类，可在容器初始化时从配置文件读取配置对其进行赋值
     */
    private static JavaMailSender mailSender;

    public static void setMailSender(JavaMailSender mailSender) {
        EmailUtil.mailSender = mailSender;
    }

    /**
     * 同步发送指定参数的邮件
     * 
     * @param from
     *            邮件发送人
     * @param tos
     *            邮件收件人
     * @param title
     *            邮件标题
     * @param text
     *            邮件正文
     * @param attachments
     *            附件List<File>
     * @param priority
     *            邮件优先级1-5从高到低
     * @param isHtml
     *            是否以html方式发送(true->是，false->否)
     * @param encoding
     *            邮件编码格式"GBK"、"UTF-8"等
     * @throws MailException
     * @throws MessagingException
     *             抛出异常
     */
    public static void sendMail(String from, List<String> tos, String title, String text, List<File> attachments,
            String priority, boolean isHtml, String encoding) throws MailException, MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, encoding);

        messageHelper.setFrom(from);

        if (tos == null || tos.size() == 0) {
            throw new IllegalArgumentException("<tos> can not be null or empty!");
        } else {
            Iterator<String> it = tos.iterator();
            while (it.hasNext()) {
                messageHelper.addTo(it.next());
            }
        }

        messageHelper.setSubject(title);
        messageHelper.setText(text, isHtml);

        if (attachments != null) {
            for (File file : attachments) {
                messageHelper.addAttachment(file.getName(), file);
            }
        }

        mimeMessage = messageHelper.getMimeMessage();
        if (priority != null) {
            mimeMessage.addHeader("X-Priority", priority);
        }

        mailSender.send(mimeMessage);
    }
}
