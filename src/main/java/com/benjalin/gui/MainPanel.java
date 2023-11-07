package com.benjalin.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableRowSorter;

import com.benjalin.dao.domain.CredentialSet;
import com.benjalin.service.CredentialSetServiceInt;
import com.benjalin.service.EncryptionDecryptionServiceInt;
import com.google.inject.name.Named;

public class MainPanel extends JPanel {

	EncryptionDecryptionServiceInt encryptorServiceInt;
	CredentialSetServiceInt credentialSetService;
	
	private JLabel usernameLabel = new JLabel("Username: ");
	private JTextField username = new JTextField(15);
	private JLabel passwordLabel = new JLabel("Password: ");
	private JPasswordField password = new JPasswordField(15);
	private JLabel encryptionKeyLabel = new JLabel("Encryption key: ");
	private JPasswordField encryptionKey = new JPasswordField(15);
	private JLabel encryptionKeyConfirmationLabel = new JLabel("Confirm encryption key: ");
	private JPasswordField encryptionKeyConfirmation = new JPasswordField(15);
	private JLabel passwordConfirmationLabel = new JLabel("Confirm password: ");
	private JPasswordField passwordConfirmation = new JPasswordField(15);
	private JLabel targetAppLabel = new JLabel("Target app: ");
	private JTextField targetApp = new JTextField(15);
	private JLabel descriptionLabel = new JLabel("Description: ");
	private JTextArea description = new JTextArea(5, 30);
	private JButton savePassword = new JButton("Save");
	private JTable passwordsInDB;
	private JPanel actionsPanel;
	TableModel model;
	
	private static final Insets insets = new Insets(5, 5, 5, 5);

	@Inject
	public MainPanel(@Named("actionsPanel") JPanel actionsPanel, CredentialSetServiceInt credentialSetServiceInt, EncryptionDecryptionServiceInt encryptionDecryptionServiceInt) {
		this.credentialSetService = credentialSetServiceInt;
		this.encryptorServiceInt = encryptionDecryptionServiceInt;
		this.actionsPanel = actionsPanel;
		this.passwordsInDB = ((ActionsPanel)actionsPanel).getPassowrdsInDB();
		this.setSize(new Dimension(500, 500));
		this.setLayout(new GridBagLayout());
		model = new TableModel(credentialSetService.getAll());
		this.addComponents();
		this.addButtonListener();
		this.setFocusTraversalPolicyProvider(true);
		this.setFocusTraversalPolicy(new MyFocusTraversalPolicy());
		passwordsInDB.setAutoCreateRowSorter(true);
		passwordsInDB.getRowSorter().toggleSortOrder(0);
	}
	
	private void addComponents() {
		this.add(usernameLabel, getGBC(1, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(username, getGBC(1, 2, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(passwordLabel, getGBC(2, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(password, getGBC(2, 2, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(passwordConfirmationLabel, getGBC(3, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(passwordConfirmation, getGBC(3, 2, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(encryptionKeyLabel, getGBC(4, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(encryptionKey, getGBC(4, 2, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(encryptionKeyConfirmationLabel, getGBC(5, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(encryptionKeyConfirmation, getGBC(5, 2, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(targetAppLabel, getGBC(6, 1, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(targetApp, getGBC(6, 2, GridBagConstraints.NORTHWEST, 1, 1));
		description.setLineWrap(true);
		this.add(descriptionLabel, getGBC(1, 3, GridBagConstraints.NORTHWEST, 1, 1));
		this.add(new JScrollPane(description), getGBC(2, 3, GridBagConstraints.NORTHWEST, 3, 3));
		this.add(savePassword, getGBC(5, 3, GridBagConstraints.NORTHWEST, 1, 2));
		passwordsInDB.setModel(model);
		((ActionsPanel)actionsPanel).addListeners();
		this.add(actionsPanel, getGBC(7, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.REMAINDER, 1));
		JScrollPane tableScrollPane = new JScrollPane(passwordsInDB);
		tableScrollPane.setPreferredSize(new Dimension(830, 200));
		this.add(tableScrollPane, getGBC(8, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.REMAINDER, 1));
	}
	
	public static GridBagConstraints getGBC(int row, int column, int anchor, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = column;
		gbc.gridy = row;
		gbc.anchor = anchor;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = MainPanel.insets;
		return gbc;
	}
	
	private void addButtonListener() {
		savePassword.addActionListener(l -> {
			System.out.println("sickos: " + credentialSetService.getAll().size());
			if(!this.isCSDataValid()){
				String message = this.getValidityFailureMessage();
				JOptionPane.showMessageDialog(null, message);
				this.clearInputFields();
				return;
			}
			CredentialSet newCredentialSet = this.getCredentialSetDataFromUI();
			this.clearInputFields();
			credentialSetService.save(newCredentialSet);
			((TableModel)this.passwordsInDB.getModel()).setCredentialSet(credentialSetService.getAll());
		});
	}
	private String getValidityFailureMessage() {
		if(!verifyMatch(new String(this.password.getPassword()), new String(this.passwordConfirmation.getPassword()))) {
			return "Your password and its confirmation do not match. Please correct that!";
		}
		if(!verifyMatch(new String(this.encryptionKey.getPassword()), new String(this.encryptionKeyConfirmation.getPassword()))) {
			return "Your encryption key and its confirmation do not match. Please correct that!";
		}
		if(!assertNecessaryFields()) {
			return "Your password, your encryption key, and your target app may not be empty. Please correct that!";
		}
		throw new IllegalStateException("Something is wrong and we do not know what. Passwords match, encryption keys match, all necessary data are present, yet we're having a complaint in input validity check.");
	}
	
	private boolean isCSDataValid() {
		return verifyMatch(new String(this.password.getPassword()), new String(this.passwordConfirmation.getPassword())) 
				&& verifyMatch(new String(this.encryptionKey.getPassword()), new String(this.encryptionKeyConfirmation.getPassword()))
				&& assertNecessaryFields();
	}
	private CredentialSet getCredentialSetDataFromUI() {
		CredentialSet result = new CredentialSet();
		String username = this.username.getText();
		String password = new String(this.password.getPassword());
		String description = this.description.getText();
		String encryptionKey = new String(this.encryptionKey.getPassword());
		String targetApp = this.targetApp.getText();
		result.setUsername(username);
		password = encryptorServiceInt.encrypt(password, encryptionKey);
		result.setEncryptedPassword(password);
		result.setTargetApp(targetApp);
		result.setAppDescription(description);
		return result;
	}
	
	private boolean assertNecessaryFields() {
		boolean result = true;
		result &= !"".equals(new String(this.password.getPassword()));
		result &= !"".equals(new String(this.encryptionKey.getPassword()));
		return result;
	}
	
	private void clearInputFields() {
		this.username.setText("");
		this.password.setText("");
		this.passwordConfirmation.setText("");
		this.encryptionKey.setText("");
		this.encryptionKeyConfirmation.setText("");
		this.targetApp.setText("");
		this.description.setText("");
	}
	
	private boolean verifyMatch(String first, String second) {
		return first.equals(second);
	}
	
	
	private class MyFocusTraversalPolicy extends FocusTraversalPolicy {
		List<Component> components = Arrays.asList(username, password, passwordConfirmation, encryptionKey, encryptionKeyConfirmation, targetApp, description, savePassword);

		@Override
		public Component getComponentAfter(Container aContainer,
				Component aComponent) {
			System.out.println("get component after");
			int index = components.indexOf(aComponent);
			index++;
			index %= components.size();
			return components.get(index);
		}
		
		@Override
		public Component getComponentBefore(Container aContainer,
				Component aComponent) {
			int index = components.indexOf(aComponent);
			index--;
			index += components.size();
			index %= components.size();
			return components.get(index);
		}
		
		@Override
		public Component getDefaultComponent(Container aContainer) {
			return username;
		}
		
		@Override
		public Component getFirstComponent(Container aContainer) {
			return username;
		}
		
		@Override
		public Component getLastComponent(Container aContainer) {
			return savePassword;
		}
	}
}
