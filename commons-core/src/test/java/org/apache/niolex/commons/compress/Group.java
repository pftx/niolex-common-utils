/**
 * Group.java
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
package org.apache.niolex.commons.compress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.niolex.commons.test.MockUtil;

/**
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-19
 */
public class Group {

	private Long groupId;
	private String groupName;
	private int groupStatus;

	private List<CTBean> list;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(int groupStatus) {
		this.groupStatus = groupStatus;
	}

	public List<CTBean> getList() {
		return list;
	}

	public void setList(List<CTBean> list) {
		this.list = list;
	}

	public static final Group makeGroup() {
		Group g = new Group();
		g.setGroupId((long)(MockUtil.ranInt(Integer.MAX_VALUE) << 32) + MockUtil.ranInt(Integer.MAX_VALUE));
		g.setGroupName("gigidie" + MockUtil.ranInt(12312));
		g.setGroupStatus(1280934);
		List<CTBean> list = new ArrayList<CTBean>();
		g.setList(list);
		CTBean t = new CTBean(3, "Qute", 12212, new Date());
		list.add(t);
		t = new CTBean(4, "Eda", 1312, new Date());
		list.add(t);
		t = new CTBean(5, "Rdet", 1312, new Date());
		list.add(t);
		return g;
	}
}
