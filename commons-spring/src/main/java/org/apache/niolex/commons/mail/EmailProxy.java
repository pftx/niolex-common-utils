/**
 * EmailProxy.java
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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;


/**
 * 能够发送邮件的代理类
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 *
 * @version 1.0.0, $Date: 2011-8-9$
 *
 */
public class EmailProxy {

    private static final Logger LOG = LoggerFactory.getLogger(EmailProxy.class);

    private String from;
    private List<String> to;
    private String title;
    private String encoding;

    /**
     * Send an ordinary email with no attachment.
     *
     * @param text the email body
     * @param isHtml whether the body is email or not
     * @return true if success, false otherwise
     */
    public boolean sendEmail(String text, boolean isHtml) {
        try {
            EmailUtil.sendMail(from, to, title, text, null, "1", isHtml, encoding);
            return true;
        } catch (Exception e) {
            LOG.warn("Failed to send email.", e);
            return false;
        }
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        if (to != null) {
            String[] tos = to.split(" *, *");
            this.to = Arrays.asList(tos);
        }
    }

    public void setTo(List<String> to) {
		this.to = to;
	}

	public void setTitle(String title) {
        this.title = title;
    }

    public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setMailSender(JavaMailSender mailSender) {
        EmailUtil.setMailSender(mailSender);
    }
}
