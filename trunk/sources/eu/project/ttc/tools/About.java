package eu.project.ttc.tools;

import java.awt.Frame;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class About {

	private Frame frame;
	
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	
	private Frame getFrame() {
		return this.frame;
	}
	
	private Preferences preferences;
	
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
		this.setUrl();
		this.setIcon();
	}
	
	private Preferences getPreferences() {
		return this.preferences;
	}
	
	private final String getIconUrl() {
		return this.getPreferences().getIcon();
	}
	
	private final String getTitle() {
		return this.getPreferences().getTitle();
	}
	
	private final String getVersion() {
		return "(version " + this.getPreferences().getVersion() + ")";
	}
	
	private final String getLicense() {
		return this.getPreferences().getLicense();
	}
	
	private final String getSummary() {
		return this.getPreferences().getSummary();
	}
	
	private URL url;
	
	private void setUrl() {
		// this.url = ClassLoader.getSystemResource(this.getIconUrl());
		this.url = this.getClass().getClassLoader().getResource(this.getIconUrl());
	}
	
	private URL getUrl() {
		return this.url;
	}
	
	private ImageIcon icon;
	
	private void setIcon() {
		this.icon = new ImageIcon(this.getUrl());
	}
	
	private ImageIcon getIcon() {
		return this.icon;
	}
		
	private String getMessage() {
		return this.getTitle() + " " + this.getVersion() + "\n" + this.getSummary() + "\n" + this.getLicense();
	}
	
	public void show() { 
		JOptionPane.showMessageDialog(this.getFrame(),this.getMessage(),"About",JOptionPane.INFORMATION_MESSAGE,this.getIcon());
	}
	
}
