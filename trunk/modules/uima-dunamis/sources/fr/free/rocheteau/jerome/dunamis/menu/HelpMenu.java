package fr.free.rocheteau.jerome.dunamis.menu;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class HelpMenu {
	
	private static Dimension itemDimension = new Dimension(192,32);
	
	private JMenuItem helpHelp;
	
	private void setHelpHelp() {
		this.helpHelp = new JMenuItem("Help");
		this.helpHelp.setActionCommand("help");
		this.helpHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));
		this.helpHelp.getAccessibleContext().setAccessibleDescription("Menu > Help > Help");
		this.helpHelp.setPreferredSize(itemDimension);

	}
	
	private JMenuItem getHelpHelp() {
		return this.helpHelp;
	}
	
	private JMenuItem aboutHelp;
	
	private void setAboutHelp() {
		this.aboutHelp = new JMenuItem("About");
		this.aboutHelp.setActionCommand("about");
		this.aboutHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
		this.aboutHelp.getAccessibleContext().setAccessibleDescription("Menu > Help > About");
		this.aboutHelp.setPreferredSize(itemDimension);

	}
	
	private JMenuItem getAboutHelp() {
		return this.aboutHelp;
	}
	
	private JMenu menu;
	
	private void setMenu() {
		this.menu = new JMenu("Help");
		this.menu.getAccessibleContext().setAccessibleDescription("Menu > Help");
		this.menu.add(this.getHelpHelp());
		this.menu.add(this.getAboutHelp());
	}
	
	
	public JMenu getComponent() {
		return this.menu;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getHelpHelp().addActionListener(listener);
		this.getAboutHelp().addActionListener(listener);
	}
	
	public HelpMenu() {
		this.setHelpHelp();
		this.setAboutHelp();
		this.setMenu();
	}
	
}
