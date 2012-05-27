package org.apache.niolex.commons.compress;

import java.util.Date;

public class TestBean {
	private int likely;
	private String name;
	private long id;
	private Date birth;


	public TestBean() {
		super();
	}

	public TestBean(int likely, String name, long id, Date birth) {
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
		if (obj instanceof TestBean) {
			TestBean other = (TestBean) obj;
			return this.birth.equals(other.birth) && this.name.equals(other.name)
					&& this.id == other.id && this.likely == other.likely;
		}
		return false;
	}

}