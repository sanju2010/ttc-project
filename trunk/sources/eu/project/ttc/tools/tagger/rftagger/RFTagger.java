package eu.project.ttc.tools.tagger.rftagger;

import java.awt.Component;
import java.io.File;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.tagger.Tagger;
import eu.project.ttc.tools.tagger.TaggerTool;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;

public class RFTagger implements TaggerTool {

	private Tagger parent;
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#setParent(eu.project.ttc.tools.tagger.Tagger)
	 */
	@Override
	public void setParent(Tagger parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getParent()
	 */
	@Override
	public Tagger getParent() {
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
	
	private RFTaggerSettings settings;
	
	private void setSettings() {
		this.settings = new RFTaggerSettings(System.getProperty("user.home") + File.separator + ".rftagger.settings");
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getSettings()
	 */
	@Override
	public RFTaggerSettings getSettings() {
		return this.settings;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("rftagger.properties");
		try {
			this.preferences.load();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getAbout()
	 */
	@Override
	public About getAbout() {
		return this.about;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getPreferences()
	 */
	@Override
	public Preferences  getPreferences() {
		return this.preferences;
	}
	
	/* (non-Javadoc)
	 * @see eu.project.ttc.tools.tildetagger.TaggerEmbedder#getComponent()
	 */
	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
	
	public RFTagger() {
		this.setSettings();
		this.setPreferences();
		this.setAbout();
	}
	
}
