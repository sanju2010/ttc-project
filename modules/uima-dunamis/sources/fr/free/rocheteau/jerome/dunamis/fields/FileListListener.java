package fr.free.rocheteau.jerome.dunamis.fields;

import java.io.File;

import javax.swing.JFileChooser;

public class FileListListener extends AbstractListListener<File> {
	
	@Override
	public void doMove() {
		int rv = FieldFactory.getChooser().showOpenDialog(/*this.getList()*/null);
		if (rv == JFileChooser.APPROVE_OPTION) {
			File file = FieldFactory.getChooser().getSelectedFile();
			this.getModel().addElement(file);
		}
	}
	
}
