package com.benjalin.service;

import java.sql.SQLException;
import java.util.Set;

import com.benjalin.dao.domain.CredentialSet;

public interface CredentialSetServiceInt {

	CredentialSet save(CredentialSet credentialSet) throws SQLException ;
	
	void delete(CredentialSet credentialSet);
	
	void delete(long id);
	
	CredentialSet getById(long id);
	
	CredentialSet update(CredentialSet credentialSet);
	
	Set<CredentialSet> getAll();
	
	Set<CredentialSet> getFilteredByTargetApp(String appName);
	
	void putOnClipboard(String message);
	
	void scheduleClearingClipboard(long millis);
}