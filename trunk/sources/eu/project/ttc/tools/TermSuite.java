package eu.project.ttc.tools;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;

import eu.project.ttc.tools.commons.InvalidTermSuiteConfiguration;
import eu.project.ttc.tools.commons.ToolController;
import eu.project.ttc.tools.config.AlignerSettings;
import eu.project.ttc.tools.config.IndexerSettings;
import eu.project.ttc.tools.spotter.SpotterController;
import eu.project.ttc.tools.spotter.SpotterModel;
import eu.project.ttc.tools.spotter.SpotterView;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.aligner.Aligner;
import eu.project.ttc.tools.aligner.AlignerViewer;
import eu.project.ttc.tools.indexer.Indexer;
import eu.project.ttc.tools.indexer.IndexerViewer;
import eu.project.ttc.tools.various.About;
import eu.project.ttc.tools.various.Preferences;
import eu.project.ttc.tools.various.ToolBar;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

/**
 * Main class of the GUI version of TermSuite.
 */
public class TermSuite implements Runnable {

    /** Current version of the program */
    public static final String TERMSUITE_VERSION = "1.5-SNAPSHOT";
    /** Name of the directory where the config is persisted */
    public static final String CFG_ROOTDIR_NAME = ".term-suite";
    /** Name of the file where the SpotterController config is persisted */
    public static final String CFG_SPOTTER_NAME = "spotter.settings";
    /** Name of the file where the Indexer config is persisted */
    public static final String CFG_INDEXER_NAME = "indexer.settings";
    /** Name of the file where the Aligner config is persisted */
    public static final String CFG_ALIGNER_NAME = "aligner.settings";

    /** Preferences store various data used to define the system */
    public static final Preferences preferences = new Preferences();
    static {
        preferences.setSummary(
                "This tool provides 3 tools for processing terminology extraction "
                + "and terminology alignment from comparable corpora. "
                + "It has been developed within the European TTC project (see http://www.ttc-project.eu/).");
        preferences.setLicense(
                "This software is distributed under the Apache Licence version 2.");
        preferences.setTitle("Term Suite");
        preferences.setVersion(TERMSUITE_VERSION);
        preferences.setConfigRoot( System.getProperty("user.home")
                + File.separator + CFG_ROOTDIR_NAME + File.separator + TERMSUITE_VERSION);
        preferences.setSpotterConfig(
                preferences.getConfigRoot() + File.separator + CFG_SPOTTER_NAME);
        preferences.setIndexerConfig(
                preferences.getConfigRoot() + File.separator + CFG_INDEXER_NAME);
        preferences.setAlignerConfig(
                preferences.getConfigRoot() + File.separator + CFG_ALIGNER_NAME);
    }

    public TermSuite() {
        this.setDesktop();

        initConfig();

        this.setAbout();
        this.setToolBar();
        this.setSpotter();
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
        // FIXME
//        if (this.isTaggerSelected()) {
//            this.listener.setTool(this.getSpotter());
//            SpotterEngine engine = new SpotterEngine();
//            engine.setTool(this.getSpotter());
//            this.listener.setEngine(engine);
//        } else if (this.isIndexerSelected()) {
//            this.listener.setTool(this.getIndexer());
//            IndexerEngine engine = new IndexerEngine();
//            engine.setTool(this.getIndexer());
//            this.listener.setEngine(engine);
//        } else if (this.isAlignerSelected()) {
//            this.listener.setTool(this.getAligner());
//            AlignerEngine engine = new AlignerEngine();
//            engine.setTool(this.getAligner());
//            this.listener.setEngine(engine);
//        }
    }

    //////////////////////////////////////////////////////////////// CONFIG VALUES

    /**
     * Initialize the configuration by computing the path to the various configuration
     * files for each tool.
     */
    private void initConfig() {
        // Compute the path to the configuration dir and create it if necessary
        cfgDirPath = System.getProperty("user.home")
                + File.separator + CFG_ROOTDIR_NAME + File.separator + TERMSUITE_VERSION;
        File cfgDir = new File(cfgDirPath);
        if (! cfgDir.exists()) {
            cfgDir.mkdirs();
        }

        // Compute other settings path
        cfgSpotterFile = new File(cfgDir.getAbsolutePath() + File.separator + CFG_SPOTTER_NAME);
        cfgIndexerFile = new File(cfgDir.getAbsolutePath() + File.separator + CFG_INDEXER_NAME);
        cfgAlignerFile = new File(cfgDir.getAbsolutePath() + File.separator + CFG_ALIGNER_NAME);
    }

    public File getSpotterCfg() {
        return cfgSpotterFile;
    }

    public File getIndexerCfg() {
        return cfgIndexerFile;
    }

    public File getAlignerCfg() {
        return cfgAlignerFile;
    }

    //////////////////////////////////////////////////////////////// ACTIONS

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
            this.getSpotter().saveConfiguration(); //.getSettings().doSave();
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

    public void displayException(String msg, Throwable e) {
        final JPanel errorPane = new JPanel();
        errorPane.setLayout(new BorderLayout());
        errorPane.setSize(new Dimension(30, 100));

        // Create a label for the message
        final JLabel lblMsg = new JLabel();
        lblMsg.setText("<html>" + msg.replaceAll("\n", "<br>")); // multiline
        errorPane.add(lblMsg, BorderLayout.NORTH);

        // Create and configure a text area for the exception
        if (e != null) {
            final JTextArea taErr = new JTextArea();
            taErr.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
            taErr.setEditable(false);
            StringWriter writer = new StringWriter();
            writer.write(e.getMessage());
            writer.write("\n\n");
            e.printStackTrace(new PrintWriter(writer));
            taErr.setText(writer.toString());
            // stuff it in a scrollpane with a controlled size.
            JScrollPane scrollPane = new JScrollPane(taErr);
            scrollPane.setPreferredSize(new Dimension(350, 150));
            errorPane.add(scrollPane, BorderLayout.SOUTH);
        }

        // pass the error pane to the joptionpane.
        JOptionPane.showMessageDialog(
                getFrame(),
                errorPane,
                "An error has occurred",
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
//        this.content.addTab(" Spotter ",this.embed(this.getSpotter().getView(), this.getViewer().getComponent()));
        this.content.addTab(" Spotter ", this.getSpotter().getView());
        this.content.addTab(" Indexer ",this.embed(this.getIndexer().getView(), this.getBanker().getComponent()));
        this.content.addTab(" Aligner ",this.embed(this.getAligner().getView(), this.getMixer().getComponent()));
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

	private SpotterController spotter;
	
	private void setSpotter() {
        // Prepare the MVC
        SpotterModel sModel = new SpotterModel(getSpotterCfg());
        SpotterView sView = new SpotterView();
		this.spotter = new SpotterController(sModel, sView);

        // Load the persisted configuration if any
        try {
            spotter.loadConfiguration();
        } catch (IOException e) {} // Non lethal if no configuration
	}
	
	private SpotterController getSpotter() {
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
