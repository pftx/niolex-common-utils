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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is the class used for test serialization and compression.
 *
 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
 * @version 1.0.0
 * @Date: 2012-7-19
 */
public class Benchmark {

    private static int autoInc = 0;

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

    public static final Benchmark makeBenchmark() {
        Benchmark b = new Benchmark();
        b.setClassId(908123 + autoInc++);
        b.setPersonId(MockUtil.ranInt(Integer.MAX_VALUE));
        b.setJoinId(System.currentTimeMillis());
        b.setStatus(-1293 + autoInc);
        b.setPriv(9128 + autoInc);
        // ---
        b.setName("This is the compress test benchmark.");
        b.setClassName("93209i;lads93x2j0y9adfo0932awd;" + autoInc);
        b.setOk(true);
        b.setCrDate(new Date());
        b.setCurKick(System.nanoTime());
        List<Group> list = new ArrayList<Group>();
        b.setList(list);
        // ---
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
    public String toString() {
        return "Benchmark [joinId=" + joinId + ", priv=" + priv + ", className=" + className + ", curKick=" + curKick
                + "]";
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
        return this.curKick == other.curKick && this.personId == other.personId &&
                this.crDate.equals(other.crDate) && this.list.equals(other.list);
    }

	// All the Getters && Setters

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

    // -----------------------------------------------------------------------------
    // INNER STATIC NESTED CLASSES
    // -----------------------------------------------------------------------------

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
		private int price;

		private Map<String, Bean> beanMap;

        public static final Group makeGroup() {
			Group g = new Group();
			g.setGroupId((long)(MockUtil.ranInt(Integer.MAX_VALUE) << 32) + MockUtil.ranInt(Integer.MAX_VALUE));
			g.setGroupName("Benchmark Group - " + MockUtil.ranInt(12312));
			g.setGroupStatus(1280934 + autoInc);
			g.setPrice(autoInc + MockUtil.ranInt(1000));
			Map<String, Bean> beanMap = new HashMap<String, Benchmark.Bean>();
			g.setBeanMap(beanMap);
			beanMap.put("Qute", Bean.makeBean());
			beanMap.put("Eda", Bean.makeBean());
			beanMap.put("Rdet", Bean.makeBean());
			beanMap.put("Qrio", Bean.makeBean());
			return g;
		}

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof Group))
                return false;
            Group other = (Group) obj;
            if (beanMap == null) {
                if (other.beanMap != null)
                    return false;
            } else if (!beanMap.equals(other.beanMap))
                return false;
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
            if (groupStatus != other.groupStatus)
                return false;
            if (price != other.price)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Group [groupId=" + groupId + ", groupStatus=" + groupStatus + ", price=" + price + "]";
        }

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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public Map<String, Bean> getBeanMap() {
            return beanMap;
        }

        public void setBeanMap(Map<String, Bean> beanMap) {
            this.beanMap = beanMap;
        }

	}


	/**
	 * The third level of test.
	 *
	 * @author <a href="mailto:xiejiyun@gmail.com">Xie, Jiyun</a>
	 * @version 1.0.0
	 * @Date: 2012-7-23
	 */
	public static class Bean {
		private int likely;
		private String name;
		private long id;
		private Date birth;

		public static final Bean makeBean() {
		    return new Bean(autoInc, "Bennchmark Bean", System.nanoTime() + MockUtil.ranInt(939393), new Date());
		}

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

		@Override
        public String toString() {
            return "Bean [likely=" + likely + ", name=" + name + ", id=" + id + "]";
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


	}
}

