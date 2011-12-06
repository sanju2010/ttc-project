package eu.project.ttc.tools.tagger;

import java.awt.Component;

import eu.project.ttc.tools.utils.About;
import eu.project.ttc.tools.utils.Preferences;

public interface TaggerTool {

	public void setParent(Tagger parent);

	public Tagger getParent();

	public void error(Exception e);

	public void warning(String message);

	public void message(String message);

	public TaggerSettings getSettings();

	public About getAbout();

	public Preferences getPreferences();

	public Component getComponent();

}