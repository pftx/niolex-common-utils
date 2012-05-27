/**
 * EmailProxyTest.java
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-5-27
 */
public class EmailProxyTest {
	@Mock
	private JavaMailSender mailSender;
	@Mock
	private MimeMessage mime;
	private EmailProxy emailProxy;

	@Before
	public void createEmailProxy() throws Exception {
		emailProxy = new EmailProxy();
		emailProxy.setMailSender(mailSender);
		when(mailSender.createMimeMessage()).thenReturn(mime);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.mail.EmailProxy#sendEmail(java.lang.String, boolean)}.
	 */
	@Test
	public final void testSendEmail() {
		emailProxy.setEncoding("utf-8");
		emailProxy.setFrom("xiejiyun@gmail.com");
		emailProxy.setTo("xiejiyun@gmail.com, abc@163.com");
		emailProxy.setTitle("Not yet implemented");
		boolean r = emailProxy.sendEmail("This is a general test message.", false);
		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		System.out.println(captor.getValue());
		assertTrue(r);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.mail.EmailProxy#setTo(java.lang.String)}.
	 */
	@Test
	public final void testSetTo() {
		emailProxy.setEncoding("utf-8");
		emailProxy.setFrom("xiejiyun@gmail.com");
		emailProxy.setTitle("Not yet implemented");
		boolean r = emailProxy.sendEmail("This is a general test message.", false);
		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender, never()).send(captor.capture());
		assertFalse(r);
	}

	/**
	 * Test method for {@link org.apache.niolex.commons.mail.EmailProxy#setTo(java.lang.String)}.
	 */
	@Test
	public final void testSetToString() {
		emailProxy.setEncoding("utf-8");
		emailProxy.setFrom("xiejiyun@gmail.com");
		emailProxy.setTitle("Not yet implemented");
		List<String> tos = new ArrayList<String>();
		tos.add("xiejiyun@gmail.com");
		tos.add("abc@163.com");
		emailProxy.setTo(tos );
		emailProxy.sendEmail("This is a general test message.", false);
		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailSender).send(captor.capture());
		System.out.println(captor.getValue());
	}

}
