package eu.project.ttc.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class TermSuiteListener implements ActionListener {
	
	private TermSuiteTool termSuiteTool;
	
	public void setTermSuiteTool(TermSuiteTool termSuiteTool) {
		this.termSuiteTool = termSuiteTool;
	}
	
	private TermSuiteTool getTermSuiteTool() {
		return this.termSuiteTool;
	}

	private TermSuiteEngine termSuiteEngine;
	
	public void setTermSuiteEngine(TermSuiteEngine taggerEngine) {
		this.termSuiteEngine = taggerEngine;
	}
	
	private TermSuiteEngine getTermSuiteEngine() {
		return this.termSuiteEngine;
	}
	
	private void doQuit() {
		String message = "Do you really want to quit " + this.getTermSuiteTool().getParent().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getTermSuiteTool().getParent().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getTermSuiteTool().getParent().quit();				
		}
	}
	
	public void doRun() {
		this.getTermSuiteTool().getSettings().doUpdate();
		try {
			this.getTermSuiteTool().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getTermSuiteTool().getParent().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		this.getTermSuiteTool().getParent().getViewer().doEnable(false);
		this.getTermSuiteTool().getParent().getViewer().getResultModel().clear();
		try {
			this.getTermSuiteTool().getParent().getToolBar().getRun().setEnabled(false); 
			TermSuiteRunner runner = new TermSuiteRunner();
			runner.process(this.getTermSuiteEngine(), this);
			// this.getTermSuiteTool().getParent().getToolBar().getPause().setEnabled(true);
			// this.getTermSuiteTool().getParent().getToolBar().getStop().setEnabled(true);
			// TODO
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getTermSuiteTool().getParent().getFrame(),e.getMessage(),"Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
	}

	public void reset() {
		this.getTermSuiteTool().getParent().getToolBar().getRun().setEnabled(true);
		this.getTermSuiteTool().getParent().getToolBar().getPause().setEnabled(false);
		this.getTermSuiteTool().getParent().getToolBar().getStop().setEnabled(false);
	}
	
	public void initialize(int size) {
    	this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setMaximum(size);
    	this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setIndeterminate(false);
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setValue(0);
	}
	
	public void flush(int value, int limit) {
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setValue(value);
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setString(value + " / " + limit);
	}
	
	/*
	
	private void doResume() {
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setString("Resumed...");
		this.getTermSuiteTool().getParent().getToolBar().getPause().setEnabled(true);
	}
	
	private void doPause() {
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setString("Paused...");
		this.getTermSuiteTool().getParent().getToolBar().getRun().setText("Resume");
		this.getTermSuiteTool().getParent().getToolBar().getRun().setActionCommand("resume");
		this.getTermSuiteTool().getParent().getToolBar().getRun().setEnabled(true);
		this.getTermSuiteTool().getParent().getToolBar().getPause().setEnabled(false);
	}

	private void doStop() {
		this.getTermSuiteTool().getParent().getToolBar().getRun().setEnabled(true);
		this.getTermSuiteTool().getParent().getToolBar().getPause().setEnabled(false);
		this.getTermSuiteTool().getParent().getToolBar().getStop().setEnabled(false);
		if (this.getTermSuiteTool().getParent().isCommandLineInterface()) {
			this.getTermSuiteTool().getParent().quit();
		}
		this.getTermSuiteTool().getParent().getTaggerViewer().doEnable(true);
	}
	*/
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object instanceof JButton) {
			JButton source = (JButton) object;
			String action = source.getActionCommand();
			if (action.equals("quit")) { 
				this.doQuit();
			} else if (action.equals("about")) {
				this.getTermSuiteTool().getParent().getAbout().show();
			} else if (action.equals("run")) {
				this.doRun();
			} else if (action.equals("pause")) {
				
			} else if (action.equals("resume")) {
				
			} else if (action.equals("stop")) {
				
			}
		} 
	}
	
}
