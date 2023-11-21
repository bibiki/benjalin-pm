package com.benjalin.gui;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.benjalin.config.GuiceConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2837611333733254395L;

	@Inject
	public MainFrame(@Named("mainPanel") JPanel mainPanel) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPanel);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new GuiceConfiguration());
		JFrame mainFrame = injector.getInstance(MainFrame.class);
	}
}