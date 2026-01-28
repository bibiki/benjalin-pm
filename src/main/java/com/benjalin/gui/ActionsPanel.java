package com.benjalin.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.benjalin.dao.domain.CredentialSet;
import com.benjalin.service.CredentialSetServiceInt;
import com.benjalin.service.EncryptionDecryptionServiceInt;
import com.google.common.base.Strings;
import com.google.inject.name.Named;

public class ActionsPanel extends JPanel {

	private static final long serialVersionUID = -3750331948757741234L;
	private JLabel filterApps = new JLabel("Filter apps");
	private JTextField filterAppTextField = new JTextField(15);
	private final JButton copyToClipboard = new JButton();
	private final JButton removeThisCredentialSet = new JButton();
	private final JTable passwordsInDB;
	private final CredentialSetServiceInt credentialSetServiceInt;
	private final EncryptionDecryptionServiceInt encryptor;
	
	@Inject
	public ActionsPanel(@Named("passwordsInDB") JTable passwordsInDB, CredentialSetServiceInt credentialSetServiceInt, EncryptionDecryptionServiceInt encryptor) {
		this.credentialSetServiceInt = credentialSetServiceInt;
		this.encryptor = encryptor;
		this.passwordsInDB = passwordsInDB;
		setLayout(new GridBagLayout());
		try {
			Image clipboard = ImageIO.read(getClass().getClassLoader().getResource("clipboard-icon.png"));
			Image delete = ImageIO.read(getClass().getClassLoader().getResource("delete-icon.png"));
			copyToClipboard.setIcon(new ImageIcon(clipboard));
			removeThisCredentialSet.setIcon(new ImageIcon(delete));
			setLayout(new GridBagLayout());
			add(filterApps, MainPanel.getGBC(0, 0, GridBagConstraints.NORTHWEST, 2, 1));
			add(filterAppTextField, MainPanel.getGBC(0, 2, GridBagConstraints.NORTHWEST, 1, 1));
			add(copyToClipboard, MainPanel.getGBC(1, 0, GridBagConstraints.NORTHWEST, 1, 1));
			add(removeThisCredentialSet, MainPanel.getGBC(1, 1, GridBagConstraints.NORTHWEST, 1, 1));
		  } catch (IOException ex) {
			  ex.printStackTrace();
		  }
	}
	
	public void addListeners() {
		copyToClipboard.addActionListener(l -> {
			if(passwordsInDB.getSelectedRow() < 0) {
				JOptionPane.showMessageDialog(null, "You should select a row first!");
				return;
			}
			JPasswordField encryptionKey = new JPasswordField(15);
			int okCxl = 0;

			String key = null;
			while(okCxl != JOptionPane.CANCEL_OPTION && Strings.isNullOrEmpty(key)) {
				okCxl = JOptionPane.showConfirmDialog(null, encryptionKey, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				key = new String(encryptionKey.getPassword());
			}
			if(okCxl == JOptionPane.OK_OPTION) {
				long id = (long)passwordsInDB.getModel().getValueAt(passwordsInDB.getSelectedRow(), -1);
				CredentialSet credentialSet = credentialSetServiceInt.getById(id);
				String encryptedPassword = credentialSet.getEncryptedPassword();
				try {
					String decryptedPassword = encryptor.decrypt(encryptedPassword, key);
					credentialSetServiceInt.putOnClipboard(decryptedPassword);
					credentialSetServiceInt.scheduleClearingClipboard(15000l);
				}
				catch(RuntimeException re) {
					JOptionPane.showMessageDialog(null, re.getMessage());
				}
			}
		});
		
		removeThisCredentialSet.addActionListener(l -> {
			if(passwordsInDB.getSelectedRow() < 0) {
				JOptionPane.showMessageDialog(null, "You should select a row first!");
				return;
			}
			int okCxl = JOptionPane.showConfirmDialog(null, "This will delete your credentials from the database. You will not be able to retreive them ever again.", "Confirm removal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(JOptionPane.OK_OPTION == okCxl) {
				long id = (long)passwordsInDB.getModel().getValueAt(passwordsInDB.getSelectedRow(), 3);
				credentialSetServiceInt.delete(id);
				filterAppTextField.setText("");
				((TableModel)this.passwordsInDB.getModel()).setCredentialSet(credentialSetServiceInt.getAll());
			}
		});
		
		filterAppTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				String text = filterAppTextField.getText();
				if(text.equals("")) {
					((TableModel)ActionsPanel.this.passwordsInDB.getModel()).setCredentialSet(credentialSetServiceInt.getAll());
				}
				else {
					((TableModel)ActionsPanel.this.passwordsInDB.getModel()).setCredentialSet(credentialSetServiceInt.getFilteredByTargetApp(text));
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				String text = filterAppTextField.getText();
				if(text.equals("")) {
					((TableModel)ActionsPanel.this.passwordsInDB.getModel()).setCredentialSet(credentialSetServiceInt.getAll());
				}
				else {
					((TableModel)ActionsPanel.this.passwordsInDB.getModel()).setCredentialSet(credentialSetServiceInt.getFilteredByTargetApp(text));
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}
		});;
	}
	
	public JTable getPassowrdsInDB() {
		return this.passwordsInDB;
	}
}
