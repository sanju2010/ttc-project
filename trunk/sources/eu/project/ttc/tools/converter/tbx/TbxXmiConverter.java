package eu.project.ttc.tools.converter.tbx;

import java.awt.Component;
import java.io.File;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteTool;

public class TbxXmiConverter implements TermSuiteTool {

	private TermSuite parent;
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#setParent(eu.project.ttc.tools.tagger.Tagger)
	 */
	@Override
	public void setParent(TermSuite parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getParent()
	 */
	@Override
	public TermSuite getParent() {
		return this.parent;
	}
	
	private XmiTbxConverterSettings settings;
	
	private void setSettings() {
		this.settings = new XmiTbxConverterSettings(System.getProperty("user.home") + File.separator + ".converter-tbx-xmi.settings");
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getSettings()
	 */
	@Override
	public XmiTbxConverterSettings getSettings() {
		return this.settings;
	}

	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getComponent()
	 */
	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
	
	public TbxXmiConverter() {
		this.setSettings();
	}
	
}
