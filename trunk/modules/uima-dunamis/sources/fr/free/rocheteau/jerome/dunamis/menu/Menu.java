package fr.free.rocheteau.jerome.dunamis.menu;

import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

public class Menu {
	
	private FileMenu fileMenu;
	
	private void setFileMenu() {
		this.fileMenu = new FileMenu();
	}
	
	private FileMenu getFileMenu() {
		return this.fileMenu;
	}

	private HelpMenu helpMenu;
	
	private void setHelpMenu() {
		this.helpMenu = new HelpMenu();
	}
	
	private HelpMenu getHelpMenu() {
		return this.helpMenu;
	}
	
	private JMenuBar menuBar;
	
	private void setMenuBar() {
		this.menuBar = new JMenuBar();
		this.menuBar.add(this.getFileMenu().getComponent());
		this.menuBar.add(this.getHelpMenu().getComponent());
	}
	
	public JMenuBar getComponent() {
		return this.menuBar;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getFileMenu().enableListeners(listener);
		this.getHelpMenu().enableListeners(listener);
	}
	
	public Menu() {
		this.setFileMenu();
		this.setHelpMenu();
		this.setMenuBar();
	}
	
}
