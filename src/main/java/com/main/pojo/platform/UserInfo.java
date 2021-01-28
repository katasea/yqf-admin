package com.main.pojo.platform;

import java.io.Serializable;
import java.util.List;

import com.common.auto.annotation .Column;
import com.common.auto.annotation.Table;
import com.common.CommonUtil;

/**
 * 用户信息
 *
 * @author CFQ
 */
@Table(name = "PM_USER")
public class UserInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 47138820556225178L;
	/**
	 * 用户编号【用于登录】
	 */
	@Column(flag = "primary")
	private String userid;
	/**
	 * 用户电话
	 */
	@Column(type = "varchar(11)")
	private String phone;
	/**
	 * 邮件地址
	 */
	@Column()
	private String email;

	/**
	 * 用户名称
	 */
	@Column(oth = "NOT NULL")
	private String username;

	/**
	 * 用户助记码
	 */
	@Column(keyWordFilte = true)
	private String zjm;
	/**
	 * 用户密码
	 */
	@Column(oth = "NOT NULL")
	private String password;
	/**
	 * 是否启用
	 * 1 启用  2 锁定 3 删除
	 */
	@Column
	private String enable;
	/**
	 * 生日
	 */
	@Column(type = "varchar(10)")
	private String birthday;
	/**
	 * 性别
	 */
	@Column(type = "int")
	private String sex;

	private String sexText;
	/**
	 * 人员岗位
	 */
	@Column
	private String post;

	private String postText;
	/**
	 * 人员类型
	 */
	@Column
	private String userstyle;

	private String userstyleText;
	/**
	 * 身份证号
	 */
	@Column
	private String idcard;
	/**
	 * 人员状态
	 */
	@Column
	private String userstatus;

	private String userstatusText;
	/**
	 * 入职时间
	 */
	@Column(type = "varchar(10)")
	private String entrytime;
	/**
	 * 所属部门
	 */
	@Column(oth = "NOT NULL")
	private String deptid;

	/**
	 * 所属部门名称
	 */
	private String deptname;

	/**
	 * 所属公司单位
	 */
	@Column(oth = "NOT NULL")
	private String companyid;

	/**
	 * 所属公司单位名称
	 */
	private String companyname;

	/**
	 * 获取菜单来源，是由角色关联获取的这个菜单[1]，还是由本身特殊设置关联的菜单[2]，用于菜单查看所属用户界面使用UserMgr
	 */
	private String resfrom;

	/**
	 * socket information
	 */
	@Column
	private Short connected;
	@Column
	private Long mostsignbits;
	@Column
	private Long leastsignbits;
	@Column
	private String lastconnecteddate;

	private List<RoleInfo> roleInfos;
	private boolean isAdmin;
	//======================GETTERS && SETTERS=================//


	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public List<RoleInfo> getRoleInfos() {
		return roleInfos;
	}

	public void setRoleInfos(List<RoleInfo> roleInfos) {
		this.roleInfos = roleInfos;
	}

	public String getSexText() {
		return sexText;
	}

	public void setSexText(String sexText) {
		this.sexText = sexText;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getUserstyleText() {
		return userstyleText;
	}

	public void setUserstyleText(String userstyleText) {
		this.userstyleText = userstyleText;
	}

	public String getUserstatusText() {
		return userstatusText;
	}

	public void setUserstatusText(String userstatusText) {
		this.userstatusText = userstatusText;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPost() {
		return CommonUtil.nullToStr(post);
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getUserstyle() {
		return userstyle;
	}

	public void setUserstyle(String userstyle) {
		this.userstyle = userstyle;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}

	public String getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}

	public String getResfrom() {
		return resfrom;
	}

	public void setResfrom(String resfrom) {
		this.resfrom = resfrom;
	}

	public String getZjm() {
		return zjm;
	}

	public void setZjm(String zjm) {
		this.zjm = zjm;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		switch (enable) {
			case "1":
				this.enable = "1";
				break;
			case "2":
				this.enable = "2";
				break;
			case "3":
				this.enable = "3";
				break;
			default : this.enable = "2";
		}
	}

	@Override
	public String toString() {
		return "Userid:" + userid + ",username:" + username + ",password:" + password + ",enable:" + enable;
	}

	public Short getConnected() {
		return connected;
	}

	public void setConnected(Short connected) {
		this.connected = connected;
	}

	public Long getMostsignbits() {
		return mostsignbits;
	}

	public void setMostsignbits(Long mostsignbits) {
		this.mostsignbits = mostsignbits;
	}

	public Long getLeastsignbits() {
		return leastsignbits;
	}

	public void setLeastsignbits(Long leastsignbits) {
		this.leastsignbits = leastsignbits;
	}

	public String getLastconnecteddate() {
		return lastconnecteddate;
	}

	public void setLastconnecteddate(String lastconnecteddate) {
		this.lastconnecteddate = lastconnecteddate;
	}
}
