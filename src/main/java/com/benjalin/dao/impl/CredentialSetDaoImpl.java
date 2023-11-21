package com.benjalin.dao.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.derby.jdbc.EmbeddedDriver;

import com.benjalin.dao.CredentialSetDaoInt;
import com.benjalin.dao.domain.CredentialSet;

public class CredentialSetDaoImpl implements CredentialSetDaoInt {
	
	
	String createTable = "create table credential_set(id integer primary key generated always as identity(Start with 1, Increment by 1), username varchar(25), encrypted_password varchar(50), target_app varchar(50), app_description varchar(50))";
	Connection conn = null;
	PreparedStatement preparedStatement;
	private String userHomeDir = System.getProperty("user.home");
    private String databaseDir = userHomeDir + "/.benjalinpm";
	
	public CredentialSetDaoImpl() {
		System.setProperty("derby.system.home", databaseDir);
		Properties properties = new Properties();
		properties.put("password", "pass123");
		Driver derbyEmbeddedDriver = new EmbeddedDriver();
		try {
			DriverManager.registerDriver(derbyEmbeddedDriver);
			conn = DriverManager.getConnection("jdbc:derby:benjalinpm;create=true", properties);
			conn.setAutoCommit(true);
			Statement statement = conn.createStatement();
			statement.execute(createTable);
		} catch (SQLException e) {
			if(e.getErrorCode() == 30000) {
				return;
			}
			e.printStackTrace();
			System.exit(0);
		}
	}

	public CredentialSet save(CredentialSet credentialSet) {
		try {
			preparedStatement = conn.prepareStatement("insert into credential_set(username, encrypted_password, target_app, app_description) values(?, ?, ?, ?)");
			preparedStatement.setString(1, credentialSet.getUsername());
			preparedStatement.setString(2, credentialSet.getEncryptedPassword());
			preparedStatement.setString(3, credentialSet.getTargetApp());
			preparedStatement.setString(4, credentialSet.getAppDescription());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return credentialSet;
	}
	
	public void delete(CredentialSet credentialSet) {
		try {
			preparedStatement = conn.prepareStatement("delete from credential_set where id = ?");
			preparedStatement.setLong(1, credentialSet.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public CredentialSet getById(long id) {
		CredentialSet credentialSet = null;
		try {
			preparedStatement = conn.prepareStatement("select * from credential_set where id=?");
			preparedStatement.setLong(1, id);
			ResultSet result = preparedStatement.executeQuery();
			if(result != null && result.next()) {
				credentialSet = new CredentialSet();
				credentialSet.setId(result.getLong("id"));
				credentialSet.setUsername(result.getString("username"));
				credentialSet.setTargetApp(result.getString("target_app"));
				credentialSet.setAppDescription(result.getString("app_description"));
				credentialSet.setEncryptedPassword(result.getString("encrypted_password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return credentialSet;
	}
	
	public CredentialSet update(CredentialSet credentialSet) {
		return this.getById(credentialSet.getId());
	}

	public Set<CredentialSet> getAll() {
		Set<CredentialSet> credentialSets = new HashSet<>();
		try {
			Statement preparedStatement = conn.createStatement();
			ResultSet result = preparedStatement.executeQuery("select * from credential_set");
			while(result != null && result.next()) {
				CredentialSet credentialSet = new CredentialSet();
				credentialSet = new CredentialSet();
				credentialSet.setId(result.getLong("id"));
				credentialSet.setUsername(result.getString("username"));
				credentialSet.setAppDescription(result.getString("app_description"));
				credentialSet.setTargetApp(result.getString("target_app"));
				credentialSets.add(credentialSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return credentialSets;
	}
	
	@Override
	public Set<CredentialSet> getAllFilteredByTargetApp(String appName) {
		Set<CredentialSet> credentialSets = new HashSet<>();
		try {
			Statement preparedStatement = conn.createStatement();
			ResultSet result = preparedStatement.executeQuery("select * from credential_set where target_app like '%" + appName + "%'");
			while(result != null && result.next()) {
				CredentialSet credentialSet = new CredentialSet();
				credentialSet = new CredentialSet();
				credentialSet.setId(result.getLong("id"));
				credentialSet.setUsername(result.getString("username"));
				credentialSet.setAppDescription(result.getString("app_description"));
				credentialSet.setTargetApp(result.getString("target_app"));
				credentialSets.add(credentialSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return credentialSets;
	}
}
