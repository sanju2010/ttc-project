package fr.free.rocheteau.jerome.dunamis.models;

import javax.swing.DefaultListModel;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class ProcessingResultResource implements SharedResourceObject {

	private DefaultListModel files;
	
	private void setFiles() {
		this.files = new DefaultListModel();
	}
	
	public DefaultListModel getFiles() {
		return this.files;
	}
	
	public ProcessingResultResource() {
		this.setFiles();
	}
		
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		// TODO Auto-generated method stub

	}

}
