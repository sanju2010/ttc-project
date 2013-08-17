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
import eu.project.ttc.tools.indexer.IndexerController;
import eu.project.ttc.tools.indexer.IndexerModel;
import eu.project.ttc.tools.indexer.IndexerView;
import eu.project.ttc.tools.spotter.SpotterController;
import eu.project.ttc.tools.spotter.SpotterModel;
import eu.project.ttc.tools.spotter.SpotterView;
import eu.project.ttc.tools.various.MainToolBar;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.aligner.Aligner;
import eu.project.ttc.tools.aligner.AlignerViewer;

/**
 * Main class of the GUI version of TermSuite.
 *
 * This class is reponsible for creating the main GUI of TermSuite, as
 * well as creating the views, controllers and models of the tools.
 */
public class TermSuite implements Runnable {

    private Desktop desktop;

    public TermSuite() {
        // Prepare minimal GUI
        initCommonGUIComponents();

        // Initialize configuration and tools
        initConfigs();
        initSpotter();
        initIndexer();
        this.setAligner(); // TODO
        this.setMixer();   // TODO

        // Create and bind the Window
        createMainWindow(new Dimension(1000, 1000));
        getMainWindow().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                TermSuite.this.doQuit();
            }
        });
    }

    /**
     * Implements Runnable. Actually mimics it as Swing components already
     * are running. Just show the Window.
     */
    public void run() {
        getMainWindow().setVisible(true);
    }

    /**
     * Access the tool currently manipulated by the user, that is the one
     * with the focus.
     */
    public ToolController getTermSuiteTool() {
        switch ( getMainTabs().getSelectedIndex() ) {
            case 0:
                return getSpotter();
            case 1:
                return getIndexer();
            case 2:
//                return getAligner();
            default:
                // FIXME should not happen... but for now everything but spotted ends up here!
                return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////// CONFIG VALUES

    /**
     * Create the root directory for configuration files if necessary.
     */
    private void initConfigs() {
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
            spotter.validateAndSaveConfiguration();
            indexer.validateAndSaveConfiguration();
            this.getAligner().getSettings().doSave(); // FIXME
            if ( showConfirmation ) {
                JOptionPane.showMessageDialog(this.getMainWindow(),
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
     * Operates the «quitting process» of the application. It asks the user if
     * he really wants to quit and if he wouldn't want to save its configuration
     * in the process.
     *
     * The user still can cancel the process at this point.
     */
    private void doQuit() {
        int saveAndQuitResponse = JOptionPane.showConfirmDialog(
                this.getMainWindow(),
                "You are about to quit " + TermSuiteVersion.TITLE + ".\n"
                + "Would you like to save the configuration before?",
                "Save and Quit",
                JOptionPane.YES_NO_CANCEL_OPTION);
        switch (saveAndQuitResponse) {
            case JOptionPane.YES_OPTION:
                doSave(false);
                // no break as we must quit afterward
            case JOptionPane.NO_OPTION:
                this.getMainWindow().setVisible(false);
                this.getMainWindow().dispose();
                System.exit(0);
            // Otherwise, cancel the quit!
        }
    }

    /**
     * Run the currently selected TermSuite tool in its current configuration.
     * Prepare the GUI in consequence.
     */
    public void doRun() {
        try {
            // Prepare for start
            getToolBar().setRunMode(true);
            getToolBar().setProgress(0, "Preparing the runner...");
            getTermSuiteTool().runStarts();

            // Check configuration and prepare the runner
            getTermSuiteTool().validateAndSaveConfiguration();
            final TermSuiteRunner runner = new TermSuiteRunner(getTermSuiteTool());

            // Bind everything GUI related to the execution
            getToolBar().getStop().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("stop".equals(e.getActionCommand())) {
                        runner.cancel(false);
                    }
                }
            });
            runner.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ( "progress".equals(evt.getPropertyName()) ) {
                        int progress = runner.getProgress();
                        getToolBar().setProgress(progress, progress + " %");
                    } else if ( "inerror".equals(evt.getPropertyName()) ) {
                        // Reset GUI
                        int progress = runner.getProgress();
                        getToolBar().setProgress(progress, "The analysis failed!");
                        getToolBar().setRunMode(false);
                        getTermSuiteTool().runEnds();
                        displayException("The analysis failed!", runner.getLastError());
                    }else if ( "cancelled".equals(evt.getPropertyName()) ) {
                        // Reset GUI
                        getToolBar().setProgress(0, "The analysis has been cancelled!");
                        getToolBar().setRunMode(false);
                        getTermSuiteTool().runEnds();
                    } else if ( "done".equals(evt.getPropertyName()) ) {
                        // Reset GUI
                        getToolBar().setProgress(100, "The analysis is complete!");
                        getToolBar().setRunMode(false);
                        getTermSuiteTool().runEnds();
                    }
                }
            });

            // Execute
            getToolBar().setProgress(0, "Starting the analysis...");
            runner.execute();
        } catch (InvalidTermSuiteConfiguration e) {
            displayException("Unable to run: the TermSuite configuration is invalid.", e);
            e.printStackTrace();
        } catch (IOException e) {
            displayException("Unable to run: the TermSuite configuration cannot be written on disk.", e);
            e.printStackTrace();
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
                getMainWindow(),
                errorPane,
                "An error has occurred",
                JOptionPane.ERROR_MESSAGE);
    }


    /***************************************************************************************************** MAIN PANE */

    private JFrame mainWindow;

    /**
     * Create the only frame of the application. This frame contains
     * all the components that the application is made of.
     *
     * @param preferredDimension
     *      preferred window dimension of the application
     */
    private void createMainWindow(Dimension preferredDimension) {
        // Create the window
        this.mainWindow = new JFrame();
        this.mainWindow.setTitle(TermSuiteVersion.TITLE);
        this.mainWindow.setPreferredSize(preferredDimension);
        this.mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.mainWindow.setJMenuBar(null);
        this.mainWindow.pack();
        this.mainWindow.setLocationRelativeTo(null);
        this.mainWindow.setResizable(true);

        // Add the components
        JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainPane.setTopComponent(this.getToolBar());
        mainPane.setBottomComponent(this.getMainTabs());
        mainPane.setDividerSize(0);
        mainPane.setEnabled(false);
        this.mainWindow.getContentPane().add(mainPane);
    }

    private JTabbedPane mainTabs;
    private MainToolBar toolBar;

    /**
     * Creates all the components that constitutes the main GUI frame
     * in which the tools are plugged: toolbar and tabs.
     *
     * This is not responsible for plugging the tools, neither for
     * creating the frame itself.
     */
    private void initCommonGUIComponents() {
        // Check for Desktop support
        if (Desktop.isDesktopSupported()) {
            this.desktop = Desktop.getDesktop();
        }

        // Toolbar
        this.toolBar = new MainToolBar();
        toolBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( "run".equals( e.getActionCommand() ) ) {
                    doRun();
                } else if ( "save".equals( e.getActionCommand() ) ) {
                    doSave(true);
                } else if ( "quit".equals( e.getActionCommand() ) ) {
                    doQuit();
                }
            }
        });

        // Tabs for the tools
        this.mainTabs = new JTabbedPane();
        this.mainTabs.setTabPlacement(JTabbedPane.LEFT);
    }

    public MainToolBar getToolBar() {
        return this.toolBar;
    }

    private JTabbedPane getMainTabs() {
        return this.mainTabs;
    }

    /*************************************************************************************************** SPOTTER TAB */

	private SpotterController spotter;

	private void initSpotter() {
        // Main content must have been initialized
        assert getMainTabs() != null;

        // Prepare the MVC
        SpotterModel sModel = new SpotterModel(getSpotterCfg());
        SpotterView sView = new SpotterView();
		this.spotter = new SpotterController(sModel, sView);
        // Add to the tabs
        getMainTabs().insertTab(" Spotter ", null, sView, null, 0);

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

    /*************************************************************************************************** INDEXER TAB */

    private IndexerController indexer;

    private void initIndexer() {
        // Main content must have been initialized
        assert getMainTabs() != null;

        // Prepare the MVC
        IndexerModel sModel = new IndexerModel(getIndexerCfg());
        IndexerView sView = new IndexerView();
        this.indexer = new IndexerController(sModel, sView);
        // Add to the tabs
        getMainTabs().insertTab(" Indexer ", null, sView, null, 1);

        // Load the persisted configuration if any
        try {
            indexer.loadConfiguration();
        } catch (IOException e) {
            // Non lethal if no configuration
        } catch (InvalidTermSuiteConfiguration e) {
            // Problem
            displayException("There was a problem loading the Indexer configuration.<br/>" +
                    "I recommend deleting the configuration file '" + indexer.getConfigurationFile() +
                    "' if it exists. If not, that may be the first time you run the program and " +
                    "the default configuration does not apply to you.", e);
        }
    }

    private IndexerController getIndexer() {
        return this.indexer;
    }

    /*************************************************************************************************** ALIGNER TAB */

	private Aligner aligner;
	
	private void setAligner() {
        AlignerSettings cfg = new AlignerSettings( TermSuiteVersion.CFG_INDEXER );
		this.aligner = new Aligner(cfg);
		this.aligner.setParent(this);

        //        this.mainTabs.addTab(" Aligner ", this.embed(this.getAligner().getView(), this.getMixer().getComponent()));
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
	
	public JFrame getMainWindow() {
		return this.mainWindow;
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
