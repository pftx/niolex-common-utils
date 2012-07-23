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
package org.apache.niolex.commons.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This is the class used for test serialization and compression.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-19
 */
public class Benchmark {

	/**
	 * All these fields are useless, just for test serialization and compression.
	 */
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

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj == null || !(obj instanceof Benchmark)) {
			return false;
		}
		Benchmark other = (Benchmark)obj;
		return this.curKick == other.curKick && this.personId == other.personId;
	}


	/**
	 * The inner nested class, for the test.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-23
	 */
	public static class Group {

		private Long groupId;
		private String groupName;
		private int groupStatus;

		private List<Bean> list;

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

		public List<Bean> getList() {
			return list;
		}

		public void setList(List<Bean> list) {
			this.list = list;
		}

		public static final Group makeGroup() {
			Group g = new Group();
			g.setGroupId((long)(MockUtil.ranInt(Integer.MAX_VALUE) << 32) + MockUtil.ranInt(Integer.MAX_VALUE));
			g.setGroupName("gigidie" + MockUtil.ranInt(12312));
			g.setGroupStatus(1280934);
			List<Bean> list = new ArrayList<Bean>();
			g.setList(list);
			Bean t = new Bean(3, "Qute", 12212, new Date());
			list.add(t);
			t = new Bean(4, "Eda", 1312, new Date());
			list.add(t);
			t = new Bean(5, "Rdet", 1312, new Date());
			list.add(t);
			return g;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
			result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
			result = prime * result + ((list == null) ? 0 : list.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Group other = (Group) obj;
			if (groupId == null) {
				if (other.groupId != null)
					return false;
			} else if (!groupId.equals(other.groupId))
				return false;
			if (groupName == null) {
				if (other.groupName != null)
					return false;
			} else if (!groupName.equals(other.groupName))
				return false;
			if (list == null) {
				if (other.list != null)
					return false;
			} else if (!list.equals(other.list))
				return false;
			return true;
		}

	}


	/**
	 * The third level of test.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-23
	 */
	public static  class Bean {
		private int likely;
		private String name;
		private long id;
		private Date birth;


		public Bean() {
			super();
		}

		public Bean(int likely, String name, long id, Date birth) {
			super();
			this.likely = likely;
			this.name = name;
			this.id = id;
			this.birth = birth;
		}

		public int getLikely() {
			return likely;
		}
		public void setLikely(int likely) {
			this.likely = likely;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public Date getBirth() {
			return birth;
		}
		public void setBirth(Date birth) {
			this.birth = birth;
		}

		@Override
		public boolean equals(Object obj) {
			if (super.equals(obj)) {
				return true;
			}
			if (obj instanceof Bean) {
				Bean other = (Bean) obj;
				return this.birth.equals(other.birth) && this.name.equals(other.name)
						&& this.id == other.id && this.likely == other.likely;
			}
			return false;
		}

	}
}

