package org.jsp.app;



import javax.persistence.*;


/**
 * The persistent class for the user1 database table.
 * 
 */
@Entity
//@NamedQuery(name="User1.findAll", query="SELECT u FROM User1 u")
public class User1 {

	private String userName;
	@Id
	private String userEmail;

	

	private String userPassword;

	private String userPn;

	public User1() {
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPn() {
		return this.userPn;
	}

	public void setUserPn(String userPn) {
		this.userPn = userPn;
	}
	

}