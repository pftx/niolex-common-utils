/**
 * EmailUtilTest.java
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
package org.apache.niolex.commons.mail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-27
 */
public class EmailUtilTest {
	@Mock
	private JavaMailSender mailSender;
	@Mock
	private MimeMessage mime;

	@Before
	public void createEmailUtil() throws Exception {
		EmailUtil.setMailSender(mailSender);
		when(mailSender.createMimeMessage()).thenReturn(mime);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.mail.EmailUtil#sendMail(java.lang.String, java.util.List, java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean, java.lang.String)}.
	 * @throws Exception
	 * @throws MailException
	 */
	@Test
	public final void testSendMail() throws MailException, Exception {
		List<String> tos = new ArrayList<String>();
		tos.add("xiejiyun@gmail.com");
		tos.add("abc@163.com");
		File f = new File(System.getProperty("java.io.tmpdir"));
		List<File> lists = Arrays.asList(f.listFiles());
		EmailUtil.sendMail("xxx", tos, "test msg",
				"test msg body", lists , "1", false, "gbk");
		verify(mailSender).send(mime);
	}

	@Test
	public final void testSendMail3() throws MailException, Exception {
		List<String> tos = new ArrayList<String>();
		tos.add("xiejiyun@gmail.com");
		tos.add("abc@163.com");
		File f = new File(System.getProperty("java.io.tmpdir"));
		List<File> lists = Arrays.asList(f.listFiles());
		EmailUtil.sendMail("xxx", tos, "test msg",
				"test msg body", lists , null, false, "gbk");
		verify(mailSender).send(mime);
	}

	@Test(expected=IllegalArgumentException.class)
	public final void testSendMail2() throws MailException, Exception {
		List<String> tos = new ArrayList<String>();
		File f = new File(System.getProperty("java.io.tmpdir"));
		List<File> lists = Arrays.asList(f.listFiles());
		EmailUtil.sendMail("xxx", tos, "test msg",
				"test msg body", lists , "1", false, "gbk");
		verify(mailSender).send(mime);
	}

}
