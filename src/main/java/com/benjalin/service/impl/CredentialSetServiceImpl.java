package com.benjalin.service.impl;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Set;

import javax.inject.Inject;

import com.benjalin.dao.CredentialSetDaoInt;
import com.benjalin.dao.domain.CredentialSet;
import com.benjalin.service.CredentialSetServiceInt;

public class CredentialSetServiceImpl implements CredentialSetServiceInt {

	@Inject
	private CredentialSetDaoInt credentialSetDaoInt;
	
	public CredentialSet save(CredentialSet credentialSet) {
		return credentialSetDaoInt.save(credentialSet);
	}
	
	public CredentialSet update(CredentialSet credentialSet) {
		return credentialSetDaoInt.update(credentialSet);
	}
	
	public CredentialSet getById(long id) {
		return credentialSetDaoInt.getById(id);
	}
	
	public void delete(CredentialSet credentialSet) {
		credentialSetDaoInt.delete(credentialSet);
	}
	
	public void delete(long id) {
		CredentialSet credentialSet = credentialSetDaoInt.getById(id);
		credentialSetDaoInt.delete(credentialSet);
	}
	
	public Set<CredentialSet> getAll() {
		return credentialSetDaoInt.getAll();
	}
	
	public void putOnClipboard(String message) {
		StringSelection selection = new StringSelection(message);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
	
	public void scheduleClearingClipboard(long millis) {
		new Thread(() -> {
			try {
				Thread.sleep(millis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			putOnClipboard("");
		}, "Overwriting clipboard").start();
	}
	
	@Override
	public Set<CredentialSet> getFilteredByTargetApp(String appName) {
		return credentialSetDaoInt.getAllFilteredByTargetApp(appName);
	}
}
