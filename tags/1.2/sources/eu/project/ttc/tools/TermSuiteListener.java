package eu.project.ttc.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class TermSuiteListener implements ActionListener, PropertyChangeListener  {
	
	private TermSuiteTool termSuiteTool;
	
	public void setTool(TermSuiteTool termSuiteTool) {
		this.termSuiteTool = termSuiteTool;
	}
	
	private TermSuiteTool getTermSuiteTool() {
		return this.termSuiteTool;
	}

	private TermSuiteEngine termSuiteEngine;
	
	public void setEngine(TermSuiteEngine taggerEngine) {
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
			this.runner = new TermSuiteRunner(this.getTermSuiteTool().getParent(), this.getTermSuiteEngine());
	        this.runner.addPropertyChangeListener(this);
			this.runner.execute();
			this.getTermSuiteTool().getParent().getToolBar().getStop().setEnabled(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getTermSuiteTool().getParent().getFrame(),e.getMessage(),"Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
	}
	
	private TermSuiteRunner runner;
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("progress")) {
			int progress = this.runner.getProgress();
			this.flush(progress);			
		}
	}
	
	public void flush(int value) {
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setValue(value);
		this.getTermSuiteTool().getParent().getToolBar().getProgressBar().setString(value + " %");
	}
	
	private void doStop() {
		this.runner.cancel(false);
	}
	
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
			} else if (action.equals("stop")) {
				this.doStop();
			}  else if (action.equals("save")) {
				this.getTermSuiteTool().getParent().save();
			}
		} 
	}
	
}
