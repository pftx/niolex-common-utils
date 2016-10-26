/**
 * EmailUtil.java
 *
 * Copyright 2011 Niolex, Inc.
 *
 * Niolex licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.commons.mail;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.niolex.commons.bean.Pair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.google.common.collect.Lists;

/**
 * EmailUtil是一个利用Spring Email框架进行同步发送邮件的工具类
 *
 * 目前提供的功能如下：
 * <pre>
 * 1. public static void sendMail(
 *              String from,
 *              List&lt;String&gt; tos,
 *              String title,
 *              String text,
 *              List&lt;File&gt; attachments,
 *              String priority,
 *              boolean isHtml,
 *              String encoding
 * );</pre>
 * 同步发送指定参数的邮件
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @since 2010-11-18
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
     * Send a HTML email to this person.
     *
     * @param from the email sender
     * @param to the email receiver
     * @param title the email title
     * @param text the email body
     * @throws MailException in case of failure when sending the message
     * @throws MessagingException if failed to create multi-part email message
     */
    public static void sendHtmlMail(String from, String to, String title, String text)
            throws MailException, MessagingException {
        sendMail(from, Collections.singletonList(to), title, text, null, null, true, "UTF-8");
    }

    /**
     * Send an email.
     * 同步发送指定参数的邮件
     *
     * @param from the email sender
     * @param tos the email receiver list
     * @param title the email title
     * @param text the email body
     * @param attachments a list of files attachments
     * @param priority priority from 1-5 the smaller is higher
     * @param isHtml is the text in HTML format or not
     * @param encoding the encoding of email, i.e. "GBK"、"UTF-8"
     * @throws MailException in case of failure when sending the message
     * @throws MessagingException if failed to create multi-part email message
     */
    public static void sendMail(String from, List<String> tos, String title, String text, List<File> attachments,
            String priority, boolean isHtml, String encoding) throws MailException, MessagingException {
        List<Pair<String, InputStreamSource>> att = null;
        if (attachments != null) {
            att = Lists.newArrayList();
            for (File file : attachments) {
                InputStreamSource source = new FileSystemResource(file);
                Pair<String, InputStreamSource> pair = Pair.create(file.getName(), source);
                att.add(pair);
            }
        }
        String[] toArr = null;
        if (tos != null) {
            toArr = tos.toArray(new String[tos.size()]);
        }
        sendMail(from, toArr, null, title, text, att, priority, isHtml, encoding);
    }

    /**
     * Send an email.
     * 同步发送指定参数的邮件
     *
     * @param from the email sender
     * @param tos the email receivers array
     * @param ccs the carbon copiers array
     * @param title the email title
     * @param text the email body
     * @param attachments the email attachments list
     * @param priority priority from 1-5 the smaller is higher
     * @param isHtml is the text in HTML format or not
     * @param encoding the encoding of email, i.e. "GBK"、"UTF-8"
     * @throws MailException in case of failure when sending the message
     * @throws MessagingException if failed to create multi-part email message
     */
    public static void sendMail(String from, String[] tos, String[] ccs, String title, String text,
            List<Pair<String, InputStreamSource>> attachments, String priority, boolean isHtml,
            String encoding) throws MailException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, encoding);

        messageHelper.setFrom(from);
        messageHelper.setBcc(from);

        if (ArrayUtils.isEmpty(tos)) {
            throw new IllegalArgumentException("<tos> can not be null or empty!");
        } else {
            messageHelper.setTo(tos);
        }

        if (!ArrayUtils.isEmpty(ccs)) {
            messageHelper.setCc(ccs);
        }

        messageHelper.setSubject(title);
        messageHelper.setText(text, isHtml);

        if (attachments != null) {
            for (Pair<String, InputStreamSource> pair : attachments) {
                messageHelper.addAttachment(pair.a, pair.b);
            }
        }

        mimeMessage = messageHelper.getMimeMessage();
        if (priority != null) {
            mimeMessage.addHeader("X-Priority", priority);
        }

        mailSender.send(mimeMessage);
    }

}
