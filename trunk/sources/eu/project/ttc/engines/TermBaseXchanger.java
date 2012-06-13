package eu.project.ttc.engines;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.project.ttc.tools.indexer.TBXSettings;
import eu.project.ttc.tools.indexer.TBXSettings.FilterRules;
import eu.project.ttc.tools.utils.TermPredicate;
import eu.project.ttc.tools.utils.TermPredicates;
import eu.project.ttc.tools.utils.TermPredicates.ListBasedTermPredicate;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;

public class TermBaseXchanger extends JCasAnnotator_ImplBase {

	private TermPredicate predicate = TermPredicates.createNounAdjectivePredicate();
	
	private TermPredicate filterRule;
	private File file;
	
	private void setDirectory(String path) throws IOException {
		this.file = new File(path);
	}
	
	private File getDirectory() {
		return this.file;
	}
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getDirectory() == null) {
				String path = (String) context
						.getConfigParameterValue("Directory");
				this.setDirectory(path);

				// Set up TBX filtering
				if (Boolean.TRUE.equals(context
						.getConfigParameterValue(TBXSettings.P_KEEP_VERBS)))
					predicate = TermPredicates.createOrPredicate(predicate,
							TermPredicates.createVerbAdverbPredicate());

				filterRule = getFilterRulePredicate(
						(String) context
								.getConfigParameterValue(TBXSettings.P_FILTER_RULE),
						(Float) context
								.getConfigParameterValue(TBXSettings.P_FILTERING_THRESHOLD));
				
				predicate = TermPredicates.createAndPredicate(predicate, filterRule);
			}
			if (this.variants == null) {
				this.setVariants();
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private TermPredicate getFilterRulePredicate(String filterRule,
			Float filteringThreshold) throws Exception {
		try {
			FilterRules rule = FilterRules.valueOf(filterRule);
			TermPredicate pred = null;
			int cutoff = (int) Math.floor(filteringThreshold.doubleValue());
			switch (rule) {
			case None:
				pred = TermPredicates.TRIVIAL_ACCEPTOR;
				break;

			case OccurrenceThreshold:
				pred = TermPredicates.createOccurrencesPredicate(cutoff);
				break;
			case FrequencyThreshold:
				pred = TermPredicates
						.createFrequencyPredicate(filteringThreshold);
				break;

			case SpecificityThreshold:
				pred = TermPredicates
						.createSpecificityPredicate(filteringThreshold);
				break;

			case TopNByOccurrence:
				pred = TermPredicates.createTopNByOccurrencesPredicate(cutoff);
				break;

			case TopNByFrequency:
				pred = TermPredicates.createTopNByFrequencyPredicate(cutoff);
				break;

			case TopNBySpecificity:
				pred = TermPredicates.createTopNBySpecificityPredicate(cutoff);
				break;

			default:
				throw new Exception(filterRule);
			}

			return pred;

		} catch (Exception e) {
			throw new Exception("Invalid filter rule : " + e.getMessage(), null);
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
			FSIterator<Annotation> iterator = index.iterator();
			String name = "terminology.tbx";
			if (iterator.hasNext()) 
			{
				SourceDocumentInformation sdi = (SourceDocumentInformation) iterator.next();
				String uri = sdi.getUri();
				int first = uri.lastIndexOf('/');
				int last = uri.lastIndexOf('.');
				name = uri.substring(first, last) + ".tbx";
			}
			File file = new File(this.getDirectory(), name);
			this.getContext().getLogger().log(Level.INFO, "Exporting " + file.getAbsolutePath());
			this.index(cas);
			this.create(cas, file);
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
		
	}

	private void index(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		ArrayList<TermAnnotation> termList = new ArrayList<TermAnnotation>();
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			termList.add(annotation);
			if (annotation.getVariants() != null) {
				String id = "langset-" + annotation.getAddress();
				for (int i = 0; i < annotation.getVariants().size(); i++) {
					TermAnnotation variant = annotation.getVariants(i);
					this.variants.add(variant);
					Set<TermAnnotation> variants = this.variantsOf.get(id);
					if (variants == null) {
						variants = new HashSet<TermAnnotation>();
						this.variantsOf.put(id, variants);
					}
					variants.add(variant);
					Set<String> bases = this.basesOf.get(variant);
					if (bases == null) {
						bases = new HashSet<String>();
						this.basesOf.put(variant, bases);
					}
					bases.add(id);
				}
			}
		}
		
		if (filterRule instanceof ListBasedTermPredicate)
			((ListBasedTermPredicate) filterRule).initialize(termList);
	}

	private void create(JCas cas, File file) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		Element martif = document.createElement("martif");
		martif.setAttribute("type", "TBX");
		document.appendChild(martif);
		Element header = document.createElement("martifHeader");
		martif.appendChild(header);
		Element fileDesc = document.createElement("fileDesc");
		header.appendChild(fileDesc);
		Element encodingDesc = document.createElement("encodingDesc");
		header.appendChild(encodingDesc);
		Element  encodingP = document.createElement("p");
		encodingP.setAttribute("type", "XCSURI");
		encodingP.setTextContent("http://ttc-project.googlecode.com/files/ttctbx.xcs");
		encodingDesc.appendChild(encodingP);
		Element sourceDesc = document.createElement("sourceDesc");
		Element  p = document.createElement("p");
		p.setTextContent(file.getAbsolutePath());
		sourceDesc.appendChild(p);
		fileDesc.appendChild(sourceDesc);
		Element text = document.createElement("text");
		martif.appendChild(text);
		Element body = document.createElement("body");
		text.appendChild(body);
		this.process(cas, document, body);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://ttc-project.googlecode.com/files/tbxcore.dtd");
		transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		try {
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (IllegalArgumentException e) {
			// Ignore
		}
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}

	private Set<TermAnnotation> variants;
	
	private Map<String, Set<TermAnnotation>> variantsOf;
	
	private Map<TermAnnotation, Set<String>> basesOf;
	
	private void setVariants() {
		this.variants = new HashSet<TermAnnotation>();
		this.variantsOf = new HashMap<String, Set<TermAnnotation>>();
		this.basesOf = new HashMap<TermAnnotation, Set<String>>();
	}
		
	private void process(JCas cas, Document document, Element body) {
		DecimalFormat format = new DecimalFormat("##.######");
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			
			// If term matches the filtering rules, the we add it to output
			if (predicate.accept(annotation)) {
				Element termEntry = document.createElement("termEntry");
				termEntry.setAttribute("xml:id", "entry-" + annotation.getAddress());
				body.appendChild(termEntry);
				Element langSet = document.createElement("langSet");
				String id = "langset-" + annotation.getAddress();
				langSet.setAttribute("xml:id", id);
				langSet.setAttribute("xml:lang", cas.getDocumentLanguage());
				termEntry.appendChild(langSet);

				Set<String> bases = this.basesOf.get(annotation);
				if (bases != null) {
					for (String base : bases) {
						this.addTermBase(document, langSet, base, null);
					}
				}

				Set<TermAnnotation> variants = this.variantsOf.get(id);
				if (variants != null) {
					for (TermAnnotation variant : variants) {
						this.addTermVariant(document, langSet, "langset-" + variant.getAddress(), variant.getCoveredText());
					}
				}

				Element tig = document.createElement("tig");
				tig.setAttribute("xml:id", "term-" + annotation.getAddress());
				langSet.appendChild(tig);
				Element term = document.createElement("term");
				term.setTextContent(annotation.getCoveredText());
				tig.appendChild(term);
				if (this.variants.contains(annotation)) {
					this.addNote(document, langSet, tig, "termType", "variant");				
				} else {
					this.addNote(document, langSet, tig, "termType", "termEntry");
				}
				this.addNote(document, langSet, tig, "partOfSpeech", "noun");
				this.addNote(document, langSet, tig, "termPattern", annotation.getCategory());
				this.addNote(document, langSet, tig, "termComplexity", this.getComplexity(annotation));
				this.addNote(document, langSet, tig, "termSpecifity", annotation.getSpecificity());
				this.addDescrip(document, langSet, tig, "nbOccurrences", annotation.getOccurrences());
				this.addDescrip(document, langSet, tig, "relativeFrequency", format.format(annotation.getFrequency()));
				// this.addDescrip(document, langSet, tig, "domainSpecificity", annotation.getSpecificity());
			}}
	}
	
	private String getComplexity(TermAnnotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation) {
			SingleWordTermAnnotation swt = (SingleWordTermAnnotation) annotation;
			if (swt.getCompound()) {
				if (swt.getNeoclassical()) {
					return "neoclassical-compound";
				} else {
					return "compound";
				}
			} else {
				return "single-word";
			}
		} else {
			return "multi-word";
		}
	}

	private void addDescrip(Document document, Element lang, Element element, String type, Object value) {
		Element descrip = document.createElement("descrip");
		element.appendChild(descrip);
		descrip.setAttribute("type", type);
		descrip.setTextContent(value.toString());
	}
	
	private void addTermBase(Document document, Element lang, String target, Object value) {
		Element descrip = document.createElement("descrip");
		lang.appendChild(descrip);
		descrip.setAttribute("type", "termBase");
		descrip.setAttribute("target", target);
		if (value != null) {
			descrip.setTextContent(value.toString());
		}
	}

	private void addTermVariant(Document document, Element lang, String target, Object value) {
		Element descrip = document.createElement("descrip");
		lang.appendChild(descrip);
		descrip.setAttribute("type", "termVariant");
		descrip.setAttribute("target", target);
		if (value != null) {
			descrip.setTextContent(value.toString());
		}
	}
	
	private void addNote(Document document, Element lang, Element element, String type, Object value) {
		Element termNote = document.createElement("termNote");
		element.appendChild(termNote);
		termNote.setAttribute("type", type);
		termNote.setTextContent(value.toString());
	}
	
}