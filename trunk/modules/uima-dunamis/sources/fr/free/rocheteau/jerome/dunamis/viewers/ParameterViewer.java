package fr.free.rocheteau.jerome.dunamis.viewers;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;

import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import fr.free.rocheteau.jerome.dunamis.fields.Field;
import fr.free.rocheteau.jerome.dunamis.models.CasProcessor;

public class ParameterViewer extends GenericViewer<Void> {
	
	private CasProcessor casProcessor;
	
	private void setCasProcessor(CasProcessor casProcessor) {
		this.casProcessor = casProcessor;
	}
	
	private CasProcessor getCasProcessor() {
		return this.casProcessor;
	}
	
	private SettingViewer settingViewer;
	
	private void setSettingViewer() {
		this.settingViewer = new SettingViewer();
		// this.metaData.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.settingViewer.update(this.getCasProcessor().getMetaData(),this.getCasProcessor().getSettings());
	}
	
	private SettingViewer getSettingViewer() {
		return this.settingViewer;
	}
	
	@Override
	protected JComponent getComponent() {
		return this.settingViewer.getComponent();
	}
	
	@Override
	public void doApply() {
		ConfigurationParameterSettings settings = this.getCasProcessor().getMetaData().getConfigurationParameterSettings();
		List<Field> fields = this.getSettingViewer().getFields();
		for (Field field : fields) {
			if (field.isModified()) {
				String name = field.getName();
				Object value = field.getValue();
				settings.setParameterValue(name,value);
			} 
		}
		this.hide();
		this.getCasProcessor().updateSettings();
	}
	
	@Override
	public void doCancel() {
		this.hide();
	}

	@Override
	protected Dimension getComponentDimension() {
		return new Dimension(640,480);
	}
	
	public Void getValue() {
		return null;
	}
	
	public ParameterViewer(CasProcessor proc) {
		this.setCasProcessor(proc);
		this.setSettingViewer();
		this.build(proc.getMetaData().getName().trim() + " Parameters");
	}
	
}
