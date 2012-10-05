/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package eu.project.ttc.tools.utils;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.uima.resource.ResourceInitializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.utils.TranslationList.TranslationCandidate;
import eu.project.ttc.tools.utils.TranslationList.TranslationTerm;

/**
 * Writes a {@link TranslationList} to TBX xml format.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public final class TranslationListTBXWriter {

	// ////////////////////////////////////////////////////////////////////////
	// Constants

	/** Name of the programm that produced the TBX file */
	public static final String ORIGINATING_PROGRAM_NAME = TermSuite.class
			.getSimpleName() + " " + TermSuite.TERMSUITE_VERSION;

	/** URL of the format DTD. */
	public static final String TBX_DTD_URI = "http://ttc-project.googlecode.com/files/tbxcore.dtd";

	/** TTC format XCS URI. */
	public static final String TTC_XCS_URI = "http://ttc-project.googlecode.com/files/ttctbx.xcs";

	private static final String ROOT_ELEMENT = "martif";

	private static final String HEADER_ELEMENT = "martifHeader";

	private static final String PAR_ELEMENT = "p";

	private static final String SOURCE_DESCRIPTION_ELEMENT = "sourceDesc";

	private static final String FILE_DESCRIPTION_ELEMENT = "fileDesc";

	private static final String ENC_DESCRIPTION_ELEMENT = "encodingDesc";

	private static final String TEXT_ELEMENT = "text";

	private static final String BODY_ELEMENT = "body";

	private static final String TERM_ENTRY_ELEMENT = "termEntry";

	private static final String LANGSET_ELEMENT = "langSet";

	private static final String TERM_ELEMENT = "term";

	private static final String TIG_ELEMENT = "tig";

	private static final String XREF_ELEMENT = "xref";

	private static final String DESCRIP_ELEMENT = "descrip";

	private static final String ADMIN_ELEMENT = "admin";

	private static final String TYPE_ATTRIBUTE = "type";

	private static final String TARGET_ATTRIBUTE = "target";

	private static final String XML_LANG_ATTRIBUTE = XMLConstants.XML_NS_PREFIX + ":lang";

	private static final String XML_ID_ATTRIBUTE = XMLConstants.XML_NS_PREFIX + ":id";

	// ////////////////////////////////////////////////////////////////////////
	// Class members

	/** XML document builder */
	private DocumentBuilder mBuilder;

	/** XML serialization transformer */
	private Transformer mTransformer;

	/** Rounds and prints double numbers */
	private final NumberFormat scoreFormatter = NumberFormat.getNumberInstance(Locale.US);
	
	/** Default constructor */
	public TranslationListTBXWriter() throws ResourceInitializationException {

		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			mTransformer = transformerFactory.newTransformer();
			mTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			mTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
					TBX_DTD_URI);
			mTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			try {
				mTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
				mTransformer.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount", "2");
			} catch (IllegalArgumentException e) {
				// Ignore, too bad we won't be able to indent
			}

			mBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			scoreFormatter.setMaximumFractionDigits(4);
			scoreFormatter.setMinimumFractionDigits(4);
		} catch (TransformerConfigurationException e) {
			throw new ResourceInitializationException(e);
		} catch (IllegalArgumentException e) {
			throw new ResourceInitializationException(e);
		} catch (ParserConfigurationException e) {
			throw new ResourceInitializationException(e);
		}
	}

	public void write(TranslationList list, File output) throws TransformerException {
		
		// Create DOM
		Document document = mBuilder.newDocument();
		Element root = createDocumentElement(document);
		Element header = createHeaderElement(document, output);
		root.appendChild(header);

		Element text = document.createElement(TEXT_ELEMENT);
		Element body = document.createElement(BODY_ELEMENT);
		text.appendChild(body);
		root.appendChild(text);

		// Append candidates
		addTranslationListElements(document, body, list);
		
		// Write DOM to file
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(output);
		mTransformer.transform(source, result);
	}

	/**
	 * Creates the document root element
	 * 
	 * @param document
	 *            The XML document
	 * @return The root {@link Element}
	 */
	private Element createDocumentElement(Document document) {
		Element martif = document.createElement(ROOT_ELEMENT);
		martif.setAttribute(TYPE_ATTRIBUTE, "TBX");
		document.appendChild(martif);
		return martif;
	}

	/**
	 * Creates the document header
	 * 
	 * @param document
	 *            The document
	 * @param file
	 *            The output file
	 * @return The header {@link Element}
	 */
	private Element createHeaderElement(Document document, File file) {
		Element header = document.createElement(HEADER_ELEMENT);
		
		Element fileDesc = document.createElement(FILE_DESCRIPTION_ELEMENT);
		Element sourceDesc = document.createElement(SOURCE_DESCRIPTION_ELEMENT);
		Element p = document.createElement(PAR_ELEMENT);
		p.setTextContent(file.getAbsolutePath());
		sourceDesc.appendChild(p);
		fileDesc.appendChild(sourceDesc);
		
		Element encodingDesc = document.createElement(ENC_DESCRIPTION_ELEMENT);
		p = document.createElement(PAR_ELEMENT);
		p.setAttribute(TYPE_ATTRIBUTE, "XCSURI");
		p.setTextContent(TTC_XCS_URI);
		encodingDesc.appendChild(p);

		header.appendChild(fileDesc);
		header.appendChild(encodingDesc);
		
		return header;
	}
	
	private void addTranslationListElements(Document document, Element body,
			TranslationList list) {
		int currId = 0;
		int targetLangsetId = 0;
		String source = list.getSource();
		String target = list.getTarget();
		TranslationTerm term;
		for (String entryTerm : list) {

			term = list.getTermData(entryTerm);

			// Create entry and main langset element
			Element entry = document.createElement(TERM_ENTRY_ELEMENT);
			addXmlId(entry, "term-" + currId);
			Element langset = createHeadLangset(document, source, term, currId);
			entry.appendChild(langset);

			// Create a langset for each candidate
			for (TranslationCandidate candidate : term) {
				langset = createCandidateLangset(document, target, candidate, targetLangsetId);
				entry.appendChild(langset);
				targetLangsetId++;
			}
			
			body.appendChild(entry);
			currId++;
		}
	}

	private Element createHeadLangset(Document document, String lang,
			TranslationTerm termData, int langsetId) {
		Element headLangset = document.createElement(LANGSET_ELEMENT);
		addXmlLang(headLangset, lang);
		addXmlId(headLangset, lang + "." + LANGSET_ELEMENT + "-" + langsetId);

		headLangset.appendChild(createXRef(document, lang,
				termData.getLangset()));
		headLangset.appendChild(createTig(document, termData.getTerm()));

		return headLangset;
	}

	private Element createCandidateLangset(Document document, String lang,
			TranslationCandidate candidate, int langsetId) {
		Element langset = document.createElement(LANGSET_ELEMENT);
		addXmlLang(langset, lang);
		addXmlId(langset, lang + "." + LANGSET_ELEMENT + "-" + langsetId);

		langset.appendChild(createXRef(document, lang, candidate.getLangset()));

		Element descrip = createDescripElement(document,
				"crosslingualRelation", "translationCandidate");
		//addXmlLang(descrip, lang);
		langset.appendChild(descrip);

		langset.appendChild(createDescripElement(document, "similarity",
				scoreFormatter.format(candidate.getScore())));

		Element admin = document.createElement(ADMIN_ELEMENT);
		admin.setAttribute(TYPE_ATTRIBUTE, "originatingProgram");
		admin.setTextContent(ORIGINATING_PROGRAM_NAME);
		langset.appendChild(admin);

		langset.appendChild(createTig(document, candidate.getCandidate()));
		
		return langset;
	}

	private Element createDescripElement(Document document, String type,
			String value) {
		Element descrip = document.createElement(DESCRIP_ELEMENT);
		descrip.setAttribute(TYPE_ATTRIBUTE, type);
		descrip.setTextContent(value);
		return descrip;
	}

	private Element createXRef(Document document, String lang, String id) {
		Element xref = document.createElement(XREF_ELEMENT);
		xref.setAttribute(TARGET_ATTRIBUTE, lang + "-terminology.tbx");
		xref.setAttribute(TYPE_ATTRIBUTE, "externalCrossReference");
		xref.setTextContent(id);
		return xref;
	}

	private Element createTig(Document document, String term) {
		Element tig = document.createElement(TIG_ELEMENT);
		Element termHolder = document.createElement(TERM_ELEMENT);
		termHolder.setTextContent(term);
		tig.appendChild(termHolder);
		return tig;
	}

	private void addXmlLang(Element element, String lang) {
		element.setAttribute(XML_LANG_ATTRIBUTE, lang);

	}

	private void addXmlId(Element element, String id) {
		element.setAttribute(XML_ID_ATTRIBUTE, id);
	}
}