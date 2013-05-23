package eu.project.ttc.tools;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
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

import eu.project.ttc.tools.config.TermSuiteSettings;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.aligner.Aligner;
import eu.project.ttc.tools.aligner.AlignerEngine;
import eu.project.ttc.tools.aligner.AlignerViewer;
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

	/** Current version of the program */
	public static final String TERMSUITE_VERSION = "1.4";

    private final TermSuiteSettings pConfig;

    public TermSuite() {
        this.setDesktop();
        this.setPreferences();

        // Prepare persisted config
        pConfig = new TermSuiteSettings(this.getPreferences().getVersion());

        this.setAbout();
        this.setToolBar();
        this.setTagger();
        this.setViewer();
        this.setIndexer();
        this.setBanker();

        this.setAligner();
        this.setMixer();
        this.setContent();
        this.setComponent();
        this.setFrame();
        this.setListener();
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

    public Preferences  getPreferences() {
        return this.preferences;
    }

    public eu.project.ttc.tools.config.TermSuiteSettings getPConfig() {
        return this.pConfig;
    }

    private ToolBar toolBar;

    private void setToolBar() {
        this.toolBar = new ToolBar();
    }

    public ToolBar getToolBar() {
        return this.toolBar;
    }


    public void run() {
        this.show();
    }

    private TermSuite parent;

    public void setParent(TermSuite parent) {
        this.parent = parent;
    }

    public TermSuite getParent() {
        return this.parent;
    }

    private Dimension getDimension()
    {
        return new Dimension(1000,1000);
    }

    public void enableListeners() {
        if (this.isTaggerSelected()) {
            this.listener.setTool(this.getSpotter());
            SpotterEngine engine = new SpotterEngine();
            engine.setTool(this.getSpotter());
            this.listener.setEngine(engine);
        } else if (this.isIndexerSelected()) {
            this.listener.setTool(this.getIndexer());
            IndexerEngine engine = new IndexerEngine();
            engine.setTool(this.getIndexer());
            this.listener.setEngine(engine);
        } else if (this.isAlignerSelected()) {
            this.listener.setTool(this.getAligner());
            AlignerEngine engine = new AlignerEngine();
            engine.setTool(this.getAligner());
            this.listener.setEngine(engine);
        }
    }

    /******************************************************************************************************* ACTIONS */

    public boolean isTaggerSelected() {
        return this.getContent().getSelectedIndex() == 0;
    }

    public boolean isIndexerSelected() {
        return this.getContent().getSelectedIndex() == 1;
    }

    public boolean isAlignerSelected() {
        return this.getContent().getSelectedIndex() == 2;
    }

    public void save() {
        try {
            this.getSpotter().getSettings().doSave();
            this.getIndexer().getSettings().doSave();
            this.getAligner().getSettings().doSave();
        } catch (Exception e) {
            this.error(e);
        }
    }

    private void hide() {
        this.save();
        this.getFrame().setVisible(false);
    }

    private void show() {
        this.getFrame().setVisible(true);
    }

    public void quit() {
        this.hide();
        this.getFrame().dispose();
        System.exit(0);
    }

    public void quit(Exception e) {
        // Log the exception
        UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
        e.printStackTrace();
        // Inform the user we crashed
        displayException("TermSuite has crashed because of the following error:\n", e);
        // Do crash!
        this.hide();
        this.getFrame().dispose();
        System.exit(1);
    }

    public void displayException(String msg, Exception e) {
        JOptionPane.showMessageDialog(
                getFrame(),
                msg + e.getMessage(),
                e.getClass().getSimpleName(),
                JOptionPane.ERROR_MESSAGE);
    }


    /***************************************************************************************************** MAIN PANE */

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

    private JTabbedPane embed(Component edit, Component view) {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab(" Edit ", edit);
        tabs.addTab(" View ", view);
        return tabs;
    }

    private JTabbedPane content;

    private void setContent() {
        this.content = new JTabbedPane();
        this.content.setTabPlacement(JTabbedPane.LEFT);
        this.content.addTab(" Spotter ",this.embed(this.getSpotter().getComponent(), this.getViewer().getComponent()));
        this.content.addTab(" Indexer ",this.embed(this.getIndexer().getComponent(), this.getBanker().getComponent()));
        this.content.addTab(" Aligner ",this.embed(this.getAligner().getComponent(), this.getMixer().getComponent()));
        Listener listener = new Listener();
        listener.setTermSuite(this);
        this.content.addChangeListener(listener);
    }

    /************************************************************************************************** ABOUT WINDOW */
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	public About getAbout() {
		return this.about;
	}

    /*************************************************************************************************** SPOTTER TAB */

	private Spotter spotter;
	
	private void setTagger() {
		this.spotter = new Spotter(pConfig);
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

    /*************************************************************************************************** INDEXER TAB */

	private Indexer indexer;
	
	private void setIndexer() {
		this.indexer = new Indexer(pConfig);
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

    /*************************************************************************************************** ALIGNER TAB */

	private Aligner aligner;
	
	private void setAligner() {
		this.aligner = new Aligner(pConfig);
		this.aligner.setParent(this);
	}
	
	public Aligner getAligner() {
		return this.aligner;
	}
	
	private AlignerViewer mixer;
	
	private void setMixer() {
		this.mixer = new AlignerViewer();
	}
	
	public AlignerViewer getMixer() {
		return this.mixer;
	}
	
	public JFrame getFrame() {
		return this.frame;
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



    private class Listener implements ChangeListener {

        private TermSuite termSuite;

        public void setTermSuite(TermSuite termSuite) {
            this.termSuite = termSuite;
        }

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

    /********************************************************************************************************** MAIN */

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) { }
		TermSuite termSuite = new TermSuite();
		SwingUtilities.invokeLater(termSuite);	
    }
	
}
