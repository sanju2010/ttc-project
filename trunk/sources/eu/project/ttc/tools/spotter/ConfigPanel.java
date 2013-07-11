// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
 * @author grdscarabe
 * @date 10/07/13
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
     * TODO: Describe what we try to achieve
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

        // Help pane
        epLanguage = new JEditorPane();
        epLanguage.setEditable(false);
        epLanguage.setOpaque(false);
        epLanguage.setPreferredSize( new Dimension(
                (int) epLanguage.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.language.html");
            epLanguage.setPage(res);
        } catch (IOException e) {} // No help
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
        fcInDirectory.setPreferredSize(new Dimension(
                (int) fcInDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        //fcInDirectory.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

        // Help pane
        epInDirectory = new JEditorPane();
        epInDirectory.setEditable(false);
        epInDirectory.setOpaque(false);
        epInDirectory.setPreferredSize( new Dimension(
                (int) epInDirectory.getPreferredSize().getHeight(),
                preferredWidth ));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/params/spotter.inputdirectory.html");
            epInDirectory.setPage(res);
        } catch (IOException e) {} // No help
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
        } catch (IOException e) {} // No help
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
        } catch (IOException e) {} // No help
    }

    static class LanguageItem {
        private String code;

        public LanguageItem(String code) {
            this.code = code;
        }

        public String getValue() {
            return code;
        }

        @Override
        public String toString() {
            return new Locale(code).getDisplayLanguage(Locale.ENGLISH);
        }
    }

    static class TTCFileChooser extends JPanel {

        private static final JFileChooser jfc;
        static {
            jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        private final JTextField tfPath;
        private final JButton btBrowse;
        private final String jfcTitle;

        public TTCFileChooser(String title) {
            super();
            setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

            jfcTitle = title;

            // Field to display path
            tfPath = new JTextField(25);
            //tfPath.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
            // Button to browse
            btBrowse = new JButton();
            btBrowse.setBorder(BorderFactory.createEmptyBorder());
            btBrowse.setText("Browse");
            btBrowse.setHorizontalTextPosition(SwingConstants.CENTER);
            btBrowse.setHorizontalAlignment(SwingConstants.CENTER);
            btBrowse.setActionCommand("browse");
            btBrowse.setPreferredSize( new Dimension(96,32) );
            //btBrowse.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

            add(tfPath);
            add(btBrowse);


            setPreferredSize(getPreferredSize());
//            new Dimension(
//                    Math.max(tfPath.getHeight(), btBrowse.getHeight()),
//                    (int) 1.2*(tfPath.getgetWidth() + btBrowse.getWidth()) ));

            // Bind action on button to choose file
            btBrowse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Set to current path if any
                    if ( tfPath.getText() != null ) {
                        File current = new File(tfPath.getText());
                        if (current.exists()) {
                            jfc.setCurrentDirectory(current.getParentFile());
                        }
                    }
                    // Operate browsing
                    if (e.getActionCommand().equals("browse")) {
                        jfc.setDialogTitle(jfcTitle);
                        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            // Some new path have been selected
                            tfPath.setText( jfc.getSelectedFile().getAbsolutePath() );
                        }
                    }
                }
            });
        }

    }
}
