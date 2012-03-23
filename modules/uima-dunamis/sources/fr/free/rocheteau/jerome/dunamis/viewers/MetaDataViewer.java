package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.uima.util.InvalidXMLException;

import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;

public class MetaDataViewer extends GenericViewer<Void> {
	
	private InformationViewer informationViewer;
	
	private void setInformationViewer() {
		this.informationViewer = new InformationViewer();
	}
	
	private InformationViewer getInformationViewer() {
		return this.informationViewer;
	}
	
	private CapabilityViewer capabilityViewer;
	
	private void setCapabilities() {
		this.capabilityViewer = new CapabilityViewer();
	}
	
	private CapabilityViewer getCapabilityViewer() {
		return this.capabilityViewer;
	}
	
	private TypeSystemViewer typeSystemViewer;
	
	private void setTypeSystem() {
		this.typeSystemViewer = new TypeSystemViewer();
	}
	
	private TypeSystemViewer getTypeSystemViewer() {
		return this.typeSystemViewer;
	}

	private MetaDataViewer(){
		this.setInformationViewer();
		this.setCapabilities();
		this.setTypeSystem();
		this.setComponent();
	}

	public MetaDataViewer(CasProcessor casProcessor) throws InvalidXMLException {
		this();
		this.update(casProcessor);
	}
	
	private void update(CasProcessor casprocessor) throws InvalidXMLException {
		this.getInformationViewer().update(casprocessor);
		this.getCapabilityViewer().update(casprocessor);
		this.getTypeSystemViewer().update(casprocessor);
		this.getComponent().repaint();
	}
			
	private JTabbedPane component;
	
	private void setComponent(){
		this.component = new JTabbedPane();
		
		JScrollPane descPanel = new JScrollPane();
		descPanel.setName("Information");
		descPanel.getViewport().add(this.getInformationViewer().getComponent());
		
		JScrollPane capaPanel = new JScrollPane();
		capaPanel.setName("Capabilities");
		capaPanel.getViewport().add(this.getCapabilityViewer().getComponent());
		
		JScrollPane tsPanel = new JScrollPane();
		tsPanel.setName("Type System");
		tsPanel.getViewport().add(this.getTypeSystemViewer().getComponent());
		
		this.component.add(descPanel);
		this.component.add(tsPanel);
		this.component.add(capaPanel);
		
		super.build("MetaData",false);
	}
	
	@Override
	public JTabbedPane getComponent() {
		return this.component;
	}

	@Override
	protected Dimension getComponentDimension() {
		return new Dimension(600,620);
	}

	@Override
	public void doApply() {
		this.hide();
	}

	@Override
	public void doCancel() {
		this.hide();
	}

	@Override
	public Void getValue() {
		return null;
	}
	
}
