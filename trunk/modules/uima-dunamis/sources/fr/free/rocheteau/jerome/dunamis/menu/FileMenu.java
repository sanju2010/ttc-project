package fr.free.rocheteau.jerome.dunamis.menu;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class FileMenu {
	
	private static Dimension itemDimension = new Dimension(192,32);
	
	private JMenuItem newFile;
	
	private void setNewFile() {
		this.newFile = new JMenuItem("New");
		this.newFile.setActionCommand("new");
		this.newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
		this.newFile.getAccessibleContext().setAccessibleDescription("Menu > File > New");
		this.newFile.setPreferredSize(itemDimension);

	}
	
	private JMenuItem getNewFile() {
		return this.newFile;
	}
	
	private JMenuItem openFile;
	
	private void setOpenFile() {
		this.openFile = new JMenuItem("Open");
		this.openFile.setActionCommand("open");
		this.openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
		this.openFile.getAccessibleContext().setAccessibleDescription("Menu > File > Open");
		this.openFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getOpenFile() {
		return this.openFile;
	}

	private JMenuItem openAllFile;
	
	private void setOpenAllFile() {
		this.openAllFile = new JMenuItem("Open All");
		this.openAllFile.setActionCommand("open all");
		this.openAllFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		this.openAllFile.getAccessibleContext().setAccessibleDescription("Menu > File > Open All");
		this.openAllFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getOpenAllFile() {
		return this.openAllFile;
	}
	
	private JMenuItem saveFile;
	
	private void setSaveFile() {
		this.saveFile = new JMenuItem("Save");
		this.saveFile.setActionCommand("save");
		this.saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		this.saveFile.getAccessibleContext().setAccessibleDescription("Menu > File > Save");
		this.saveFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getSaveFile() {
		return this.saveFile;
	}

	private JMenuItem saveAsFile;
	
	private void setSaveAsFile() {
		this.saveAsFile = new JMenuItem("Save As");
		this.saveAsFile.setActionCommand("save as");
		this.saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		this.saveAsFile.getAccessibleContext().setAccessibleDescription("Menu > File > Save As");
		this.saveAsFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getSaveAsFile() {
		return this.saveAsFile;
	}
	
	private JMenuItem saveAllFile;
	
	private void setSaveAllFile() {
		this.saveAllFile = new JMenuItem("Save All");
		this.saveAllFile.setActionCommand("save all");
		this.saveAllFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		this.saveAllFile.getAccessibleContext().setAccessibleDescription("Menu > File > Save All");
		this.saveAllFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getSaveAllFile() {
		return this.saveAllFile;
	}

	private JMenuItem revertFile;
	
	private void setRevertFile() {
		this.revertFile = new JMenuItem("Revert");
		this.revertFile.setActionCommand("revert");
		this.revertFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
		this.revertFile.getAccessibleContext().setAccessibleDescription("Menu > File > Revert");
		this.revertFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getRevertFile() {
		return this.revertFile;
	}
	
	private JMenuItem closeFile;
	
	private void setCloseFile() {
		this.closeFile = new JMenuItem("Close");
		this.closeFile.setActionCommand("close");
		this.closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_MASK));
		this.closeFile.getAccessibleContext().setAccessibleDescription("Menu > File > Close");
		this.closeFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getCloseFile() {
		return this.closeFile;
	}

	private JMenuItem closeAllFile;
	
	private void setCloseAllFile() {
		this.closeAllFile = new JMenuItem("Close All");
		this.closeAllFile.setActionCommand("close all");
		this.closeAllFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		this.closeAllFile.getAccessibleContext().setAccessibleDescription("Menu > File > Close All");
		this.closeAllFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getCloseAllFile() {
		return this.closeAllFile;
	}
	
	private JMenuItem quitFile;
	
	private void setQuitFile() {
		this.quitFile = new JMenuItem("Quit");
		this.quitFile.setActionCommand("quit");
		this.quitFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
		this.quitFile.getAccessibleContext().setAccessibleDescription("Menu > File > Quit");
		this.quitFile.setPreferredSize(itemDimension);
	}
	
	private JMenuItem getQuitFile() {
		return this.quitFile;
	}
	
	private JMenu fileMenu;
	
	private void setFileMenu() {
		this.fileMenu = new JMenu("File");
		this.fileMenu.getAccessibleContext().setAccessibleDescription("Menu > File");
		this.fileMenu.add(this.getNewFile());
		this.fileMenu.add(this.getOpenFile());
		this.fileMenu.add(this.getOpenAllFile());
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.getSaveFile());
		this.fileMenu.add(this.getSaveAsFile());
		this.fileMenu.add(this.getSaveAllFile());
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.getRevertFile());
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.getCloseFile());
		this.fileMenu.add(this.getCloseAllFile());
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.getQuitFile());
	}
	
	public JMenu getComponent() {
		return this.fileMenu;
	}
	
	public void enableListeners(ActionListener listener) {
		this.getNewFile().addActionListener(listener);
		this.getOpenFile().addActionListener(listener);
		this.getOpenAllFile().addActionListener(listener);
		this.getSaveFile().addActionListener(listener);
		this.getSaveAsFile().addActionListener(listener);
		this.getSaveAllFile().addActionListener(listener);
		this.getRevertFile().addActionListener(listener);
		this.getCloseFile().addActionListener(listener);
		this.getCloseAllFile().addActionListener(listener);
		this.getQuitFile().addActionListener(listener);
	}
	
	public FileMenu() {
		this.setNewFile();
		this.setOpenFile();
		this.setOpenAllFile();
		this.setSaveFile();
		this.setSaveAsFile();
		this.setSaveAllFile();
		this.setRevertFile();
		this.setCloseFile();
		this.setCloseAllFile();
		this.setQuitFile();
		this.setFileMenu();
	}
	
}
