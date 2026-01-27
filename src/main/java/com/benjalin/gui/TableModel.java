package com.benjalin.gui;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import com.benjalin.dao.domain.CredentialSet;

public class TableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = new String[]{"Target app", "Username", "Description"};
	private CredentialSet[] credentialSet;
	
	public TableModel(Set<CredentialSet> credentialSets) {
		this.credentialSet = credentialSets.toArray(new CredentialSet[credentialSets.size()]);
	}
	
	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	@Override
	public int getRowCount() {
		return credentialSet.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(0 == columnIndex) {
			return this.credentialSet[rowIndex].getTargetApp();
		}
		else if (1 == columnIndex) {
			return this.credentialSet[rowIndex].getUsername();
		}
		else if(2 == columnIndex) {
			return this.credentialSet[rowIndex].getAppDescription();
		}
		else if(3 == columnIndex) {
			return this.credentialSet[rowIndex].getId();
		}
		return "";
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 2;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}
	
	public void setCredentialSet(Set<CredentialSet> credentialSets) {
		this.credentialSet = credentialSets.toArray(new CredentialSet[credentialSets.size()]);
		this.fireTableDataChanged();
	}
}
