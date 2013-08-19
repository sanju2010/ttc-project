// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.aligner;

import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TranslationAnnotation;
import eu.project.ttc.types.TranslationCandidateAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 19/08/13
 */
public class AlignerMixerViewer {

    private static final NumberFormat FORMATTER = NumberFormat.getNumberInstance(Locale.US);

    static {
        FORMATTER.setMinimumFractionDigits(4);
        FORMATTER.setMaximumFractionDigits(4);
    }



    private AlignerMixerViewer mixer;

    private void setMixer() {
        this.mixer = new AlignerMixerViewer();
    }

    public AlignerMixerViewer getMixer() {
        return this.mixer;
    }


    private Dimension getDimension() {
        return new Dimension(600,400);
    }

    private DefaultMutableTreeNode root;

    private void setRoot() {
        this.root = new DefaultMutableTreeNode("");
    }

    private DefaultMutableTreeNode getRoot() {
        return this.root;
    }

    private DefaultTreeModel treeModel;

    private void setTreeModel() {
        this.treeModel = new DefaultTreeModel(this.getRoot());
    }

    public DefaultTreeModel getTreeModel() {
        return this.treeModel;
    }

    private JTree tree;

    private void setTree() {
        this.tree = new JTree(this.getTreeModel());
        this.tree.setRootVisible(false);
        this.tree.expandRow(1);
    }

    private JTree getTree() {
        return this.tree;
    }

    private JScrollPane treeScroll;

    private void setTreeScroll() {
        this.treeScroll = new JScrollPane();
        this.treeScroll.getViewport().add(this.getTree());
        this.treeScroll.setMinimumSize(this.getDimension());
    }

    public JScrollPane getTreeScroll() {
        return this.treeScroll;
    }

    private TableModel tableModel;

    private void setTableModel() {
        this.tableModel = new TableModel();
    }

    private TableModel getTableModel() {
        return this.tableModel;
    }

    private JTable table;

    private void setTable() {
        this.table = new JTable(this.getTableModel());
        // this.table.setDefaultRenderer(Object.class, new TableRenderer());
    }

    private JTable getTable() {
        return this.table;
    }

    private JScrollPane tableScroll;

    private void setTableScroll() {
        this.tableScroll = new JScrollPane();
        this.tableScroll.getViewport().add(this.getTable());
        this.tableScroll.setMinimumSize(this.getDimension());
    }

    public JScrollPane getTableScroll() {
        return this.tableScroll;
    }

    private JSplitPane split;

    private void setSplit() {
        this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.split.setTopComponent(this.getTableScroll());
        this.split.setBottomComponent(this.getTreeScroll());
        this.split.setContinuousLayout(true);
    }

    public JSplitPane getComponent() {
        return this.split;
    }

    private CandidateComparator comparator;

    private void setComparator() {
        this.comparator = new CandidateComparator();
    }

    private CandidateComparator getComparator() {
        return this.comparator;
    }

    public AlignerMixerViewer() {
        this.setRoot();
        this.setTreeModel();
        this.setTree();
        this.setTreeScroll();
        this.setTableModel();
        this.setTable();
        this.setTableScroll();
        this.setSplit();
        this.setComparator();
    }

    public synchronized void doLoad(JCas cas) {
        try {
            String term = this.getTerm(cas);
            Set<String> translations = this.getTranslations(cas);
            this.setCandidates(cas, term, translations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTerm(JCas cas) {
        AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
        FSIterator<Annotation> iterator = index.iterator();
        if (iterator.hasNext()) {
            return iterator.next().getCoveredText();
        } else {
            return null;
        }
    }

    private Set<String> getTranslations(JCas cas) {
        Set<String> translations = new HashSet<String>();
        AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TranslationAnnotation.type);
        FSIterator<Annotation> iterator = index.iterator();
        while (iterator.hasNext()) {
            TranslationAnnotation annotation = (TranslationAnnotation) iterator.next();
            translations.add(annotation.getCoveredText());
        }
        return translations;
    }

    private void setCandidates(JCas cas, String term, Set<String> translations) {
        int best = -1;
        boolean update = false;
        AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TranslationCandidateAnnotation.type);
        FSIterator<Annotation> iterator = index.iterator();
        java.util.List<TranslationCandidateAnnotation> candidates = new ArrayList<TranslationCandidateAnnotation>();
        while (iterator.hasNext()) {
            update = true;
            TranslationCandidateAnnotation annotation = (TranslationCandidateAnnotation) iterator.next();
            candidates.add(annotation);
            if (translations.contains(annotation.getTranslation())) {
                if (best == -1 || annotation.getRank() < best) {
                    best = annotation.getRank();
                }
            }
        }
        Collections.sort(candidates, this.getComparator());
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        node.setUserObject(term + " (" + (best == -1 ? "unranked" : new Integer(best).toString()) + ")");
        this.getRoot().add(node);
        if (update) {
            for (int i = 0; i < candidates.size(); i++) {
                TranslationCandidateAnnotation annotation = candidates.get(i);
                this.addNote(node, annotation.getTranslation(), annotation.getRank(), annotation.getScore());
            }
            this.getTableModel().update(best == -1 ? 101 : best);
        }
        this.getTreeModel().reload();
    }

    private void addNote(DefaultMutableTreeNode root,String key,Integer rank, Double score) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        if (score != null) {
            String string = rank.toString() + ") " + key +  " [" + FORMATTER.format(score) + "]";
            node.setUserObject(string);
            root.add(node);
        }
    }

    private class TableModel extends AbstractTableModel {

        /**
         *
         */
        private static final long serialVersionUID = 4693765710466876449L;

        private final String[] columns = new String[]{"Top", "Found", "Total", "Rate"};

        protected Object[][] array;

        private void setArray() {
            this.array = new Object[this.getRowCount()][this.getColumnCount()];
            for (int i = 0; i < this.getRowCount(); i++) {
                for (int j = 0; j < this.getColumnCount(); j++) {
                    if (j == 0) {
                        this.array[i][j] = new Integer((i + 1) * 5).toString();
                    } else {
                        this.array[i][j] = new Integer(0);
                    }
                }
            }
        }

        protected Object[][] getArray() {
            return this.array;
        }

        public TableModel() {
            this.setArray();
        }


        @Override
        public String getColumnName(int col) {
            return this.columns[col];
        }

        public int getColumnCount() {
            return this.columns.length;
        }

        public int getRowCount() {
            return 20;
        }

        public Object getValueAt(int row, int col) {
            return this.getArray()[row][col];
        }

        @Override
        public boolean isCellEditable(int row,int col) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            this.array[row][col] = value;
            this.fireTableCellUpdated(row, col);
        }

        public void update(int best) {
            for (int i = 0; i < this.getRowCount(); i++) {
                Integer total = (Integer) this.getValueAt(i, 2);
                this.setValueAt(new Integer(total.intValue() + 1), i, 2);
                Integer value = (Integer) this.getValueAt(i, 1);
                if (best <= (i + 1) * 5) {
                    value = new Integer(value.intValue() + 1);
                    this.setValueAt(value, i, 1);
                }
                this.setValueAt(new Integer((value.intValue() * 100) / (total.intValue() + 1)), i, 3);
            }
        }

    }

    private class CandidateComparator implements Comparator<TranslationCandidateAnnotation> {

        public int compare(TranslationCandidateAnnotation source,TranslationCandidateAnnotation target) {
            int diff = Double.compare(source.getRank(), target.getRank());
            if (diff == 0) {
                diff = Double.compare(target.getScore(), source.getScore());
                if (diff == 0) {
                    return source.getTranslation().compareTo(target.getTranslation());
                } else {
                    return diff;
                }
            } else {
                return diff;
            }
        }

    }
}
