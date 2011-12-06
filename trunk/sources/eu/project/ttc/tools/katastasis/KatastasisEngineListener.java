package eu.project.ttc.tools.katastasis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.tools.TermSuite;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;

public class KatastasisEngineListener implements ActionListener, StatusCallbackListener {

	private Katastasis katastasis;
	
	public void setKatastasis(Katastasis katastasis) {
		this.katastasis = katastasis;
	}
	
	private Katastasis getKatastasis() {
		return this.katastasis;
	}

	private void doQuit() {
		String message = "Do you really want to quit " + this.getKatastasis().getPreferences().getTitle() + "?";
		String title = "Quit";
		int response = JOptionPane.showConfirmDialog(this.getKatastasis().getFrame(),message,title,JOptionPane.YES_NO_OPTION);
		if (response == 0) {
			this.getKatastasis().quit();				
		}
	}
	
	private void doHelp() {
		try {
			TermSuite parent = this.getKatastasis().getParent();
			parent.getHelp().selectContextualizer();
			SwingUtilities.invokeLater(parent.getHelp());
		} catch (Exception e) {
			this.getKatastasis().error(e);
		} 
	}
	
	public void doProcess() {
		this.getKatastasis().getSettings().doUpdate();
		try {
			this.getKatastasis().getSettings().validate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.getKatastasis().getFrame(),e.getMessage(),e.getClass().getSimpleName(),JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		KatastasisEngine proc = new KatastasisEngine();
		proc.setKatastasis(this.getKatastasis());
		proc.execute();
		try {
			CpeDescription desc = proc.get();
			this.setTimer();
			this.getKatastasis().getToolBar().getRun().setEnabled(false);
			this.getKatastasis().getToolBar().getPause().setEnabled(true);
			this.getKatastasis().getToolBar().getStop().setEnabled(true);
			this.setEngine(desc);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this.getKatastasis().getFrame(),e.getMessage(),"Interrupted Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			JOptionPane.showMessageDialog(this.getKatastasis().getFrame(),e.getMessage(),"Execution Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			JOptionPane.showMessageDialog(this.getKatastasis().getFrame(),e.getMessage(),"Resource Initialization Exception",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
	}
	
	private void doStart() {
		int files = -1;
		Progress progress[] = this.getEngine().getProgress();
		if (progress != null) { 
			for (int i = 0; i < progress.length; i++) {
				if (progress[i].getUnit().equals(Progress.ENTITIES)) {
					files = (int) progress[i].getTotal();
					break;
				}
			}
		}
		if (files == -1) {
			this.getKatastasis().getToolBar().getProgressBar().setIndeterminate(true);
	    } else {
	    	this.getKatastasis().getToolBar().getProgressBar().setMaximum(files);
	    	this.getKatastasis().getToolBar().getProgressBar().setIndeterminate(false);
	    }
		this.getKatastasis().getToolBar().getProgressBar().setValue(0);
		this.getTimer().start();
	}
	
	private void doResume() {
		if (this.getEngine().isPaused()) {
			this.getKatastasis().getToolBar().getProgressBar().setString("Resumed...");
			this.getKatastasis().getToolBar().getPause().setEnabled(true);
			this.getTimer().restart();
			this.getEngine().resume();
		} 
	}
	
	private void doPause() {
		if (this.getEngine().isProcessing()) {
			this.getKatastasis().getToolBar().getProgressBar().setString("Paused...");
			this.getKatastasis().getToolBar().getRun().setText("Resume");
			this.getKatastasis().getToolBar().getRun().setActionCommand("resume");
			this.getKatastasis().getToolBar().getRun().setEnabled(true);
			this.getKatastasis().getToolBar().getPause().setEnabled(false);
			this.getEngine().pause();
			this.getTimer().stop();
		}
	}

	private void doStop() {
		if (this.getEngine().isProcessing() || this.getEngine().isPaused()) {
			this.getEngine().stop();
		}
		this.getTimer().stop();
		this.getKatastasis().getToolBar().getRun().setEnabled(true);
		this.getKatastasis().getToolBar().getPause().setEnabled(false);
		this.getKatastasis().getToolBar().getStop().setEnabled(false);
	}
	
	private void doTime() {
		Progress progress[] = this.getEngine().getProgress();
		if (progress != null && progress.length > 0) {
			int index = -1;
			if (index == -1) {
				for (int i = 0; i < progress.length; i++) {
					if (progress[i].getUnit().equals(Progress.ENTITIES)) {
						index = i;
						break;
					}
				}
			}
			if (index >= 0) {
				int value = (int) progress[index].getCompleted();
				this.getKatastasis().getToolBar().getProgressBar().setValue(value);
				this.getKatastasis().getToolBar().getProgressBar().setString(value + " / " + progress[index].getTotal());
			} 
		}
	}
	
	private Timer timer;
	
	private void setTimer() {
		this.timer = new Timer(0,this);
	}
	
	public Timer getTimer() {
		return this.timer;
	}
	
	private CollectionProcessingEngine engine;
	
	private void setEngine(CpeDescription desc) throws ResourceInitializationException {
		this.engine = UIMAFramework.produceCollectionProcessingEngine(desc);
		this.engine.addStatusCallbackListener(this);
		this.engine.process();
	}
	
	private CollectionProcessingEngine getEngine() {
		return this.engine;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object instanceof JButton) {
			JButton source = (JButton) object;
			String action = source.getActionCommand();
			if (action.equals("quit")) { 
				this.doQuit();
			} else if (action.equals("help")) { 
				this.doHelp();
			} else if (action.equals("about")) {
				this.getKatastasis().getAbout().show();
			} else if (action.equals("run")) {
				this.doProcess();
			} else if (action.equals("pause")) {
				this.doPause();
			} else if (action.equals("resume")) {
				this.doResume();
			} else if (action.equals("stop")) {
				this.doStop();
			}
		} else if (object instanceof Timer) {
			this.doTime();
		}
	}

	@Override
	public void initializationComplete() {
		this.doStart();
	}

	@Override
	public void batchProcessComplete() { }

	@Override
	public void collectionProcessComplete() {
		this.doStop();
		this.doLoad();
		UIMAFramework.getLogger().log(Level.INFO,this.getEngine().getPerformanceReport().toString());
		if (this.getKatastasis().isCommandLineInterface()) {
			this.getKatastasis().quit();
		}
	}
	
	@Override
	public void paused() { }

	@Override
	public void resumed() { }

	@Override
	public void aborted() { }

	@Override
	public void entityProcessComplete(CAS cas, EntityProcessStatus status) {
		if (status.isException()) {
			String message = status.getStatusMessage();
			this.getKatastasis().warning(message);
			for (Exception e : status.getExceptions()) {
				this.getKatastasis().error(e);
			}
		} else if (status.isEntitySkipped()) {
			String message = status.getStatusMessage();
			this.getKatastasis().warning(message);
		} else {
			try {
				ProcessingResult result = new ProcessingResult();
				result.setCas(cas);
				this.getKatastasis().getDocuments().getResultModel().addElement(result);
			} catch (Exception e) {
				this.getKatastasis().error(e);
			}
		}
	}

	private void doLoad() {
		try {
			String path = (String) this.getKatastasis().getSettings().getMetaData().getConfigurationParameterSettings().getParameterValue("File");
			InputStream inputStream = new FileInputStream(path);
			URL url = this.getClass().getClassLoader().getResource("eu/project/ttc/all/engines/TermContextIndexWriter.xml");
			XMLInputSource source = new XMLInputSource(url);
			XMLParser parser = UIMAFramework.getXMLParser();
			AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(source); 
			CAS cas = CasCreationUtils.createCas(ae);
			XmiCasDeserializer.deserialize(inputStream, cas);
			this.getKatastasis().getContexts().doLoad(cas.getJCas());
		} catch (Exception e) {
			this.getKatastasis().error(e);
		}
	}
		
}
