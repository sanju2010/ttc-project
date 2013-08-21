package fr.free.rocheteau.jerome.dunamis.viewers;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultTreeModel;

import fr.free.rocheteau.jerome.dunamis.Dunamis;
import fr.free.rocheteau.jerome.dunamis.models.CollectionProcessingEngine;

public class CollectionProcessingEngineViewer {
	
	private CasProcessorViewer collectionReaders;
	
	private void setCollectionReaderViewer() {
		this.collectionReaders = new CasProcessorViewer("Collection Readers");
	}
		
	private void setSelectedCollectionReaders(DefaultListModel model) {
		this.collectionReaders.setSelectedModel(model);
	}
	
	private CasProcessorViewer getCollectionReaderViewer() {
		return this.collectionReaders;
	}
	
	public DefaultTreeModel getAvailableCollectonReaders() {
		return this.collectionReaders.getAvailableModel();
	}
	
	private CasProcessorViewer analysisEngines;
	
	public void setAnalysisEngineViewer() {
		this.analysisEngines = new CasProcessorViewer("Analysis Engines");
	}
		
	private void setSelectedAnalysisEngines(DefaultListModel model) {
		this.analysisEngines.setSelectedModel(model);
	}
	
	private CasProcessorViewer getAnalysisEngineViewer() {
		return this.analysisEngines;
	}
	
	public DefaultTreeModel getAvailableAnalysisEngines() {
		return this.analysisEngines.getAvailableModel();
	}
	
	private EngineOptionViewer enginePreferences;
	
	private void setEnginePreferences() {
		this.enginePreferences = new EngineOptionViewer();
	}
	
	private EngineOptionViewer getEngineOptions() {
		return this.enginePreferences;
	}
	
	private JTabbedPane tabs;
	
	private void setTabs() {
		this.tabs = new JTabbedPane();
		this.tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.tabs.addTab("Collection Readers",this.getCollectionReaderViewer().getComponent());
		this.tabs.addTab(" Analysis Engines ",this.getAnalysisEngineViewer().getComponent());
		this.tabs.addTab("  Engine Options  ",this.getEngineOptions().getComponent());
	}
	
	public JTabbedPane getComponent() {
		return this.tabs;
	}
	
	public CollectionProcessingEngineViewer() {
		this.setCollectionReaderViewer();
		this.setAnalysisEngineViewer();
		this.setEnginePreferences();
		this.setTabs();
		this.doEnable(false);
	}
	
	public void doEnable(boolean enabled) {
		this.getCollectionReaderViewer().doEnable(enabled);
		this.getAnalysisEngineViewer().doEnable(enabled);
		this.getEngineOptions().doEnable(enabled);
		this.getComponent().setEnabled(enabled);
	}
	
	public void enableListeners(Dunamis dunamis,JList collectionProcessingEngines) {
		this.getCollectionReaderViewer().enableListeners(dunamis,collectionProcessingEngines);
		this.getAnalysisEngineViewer().enableListeners(dunamis,collectionProcessingEngines);
		this.getEngineOptions().enableListsners(dunamis);
	}
	
	public void update(Object object) {
		if (object instanceof CollectionProcessingEngine) {
			CollectionProcessingEngine cpe = (CollectionProcessingEngine) object;
			this.setSelectedCollectionReaders(cpe.getCollectionReaders());
			this.setSelectedAnalysisEngines(cpe.getAnalysisEngines());
			this.getEngineOptions().setModel(cpe);
		} 
	}
	
}
