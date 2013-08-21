package fr.free.rocheteau.jerome.dunamis.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.Timer;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceInitializationException;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.listeners.EngineListener;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;

public class ToolBar implements Runnable {

	/*
	private static URL Engine = ClassLoader.getSystemResource("fr.free.rocheteau.jerome/dunamis/images/Engine.png");
	private static URL Run = ClassLoader.getSystemResource("fr.free.rocheteau.jerome/dunamis/images/Run.png");
	private static URL Stop = ClassLoader.getSystemResource("fr.free.rocheteau.jerome/dunamis/images/Stop.png");
	private static URL Pause = ClassLoader.getSystemResource("fr.free.rocheteau.jerome/dunamis/images/Pause.png");
	*/
	
	private final Dimension dimension = new Dimension(64,32);
	private final Insets insets = new Insets(0,0,0,0);
	private final Color color = new Color(0,0,0,0);
	
	private CollectionProcessingEngine collectionProcessingEngine;
	
	public void setCollectionProcessingEngine(CollectionProcessingEngine cpe) {
		this.collectionProcessingEngine = cpe;
	}
	
	public CollectionProcessingEngine getCollectionProcessingEngine() {
		return this.collectionProcessingEngine;
	}
	
	private org.apache.uima.collection.CollectionProcessingEngine engine;
	
	private void setEngine() throws ResourceInitializationException {
		this.engine = UIMAFramework.produceCollectionProcessingEngine(this.getCollectionProcessingEngine().getDescription());
		this.engine.addStatusCallbackListener(this.getListener());
	}
	
	public org.apache.uima.collection.CollectionProcessingEngine getEngine() {
		return this.engine;
	}
	
	public void run() {
		try {
			this.setEngine();
		} catch (ResourceInitializationException e) {
			Dunamis.error(e);
		} 
	}
	
	private JButton build;
	
	private void setBuild() {
		this.build = new JButton("Build");
		this.build.setActionCommand("build");
		this.build.setEnabled(false);
		this.build.setPreferredSize(dimension);
		this.build.setMargin(insets);
		this.build.setBackground(color);
		this.build.setBorderPainted(false);
	}
	
	public JButton getBuild() {
		return this.build;
	}
	
	private JButton run;
	
	private void setRun() {
		this.run = new JButton("Run");
		this.run.setActionCommand("run");
		this.run.setEnabled(false);
		this.run.setPreferredSize(dimension);
		this.run.setMargin(insets);
		this.run.setBackground(color);
		this.run.setBorderPainted(false);
	}
	
	public JButton getRun() {
		return this.run;
	}
	
	private JButton pause;
	
	private void setPause() {
		this.pause = new JButton("Pause");
		this.pause.setActionCommand("pause");
		this.pause.setEnabled(false);
		this.pause.setPreferredSize(dimension);
		this.pause.setMargin(insets);
		this.pause.setBackground(color);
		this.pause.setBorderPainted(false);
	}
	
	public JButton getPause() {
		return this.pause;
	}
	
	private JButton stop;
	
	private void setStop() {
		this.stop = new JButton("Stop");
		this.stop.setActionCommand("stop");
		this.stop.setEnabled(false);
		this.stop.setPreferredSize(dimension);
		this.stop.setMargin(insets);
		this.stop.setBackground(color);
		this.stop.setBorderPainted(false);
	}
	
	public JButton getStop() {
		return this.stop;
	}
	
	private JProgressBar progressBar;
	
	private void setProgressBar() {
		this.progressBar = new JProgressBar();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = ((4 * screen.width) / 5) - (4 * (dimension.width + 5));
		Dimension bar = new Dimension(width,25);
		this.progressBar.setPreferredSize(bar);
		this.progressBar.setMinimumSize(new Dimension(screen.width / 2,25));
		this.progressBar.setStringPainted(true);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	private JToolBar component;
	
	private void setComponent() {
		this.component = new JToolBar();
		this.component.setFloatable(false);
		this.component.add(this.getBuild());
		this.component.add(this.getRun());
		this.component.add(this.getPause());
		this.component.add(this.getStop());
		this.component.add(this.getProgressBar());
	}
	
	public JToolBar getComponent(){
		return this.component;
	}

	private Timer timer;
	
	private void setTimer(ActionListener listener) {
		this.timer = new Timer(0,listener);
	}
	
	public Timer getTimer() {
		return this.timer;
	}
		
	private EngineListener listener;
	
	private void setListener(EngineListener listener) {
		this.listener = listener;
	}
	
	private EngineListener getListener() {
		return this.listener;
	}
	
	public void enableListeners(EngineListener listener) {
		this.setListener(listener);
		this.getBuild().addActionListener(this.getListener());
		this.getRun().addActionListener(this.getListener());
		this.getPause().addActionListener(this.getListener());
		this.getStop().addActionListener(this.getListener());
		this.setTimer(this.getListener());
	}
	
	public ToolBar() {
		this.setBuild();
		this.setRun();
		this.setPause();
		this.setStop();
		this.setProgressBar();
		this.setComponent();
	}
	
}
