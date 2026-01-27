package com.benjalin.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "CREDENTIALS")
public class CredentialSet implements DomainObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	
	@Column(name = "USERNAME", nullable = false)
	private String username;
	
	@Column(name = "ENCRYPTED_PASSWORD", nullable = false)
	private String encryptedPassword;
	
	@Column(name = "TARGET_APP", nullable = true)
	private String targetApp;
	
	@Column(name = "APP_DESCRIPTION", nullable = false)
	private String appDescription;
	
	public CredentialSet() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getTargetApp() {
		return targetApp;
	}

	public void setTargetApp(String targetApp) {
		this.targetApp = targetApp;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	@Override
	public String toString() {
		return "CredentialSet [id=" + id + ", username=" + username + ", targetApp=" + targetApp + "]";
	}
}