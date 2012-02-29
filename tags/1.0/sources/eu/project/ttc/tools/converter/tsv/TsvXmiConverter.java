package eu.project.ttc.tools.converter.tsv;

import java.awt.Component;
import java.io.File;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.TermSuiteTool;

public class TsvXmiConverter implements TermSuiteTool {

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

	private XmiTsvConverterSettings settings;
	
	private void setSettings() {
		this.settings = new XmiTsvConverterSettings(System.getProperty("user.home") + File.separator + ".converter-tsv-xmi.settings");
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getSettings()
	 */
	@Override
	public XmiTsvConverterSettings getSettings() {
		return this.settings;
	}

	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getComponent()
	 */
	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
	
	public TsvXmiConverter() {
		this.setSettings();
	}
	
}
