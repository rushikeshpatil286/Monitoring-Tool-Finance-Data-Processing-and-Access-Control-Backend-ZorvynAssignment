package com.MonitoringTool.Entities;

public class Admin {

	private Long adminid;
	
	private String adminname;
	
	private String adminemail;
	
	private String adminpassword;
	
	private String adminrole;

	public Long getAdminid() {
		return adminid;
	}

	public void setAdminid(Long adminid) {
		this.adminid = adminid;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getAdminemail() {
		return adminemail;
	}

	public void setAdminemail(String adminemail) {
		this.adminemail = adminemail;
	}

	public String getAdminpassword() {
		return adminpassword;
	}

	public void setAdminpassword(String adminpassword) {
		this.adminpassword = adminpassword;
	}

	public String getAdminrole() {
		return adminrole;
	}

	public void setAdminrole(String adminrole) {
		this.adminrole = adminrole;
	}

	@Override
	public String toString() {
		return "Admin [adminid=" + adminid + ", adminname=" + adminname + ", adminemail=" + adminemail
				+ ", adminpassword=" + adminpassword + ", adminrole=" + adminrole + "]";
	}

	public Admin(Long adminid, String adminname, String adminemail, String adminpassword, String adminrole) {
		super();
		this.adminid = adminid;
		this.adminname = adminname;
		this.adminemail = adminemail;
		this.adminpassword = adminpassword;
		this.adminrole = adminrole;
	}	
	
}
