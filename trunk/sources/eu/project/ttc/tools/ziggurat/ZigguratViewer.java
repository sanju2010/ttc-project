package eu.project.ttc.tools.ziggurat;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import eu.project.ttc.types.CandidateAnnotation;
import eu.project.ttc.types.TranslationAnnotation;

import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class ZigguratViewer {

	private Dimension getDimension() {
		return new Dimension(600,400);
	}

	private DefaultMutableTreeNode root;

	private void setRoot() {
		this.root = new DefaultMutableTreeNode("Context");
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
		// this.tree.setSelectionModel(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// TODO this.list.setCellRenderer(new Renderer());
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
	}
	
	public JSplitPane getComponent() {
		return this.split;
	}
		
	public ZigguratViewer() {
		this.setRoot();
		this.setTreeModel();
		this.setTree();
		this.setTreeScroll();
		this.setTableModel();
		this.setTable();
		this.setTableScroll();
		this.setSplit();
	}
	
	public void doLoad(JCas cas) {
		try {
			Set<String> translations = this.updateTableModel(cas);
			this.updateTreeModel(cas, translations);
		} catch (CASRuntimeException e) {
			e.printStackTrace();
		}
	}

	private Set<String> updateTableModel(JCas cas) {
		Set<String> translations = new HashSet<String>();
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TranslationAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TranslationAnnotation annotation = (TranslationAnnotation) iterator.next();
			translations.add(annotation.getTerm());
		}
		return translations;
	}
	
	private void updateTreeModel(JCas cas, Set<String> translations) {
		int best = -1;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(cas.getDocumentText());
		this.getRoot().add(node);
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(CandidateAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		String[] candidates = new String[20];
		Double[] scores = new Double[20];
		while (iterator.hasNext()) {
			CandidateAnnotation annotation = (CandidateAnnotation) iterator.next();
			String translation = annotation.getTranslation();
			Double score = annotation.getScore();
			Integer rank = annotation.getRank();
			if (rank.intValue() > 0 && rank.intValue() <= 20) {
				candidates[rank.intValue() - 1] = translation;
				scores[rank.intValue() - 1] = score;
			}
			if (translations.contains(translation)) {
				if (best == -1 || rank.intValue() < best) {
					best = rank.intValue();
				}
			}
		}
		for (int i = 0; i < 20; i++) {
			this.addNote(node, candidates[i], new Integer(i + 1), scores[i]);
		}
		this.getTableModel().update(best == -1 ? 101 : best);
		this.getTreeModel().reload();
	}
		
	private void addNote(DefaultMutableTreeNode root,String key,Integer rank, Double score) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		if (score != null) {
			String string = rank.toString() + ") " + key +  " [" + score.toString() + "]";
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
		
		@Override
		public int getColumnCount() {
			return this.columns.length;
		}

		@Override
		public int getRowCount() {
			return 20;
		}

		@Override
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
	
}
