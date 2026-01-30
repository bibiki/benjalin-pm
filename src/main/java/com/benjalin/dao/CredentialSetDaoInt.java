package com.benjalin.dao;

import java.sql.SQLException;
import java.util.Set;

import com.benjalin.dao.domain.CredentialSet;

public interface CredentialSetDaoInt {

	CredentialSet save(CredentialSet credentialSet) throws SQLException ;
	
	void delete(CredentialSet credentialSet);
	
	CredentialSet getById(long id);
	
	CredentialSet update(CredentialSet credentialSet);
	
	Set<CredentialSet> getAll();
	
	Set<CredentialSet> getAllFilteredByTargetApp(String appName);
}
