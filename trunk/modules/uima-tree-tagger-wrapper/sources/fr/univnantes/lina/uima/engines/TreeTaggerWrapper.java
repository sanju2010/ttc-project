package fr.univnantes.lina.uima.engines;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.annolab.tt4j.DefaultModelResolver;
import org.annolab.tt4j.ExecutableResolver;
import org.annolab.tt4j.TokenAdapter;
import org.annolab.tt4j.TokenHandler;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.uima.models.TreeTaggerParameter;

public class TreeTaggerWrapper extends JCasAnnotator_ImplBase {

	private String homeDirectory;
	
	private void setHomeDirectory(String home) {
		this.homeDirectory = home;
	}
	
	private String getHomeDirectory() {
		return this.homeDirectory;
	}
	
	private TreeTaggerParameter configuration;
	
	private void setConfiguration(TreeTaggerParameter configuration) {
		this.configuration = configuration;
	}
	
	private TreeTaggerParameter getConfiguration() {
		return this.configuration;
	}
	
	private Handler handler;
	
	private void setHandler() {
		this.handler = new Handler();
	}
	
	private Handler getHandler() {
		return this.handler;
	}
	
	private org.annolab.tt4j.TreeTaggerWrapper<Annotation> wrapper;	
	
	private void setWrapper() throws Exception {
		this.wrapper = new org.annolab.tt4j.TreeTaggerWrapper<Annotation>();
		this.wrapper.setHandler(this.getHandler());
		this.wrapper.setAdapter(new Adapter());
		Provider provider = new Provider(this.getHomeDirectory());
		this.wrapper.setExecutableProvider(provider);
		this.wrapper.setModelProvider(provider);
		this.wrapper.setModel(provider.getHome() + File.separator + "lib" + File.separator + this.getConfiguration().getFile() + ":" + this.getConfiguration().getEncoding());	
	}
	
	private org.annolab.tt4j.TreeTaggerWrapper<Annotation> getWrapper() {
		return this.wrapper;
	}
	
	private String annotationType;
	
	private void setAnnotationType(String type) {
		this.annotationType = type;
	}
	
	private Type getAnnotationType(JCas cas) {
		return cas.getTypeSystem().getType(this.annotationType);
	}
	
	private String tagType;
	private String tagFeature;
	
	private void setTagFeature(String feature) {
		String[] path = feature.split(":");
		if (path.length == 2) {
			this.tagType = path[0];
			this.tagFeature = path[1];
		} else {
			this.tagFeature = feature;
		}
	}
	
	private Type getTagAnnotationType(JCas cas) {
		return cas.getTypeSystem().getType(this.tagType);
	}
	
	private Feature getTagFeature(JCas cas,Type type,boolean update) {
		if (update) {
			return type.getFeatureByBaseName(this.tagFeature);
		} else {
			Type tagType = this.getTagAnnotationType(cas);
			return tagType.getFeatureByBaseName(this.tagFeature);
		}
	}
	
	private String lemmaType;
	private String lemmaFeature;
	
	private void setLemmaFeature(String feature) {
		String[] path = feature.split(":");
		if (path.length == 2) {
			this.lemmaType = path[0];
			this.lemmaFeature = path[1];
		} else {
			this.lemmaFeature = feature;
		}
	}
	
	private Type getLemmaAnnotationType(JCas cas) {
		return cas.getTypeSystem().getType(this.lemmaType);
	}
	
	private Feature getLemmaFeature(JCas cas,Type type,boolean update) {
		if (update) {
			return type.getFeatureByBaseName(this.lemmaFeature);
		} else {
			Type lemmaType = this.getLemmaAnnotationType(cas);
			return lemmaType.getFeatureByBaseName(this.lemmaFeature);
		}
	}
	
	private boolean updatingRequired;
	
	private void requireUpdate(boolean required) {
		this.updatingRequired = required;
		this.getHandler().enableUpodate(required);
	}
	
	private boolean isUpdateRequired() {
		return this.updatingRequired;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
			String home = (String) context.getConfigParameterValue("TreeTaggerHomeDirectory");
			this.setHomeDirectory(home);
			TreeTaggerParameter configuration = (TreeTaggerParameter) context.getResourceObject("TreeTaggerParameter");
			this.setConfiguration(configuration);
			String parameter = (String) context.getConfigParameterValue("TreeTaggerParameterFile");
			configuration.override(parameter);
			this.setHandler();
			this.setWrapper();
			String type = (String) context.getConfigParameterValue("AnnotationType");
			this.setAnnotationType(type);	
			String tagFeature = (String) context.getConfigParameterValue("TagFeature");
			this.setTagFeature(tagFeature);
			String lemmaFeature = (String) context.getConfigParameterValue("LemmaFeature");
			this.setLemmaFeature(lemmaFeature);
			Boolean required = (Boolean) context.getConfigParameterValue("UpdateAnnotationFeatures");
			this.requireUpdate(required.booleanValue());
		} catch (ResourceAccessException e) {
			throw new ResourceInitializationException(e);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			Type type = this.getAnnotationType(cas);
			Feature tagFeature = this.getTagFeature(cas,type,this.isUpdateRequired());
			Feature lemmaFeature = this.getLemmaFeature(cas,type,this.isUpdateRequired());
			this.getHandler().setTagFeature(tagFeature);
			this.getHandler().setLemmaFeature(lemmaFeature);
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
			FSIterator<Annotation> iter = index.iterator();
			List<Annotation> tokens = new ArrayList<Annotation>();
			while (iter.hasNext()) {
				Annotation token = iter.next();
				tokens.add(token);
			}
			this.getWrapper().process(tokens);
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	private class Handler implements TokenHandler<Annotation> {	
	
		private Feature tagFeature;
		
		public void setTagFeature(Feature feature) {
			this.tagFeature = feature;
		}
		
		private Feature lemmaFeature;
		
		public void setLemmaFeature(Feature feature) {
			this.lemmaFeature = feature;
		}
		
		private boolean update;
		
		public void enableUpodate(boolean enabled) {
			this.update = enabled;
		}
		
		public void token(Annotation annotation, String tag, String lemma) {
				CAS cas = annotation.getCAS();
				int begin = annotation.getBegin();
				int end = annotation.getEnd();
				String lemata[] = lemma.split("\\|");
				String picked = lemata[lemata.length - 1];
				if (this.update) {
					this.update(cas, annotation, this.tagFeature, tag);
					this.update(cas, annotation, this.lemmaFeature, picked);
				} else {
					this.annotate(cas, this.tagFeature, begin, end, tag);
					this.annotate(cas, this.lemmaFeature, begin, end, picked);
				}
		}

		private void update(CAS cas, Annotation annotation, Feature feature, String value) {
			try {
				annotation.setStringValue(feature,value);
			} catch (Exception e) {
				UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
			}
		}
		
		private void annotate(CAS cas, Feature feature, int begin, int end, String value) {
			try {
				Type type = feature.getDomain();
				AnnotationFS annotation = cas.createAnnotation(type, begin, end);
				annotation.setStringValue(feature,value);
				cas.addFsToIndexes(annotation);
			} catch (Exception e) {
				UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
			}
		}

	}
	
	private class Adapter implements TokenAdapter<Annotation> {

		@Override
		public String getText(Annotation annotation) {
			return annotation.getCoveredText();
		}
		
	}
	
	private class Provider extends DefaultModelResolver implements ExecutableResolver {

		public Provider(String home) throws Exception {
			this.setHome(home);
		}
		
		private String home;
		
		private void setHome(String home) throws Exception {
			File path = new File(home);
			if (path.exists()) {
				if (path.isDirectory()) {
					this.home = home;
					try {
						this.setExecutable(path);
					} catch (Exception e) {
						UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
						this.executable = "tree-tagger";
					}
				} else {
					throw new Exception("The TreeTagger's home " + home + " isn't a directory.");
				}
			} else {
				throw new Exception("The TreeTagger's home " + home + " doesn't exist.");
			}
		}
		
		public String getHome() {
			return this.home;
		}
		
		private String executable;
		
		private void setExecutable(File home) throws Exception {
			File path = new File(home,"bin");
			if (path.exists()) {
				if (path.isDirectory()) {
					String os = System.getProperty("os.name");
					String exe = "tree-tagger";
					if (os.startsWith("Windows")) {
						exe += ".exe";
					}
					File file = new File(path,exe);
					if (file.exists()) {
						if (file.isFile()) {
							if (file.canExecute()) {
								this.executable = file.getAbsolutePath();
							} else {
								throw new Exception("The TreeTagger's executable isn't an executable.");
							}
						} else {
							throw new Exception("The TreeTagger's executable isn't a file.");
						}
					} else {
						throw new Exception("The TreeTagger's executable doesn't exist.");
					}
				} else {
					throw new Exception("The TreeTagger's binary directory isn't a directory.");
				}
			} else {
				throw new Exception("The TreeTagger's binary directory doesn't exist.");
			}
		}

		@Override
		public String getExecutable() throws IOException {
			return this.executable;
		}

		@Override
		public void destroy() {

		}
		
	}
	
}
