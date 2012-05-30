package eu.project.ttc.tools.indexer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.TermSuiteTool;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import fr.free.rocheteau.jerome.dunamis.fields.FieldFactory;

public class AdvancedParameters  implements TermSuiteTool 
{

	private Dimension getDimension() 
	{
		return new Dimension(600,400);
	}
	private VectorContextParameters vectorSettings;
	
	private void setSettings(File home) 
	{
		this.vectorSettings = new VectorContextParameters(home.getAbsolutePath() + File.separator + "indexer.vector-settings");
		this.vectorSettings.getComponent().setBorder(BorderFactory.createTitledBorder("Context Vector Building Parameters"));
	}
	
	public VectorContextParameters getSettings() {
		//ConfigurationParameterSettings vc=vectorSettings.getMetaData().getAttributeValue(aName);
		return this.vectorSettings;
	}
	private JPanel component;
	JTextField field=new JTextField();
	private void setComponent() 
	{

			this.component=new JPanel();
	
			this.component.add(this.getSettings().getComponent());
	}
	private TermSuite parent;
	public String getText()
	{
		return field.getText();
	}
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	public TermSuite getParent() {
		return this.parent;
	}
	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}

	@Override
	public TermSuiteSettings getAdvancedSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	 public AdvancedParameters(File home) {
		this.setSettings(home);
		this.setComponent();
	}



}