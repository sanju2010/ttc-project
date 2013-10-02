// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.commons;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This class implements an easy way to choose a file from
 * the file system, or simply write its path.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 15/08/13
 */
@SuppressWarnings("serial")
public class TTCFileChooser extends JPanel {

    private static final JFileChooser jfc;
    static {
        jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private final JTextField tfPath;
    private final JButton btBrowse;
    private final String jfcTitle;
    private final Color defaultBgColor;
    private FileFilter jfcFilter;

    /**
     *
     * @param title
     *      title of the window to choose a file from (JFileChooser)
     * @param fileType
     *      type of the files to be loaded
     */
    public TTCFileChooser(String title, InputSource.InputSourceTypes fileType) {
        super();
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        jfcTitle = title;
        jfcFilter = jfc.getAcceptAllFileFilter();
        if ( fileType != null ) {
            switch(fileType) {
                case TXT:
                    jfcFilter = new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory() ||
                                (f.getName().endsWith(".txt") || f.getName().endsWith(".TXT"));
                        }
                        @Override
                        public String getDescription() {
                            return "Text files";
                        }
                    };
                case XMI:
                    jfcFilter = new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory() ||
                                    (f.getName().endsWith(".xmi") || f.getName().endsWith(".XMI"));
                        }
                        @Override
                        public String getDescription() {
                            return "XMI files";
                        }
                    };
            }
        }

        // Field to display path
        tfPath = new JTextField(25);
        tfPath.setEnabled(false);
        defaultBgColor = tfPath.getBackground();
        tfPath.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if ( new File(tfPath.getText()).exists() ) {
                    firePropertyChange("path", null, tfPath.getText());
                    tfPath.setBackground(defaultBgColor);
                    tfPath.setOpaque(false);
                } else {
                    // FIXME does not work!!!
                    tfPath.setBackground(Color.RED);
                    tfPath.setOpaque(true);
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if ( new File(tfPath.getText()).exists() ) {
                    firePropertyChange("path", null, tfPath.getText());
                    tfPath.setBackground(defaultBgColor);
                    tfPath.setOpaque(false);
                } else {
                    tfPath.setBackground(Color.RED);
                    tfPath.setOpaque(true);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                if ( new File(tfPath.getText()).exists() ) {
                    firePropertyChange("path", null, tfPath.getText());
                    tfPath.setBackground(defaultBgColor);
                    tfPath.setOpaque(false);
                } else {
                    tfPath.setBackground(Color.RED);
                    tfPath.setOpaque(true);
                }
            }
        });

        //tfPath.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        // Button to browse
        btBrowse = new JButton();
        btBrowse.setBorder(BorderFactory.createEmptyBorder());
        btBrowse.setText("Browse");
        btBrowse.setHorizontalTextPosition(SwingConstants.CENTER);
        btBrowse.setHorizontalAlignment(SwingConstants.CENTER);
//            btBrowse.setActionCommand("browse");
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
                        jfc.setSelectedFile( current );
                    } else {
                        jfc.setCurrentDirectory( new File(System.getProperty("user.home")) );
                    }
                }
                // Operate browsing
//                    if (e.getActionCommand().equals("browse")) {
                jfc.setDialogTitle(jfcTitle);
                jfc.setFileFilter(jfcFilter);
                if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    // Some new path have been selected
                    String prev = tfPath.getText();
                    tfPath.setText( jfc.getSelectedFile().getAbsolutePath() );
                    firePropertyChange("path", prev, tfPath.getText());
                }
//                    }
            }
        });
    }

    /**
     * Programaticaly change the path.
     * @param path  new path to be set
     */
    public void setPath(String path) {
        tfPath.setText(path); // fire removeUpdate then changedUpdate
    }

    public String getPath() {
        return tfPath.getText();
    }
}
