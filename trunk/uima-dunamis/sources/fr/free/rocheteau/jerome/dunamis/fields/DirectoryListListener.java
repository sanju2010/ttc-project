package fr.free.rocheteau.jerome.dunamis.fields;

import java.io.File;

import javax.swing.JFileChooser;

public class DirectoryListListener extends AbstractListListener<File> {
	
	private JFileChooser chooser;
	
	private void setChooser() {
		this.chooser = new JFileChooser();
		this.chooser.setDialogTitle("Directory Chooser");
		this.chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	}
	
	private JFileChooser getChooser() {
		return this.chooser;
	}
	
	public DirectoryListListener() {
		this.setChooser();
	}
	
	@Override
	public void doMove() {
		int rv = this.getChooser().showOpenDialog(this.getList());
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = this.getChooser().getSelectedFile();
			this.getModel().addElement(file);
			// Organon.setResultFiles();
		}
	}
	
}
