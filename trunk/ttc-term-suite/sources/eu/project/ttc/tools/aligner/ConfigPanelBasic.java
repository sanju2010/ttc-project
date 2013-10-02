// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.aligner;

import eu.project.ttc.tools.commons.InputSource;
import eu.project.ttc.tools.commons.LanguageItem;
import eu.project.ttc.tools.commons.TTCDirectoryChooser;
import eu.project.ttc.tools.commons.TTCFileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

/**
 * This JPanel exposes the basic configuration part of the Aligner tool.
 * That is only the parameters common to every tool and nothing specific
 * so to keep it really simple.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
@SuppressWarnings("serial")
public class ConfigPanelBasic extends JPanel {

    private static final String LBL_EVALDIR = "Evaluation Directory";
    private static final String LBL_SRCLANGUAGE = "Source language";
    private static final String LBL_SRCTERMINO = "Source terminology";
    private static final String LBL_TGTLANGUAGE = "Target language";
    private static final String LBL_TGTTERMINO = "Target terminology";
    private static final String LBL_OUTPUT = "Output directory";

    // Evaluation directory parameter
    private JLabel lblEvalDir;
    private TTCDirectoryChooser fcEvalDirectory;
    private JEditorPane epEvalDir;

    // Source language parameter
    private JLabel lblSrcLanguage;
    private JComboBox<LanguageItem> cbSrcLanguage;
    private JEditorPane epSrcLanguage;

    // Source terminology file parameter
    private JLabel lblSrcTermino;
    private TTCFileChooser fcSrcTermino;
    private JEditorPane epSrcTermino;

    // Target language parameter
    private JLabel lblTgtLanguage;
    private JComboBox<LanguageItem> cbTgtLanguage;
    private JEditorPane epTgtLanguage;

    // Target terminology file parameter
    private JLabel lblTgtTermino;
    private TTCFileChooser fcTgtTermino;
    private JEditorPane epTgtTermino;

    // Output directory
    private JLabel lblOutput;
    private TTCDirectoryChooser fcOutput;
    private JEditorPane epOutput;

    private GroupLayout cfgLayout;

    public ConfigPanelBasic() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createEvalDirComponents(pWidth);
        createSrcForm(pWidth);
        createTgtComponents(pWidth);
        createOutputComponents(pWidth);

        // Layout the components
        layoutComponents();
    }

    /**
     * This HUGE method is responsible to layout the panel by grouping
     * together parameters labels, fields and descriptions.
     *
     * Yes, this is quite massive and it looks messy, but I didn't find
     * a better way to do it as there are side effects regarding the
     * group layout.
     * So please, do not intend to change things unless you're sure you
     * plainly understand the group layout!
     */
    private void layoutComponents() {
        JPanel config = new JPanel();

        // Init the group layout
        cfgLayout = new GroupLayout(config);
        config.setLayout(cfgLayout);

        // Configure the horizontal layout
        cfgLayout.setHorizontalGroup(
                cfgLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        // Evaluation dir parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblEvalDir)
                                        .addComponent(fcEvalDirectory))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epEvalDir)
                        )
                        // Source language and terminology
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSrcLanguage)
                                        .addComponent(cbSrcLanguage))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epSrcLanguage)
                        )
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSrcTermino)
                                        .addComponent(fcSrcTermino))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epSrcTermino)
                        )
                        // Target language and terminology
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTgtLanguage)
                                        .addComponent(cbTgtLanguage))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epTgtLanguage)
                        )
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTgtTermino)
                                        .addComponent(fcTgtTermino))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epTgtTermino)
                        )
                        // Output dir parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblOutput)
                                        .addComponent(fcOutput))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epOutput)
                        )
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // Evaluation dir parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblEvalDir)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcEvalDirectory,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epEvalDir))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Source language and terminology
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblSrcLanguage)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbSrcLanguage,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epSrcLanguage))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblSrcTermino)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcSrcTermino,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epSrcTermino))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Target language and terminology
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblTgtLanguage)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbTgtLanguage,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epTgtLanguage))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblTgtTermino)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcTgtTermino,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epTgtTermino))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        // Output dir parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblOutput)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcOutput,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epOutput))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

        add(config, BorderLayout.NORTH);
    }

    /**
     * Create the components related to the evaluation dir parameter.
     */
    public void createEvalDirComponents(int preferredWidth) {
        // Eval directory label
        lblEvalDir = new JLabel("<html><b>" + LBL_EVALDIR + "</b></html>");
        lblEvalDir.setPreferredSize(new Dimension(
                (int) lblEvalDir.getPreferredSize().getHeight(),
                preferredWidth ));

        // Input directory field
        fcEvalDirectory = new TTCDirectoryChooser("Choose the evaluation directory");
        fcEvalDirectory.setPreferredSize(new Dimension(
                (int) fcEvalDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        fcEvalDirectory.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(AlignerBinding.PRM.EVALDIR.getProperty(),
                            evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help panel
        epEvalDir = new JEditorPane();
        epEvalDir.setEditable(false);
        epEvalDir.setOpaque(false);
        epEvalDir.setPreferredSize( new Dimension(
                (int) epEvalDir.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.evaluationdir.html");
            epEvalDir.setPage(res);
        } catch (IOException e) {} // No help
    }

    /**
     * Create the components related to the source terminology parameters.
     */
    public void createSrcForm(int preferredWidth) {
        // Source language label
        lblSrcLanguage = new JLabel("<html><b>" + LBL_SRCLANGUAGE + "</b></html>");
        lblSrcLanguage.setPreferredSize(new Dimension(
                (int) lblSrcLanguage.getPreferredSize().getHeight(),
                preferredWidth ));

        // Language combobox
        cbSrcLanguage = new JComboBox<LanguageItem>();
        for(String code: new String[]{"en","fr","de","es","ru","da","lv","zh"}) {
            cbSrcLanguage.addItem( new LanguageItem(code) );
        }
        cbSrcLanguage.setPreferredSize(new Dimension(
                (int) cbSrcLanguage.getPreferredSize().getHeight(),
                preferredWidth ));
        cbSrcLanguage.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    LanguageItem item = (LanguageItem) e.getItem();
                    firePropertyChange(AlignerBinding.PRM.SRCLANGUAGE.getProperty(), null, item.getValue());
                }
            }
        });

        // Help pane
        epSrcLanguage = new JEditorPane();
        epSrcLanguage.setEditable(false);
        epSrcLanguage.setOpaque(false);
        epSrcLanguage.setPreferredSize(new Dimension(
                (int) epSrcLanguage.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.srclanguage.html");
            epSrcLanguage.setPage(res);
        } catch (IOException e){} // No help

        // Source terminology label
        lblSrcTermino = new JLabel("<html><b>" + LBL_SRCTERMINO + "</b></html>");
        lblSrcTermino.setPreferredSize(new Dimension(
                (int) lblSrcTermino.getPreferredSize().getHeight(),
                preferredWidth ));

        // Source terminology file chooser
        fcSrcTermino = new TTCFileChooser("Choose the source terminology file",
                InputSource.InputSourceTypes.XMI);
        fcSrcTermino.setPreferredSize(new Dimension(
                (int) fcSrcTermino.getPreferredSize().getHeight(),
                preferredWidth ));
        fcSrcTermino.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(AlignerBinding.PRM.SRCTERMINOLOGY.getProperty(),
                            evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help pane
        epSrcTermino = new JEditorPane();
        epSrcTermino.setEditable(false);
        epSrcTermino.setOpaque(false);
        epSrcTermino.setPreferredSize(new Dimension(
                (int) epSrcTermino.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.srctermino.html");
            epSrcTermino.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the target terminology parameters.
     */
    public void createTgtComponents(int preferredWidth) {
        // Target language label
        lblTgtLanguage = new JLabel("<html><b>" + LBL_TGTLANGUAGE + "</b></html>");
        lblTgtLanguage.setPreferredSize(new Dimension(
                (int) lblTgtLanguage.getPreferredSize().getHeight(),
                preferredWidth ));

        // Target language combobox
        cbTgtLanguage = new JComboBox<LanguageItem>();
        for(String code: new String[]{"en","fr","de","es","ru","da","lv","zh"}) {
            cbTgtLanguage.addItem( new LanguageItem(code) );
        }
        cbTgtLanguage.setPreferredSize(new Dimension(
                (int) cbTgtLanguage.getPreferredSize().getHeight(),
                preferredWidth ));
        cbTgtLanguage.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    LanguageItem item = (LanguageItem) e.getItem();
                    firePropertyChange(AlignerBinding.PRM.TGTLANGUAGE.getProperty(), null, item.getValue());
                }
            }
        });

        // Help pane
        epTgtLanguage = new JEditorPane();
        epTgtLanguage.setEditable(false);
        epTgtLanguage.setOpaque(false);
        epTgtLanguage.setPreferredSize(new Dimension(
                (int) epTgtLanguage.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.tgtlanguage.html");
            epTgtLanguage.setPage(res);
        } catch (IOException e){} // No help

        // Target terminology label
        lblTgtTermino = new JLabel("<html><b>" + LBL_TGTTERMINO + "</b></html>");
        lblTgtTermino.setPreferredSize(new Dimension(
                (int) lblTgtTermino.getPreferredSize().getHeight(),
                preferredWidth ));

        // Target terminology file chooser
        fcTgtTermino = new TTCFileChooser("Choose the target terminology file",
                InputSource.InputSourceTypes.XMI);
        fcTgtTermino.setPreferredSize(new Dimension(
                (int) fcTgtTermino.getPreferredSize().getHeight(),
                preferredWidth ));
        fcTgtTermino.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(AlignerBinding.PRM.TGTTERMINOLOGY.getProperty(),
                            evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help pane
        epTgtTermino = new JEditorPane();
        epTgtTermino.setEditable(false);
        epTgtTermino.setOpaque(false);
        epTgtTermino.setPreferredSize(new Dimension(
                (int) epTgtTermino.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.tgttermino.html");
            epTgtTermino.setPage(res);
        } catch (IOException e){} // No help
    }

    /**
     * Create the components related to the output dir parameter.
     */
    public void createOutputComponents(int preferredWidth) {
        // Output directory label
        lblOutput = new JLabel("<html><b>" + LBL_OUTPUT + "</b></html>");
        lblOutput.setPreferredSize(new Dimension(
                (int) lblOutput.getPreferredSize().getHeight(),
                preferredWidth ));

        // Output directory field
        fcOutput = new TTCDirectoryChooser("Choose the evaluation directory");
        fcOutput.setPreferredSize(new Dimension(
                (int) fcOutput.getPreferredSize().getHeight(),
                preferredWidth ));
        fcOutput.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(AlignerBinding.PRM.OUTPUT.getProperty(),
                            evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help panel
        epOutput = new JEditorPane();
        epOutput.setEditable(false);
        epOutput.setOpaque(false);
        epOutput.setPreferredSize( new Dimension(
                (int) epOutput.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/aligner/param.outputdir.html");
            epOutput.setPage(res);
        } catch (IOException e) {} // No help
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setEvaluationDirectory(String evaluationDirectory) {
        fcEvalDirectory.setPath(evaluationDirectory);
    }
    public String getEvaluationDirectory() {
        return fcEvalDirectory.getPath();
    }
    public void setEvaluationDirectoryError(Throwable e) {
        lblEvalDir.setText("<html><b>" + LBL_EVALDIR + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetEvaluationDirectoryError() {
        lblEvalDir.setText("<html><b>" + LBL_EVALDIR + "</b></html>");
    }

    public void setSourceLanguage(String language) {
        for(int i=0 ; i < cbSrcLanguage.getItemCount() ; i++) {
            LanguageItem item = (LanguageItem) cbSrcLanguage.getItemAt(i);
            if ( item.getValue().equals(language) ) {
                if (cbSrcLanguage.getSelectedIndex() != i) {
                    cbSrcLanguage.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + language + "' as I do not handle this value.");
    }
    public String getSourceLanguage() {
        return ((LanguageItem) cbSrcLanguage.getSelectedItem()).getValue();
    }
    public void setSourceLanguageError(Throwable e) {
        lblSrcLanguage.setText("<html><b>" + LBL_SRCLANGUAGE + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetSourceLanguageError() {
        lblSrcLanguage.setText("<html><b>" + LBL_SRCLANGUAGE + "</b></html>");
    }

    public void setTargetLanguage(String language) {
        for(int i=0 ; i < cbTgtLanguage.getItemCount() ; i++) {
            LanguageItem item = (LanguageItem) cbTgtLanguage.getItemAt(i);
            if ( item.getValue().equals(language) ) {
                if (cbTgtLanguage.getSelectedIndex() != i) {
                    cbTgtLanguage.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + language + "' as I do not handle this value.");
    }
    public String getTargetLanguage() {
        return ((LanguageItem) cbTgtLanguage.getSelectedItem()).getValue();
    }
    public void setTargetLanguageError(Throwable e) {
        lblTgtLanguage.setText("<html><b>" + LBL_TGTLANGUAGE + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetTargetLanguageError() {
        lblTgtLanguage.setText("<html><b>" + LBL_TGTLANGUAGE + "</b></html>");
    }

    public void setOutputDirectory(String outputDirectory) {
        fcOutput.setPath(outputDirectory);
    }
    public String getOutputDirectory() {
        return fcOutput.getPath();
    }
    public void setOutputDirectoryError(Throwable e) {
        lblOutput.setText("<html><b>" + LBL_OUTPUT + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetOutputDirectoryError() {
        lblOutput.setText("<html><b>" + LBL_OUTPUT + "</b></html>");
    }

    public void setSourceTerminology(String sourceTerminology) {
        fcSrcTermino.setPath(sourceTerminology);
    }
    public String getSourceTerminology() {
        return fcSrcTermino.getPath();
    }
    public void setSourceTerminologyError(Throwable e) {
        lblSrcTermino.setText("<html><b>" + LBL_SRCTERMINO + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetSourceTerminologyError() {
        lblSrcTermino.setText("<html><b>" + LBL_SRCTERMINO + "</b></html>");
    }

    public void setTargetTerminology(String targetTerminology) {
        fcTgtTermino.setPath(targetTerminology);
    }
    public String getTargetTerminology() {
        return fcTgtTermino.getPath();
    }
    public void setTargetTerminologyError(Throwable e) {
        lblTgtTermino.setText("<html><b>" + LBL_TGTTERMINO + "</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetTargetTerminologyError() {
        lblTgtTermino.setText("<html><b>" + LBL_TGTTERMINO + "</b></html>");
    }

}
