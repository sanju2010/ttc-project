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
import eu.project.ttc.tools.commons.TermSuiteVersion;
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
import eu.project.ttc.tools.various.ToolBar;
import fr.free.rocheteau.jerome.dunamis.viewers.ProcessingResultViewer;

/**
 * Main class of the GUI version of TermSuite.
 *
 * This class is reponsible for creating the main GUI of TermSuite, as
 * well as creating the views, controllers and models of the tools.
 */
public class TermSuite implements Runnable {

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
//        this.setListener();

        // Bind window close button
        getFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                TermSuite.this.doQuit();
            }
        });
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

    public void run() {
        this.getFrame().setVisible(true);
    }

    private Dimension getDimension() {
        return new Dimension(1000,1000);
    }

    /**
     * Access the tool currently manipulated by the user, that is the one
     * with the focus.
     */
    public ToolController getTermSuiteTool() {
        switch ( getContent().getSelectedIndex() ) {
            case 0:
                return getSpotter();
            case 1:
//                return getIndexer();
            case 2:
//                return getAligner();
            default:
                // FIXME should not happen... but for now everything but spotted ends up here!
                return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////// TOOLBAR

    private ToolBar toolBar;

    private void setToolBar() {
        this.toolBar = new ToolBar();
        // Bind "about" command
        toolBar.getAbout().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( "about".equals( e.getActionCommand() ) ) {
                    getAbout().show();
                }
            }
        });
        // Bind "run" command
        toolBar.getRun().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( "run".equals( e.getActionCommand() ) ) {
                    doRun();
                }
            }
        });
        // Bind "save" command
        toolBar.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( "save".equals( e.getActionCommand() ) ) {
                    doSave(true);
                }
            }
        });
        // Bind "quit" command
        toolBar.getQuit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( "quit".equals( e.getActionCommand() ) ) {
                    doQuit();
                }
            }
        });
    }

    public ToolBar getToolBar() {
        return this.toolBar;
    }

    //////////////////////////////////////////////////////////////// CONFIG VALUES

    /**
     * Create the root directory for configuration files if necessary.
     */
    private void initConfig() {
        File cfgDir = new File( TermSuiteVersion.CFG_ROOT );
        if (! cfgDir.exists()) {
            cfgDir.mkdirs();
        }
    }

    public File getSpotterCfg() {
        return new File( TermSuiteVersion.CFG_SPOTTER );
    }

    public File getIndexerCfg() {
        return new File( TermSuiteVersion.CFG_INDEXER );
    }

    public File getAlignerCfg() {
        return new File( TermSuiteVersion.CFG_ALIGNER );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////// ACTIONS

    /**
     * Persists the models of the various tools.
     * Once executed flawlessly, the current TermSuite configuration is persisted on disk.
     *
     * @param showConfirmation  flag indicating if a confirmation dialog should be
     *                          presented to the user after the saving process.
     */
    public void doSave(boolean showConfirmation) {
        try {
            spotter.saveConfiguration();
            this.getIndexer().getSettings().doSave();
            this.getAligner().getSettings().doSave();
            if ( showConfirmation ) {
                JOptionPane.showMessageDialog(this.getFrame(),
                        "The TermSuite configuration has been successfully saved",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
            e.printStackTrace();
            displayException("An error occurred while saving the configuration.\n", e);
        }
    }

    /**
     * Quit the application.
     */
    private void doQuit() {
        int response = JOptionPane.showConfirmDialog(this.getFrame(),
                "Do you really want to quit " + TermSuiteVersion.TITLE + "?",
                "Quit",
                JOptionPane.YES_NO_OPTION);
        if (response == 0) {
            // FIXME Ask for save ?
            doSave(false);
            this.getFrame().setVisible(false);
            this.getFrame().dispose();
            System.exit(0);
        }
    }

    /**
     * Force to quit the application without user approval.
     * @param e the exception responsible for this force quit.
     */
    public void doForceQuit(Exception e) {
        // Log the exception
        UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
        e.printStackTrace();
        // Inform the user we crashed
        displayException("TermSuite has crashed because of the following error:\n", e);
        // Do crash! Save configuration first.
        doSave(false);
        this.getFrame().setVisible(false);
        this.getFrame().dispose();
        System.exit(1);
    }

    /**
     * Run the currently selected TermSuite tool in its current configuration.
     */
    public void doRun() {
        // Make sure to persist the configuration
        try {
            getTermSuiteTool().saveConfiguration();
        } catch (InvalidTermSuiteConfiguration e) {
            displayException("Unable to run: the TermSuite configuration is invalid.", e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            displayException("Unable to run: the TermSuite configuration cannot be written on disk.", e);
            e.printStackTrace();
            return;
        }

        // Run the tool
        try {
            // FIXME Gui stuff
            getViewer().doEnable(false);
            getViewer().getResultModel().clear();
            getToolBar().getRun().setEnabled(false);

            final TermSuiteRunner runner = new TermSuiteRunner(getTermSuiteTool());
            final JProgressBar pBar = getToolBar().getProgressBar();
            runner.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ( "progress".equals(evt.getPropertyName()) ) {
                        int progress = runner.getProgress();
                        pBar.setValue(progress);
                        pBar.setString(progress + " %");
                    }
                }
            });
            runner.execute();

            // FIXME again Gui stuff
            getToolBar().getStop().setEnabled(true);
            getToolBar().getStop().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("stop".equals(e.getActionCommand())) {
                        runner.cancel(false);
                    }
                }
            });
        } catch (Exception e) {
            displayException("An error occurred while running the tool.", e);
            e.printStackTrace();
        } finally {
            getToolBar().getStop().setEnabled(false);
            getToolBar().getRun().setEnabled(true);
        }
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
        this.frame.setTitle(TermSuiteVersion.TITLE);
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
//        this.content.addTab(" Spotter ", this.getSpotter().getView());
        this.content.addTab(" Spotter ", this.spotterV);
        this.content.addTab(" Indexer ",this.embed(this.getIndexer().getView(), this.getBanker().getComponent()));
        this.content.addTab(" Aligner ",this.embed(this.getAligner().getView(), this.getMixer().getComponent()));
//		ProcessingResultListener processingRresultListener = new ProcessingResultListener();
//		processingRresultListener.setViewer(this.getViewer());
//        Listener listener = new Listener();
//        listener.setTermSuite(this);
//        this.content.addChangeListener(listener);
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

    //
////	private TermSuiteListener listener;
//
//	private void setListener() {
//		this.listener = new TermSuiteListener();
//		this.enableListeners();
//		this.getToolBar().enableListeners(this.listener);
//		ProcessingResultListener processingRresultListener = new ProcessingResultListener();
//		processingRresultListener.setViewer(this.getViewer());
//		this.getViewer().enableListeners(processingRresultListener);
//		WindowListener windowListener = new WindowListener();
//		windowListener.setTermSuite(this);
//		this.getFrame().addWindowListener(windowListener);
//	}
//
//
//
//    private class Listener implements ChangeListener {
//
//        private TermSuite termSuite;
//
//        public void setTermSuite(TermSuite termSuite) {
//            this.termSuite = termSuite;
//        }
//
//        public void stateChanged(ChangeEvent event) {
//            this.termSuite.enableListeners();
//        }
//
//    }

    /************************************************************************************************** ABOUT WINDOW */
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
	}
	
	public About getAbout() {
		return this.about;
	}

    /*************************************************************************************************** SPOTTER TAB */

	private SpotterController spotter;
    private SpotterView spotterV;
	
	private void setSpotter() {
        // Prepare the MVC
        SpotterModel sModel = new SpotterModel(getSpotterCfg());
        //SpotterView sView = new SpotterView();
        this.spotterV = new SpotterView();
		this.spotter = new SpotterController(sModel, this.spotterV);

        // Load the persisted configuration if any
        try {
            spotter.loadConfiguration();
        } catch (IOException e) {
            // Non lethal if no configuration
        } catch (InvalidTermSuiteConfiguration e) {
            // Problem
            displayException("There was a problem loading the Spotter configuration.<br/>" +
                    "I recommend deleting the configuration file '" + spotter.getConfigurationFile() +
                    "' if it exists. If not, that may be the first time you run the program and " +
                    "the default configuration does not apply to you.", e);
        }
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
        IndexerSettings cfg = new IndexerSettings( TermSuiteVersion.CFG_INDEXER );
		this.indexer = new Indexer(cfg);
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
        AlignerSettings cfg = new AlignerSettings( TermSuiteVersion.CFG_INDEXER );
		this.aligner = new Aligner(cfg);
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

    /********************************************************************************************************** MAIN */

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) { }
		TermSuite termSuite = new TermSuite();
		SwingUtilities.invokeLater(termSuite);	
    }
	
}
