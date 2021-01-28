package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 用户-通知消息 关联信息
 */
@Table(name = "PM_USER_NOTICE")
public class UserNoticeRela implements Serializable{

	private static final long serialVersionUID = -4764720049818123876L;

	@Column(oth="NOT NULL")
	private String userpkid;
	@Column(oth="NOT NULL")
	private String noticepkid;
	//0未读 1已读
	@Column
	private int state;

	public String getUserpkid() {
		return userpkid;
	}

	public void setUserpkid(String userpkid) {
		this.userpkid = userpkid;
	}

	public String getNoticepkid() {
		return noticepkid;
	}

	public void setNoticepkid(String noticepkid) {
		this.noticepkid = noticepkid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
