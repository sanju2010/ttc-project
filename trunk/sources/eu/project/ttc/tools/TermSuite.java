package eu.project.ttc.tools;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.help.Help;
import eu.project.ttc.tools.indexer.Indexer;
import eu.project.ttc.tools.indexer.IndexerEngine;
import eu.project.ttc.tools.indexer.IndexerViewer;
import eu.project.ttc.tools.spotter.Spotter;
import eu.project.ttc.tools.spotter.SpotterEngine;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;
import eu.project.ttc.tools.utils.ToolBar;
import fr.free.rocheteau.jerome.dunamis.listeners.ProcessingResultListener;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

public class TermSuite implements Runnable {

	private boolean cli;
	
	private void enableCommandLineInterface(boolean enabled) {
		this.cli = enabled;
	}
	
	public boolean isCommandLineInterface() {
		return this.cli;
	}
	
	public void error(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
	}
	
	public void warning(String message) {
		UIMAFramework.getLogger().log(Level.WARNING,message);
	}
	
	public void message(String message) {
		UIMAFramework.getLogger().log(Level.INFO,message);
	}
	
	private Desktop desktop;
	
	private void setDesktop() {
		if (Desktop.isDesktopSupported()) {
			this.desktop = Desktop.getDesktop();
		}
	}
	
	public Desktop getDesktop() {
		return this.desktop;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("term-suite.properties");
		try {
			this.preferences.load();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	public About getAbout() {
		return this.about;
	}
	
	private Help help;
	
	private void setHelp() {
		this.help = new Help();
	}
	
	public Help getHelp() {
		return this.help;
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}

	private ToolBar toolBar;
	
	private void setToolBar() {
		this.toolBar = new ToolBar();
	}
	
	public ToolBar getToolBar() {
		return this.toolBar;
	}
	
	public void enableListeners() {
		if (this.isTaggerSelected()) {
			this.listener.setTermSuiteTool(this.getSpotter());
			SpotterEngine engine = new SpotterEngine();
			engine.setTagger(this.getSpotter());
			this.listener.setTermSuiteEngine(engine);
		} else if (this.isIndexerSelected()) {
			this.listener.setTermSuiteTool(this.getIndexer());
			IndexerEngine engine = new IndexerEngine();
			engine.setAcabit(this.getIndexer());
			this.listener.setTermSuiteEngine(engine);
		} /* else if (this.isContextualizerSelected()) {
			this.listener.setTermSuiteTool(this.getContextualizer());
			KatastasisEngine engine = new KatastasisEngine();
			engine.setKatastasis(this.getContextualizer());
			this.listener.setTermSuiteEngine(engine);
		} else if (this.isConverterSelected()) {
			this.getConverter().enableListsners(this.listener);
		} */
	}
		
	public void run() {
		this.show();
	}
		
	public void quit() {
		this.hide();
		this.getFrame().dispose();
		System.exit(0);
	}
	
	public void quit(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		e.printStackTrace();
		this.hide();
		this.getFrame().dispose();
		System.exit(1);
	}
	
	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}	
	
	private Dimension getDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	private Spotter spotter;
	
	private void setTagger() {
		this.spotter = new Spotter();
		this.spotter.setParent(this);
	}
	
	private Spotter getSpotter() {
		return this.spotter;
	}
	
	private ProcessingResultViewer viewer;
	
	private void setViewer() {
		this.viewer = new ProcessingResultViewer();
	}
	
	public ProcessingResultViewer getViewer() {
		return this.viewer;
	}
	
	
	private Indexer indexer;
	
	private void setIndexer() {
		this.indexer = new Indexer();
		this.indexer.setParent(this);
	}
	
	public Indexer getIndexer() {
		return this.indexer;
	}
	
	private IndexerViewer banker;
	
	private void setBanker() {
		this.banker = new IndexerViewer();
	}
	
	public IndexerViewer getBanker() {
		return this.banker;
	}

	/*
	
	private Katastasis contextualizer;
	
	private void setContextualizer() {
		this.contextualizer = new Katastasis();
		this.contextualizer.setParent(this);
	}
	
	public Katastasis getContextualizer() {
		return this.contextualizer;
	}
	
	private KatastasisViewer contextualierViewer;
	
	private void setContextualizerViewer() {
		this.contextualierViewer = new KatastasisViewer();
	}
	
	public KatastasisViewer getContextualizerViewer() {
		return this.contextualierViewer;
	}
	
	private Converter converter;
	
	private void setConverter() {
		this.converter = new Converter();
		this.converter.setParent(this);
	}
	
	public Converter getConverter() {
		return this.converter;
	}
	
	*/
	
	private JTabbedPane content;
	
	private void setContent() {
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.LEFT);
		this.content.addTab("     Spotter     ",this.getSpotter().getComponent());
		this.content.addTab("     Viewer     ",this.getViewer().getComponent());
		this.content.addTab("     Indexer    ",this.getIndexer().getComponent());
		this.content.addTab("     Banker     ",this.getBanker().getComponent());
		/*
		this.content.addTab("   Term Viewer  ",this.getTermerViewer().getComponent());
		this.content.addTab(" Contextualizer ",this.getContextualizer().getComponent());
		this.content.addTab(" Context Viewer ",this.getContextualizerViewer().getComponent());
		this.content.addTab("    Converter   ",this.getConverter().getComponent());
		this.content.addTab(" Helper ",this.getHelp().getComponent());
		*/
		Listener listener = new Listener();
		listener.setTermSuite(this);
		this.content.addChangeListener(listener);
	}
	
	public boolean isTaggerSelected() {
		return this.getContent().getSelectedIndex() == 0;
	}
	
	public boolean isViewerSelected() {
		return this.getContent().getSelectedIndex() == 1;
	}
	
	public boolean isIndexerSelected() {
		return this.getContent().getSelectedIndex() == 2;
	}
	
	public boolean isBankerSelected() {
		return this.getContent().getSelectedIndex() == 3;
	}
	
	public boolean isContextualizerSelected() {
		return this.getContent().getSelectedIndex() == 4;
	}
	
	public boolean isContextViewerSelected() {
		return this.getContent().getSelectedIndex() == 5;
	}
	
	public boolean isConverterSelected() {
		return this.getContent().getSelectedIndex() == 6;
	}
	
	private JTabbedPane getContent() {
		return this.content;
	}
	
	private JSplitPane component;
	
	private void setComponent() {
		this.component = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.component.setTopComponent(this.getToolBar().getComponent());
		this.component.setBottomComponent(this.getContent());
		this.component.setDividerSize(0);
		this.component.setEnabled(false);
	}
	
	private JSplitPane getComponent() {
		return this.component;
	}
	
	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle(this.getPreferences().getTitle());
		this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getComponent());
		this.frame.setJMenuBar(null);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(true);
	}

	private void hide() {
		this.save();
		this.getFrame().setVisible(false);
	}
	
	private void show() {
		this.getFrame().setVisible(true);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public TermSuite(boolean cli) {
		this.enableCommandLineInterface(cli);
		this.setDesktop();
		this.setHelp();
		this.setPreferences();
		this.setAbout();
		this.setToolBar();
		this.setTagger();
		this.setViewer();
		this.setIndexer();
		this.setBanker();
		/*
		this.setTermerViewer();
		this.setContextualizer();
		this.setContextualizerViewer();
		this.setConverter();
		*/
		this.setContent();
		this.setComponent();
		this.setFrame();
		this.setListener();
	}
	
	private TermSuiteListener listener;
	
	private void setListener() {
		this.listener = new TermSuiteListener();
		this.enableListeners();
		this.getToolBar().enableListeners(this.listener);
		ProcessingResultListener processingRresultListener = new ProcessingResultListener();
		processingRresultListener.setViewer(this.getViewer());
		this.getViewer().enableListeners(processingRresultListener);
		WindowListener windowListener = new WindowListener();
		windowListener.setTermSuite(this);
		this.getFrame().addWindowListener(windowListener);
	}
		
	private void save() {
		try {
			this.getSpotter().getSettings().doSave();
			this.getIndexer().getSettings().doSave();
			/*
			this.getContextualizer().getSettings().doSave();
			this.getConverter().doSave();
			*/
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }
		boolean cli = false;
		String wrong = null;
		for (String arg : args) {
			if (arg.equals("--cli")) {
				cli = true;
				break;
			} else if (arg.equals("--gui")) {
				cli = false;
				break;
			} else {
				wrong = arg;
				break;
			}
		}
		if (wrong == null) {
			TermSuite termSuite = new TermSuite(cli);
			SwingUtilities.invokeLater(termSuite);			
		} else {
			UIMAFramework.getLogger().log(Level.SEVERE,"Wrong option: " + wrong);
			UIMAFramework.getLogger().log(Level.INFO,"Options allowed: --cli | --gui");
			System.exit(1);
		}
    }
	
	
	private class Listener implements ChangeListener {

		private TermSuite termSuite;
		
		public void setTermSuite(TermSuite termSuite) {
			this.termSuite = termSuite;
		}
		
		@Override
		public void stateChanged(ChangeEvent event) {
			this.termSuite.enableListeners();
		}
		
	}
	
	private class WindowListener extends WindowAdapter {
				
		private TermSuite termSuite;
		
		public void setTermSuite(TermSuite termSuite) {
			this.termSuite = termSuite;
		}
		
		private TermSuite getTermSuite() {
			return this.termSuite;
		}
		
		public void windowClosing(WindowEvent event) {
			String message = "Do you really want to quit " + this.getTermSuite().getPreferences().getTitle() + "?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getTermSuite().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getTermSuite().quit();
			} 
		 }
		
	}
	
}
