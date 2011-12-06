package eu.project.ttc.tools.tagger;

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

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.tagger.freeling.FreeLing;
import eu.project.ttc.tools.tagger.freeling.FreeLingEngine;
import eu.project.ttc.tools.tagger.rftagger.RFTagger;
import eu.project.ttc.tools.tagger.rftagger.RFTaggerEngine;
import eu.project.ttc.tools.tagger.tildetagger.TildeTagger;
import eu.project.ttc.tools.tagger.tildetagger.TildeTaggerEngine;
import eu.project.ttc.tools.tagger.treetagger.TreeTagger;
import eu.project.ttc.tools.tagger.treetagger.TreeTaggerEngine;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;
import eu.project.ttc.tools.utils.ToolBar;
import fr.free.rocheteau.jerome.dunamis.listeners.ProcessingResultListener;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

public class Tagger implements Runnable {

	private TermSuite parent;
	
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}
	
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
	
	private Preferences preferences;
	
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}
	
	private About about;
	
	public void setAbout(About about) {
		this.about = about;
	}
	
	public About getAbout() {
		return this.about;
	}

	private ToolBar toolBar;
	
	private void setToolBar() {
		this.toolBar = new ToolBar();
	}
	
	public ToolBar getToolBar() {
		return this.toolBar;
	}
	
	private ProcessingResultViewer documents;
	
	private void setDocuments() {
		this.documents = new ProcessingResultViewer();
	}
	
	public ProcessingResultViewer getDocuments() {
		return this.documents;
	}

	private TreeTagger treeTagger;
	
	private void setTreeTagger() {
		this.treeTagger = new TreeTagger();
		this.treeTagger.setParent(this);
	}
	
	private TreeTagger getTreeTagger() {
		return this.treeTagger;
	}
	
	private TildeTagger tildeTagger;
	
	private void setTildeTagger() {
		this.tildeTagger = new TildeTagger();
		this.tildeTagger.setParent(this);
	}
	
	private TildeTagger getTildeTagger() {
		return this.tildeTagger;
	}
	
	private RFTagger rfTagger;
	
	private void setRFTagger() {
		this.rfTagger = new RFTagger();
		this.rfTagger.setParent(this);
	}
	
	private RFTagger getRFTagger() {
		return this.rfTagger;
	}
	
	private FreeLing freeLing;
	
	private void setFreeLing() {
		this.freeLing = new FreeLing();
		this.freeLing.setParent(this);
	}
	
	private FreeLing getFreeLing() {
		return this.freeLing;
	}
	
	private JTabbedPane content;
	
	private void setContent() {
		this.content = new JTabbedPane();
		this.content.setTabPlacement(JTabbedPane.TOP);
		this.content.addTab("  TreeTagger  ",this.getTreeTagger().getComponent());
		this.content.addTab(" Tilde Tagger ",this.getTildeTagger().getComponent());
		this.content.addTab("   RFTagger   ",this.getRFTagger().getComponent());
		this.content.addTab("   FreeLing   ",this.getFreeLing().getComponent());
		this.content.addTab("  Documents   ",this.getDocuments().getComponent());
		Listener listener = new Listener();
		listener.setTagger(this);
		this.content.addChangeListener(listener);
	}
	
	private JTabbedPane getContent() {
		return this.content;
	}
	
	public boolean isTreeTaggerSelected() {
		return this.getContent().getSelectedIndex() == 0;
	}
	
	public boolean isTildeTaggerSelected() {
		return this.getContent().getSelectedIndex() == 1;
	}
	
	public boolean isRFTaggerSelected() {
		return this.getContent().getSelectedIndex() == 2;
	}
	
	public boolean isFreeLingSelected() {
		return this.getContent().getSelectedIndex() == 3;
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
	
	private Dimension getDimension() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (4 * screen.width) / 5;
		int height = (4 * screen.height) / 5;
		Dimension dimension = new Dimension(width,height);
		return dimension;
	}
	
	private JFrame frame;
	
	private void setFrame() {
		this.frame = new JFrame();
		this.frame.setTitle("Tagger");
		this.frame.setPreferredSize(this.getDimension());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.getContentPane().add(this.getComponent());
		this.frame.setJMenuBar(null);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
	}

	private void hide() {
		this.getFrame().setVisible(false);
	}
	
	private void show() {
		this.getFrame().setVisible(true);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public Tagger(boolean cli) {
		this.enableCommandLineInterface(cli);
		this.setToolBar();
		this.setTreeTagger();
		this.setTildeTagger();
		this.setRFTagger();
		this.setFreeLing();
		this.setDocuments();
		this.setContent();
		this.setComponent();
		this.setFrame();
		this.setListener();
	}
	
	private TaggerEngineListener listener;
	
	private void setListener() {
		this.listener = new TaggerEngineListener();
		this.enableListsners();
		this.getToolBar().enableListeners(this.listener);
		ProcessingResultListener processingRresultListener = new ProcessingResultListener();
		processingRresultListener.setViewer(this.getDocuments());
		this.getDocuments().enableListeners(processingRresultListener);
		WindowListener windowListener = new WindowListener();
		windowListener.setTagger(this);
		this.getFrame().addWindowListener(windowListener);
	}
	
	public void enableListsners() {
		if (this.isTreeTaggerSelected()) {
			this.setPreferences(this.getTreeTagger().getPreferences());
			this.setAbout(this.getTreeTagger().getAbout());
			this.listener.setTaggerTool(this.getTreeTagger());
			TreeTaggerEngine engine = new TreeTaggerEngine();
			engine.setTaggerTool(this.getTreeTagger());
			this.listener.setTaggerEngine(engine);
		} else if (this.isTildeTaggerSelected()) {
			this.setPreferences(this.getTildeTagger().getPreferences());
			this.setAbout(this.getTildeTagger().getAbout());
			this.listener.setTaggerTool(this.getTildeTagger());
			TildeTaggerEngine engine = new TildeTaggerEngine();
			engine.setTaggerTool(this.getTildeTagger());
			this.listener.setTaggerEngine(engine);
		} else if (this.isRFTaggerSelected()) {
			this.setPreferences(this.getRFTagger().getPreferences());
			this.setAbout(this.getRFTagger().getAbout());
			this.listener.setTaggerTool(this.getRFTagger());
			RFTaggerEngine engine = new RFTaggerEngine();
			engine.setTaggerTool(this.getRFTagger());
			this.listener.setTaggerEngine(engine);
		} else if (this.isFreeLingSelected()) {
			this.setPreferences(this.getFreeLing().getPreferences());
			this.setAbout(this.getFreeLing().getAbout());
			this.listener.setTaggerTool(this.getFreeLing());
			FreeLingEngine engine = new FreeLingEngine();
			engine.setTaggerTool(this.getFreeLing());
			this.listener.setTaggerEngine(engine);
		}
	}
	
	private void process() {
		/*
		TreeTaggerEngineListener engineListener = new TreeTaggerEngineListener();
		engineListener.setTreeTagger(this);
		engineListener.doProcess();
		*/
	}
	
	public void run() {
		if (this.isCommandLineInterface()) {
			this.process();
		} else {
			this.show();
		}
	}
	
	private void save() {
		try {
			this.getTreeTagger().getSettings().doSave();
			this.getTildeTagger().getSettings().doSave();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	public void quit() {
		this.save();
		this.hide();
		this.getFrame().dispose();
		if (this.getParent() == null) {
			System.exit(0);			
		}
	}
	
	public void quit(Exception e) {
		this.save();
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
		e.printStackTrace();
		this.hide();
		this.getFrame().dispose();
		if (this.getParent() == null) {
			System.exit(1);			
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
			Tagger treeTagger = new Tagger(cli);
			SwingUtilities.invokeLater(treeTagger);			
		} else {
			UIMAFramework.getLogger().log(Level.SEVERE,"Wrong option: " + wrong);
			UIMAFramework.getLogger().log(Level.INFO,"Options allowed: --cli | --gui");
			System.exit(1);
		}
    }
	
	private class WindowListener extends WindowAdapter {
				
		private Tagger tagger;
		
		public void setTagger(Tagger tagger) {
			this.tagger = tagger;
		}
		
		private Tagger getTagger() {
			return this.tagger;
		}
		
		public void windowClosing(WindowEvent event) {
			String message = "Do you really want to quit " + this.getTagger().getPreferences().getTitle() + "?";
			String title = "Exit?";
			int response = JOptionPane.showConfirmDialog(this.getTagger().getFrame(),message,title,JOptionPane.OK_CANCEL_OPTION);
			if (response == 0) {
				this.getTagger().quit();
			} 
		 }
		
	}
	
	private class Listener implements ChangeListener {

		private Tagger tagger;
		
		public void setTagger(Tagger tagger) {
			this.tagger = tagger;
		}
		
		@Override
		public void stateChanged(ChangeEvent event) {
			this.tagger.enableListsners();
		}
		
	}
	
}
