package eu.project.ttc.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * This class is responsible for dispatching all events triggered by the interface to methods running the
 * corresponding actions.
 */
public class TermSuiteListener implements ActionListener, PropertyChangeListener  {

    private ToolController termSuiteTool;
    private TermSuiteEngine termSuiteEngine;
    private TermSuiteRunner runner;

    public void setTool(ToolController termSuiteTool) {
        this.termSuiteTool = termSuiteTool;
    }

    private ToolController getTermSuiteTool() {
        return this.termSuiteTool;
    }

    public void setEngine(TermSuiteEngine taggerEngine) {
        this.termSuiteEngine = taggerEngine;
    }

    private TermSuiteEngine getTermSuiteEngine() {
        return this.termSuiteEngine;
    }

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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////// ACTIONS

    /**
     * This method chooses the right method to be executed given the action event triggered.
     * It basically routes the event to the action.
     */
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

	private void doQuit() {
		int response = JOptionPane.showConfirmDialog(
                this.getTermSuiteTool().getParent().getFrame(),
                "Do you really want to quit " + this.getTermSuiteTool().getParent().getPreferences().getTitle() + "?",
                "Quit",
                JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getTermSuiteTool().getParent().quit();
		}
	}

    /**
     * Run TermSuite in the current configuration.
     */
	public void doRun() {
// FIXME		this.getTermSuiteTool().getSettings().doUpdate();
		try {
// FIXME			this.getTermSuiteTool().getSettings().validate();
		} catch (Exception e) {
            getTermSuiteTool().getParent().displayException("An error occurred while validating config.", e);
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
            getTermSuiteTool().getParent().displayException("An error occurred while validating config.", e);
			e.printStackTrace();
			this.getTermSuiteTool().getParent().getToolBar().getRun().setEnabled(true);
		} 
	}
	
	private void doStop() {
		this.runner.cancel(false);
	}
	
}
