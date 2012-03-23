package fr.free.rocheteau.jerome.dunamis.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import fr.free.rocheteau.jerome.dunamis.Dunamis;

public class Package implements Transferable, Cloneable {

	private File file;
	
	private void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	private String name;

	private void setName() {
		this.name = this.getFile().getName();
	}

	private void setName(String name) {
		this.name = name;
	}

	private String getName() {
		return this.name;
	}
	
	public Package(File file) {
		this.setFile(file);
		this.setName();
	}
	
	public Package(File file,String name) {
		this.setFile(file);
		this.setName(name);
	}
	
	public String toString() {
		return this.getName();
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (this.isDataFlavorSupported(flavor)) {
			return this;	
		} else {
			return null;
		}
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{ DataFlavor.stringFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.stringFlavor);
	}
	
	public Package clone() {
		Package pkg = null;
	    try {
	    	pkg = (Package) super.clone();
	    } catch(CloneNotSupportedException e) {
	    	Dunamis.error(e);
	    }
	    return pkg;
	}
	
}
