package eu.project.ttc.oldies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import fr.univnantes.lina.uima.engines.Annotator;

public class SoapAnnotator extends Annotator {

	private URL server;
	
	private void setServer(URL server) {
		this.server = server;
	}
	
	private void setServerAdress(String serverAdress) throws MalformedURLException {
		URL server = new URL(serverAdress);
		this.setServer(server);
	}
	
	private URL getServer() {
		return this.server;
	}
	
	private String action;
	
	private void setAction(String soapAction) {
		this.action = soapAction;
	}
	
	private String getAction() {
		return this.action;
	}
	
	private String authorization;
	
	private void setAuthorization(String username,String password) {
		if (username == null || password == null) {
			this.authorization = null;
		} else {
			Base64 base = new Base64();
			String passphrase = username + ":" + password;
			byte[] code = base.encode(passphrase.getBytes());
			this.authorization = new String(code);
		}
	}
	
	private String getAuthorization() {
		return this.authorization;
	}
	
	private HttpURLConnection connection;
	
	private void setConnection() throws IOException {
		this.connection = (HttpURLConnection) this.getServer().openConnection();
		this.connection.setDoOutput(true);
		this.connection.setDoInput(true);
		this.connection.setRequestMethod("POST");
		this.connection.setRequestProperty("SOAPAction",this.getAction());

		if (this.getAuthorization() != null) {
			this.connection.setRequestProperty("Authorization","Basic " + this.getAuthorization());
		}
	}
	
	private HttpURLConnection getConnection() {
		return this.connection;
	}
	
	@Override
	public void doInitialize() throws ResourceInitializationException {
		try {
			String serverAdress = (String) this.getParameter("ServerAdress");
			this.setServerAdress(serverAdress);
			String soapAction = (String) this.getParameter("SoapAction");
			this.setAction(soapAction);
			String username = (String) this.getParameter("Username");
			String password = (String) this.getParameter("Password");
			this.setAuthorization(username,password);
			this.setConnection();
		} catch (MalformedURLException e) {
			throw new ResourceInitializationException(e);
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void doFinalize() {
		this.getConnection().disconnect();
	}
	
	@Override
	public void doProcess(JCas cas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		
	}

}
