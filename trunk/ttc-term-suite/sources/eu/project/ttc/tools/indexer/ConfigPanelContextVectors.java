// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

/**
 * This JPanel exposes the configuration of the Indexer tool regarding the
 * context vectors construction.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
@SuppressWarnings("serial")
public class ConfigPanelContextVectors extends JPanel {

    protected static final String LBL_ASSOCMEASURE = "Association measure";

    // Association measure parameter
    private JLabel lblAssociationMeasure;
    private JComboBox<ClassItem> cbAssociationMeasure;
    private JEditorPane epAssociationMeasure;

    private GroupLayout cfgLayout;

    public ConfigPanelContextVectors() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Prepare components
        int pWidth = (int) getPreferredSize().getWidth() / 2;
        createAssociationMeasureComponents(pWidth);

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
                        // Association measure parameter
                        .addGroup(cfgLayout.createSequentialGroup()
                                .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblAssociationMeasure)
                                        .addComponent(cbAssociationMeasure))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(epAssociationMeasure))
        );

        // Configure the vertical layout
        cfgLayout.setVerticalGroup(
                cfgLayout.createSequentialGroup()
                        // Association measure parameter
                        .addGroup(cfgLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(cfgLayout.createSequentialGroup()
                                        .addComponent(lblAssociationMeasure)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbAssociationMeasure,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addComponent(epAssociationMeasure))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        );

        add(config, BorderLayout.NORTH);
    }

    /**
     * Create the components related to the association measure parameter.
     */
    public void createAssociationMeasureComponents(int preferredWidth) {
        // Label
        lblAssociationMeasure = new JLabel("<html><b>"+LBL_ASSOCMEASURE+"</b></html>");
        lblAssociationMeasure.setPreferredSize(new Dimension(
                (int) lblAssociationMeasure.getPreferredSize().getHeight(),
                preferredWidth ));

        // Comboxbox as it is a limited list of choices
        cbAssociationMeasure = new JComboBox<ClassItem>();
        cbAssociationMeasure.addItem(new ClassItem(
                "eu.project.ttc.metrics.LogLikelihood", "Log likelyhood"));
        cbAssociationMeasure.addItem(new ClassItem(
                "eu.project.ttc.metrics.MutualInformation", "Mutual information"));
        cbAssociationMeasure.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Detected an association measurechange, fire property change.");
                    firePropertyChange(IndexerBinding.PRM.ASSOCIATIONMEASURE.getProperty(), null,
                            cbAssociationMeasure.getSelectedItem());
                }
            }
        });

        // Editor pane to display help
        epAssociationMeasure = new JEditorPane();
        epAssociationMeasure.setEditable(false);
        epAssociationMeasure.setOpaque(false);
        epAssociationMeasure.setPreferredSize(new Dimension(
                (int) epAssociationMeasure.getPreferredSize().getHeight(),
                preferredWidth));
        try {
            URL res = getClass().getResource("/eu/project/ttc/gui/texts/indexer/param.associationmeasure.html");
            epAssociationMeasure.setPage(res);
        } catch (IOException e){} // No help
    }

    ////////////////////////////////////////////////////////////////////// ACCESSORS

    public void setAssociationMeasure(String associationMeasure) {
        for(int i=0 ; i < cbAssociationMeasure.getItemCount() ; i++) {
            ClassItem item = (ClassItem) cbAssociationMeasure.getItemAt(i);
            if ( item.getClassName().equals(associationMeasure) ) {
                if (cbAssociationMeasure.getSelectedIndex() != i) {
                    cbAssociationMeasure.setSelectedItem( item );
                }
                return;
            }
        }
        // If reach here, we have a problem
        throw new IllegalArgumentException("I cannot reflect the change to value '"
                + associationMeasure + "' as I do not handle this value.");
    }
    public void setAssociationMeasureError(Throwable e) {
        lblAssociationMeasure.setText("<html><b>"+LBL_ASSOCMEASURE+"</b><br/><p style=\"color: red; font-size: small\">"
                + e.getMessage() + "</p></html>");
    }
    public void unsetAssociationMeasureError() {
        lblAssociationMeasure.setText("<html><b>"+LBL_ASSOCMEASURE+"</b></html>");
    }
    public String getAssociationMeasure() {
        return ((ClassItem) cbAssociationMeasure.getSelectedItem()).getClassName();
    }
}
