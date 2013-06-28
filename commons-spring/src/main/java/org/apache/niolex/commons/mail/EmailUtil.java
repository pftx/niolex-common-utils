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

import org.apache.commons.lang.ArrayUtils;
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
 *
 * 1. public static void sendMail(
 *              String from,
 *              List tos,
 *              String title,
 *              String text,
 *              List<File> attachments,
 *              String priority,
 *              boolean isHtml,
 *              String encoding
 * );
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
     * @param from
     * @param to
     * @param title
     * @param text
     * @throws MailException
     * @throws MessagingException
     */
    public static void sendHtmlMail(String from, String to, String title, String text) throws MailException, MessagingException {
        sendMail(from, Collections.singletonList(to), title, text, null, null, true, "UTF-8");
    }

    /**
     * Send an email
     * 同步发送指定参数的邮件
     *
     * @param from email sender
     * @param tos email receiver
     * @param title email title
     * @param text email body
     * @param attachments a List<Pair<String, InputStreamSource>> attachements
     * @param priority priority from 1-5 higher - lower
     * @param isHtml is the text in html format or not
     * @param encoding the encoding of email, i.e. "GBK"、"UTF-8"
     * @throws MailException
     * @throws MessagingException
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
        sendMail(from, toArr, new String[] {from}, title, text, att, priority, isHtml, encoding);
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
     *            附件List<Pair<String, InputStreamSource>>
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
    public static void sendMail(String from, String[] tos, String[] ccs, String title, String text,
            List<Pair<String, InputStreamSource>> attachments, String priority, boolean isHtml,
            String encoding) throws MailException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, encoding);

        messageHelper.setFrom(from);

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
