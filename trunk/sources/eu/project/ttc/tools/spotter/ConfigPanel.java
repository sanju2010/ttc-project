// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.LanguageItem;
import eu.project.ttc.tools.commons.TTCFileChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
 * This JPanel exposes the configuration part of the Spotter tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class ConfigPanel extends JPanel {

    // Main title
    private JEditorPane epHelp;

    // Language parameter
    private JLabel lblLanguage;
    private JComboBox cbLanguage;
    private JEditorPane epLanguage;

    // Input directory parameter
    private JLabel lblInDirectory;
    private JEditorPane epInDirectory;
    private TTCFileChooser fcInDirectory;
    private Color fcInDirectoryInErrorColor = Color.RED;
    private Color fcInDirectoryRegularColor;

    // Output directory parameter
    private JLabel lblOutDirectory;
    private JEditorPane epOutDirectory;
    private TTCFileChooser fcOutDirectory;

    // TreeTagger directory parameter
    private JLabel lblTtgDirectory;
    private JEditorPane epTtgDirectory;
    private TTCFileChooser fcTtgDirectory;

    private GroupLayout cfgLayout;

    public ConfigPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        createHelpTitle();
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createLanguageForm(pWidth);
        createInputDirectoryForm(pWidth);
        createOutputDirectoryForm(pWidth);
        createTtgDirectoryForm(pWidth);

        // Layout components together
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
                        // Language parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblLanguage)
                                        .addComponent(cbLanguage))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epLanguage))
                        // Input directory parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblInDirectory)
                                        .addComponent(fcInDirectory))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epInDirectory)
                        )
                        // Output directory parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblOutDirectory)
                                        .addComponent(fcOutDirectory))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epOutDirectory)
                        )
                        // TreeTagger directory parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTtgDirectory)
                                        .addComponent(fcTtgDirectory))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epTtgDirectory)
                        )
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // Language parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblLanguage)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbLanguage,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epLanguage))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Input directory parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblInDirectory)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcInDirectory))
                                .addComponent(epInDirectory)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Output directory parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblOutDirectory)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcOutDirectory))
                                .addComponent(epOutDirectory)
                        )
                                // TreeTagger directory parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblTtgDirectory)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fcTtgDirectory))
                                .addComponent(epTtgDirectory)
                        )
        );


        // Set everything together
        Box box = Box.createVerticalBox();
        box.setOpaque(false);
        box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        box.add(epHelp);
        box.add( Box.createRigidArea( new Dimension(0, 15) ) );
        box.add( new JSeparator(SwingConstants.HORIZONTAL) );
        box.add( Box.createRigidArea( new Dimension(0, 15) ) );
        box.add(config);
        add(box, BorderLayout.NORTH);
    }

    private void createHelpTitle() {
        epHelp = new JEditorPane();
        epHelp.setEditable(false);
        epHelp.setOpaque(false);
        try {
            URL resHelp = getClass().getResource("/eu/project/ttc/gui/params/spotter.help.html");
            epHelp.setPage(resHelp);
        } catch (IOException e) {} // No help available
    }

    /**
     * Create the ParameterGroup dedicated to configure the language to be used.
     */
    public void createLanguageForm(int preferredWidth) {
        // Language label
        lblLanguage = new JLabel("<html><b>Language</b></html>");
        lblLanguage.setPreferredSize(new Dimension(
                (int) lblLanguage.getPreferredSize().getHeight(),
                preferredWidth ));

        // Language combobox
        cbLanguage = new JComboBox();
        for(String code: new String[]{"en","fr","de","es","ru","da","lv","zh"}) {
            cbLanguage.addItem( new LanguageItem(code) );
        }
        cbLanguage.setPreferredSize(new Dimension(
                (int) cbLanguage.getPreferredSize().getHeight(),
                preferredWidth ));
        cbLanguage.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    LanguageItem item = (LanguageItem) e.getItem();;
                    System.out.println("Detected a language change, fire spotter.language property change.");
                    firePropertyChange(SpotterBinding.EVT_LANGUAGE, null, item.getValue());
                }
            }
        });

        // Help pane
        epLanguage = new JEditorPane();

        epLanguage.setEditable(false);
        epLanguage.setOpaque(false);
        epLanguage.setPreferredSize(new Dimension(
                (int) epLanguage.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.language.html");
            epLanguage.setPage(res);
        } catch (IOException e){} // No various
    }

    /**
     * Create the ParameterGroup dedicated to configure the input directory.
     */
    public void createInputDirectoryForm(int preferredWidth) {
        // Input directory label
        lblInDirectory = new JLabel("<html><b>Input Directory</b></html>");
        lblInDirectory.setPreferredSize(new Dimension(
                (int) lblInDirectory.getPreferredSize().getHeight(),
                preferredWidth ));

        // Input directory field
        fcInDirectory = new TTCFileChooser("Choose the input directory");
        fcInDirectoryRegularColor = fcInDirectory.getBackground();
        fcInDirectory.setPreferredSize(new Dimension(
                (int) fcInDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        //fcInDirectory.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        fcInDirectory.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(SpotterBinding.EVT_INPUT, evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help panel
        epInDirectory = new JEditorPane();
        epInDirectory.setEditable(false);
        epInDirectory.setOpaque(false);
        epInDirectory.setPreferredSize( new Dimension(
                (int) epInDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.inputdirectory.html");
            epInDirectory.setPage(res);
        } catch (IOException e) {} // No various
    }

    /**
     * Create the ParameterGroup dedicated to configure the output directory.
     */
    public void createOutputDirectoryForm(int preferredWidth) {
        // Output directory label
        lblOutDirectory = new JLabel("<html><b>Output Directory</b></html>");
        lblOutDirectory.setPreferredSize(new Dimension(
                (int) lblOutDirectory.getPreferredSize().getHeight(),
                preferredWidth ));

        // Output directory field
        fcOutDirectory = new TTCFileChooser("Choose the output directory");
        fcOutDirectory.setPreferredSize(new Dimension(
                (int) fcOutDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        //fcOutDirectory.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        fcOutDirectory.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(SpotterBinding.EVT_OUTPUT, evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help pane
        epOutDirectory = new JEditorPane();
        epOutDirectory.setEditable(false);
        epOutDirectory.setOpaque(false);
        epOutDirectory.setPreferredSize( new Dimension(
                (int) epOutDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.outputdirectory.html");
            epOutDirectory.setPage(res);
        } catch (IOException e) {} // No various
    }

    /**
     * Create the ParameterGroup dedicated to configure the tree tagger directory.
     */
    public void createTtgDirectoryForm(int preferredWidth) {
        // TreeTagger directory label
        lblTtgDirectory = new JLabel("<html><b>TreeTagger Home Directory</b></html>");
        lblTtgDirectory.setPreferredSize(new Dimension(
                (int) lblTtgDirectory.getPreferredSize().getHeight(),
                preferredWidth ));

        // TreeTagger directory field
        fcTtgDirectory = new TTCFileChooser("Choose the treetagger home directory");
        fcTtgDirectory.setPreferredSize(new Dimension(
                (int) fcTtgDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        //fcTtgDirectory.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        fcTtgDirectory.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("path".equals(evt.getPropertyName()))
                    firePropertyChange(SpotterBinding.EVT_TTGHOME, evt.getOldValue(), evt.getNewValue());
            }
        });

        // Help pane
        epTtgDirectory = new JEditorPane();
        epTtgDirectory.setEditable(false);
        epTtgDirectory.setOpaque(false);
        epTtgDirectory.setPreferredSize( new Dimension(
                (int) epTtgDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.ttgdirectory.html");
            epTtgDirectory.setPage(res);
        } catch (IOException e) {} // No various
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setLanguage(String language) {
        for(int i=0 ; i < cbLanguage.getItemCount() ; i++) {
            LanguageItem item = (LanguageItem) cbLanguage.getItemAt(i);
            if ( item.getValue().equals(language) ) {
                if (cbLanguage.getSelectedIndex() != i) {
                    cbLanguage.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + language + "' as I do not handle this value.");
    }

    public void setLanguageError(IllegalArgumentException e) {
        lblLanguage.setText("<html><b>Language</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetLanguageError() {
        lblLanguage.setText("<html><b>Language</b></html>");
    }

    public String getLanguage() {
        return ((LanguageItem) cbLanguage.getSelectedItem()).getValue();
    }

    public void setInputDirectory(String inputDirectory) {
        fcInDirectory.setPath(inputDirectory);
    }

    public void setInputDirectoryError(IllegalArgumentException e) {
        lblInDirectory.setText("<html><b>Input Directory</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetInputDirectoryError() {
        lblInDirectory.setText("<html><b>Input Directory</b></html>");
    }

    public String getInputDirectory() {
        return fcInDirectory.getPath();
    }

    public void setOutputDirectory(String outputDirectory) {
        fcOutDirectory.setPath(outputDirectory);
    }

    public void setOutputDirectoryError(IllegalArgumentException e) {
        lblOutDirectory.setText("<html><b>Output Directory</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetOutputDirectoryError() {
        lblOutDirectory.setText("<html><b>Output Directory</b></html>");
    }

    public String getOutputDirectory() {
        return fcOutDirectory.getPath();
    }

    public void setTreetaggerHome(String ttgDirectory) {
        fcTtgDirectory.setPath(ttgDirectory);
    }

    public void setTreetaggerHomeError(IllegalArgumentException e) {
        lblTtgDirectory.setText("<html><b>TreeTagger Home Directory</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetTreetaggerHomeError() {
        lblTtgDirectory.setText("<html><b>TreeTagger Home Directory</b></html>");
    }

    public String getTreetaggerHome() {
        return fcTtgDirectory.getPath();
    }
}
