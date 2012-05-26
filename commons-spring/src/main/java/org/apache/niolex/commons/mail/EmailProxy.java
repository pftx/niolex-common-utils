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

import org.apache.niolex.commons.stream.LimitRateInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;


/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * 
 * @version 1.0.0, $Date: 2011-8-9$
 * 
 */
public class EmailProxy {

    private static final Logger log = LoggerFactory.getLogger(LimitRateInputStream.class);
    private String from;
    private List<String> to;
    private String title;
    private String encoding;
    
    public void sendEmail(String text, boolean isHtml) {
        try {
            EmailUtil.sendMail(from, to, title, text, null, "1", isHtml, encoding);
        } catch (Exception e) {
            log.warn("Failed to send email.", e);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMailSender(JavaMailSender mailSender) {
        EmailUtil.setMailSender(mailSender);
    }
}
