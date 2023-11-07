package com.benjalin.config;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.benjalin.dao.CredentialSetDaoInt;
import com.benjalin.dao.impl.CredentialSetDaoImpl;
import com.benjalin.gui.ActionsPanel;
import com.benjalin.gui.MainPanel;
import com.benjalin.service.CredentialSetServiceInt;
import com.benjalin.service.EncryptionDecryptionServiceInt;
import com.benjalin.service.impl.CredentialSetServiceImpl;
import com.benjalin.service.impl.EncryptionDecryptionServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class GuiceConfiguration extends AbstractModule {

	@Override
	protected void configure() {
		bind(JPanel.class).annotatedWith(Names.named("mainPanel")).to(MainPanel.class);
		bind(JPanel.class).annotatedWith(Names.named("actionsPanel")).to(ActionsPanel.class);
		bind(JTable.class).annotatedWith(Names.named("passwordsInDB")).to(JTable.class);
		bind(EncryptionDecryptionServiceInt.class).to(EncryptionDecryptionServiceImpl.class);
		bind(CredentialSetServiceInt.class).to(CredentialSetServiceImpl.class);
		bind(CredentialSetDaoInt.class).to(CredentialSetDaoImpl.class);
	}
}
