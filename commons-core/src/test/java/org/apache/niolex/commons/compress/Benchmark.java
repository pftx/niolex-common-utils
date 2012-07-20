/**
 * Benchmark.java
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
public class Benchmark {

	private int classId;
	private long personId;
	private long joinId;
	private int status;
	private int priv;
	public String name;
	private String className;
	private boolean isOk;
	private Date crDate;
	private long curKick;
	private List<Group> list;

	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public long getPersonId() {
		return personId;
	}
	public void setPersonId(long personId) {
		this.personId = personId;
	}
	public long getJoinId() {
		return joinId;
	}
	public void setJoinId(long joinId) {
		this.joinId = joinId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPriv() {
		return priv;
	}
	public void setPriv(int priv) {
		this.priv = priv;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public boolean isOk() {
		return isOk;
	}
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
	public Date getCrDate() {
		return crDate;
	}
	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}
	public long getCurKick() {
		return curKick;
	}
	public void setCurKick(long curKick) {
		this.curKick = curKick;
	}
	public List<Group> getList() {
		return list;
	}
	public void setList(List<Group> list) {
		this.list = list;
	}

	public static final Benchmark makeBenchmark() {
		Benchmark b = new Benchmark();
		b.setClassId(908123);
		b.setClassName("93209i;lads93209adfo0932awd");
		b.setCrDate(new Date());
		b.setCurKick(System.nanoTime());
		b.setJoinId(System.currentTimeMillis());
		b.setName("This is the compress test benchmark.");
		b.setOk(true);
		b.setPersonId(MockUtil.ranInt(Integer.MAX_VALUE));
		b.setPriv(9128);
		b.setStatus(-1293);
		List<Group> list = new ArrayList<Group>();
		b.setList(list);
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		list.add(Group.makeGroup());
		return b;
	}
}

