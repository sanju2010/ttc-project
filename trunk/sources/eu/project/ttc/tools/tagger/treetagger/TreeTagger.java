package eu.project.ttc.tools.tagger.treetagger;

import java.awt.Component;
import java.io.File;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.tools.tagger.Tagger;
import eu.project.ttc.tools.tagger.TaggerTool;
import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;

public class TreeTagger implements TaggerTool {

	private Tagger parent;
	
	public void setParent(Tagger parent) {
		this.parent = parent;
	}
	
	public Tagger getParent() {
		return this.parent;
	}
	
	public void error(Exception e) {
		UIMAFramework.getLogger().log(Level.SEVERE,e.getMessage());
	}
	
	public void warning(String message) {
		UIMAFramework.getLogger().log(Level.WARNING,message);
	}
	
	public void message(String message) {
		UIMAFramework.getLogger().log(Level.INFO,message);
	}

	private TreeTaggerSettings settings;
	
	private void setSettings() {
		this.settings = new TreeTaggerSettings(System.getProperty("user.home") + File.separator + ".treetagger.settings");
	}
	
	public TreeTaggerSettings getSettings() {
		return this.settings;
	}
	
	private Preferences preferences;
	
	private void setPreferences() {
		this.preferences = new Preferences("treetagger.properties");
		try {
			this.preferences.load();
		} catch (Exception e) {
			this.error(e);
		}
	}
	
	public Preferences  getPreferences() {
		return this.preferences;
	}
	
	private About about;
	
	private void setAbout() {
		this.about = new About();
		this.about.setPreferences(this.getPreferences());
	}
	
	public About getAbout() {
		return this.about;
	}
	
	public TreeTagger() {
		this.setSettings();
		this.setPreferences();
		this.setAbout();
	}

	@Override
	public Component getComponent() {
		return this.getSettings().getComponent();
	}
		
}
