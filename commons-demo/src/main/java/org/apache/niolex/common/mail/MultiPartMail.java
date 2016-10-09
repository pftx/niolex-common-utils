/**
 * MultiPartMail.java
 *
 * Copyright 2012 Niolex, Inc.
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
package org.apache.niolex.common.mail;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0, $Date: 2012-11-25$
 */
public class MultiPartMail {

    public static void main(String[] args) throws IOException, EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Apache logo");
        attachment.setName("Apache_logo.gif");

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setDebug(true);
        // Set email host
        email.setHostName("10.7.2.18");
        // email.setAuthentication("nice_to_meet", "asf_logo_me");
        // email.setSSL(true);
        // Set email from
        email.setFrom("commons.email@live.com", "Commons Email");
        email.setBounceAddress("lei.gao@renren-inc.com");
        // Set email content
        email.addTo("jiyun.xie@live.com", "Jiyun Xie");
        email.setSubject("Foll Alert The Git test");
        email.setMsg("Here is Apache's logo, please enjoy it!");

        // add the attachment
        email.attach(attachment);

        // send the email
        email.send();
    }

}
