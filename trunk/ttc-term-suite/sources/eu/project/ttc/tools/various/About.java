package eu.project.ttc.tools.various;

import eu.project.ttc.tools.commons.TermSuiteVersion;

import java.awt.Frame;

import javax.swing.JOptionPane;



public class About {

	private Frame frame;
	
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	
	private Frame getFrame() {
		return this.frame;
	}
	
	private final String getTitle() {
		return TermSuiteVersion.TITLE;
	}
	
	private final String getVersion() {
        return "(version " + TermSuiteVersion.VERSION + ")";
	}
	
	private final String getLicense() {
        return TermSuiteVersion.LICENSE;
	}
	
	private final String getSummary() {
        return TermSuiteVersion.SUMMARY;
	}
		
	private String getMessage() {
		return getTitle() + " " + getVersion() + "\n" + getSummary() + "\n" + getLicense();
	}
	
	public void show() { 
		JOptionPane.showMessageDialog(this.getFrame(),
                this.getMessage(),
                "About",
                JOptionPane.INFORMATION_MESSAGE,
                null);
	}
	
}
