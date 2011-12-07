package eu.project.ttc.tools.converter.tbx;

import java.awt.Component;
import java.io.File;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.converter.Converter;
import eu.project.ttc.tools.converter.ConverterTool;

public class TbxXmiConverter implements ConverterTool {

	private Converter parent;
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#setParent(eu.project.ttc.tools.tagger.Tagger)
	 */
	@Override
	public void setParent(Converter parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getParent()
	 */
	@Override
	public Converter getParent() {
		return this.parent;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#error(java.lang.Exception)
	 */
	@Override
	public void error(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#warning(java.lang.String)
	 */
	@Override
	public void warning(String message) {
		UIMAFramework.getLogger().log(Level.WARNING,message);
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#message(java.lang.String)
	 */
	@Override
	public void message(String message) {
		UIMAFramework.getLogger().log(Level.INFO,message);
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
