package eu.project.ttc.oldies;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.BindingProvider;

import org.apache.commons.codec.binary.Base64;

import com.tilde.tagger.TaggerService;
import com.tilde.tagger.TaggerServiceSoap;

public class TildeTaggerSoapServiceTest {

	public final static String SERVER = "https://www.tilde.com/tagger/Service.asmx";
	public final static String SOAP_ACTION = "http://www.tilde.com/tagger/PosTagger";
	public final static String AGAIN_SOAP_ACTION = "http://www.tilde.com/tagger/Tokenize";

	public final static String OUTPUT_FORMAT = "treetagger";
	public final static String TEXT = "Antins\n,\nes\nšo\njomu\nesmu\npētijis\ndiezgan\ndaudz\n,\nbet\nko\ntas\ndod\n.\n";
	public final static String AGAIN_TEXT = "Antins, es šo jomu esmu pētijis diezgan daudz, bet ko tas dod.";
	public final static String LANG = "lv";

	public final static String USERNAME = "TTC";
	public final static String PASSWORD = "TTCt@g3r";

	public static void main(String[] args) {
		// System.out.println("---- 1st WAY ----\n");
		secondWay();
		// System.out.println("\n---- 2nd WAY ----\n");
		// secondWay();
	}
	
	public static void secondWay() {
		TaggerService service = new TaggerService();
		TaggerServiceSoap port = service.getTaggerServiceSoap();
		
		/*******************UserName & Password ******************************/
        Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
        // requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, TaggerService.TAGGERSERVICE_WSDL_LOCATION);
 
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("Username", Collections.singletonList("TTC"));
        headers.put("Password", Collections.singletonList("TTCt@g3r"));
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        /**********************************************************************/
		
		String result = port.tokenize(TEXT, LANG);// posTagger(TEXT,OUTPUT_FORMAT,LANG);
		System.out.println(result);
	}
	
	public static void firstWay() {

		try {
			URL url = new URL(SERVER);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			try {

				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("SOAPAction", AGAIN_SOAP_ACTION);

				Base64 base = new Base64();
				String passphrase = USERNAME + ":" + PASSWORD;
				String authorization = new String(base.encode(passphrase.getBytes()));
				connection.setRequestProperty("Authorization","Basic " + authorization);

				OutputStream out = connection.getOutputStream();
				Writer wout = new OutputStreamWriter(out);

				wout.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"); 
				wout.write("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");

				wout.write("<soap:Body>");
				wout.write("<PosTagger xmlns=\"http://www.tilde.com/tagger/\">");
				wout.write("<text>" + AGAIN_TEXT + "</text>");
				wout.write("<lang>" + LANG + "</lang>");
				wout.write("</PosTagger>");
				wout.write("</soap:Body>");

				wout.write("</soap:Envelope>");
				wout.flush();
				wout.close();

				InputStream in = connection.getInputStream();
				int c;
				while ((c = in.read()) != -1) System.out.write(c);
				in.close();

			}  finally {
				connection.disconnect();
			}
		}catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	} 

} 