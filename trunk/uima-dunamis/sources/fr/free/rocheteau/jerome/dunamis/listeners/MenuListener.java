package fr.free.rocheteau.jerome.dunamis.listeners;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fr.free.rocheteau.jerome.dunamis.Dunamis;

public class MenuListener implements ActionListener {

	private CollectionProcessingEngineFilter filter;
	private JFileChooser chooser;
	
	public MenuListener() {
		this.filter = new CollectionProcessingEngineFilter();
		this.chooser = new JFileChooser();
		this.chooser.setDialogTitle("File Chooser");
		this.chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	this.chooser.setPreferredSize(new Dimension(750,500));
    	this.chooser.addChoosableFileFilter(this.filter);
	}
	
	private Dunamis dunamis;
	
	public void setDunamis(Dunamis dunamis) {
		this.dunamis = dunamis;
	}
	
	private Dunamis getDunamis() {
		return this.dunamis;
	}
		
	private void doNew() {
		this.chooser.setMultiSelectionEnabled(false);
		int rv = this.chooser.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = this.chooser.getSelectedFile();
			if (file.exists()) {
				
			} else {
				this.getDunamis().getEngineChooser().doNew(file);
			}
		}
	}
	
	private void doOpen() {
		this.chooser.setMultiSelectionEnabled(false);
		int rv = this.chooser.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = this.chooser.getSelectedFile();
			if (file.isFile()) {
				this.getDunamis().getEngineChooser().doOpen(file);
			}
		}
	}

	private void doOpenAll() {
		this.chooser.setMultiSelectionEnabled(true);
		int rv = this.chooser.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File[] files = this.chooser.getSelectedFiles();
			for (File file : files) {
				if (file.isFile()) {
					this.getDunamis().getEngineChooser().doOpen(file);
				}
			}
		}
	}
	
	private void doSave() {
		this.getDunamis().getEngineChooser().doSave();
	}
	
	private void doSaveAll() {
		this.getDunamis().getEngineChooser().doSaveAll();
	}
	
	private void doSaveAs() {
		this.chooser.setMultiSelectionEnabled(false);
		int rv = this.chooser.showOpenDialog(null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = this.chooser.getSelectedFile();
			if (file.exists()) {
				String question = "Do you want to override this file?";
				rv = JOptionPane.showConfirmDialog(null,question,"Override File",JOptionPane.YES_NO_OPTION);
				if (rv == 0) {
					this.getDunamis().getEngineChooser().doSaveAs(file);
				}
			} else {
				this.getDunamis().getEngineChooser().doSaveAs(file);
			}
		}
	}

	private void doRevert() {
		String question = "Do you want to revert this file?";
		int rv = JOptionPane.showConfirmDialog(null,question,"Revert File",JOptionPane.YES_NO_OPTION);
		if (rv == 0) {
			try {
				this.getDunamis().getEngineChooser().doRevert();
			} catch (Exception e) {
				Dunamis.error(e);
			}
		}
	}
	
	private void doClose() {
		String question = "Do you want to save this file?";
		int rv = JOptionPane.showConfirmDialog(null,question,"Close File",JOptionPane.YES_NO_OPTION);
		if (rv == 0) {
			this.doSave();
		}
		this.getDunamis().getEngineChooser().doClose();
	}
	
	private void doCloseAll() {
		String question = "Do you want to save all these files?";
		int rv = JOptionPane.showConfirmDialog(null,question,"Close All Files",JOptionPane.YES_NO_OPTION);
		if (rv == 0) {
			this.doSaveAll();
		}
		this.getDunamis().getEngineChooser().doCloseAll();
	}
	
	private void doQuit() {
		this.getDunamis().quit();
	}
	
	private URI getHelp() throws IOException {
		try {
			String uri = this.getDunamis().getPreferences().getHelp();
			return new URI(uri);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	private void doHelp() {
		try {
			if (this.getDunamis().getDesktop().isSupported(Desktop.Action.BROWSE)) {
				this.getDunamis().getDesktop().browse(this.getHelp());
			} 
		} catch (IOException e) {
			Dunamis.error(e);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object instanceof JMenuItem) {
			JMenuItem source = (JMenuItem) object;
			String action = source.getActionCommand();
			if (action.equals("new")) { 
				this.doNew();
			} else if (action.equals("open")) { 
				this.doOpen();
			} else if (action.equals("open all")) { 
				this.doOpenAll();
			} else if (action.equals("save")) { 
				this.doSave();
			} else if (action.equals("save as")) { 
				this.doSaveAs();
			} else if (action.equals("save all")) { 
				this.doSaveAll();
			} else if (action.equals("revert")) { 
				this.doRevert();
			} else if (action.equals("close")) { 
				this.doClose();
			} else if (action.equals("close all")) { 
				this.doCloseAll();
			} else if (action.equals("quit")) { 
				this.doQuit();
			} else if (action.equals("help")) { 
				this.doHelp();
			}  else if (action.equals("about")) {
				this.getDunamis().getAbout().show();
			}
		}
	}

}
