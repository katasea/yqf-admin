package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.common.CommonUtil;

import java.io.Serializable;

/**
 * 通知消息
 */
@Table(name = "PM_NOTICE")
public class NoticeInfo implements Serializable{
	private static final long serialVersionUID = 5026185157773936431L;

	@Column(flag = "primary",autoGenneral=true)
	private String pkid;
	@Column(oth="NOT NULL")
	private String title;
	@Column(type = "varchar(1000)",oth="NOT NULL")
	private String content;
	//2018-01-01 12:33:22
	@Column(type = "char(19)",oth="NOT NULL")
	private String senttime;
	@Column(oth="NOT NULL")
	private String fromwho;
	@Column(oth="NOT NULL")
	private String fromwhoname;
	//待实现单点聊天功能。
	private String towho;
	//1 系统通知 email.png 2 用户通知 message.png 3 用户提醒 clock.png
	@Column
	private int type;

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if(CommonUtil.isNotEmpty(title)) {
			title = title.replace("'","‘");
			title = title.replace("\"","“");
		}
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(CommonUtil.isNotEmpty(content)) {
			content = content.replace("'", "‘");
			content = content.replace("\"","“");
		}
		this.content = content;
	}

	public String getSenttime() {
		return senttime;
	}

	public void setSenttime(String senttime) {
		this.senttime = senttime;
	}

	public String getFromwho() {
		return fromwho;
	}

	public void setFromwho(String fromwho) {
		this.fromwho = fromwho;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFromwhoname() {
		return fromwhoname;
	}

	public void setFromwhoname(String fromwhoname) {
		this.fromwhoname = fromwhoname;
	}

	public String getTowho() {
		return towho;
	}
	public void setTowho(String towho) {
		this.towho = towho;
	}
}
