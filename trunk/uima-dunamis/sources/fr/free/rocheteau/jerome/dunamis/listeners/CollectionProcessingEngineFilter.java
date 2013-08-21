package fr.free.rocheteau.jerome.dunamis.listeners;

import java.io.File;

public class CollectionProcessingEngineFilter 
	extends javax.swing.filechooser.FileFilter 
		implements java.io.FileFilter {

	public String getDescription() {
		return "UIMA Collection Processing Engine XML Descriptor";
	}
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			return file.getName().endsWith(".xml");
		}
	}
	
}
