package eu.project.ttc.tools.various;

import java.io.InputStream;
import java.util.Properties;

public class Preferences {

    // Previously the properties were loaded from an external file
	private final Properties properties = new Properties();

    private Properties getProperties() {
		return this.properties;
	}
	
//	public void load() throws Exception {
//		InputStream in = this.getClass().getResourceAsStream("/" + this.getResource());
//		this.properties.loadFromXML(in);
//		in.close();
//	}

    public void setSummary(String s) {
        getProperties().setProperty("summary", s);
    }

	public String getSummary() {
		return this.getProperties().getProperty("summary");
	}


    public void setLicense(String license) {
        getProperties().setProperty("license", license);
    }

	public String getLicense() {
		return this.getProperties().getProperty("license");
	}

    public void setTitle(String title) {
        getProperties().setProperty("title", title);
    }

	public String getTitle() {
		return this.getProperties().getProperty("title");
	}

    public void setVersion(String version) {
        getProperties().setProperty("version", version);
    }

	public String getVersion() {
		return this.getProperties().getProperty("version");
	}

    public void setConfigRoot(String configRoot) {
        getProperties().setProperty("config.root", configRoot);
    }

    public String getConfigRoot() {
        return getProperties().getProperty("config.root");
    }

    public void setSpotterConfig(String s) {
        getProperties().setProperty("config.spotter", s);
    }

    public String getSpotterConfig() {
        return getProperties().getProperty("config.spotter");
    }

    public void setIndexerConfig(String s) {
        getProperties().setProperty("config.indexer", s);
    }

    public String getIndexerConfig() {
        return getProperties().getProperty("config.indexer");
    }

    public void setAlignerConfig(String s) {
        getProperties().setProperty("config.aligner", s);
    }

    public String getAlignerConfig() {
        return getProperties().getProperty("config.aligner");
    }
}
