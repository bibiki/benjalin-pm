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
	
	public CredentialSetDaoImpl() {
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
			// TODO Auto-generated catch block
			System.out.println("code code code " + e.getErrorCode());
			if(e.getErrorCode() == 30000) {
				return;
			}
			e.printStackTrace();
			System.exit(0);
		}
	}

	public CredentialSet save(CredentialSet credentialSet) {
		System.out.println("SAVING CREDETIAL SET");
		try {
			preparedStatement = conn.prepareStatement("insert into credential_set(username, encrypted_password, target_app, app_description) values(?, ?, ?, ?)");
			preparedStatement.setString(1, credentialSet.getUsername());
			preparedStatement.setString(2, credentialSet.getEncryptedPassword());
			preparedStatement.setString(3, credentialSet.getTargetApp());
			preparedStatement.setString(4, credentialSet.getAppDescription());
			int result = preparedStatement.executeUpdate();
			System.out.println("saved " + result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return credentialSet;
	}
	
	public void delete(CredentialSet credentialSet) {
		System.out.println("DELETING CREDENTIAL SET: " + credentialSet);
		try {
			preparedStatement = conn.prepareStatement("delete from credential_set where id = ?");
			preparedStatement.setLong(1, credentialSet.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CredentialSet getById(long id) {
		System.out.println("GETTING CREDENTIAL SET BY ID");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return credentialSet;
	}
	
	public CredentialSet update(CredentialSet credentialSet) {
		System.out.println("UPDATING CREDENTIAL SET");
		return this.getById(credentialSet.getId());
	}

	public Set<CredentialSet> getAll() {
		System.out.println("RETREIVING ALL CREDENTIAL SETS");
		Set<CredentialSet> credentialSets = new HashSet<>();
		try {
			Statement preparedStatement = conn.createStatement();
			ResultSet result = preparedStatement.executeQuery("select * from credential_set");
			System.out.println(result == null);
			while(result != null && result.next()) {
				System.out.println("u");
				CredentialSet credentialSet = new CredentialSet();
				credentialSet = new CredentialSet();
				credentialSet.setId(result.getLong("id"));
				credentialSet.setUsername(result.getString("username"));
				credentialSet.setAppDescription(result.getString("app_description"));
				credentialSet.setTargetApp(result.getString("target_app"));
				credentialSets.add(credentialSet);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			System.out.println(result == null);
			while(result != null && result.next()) {
				System.out.println("u");
				CredentialSet credentialSet = new CredentialSet();
				credentialSet = new CredentialSet();
				credentialSet.setId(result.getLong("id"));
				credentialSet.setUsername(result.getString("username"));
				credentialSet.setAppDescription(result.getString("app_description"));
				credentialSet.setTargetApp(result.getString("target_app"));
				credentialSets.add(credentialSet);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return credentialSets;
	}
}
