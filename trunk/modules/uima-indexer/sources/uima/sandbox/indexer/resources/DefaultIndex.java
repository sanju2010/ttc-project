package uima.sandbox.indexer.resources;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

public class DefaultIndex implements Index {

	private static final long serialVersionUID = 3699707737959624316L;
	
	private JCas instance;
	
	@Override
	public JCas get() {
		return this.instance;
	}
	
	@Override
	public void load(InputStream inputStream) throws Exception { 
		if (this.instance != null) {
			CAS cas = this.instance.getCas();
			XmiCasDeserializer.deserialize(inputStream, cas);
		}
	}
	
	@Override
	public void store(OutputStream outputStream) throws Exception {
		if (this.instance != null) {
			CAS cas = this.instance.getCas();
			TypeSystem typeSystem = cas.getTypeSystem();
			XmiCasSerializer.serialize(cas, typeSystem, outputStream);
		}
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { 
		if (this.instance == null) {
			URL url = data.getUrl();
			try {
				XMLInputSource source = new XMLInputSource(url);
				XMLParser parser = UIMAFramework.getXMLParser();
				AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(source);
				CAS cas = CasCreationUtils.createCas(ae); 
				this.instance = cas.getJCas();
			} catch (Exception e) {
				throw new ResourceInitializationException(e);
			}
		}
	}
	
}
