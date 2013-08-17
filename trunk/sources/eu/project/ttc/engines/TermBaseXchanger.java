package eu.project.ttc.engines;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.project.ttc.tools.indexer.IndexerBinding;
import eu.project.ttc.tools.indexer.IndexerModel;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.project.ttc.tools.utils.IndexerTSVBuilder;
import eu.project.ttc.tools.utils.TermPredicate;
import eu.project.ttc.tools.utils.TermPredicates;
import eu.project.ttc.tools.utils.TermPredicates.ListBasedTermPredicate;
import eu.project.ttc.types.FormAnnotation;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;

public class TermBaseXchanger extends JCasAnnotator_ImplBase {

	/** Prints float out numbers */
	private static final NumberFormat NUMBER_FORMATTER = NumberFormat
			.getNumberInstance(Locale.US);

	/** Prefix used in langset ids */
	public static final String LANGSET_ID_PREFIX = "langset-";

	/** Prefix used in langset ids */
	public static final String TERMENTRY_ID_PREFIX = "entry-";

	/** Prefix used in langset ids */
	public static final String TIG_ID_PREFIX = "term-";

	/** Global filter for tbx output */
	private TermPredicate predicate = TermPredicates
			.createNounAdjectivePredicate();

	/** Initial predicate */
	private TermPredicate initial;

	/** TBX filter rule as specified by the parameters */
	private TermPredicate filterRule;

	private File file;

	/** Term sorter in TBX output */
	private Comparator<TermAnnotation> outputComparator;

	/** TSV output */
	private IndexerTSVBuilder tsv;

	/** TSV output flag */
	private boolean tsvEnabled = false;

	private void setDirectory(String path) throws IOException {
		this.file = new File(path);
	}

	private File getDirectory() {
		return this.file;
	}

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getDirectory() == null) {
				String path = (String) context
						.getConfigParameterValue("Directory");
				this.setDirectory(path);

				// Set up TBX filtering
				if (Boolean.TRUE.equals(context
						.getConfigParameterValue(IndexerBinding.CFG.KEEPVERBS.getParameter())))
					predicate = TermPredicates.createOrPredicate(predicate,
							TermPredicates.createVerbAdverbPredicate());

				initial = predicate;

				filterRule = getFilterRulePredicate(
						(String) context
								.getConfigParameterValue(IndexerBinding.CFG.FILTERRULE.getParameter()),
						(Float) context
								.getConfigParameterValue(IndexerBinding.CFG.FILTERINGTLD.getParameter()));

				predicate = TermPredicates.createAndPredicate(predicate,
						filterRule);
				tsvEnabled = Boolean.TRUE.equals(context
						.getConfigParameterValue(IndexerBinding.CFG.TSV.getParameter()));

			}
			if (this.variants == null) {
				this.setVariants();
			}

			NUMBER_FORMATTER.setMaximumFractionDigits(4);
			NUMBER_FORMATTER.setMinimumFractionDigits(4);
			NUMBER_FORMATTER.setRoundingMode(RoundingMode.UP);
			NUMBER_FORMATTER.setGroupingUsed(false);

		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	private TermPredicate getFilterRulePredicate(String filterRule,
			Float filteringThreshold) throws Exception {
		try {
			IndexerBinding.FilterRules rule = IndexerBinding.FilterRules.valueOf(filterRule);
			TermPredicate pred = null;
			int cutoff = (int) Math.floor(filteringThreshold.doubleValue());
			switch (rule) {
			case None:
				pred = TermPredicates.TRIVIAL_ACCEPTOR;
				outputComparator = TermPredicates.ASCENDING_TEXT_ORDER;
				break;

			case OccurrenceThreshold:
				pred = TermPredicates.createOccurrencesPredicate(cutoff);
				outputComparator = TermPredicates.DESCENDING_OCCURRENCE_ORDER;
				break;
			case FrequencyThreshold:
				pred = TermPredicates
						.createFrequencyPredicate(filteringThreshold);
				outputComparator = TermPredicates.DESCENDING_FREQUENCY_ORDER;
				break;

			case SpecificityThreshold:
				pred = TermPredicates
						.createSpecificityPredicate(filteringThreshold);
				outputComparator = TermPredicates.DESCENDING_SPECIFICITY_ORDER;
				break;

			case TopNByOccurrence:
				pred = TermPredicates.createTopNByOccurrencesPredicate(cutoff);
				outputComparator = TermPredicates.DESCENDING_OCCURRENCE_ORDER;
				break;

			case TopNByFrequency:
				pred = TermPredicates.createTopNByFrequencyPredicate(cutoff);
				outputComparator = TermPredicates.DESCENDING_FREQUENCY_ORDER;
				break;

			case TopNBySpecificity:
				pred = TermPredicates.createTopNBySpecificityPredicate(cutoff);
				outputComparator = TermPredicates.DESCENDING_SPECIFICITY_ORDER;
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
			AnnotationIndex<Annotation> index = cas
					.getAnnotationIndex(SourceDocumentInformation.type);
			FSIterator<Annotation> iterator = index.iterator();
			String name = "terminology.tbx";
			if (iterator.hasNext()) {
				SourceDocumentInformation sdi = (SourceDocumentInformation) iterator
						.next();
				String uri = sdi.getUri();
				int first = uri.lastIndexOf('/');
				int last = uri.lastIndexOf('.');
				name = uri.substring(first, last) + ".tbx";
			}
			File file = new File(this.getDirectory(), name);
			if (tsvEnabled)
				tsv = new IndexerTSVBuilder(new FileWriter(new File(
						this.getDirectory(), name.replace(".tbx", ".tsv")),
						false));

			this.getContext().getLogger()
					.log(Level.INFO, "Exporting " + file.getAbsolutePath());
			List<TermAnnotation> annots = this.index(cas);
			this.create(cas, annots, file);

			if (tsvEnabled) {
				tsv.close();
				tsv = null;
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}

	}

	private List<TermAnnotation> index(JCas cas) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermAnnotation.type);
		ArrayList<TermAnnotation> termList = new ArrayList<TermAnnotation>();
		FSIterator<Annotation> iterator = index.iterator();
		int variantCount;
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			String id = LANGSET_ID_PREFIX + annotation.getAddress();
			annotation.setLangset(id);
			termList.add(annotation);
			variantCount = getVariantCount(annotation.getVariants());
			for (int i = 0; i < variantCount; i++) {
				TermAnnotation variant = annotation.getVariants(i);
				if (variant.getLangset() == null)
					variant.setLangset(LANGSET_ID_PREFIX + variant.getAddress());
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

		// We remove variants from the termList
		for (TermAnnotation var : variants)
			termList.remove(var);

		ArrayList<TermAnnotation> accepted = new ArrayList<TermAnnotation>();
		for (TermAnnotation annotation : termList)
			if (initial.accept(annotation)) {
				accepted.add(annotation);
			}

		if (filterRule instanceof ListBasedTermPredicate)
			((ListBasedTermPredicate) filterRule).initialize(accepted);

		Collections.sort(termList, outputComparator);
		System.out.println("Terms in sorted term list : " + termList.size());
		return termList;
	}

	private int getVariantCount(FSArray vars) {
		return vars == null ? 0 : vars.size();
	}

	private void create(JCas cas, List<TermAnnotation> termList, File file)
			throws Exception {
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
		Element encodingP = document.createElement("p");
		encodingP.setAttribute("type", "XCSURI");
		encodingP
				.setTextContent("http://ttc-project.googlecode.com/files/ttctbx.xcs");
		encodingDesc.appendChild(encodingP);
		Element sourceDesc = document.createElement("sourceDesc");
		Element p = document.createElement("p");
		p.setTextContent(file.getAbsolutePath());
		sourceDesc.appendChild(p);
		fileDesc.appendChild(sourceDesc);
		Element text = document.createElement("text");
		martif.appendChild(text);
		Element body = document.createElement("body");
		text.appendChild(body);
		this.process(cas, termList, document, body);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				"http://ttc-project.googlecode.com/files/tbxcore.dtd");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
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

	private void process(JCas cas, List<TermAnnotation> termList,
			Document document, Element body) throws IOException {
		String lang = cas.getDocumentLanguage();
		int count = 0;
		for (TermAnnotation annotation : termList) {
			String id = annotation.getLangset();

			// If term matches the filtering rules, the we add it to output
			if (predicate.accept(annotation)) {
				// Add main term entry
				addTermEntry(document, body, id, annotation, lang, false);

				// Add term variants
				Set<TermAnnotation> tVars = variantsOf.get(id);
				if (tVars != null) {
					for (TermAnnotation tVariant : tVars) {
						addTermEntry(document, body, tVariant.getLangset(),
								tVariant, lang, true);
					}
				}

				count++;
				// finish TSV entry if needed
				if (tsvEnabled) {
					tsv.endTerm();
				}
			}
		}
		System.out.println("Terms saved : " + count);
	}

	private void addTermEntry(Document doc, Element body, String langsetId,
			TermAnnotation term, String language, boolean isVariant)
			throws IOException {
		Element termEntry = doc.createElement("termEntry");
		termEntry.setAttribute("xml:id",
				TERMENTRY_ID_PREFIX + term.getAddress());
		body.appendChild(termEntry);
		Element langSet = doc.createElement("langSet");
		langSet.setAttribute("xml:id", langsetId);
		langSet.setAttribute("xml:lang", language);
		termEntry.appendChild(langSet);

		Set<String> bases = this.basesOf.get(term);
		if (bases != null) {
			for (String base : bases) {
				this.addTermBase(doc, langSet, base, null);
			}
		}

		Set<TermAnnotation> variants = this.variantsOf.get(langsetId);
		int globalOcc = term.getOccurrences();
		if (variants != null) {
			for (TermAnnotation variant : variants) {
				this.addTermVariant(doc, langSet, variant.getLangset(),
						variant.getCoveredText());
				if (!variant.getCoveredText().contains(term.getCoveredText()))
					globalOcc += variant.getOccurrences();
			}
		}
		this.addDescrip(doc, langSet, langSet, "nbOccurrences", globalOcc);

		Element tig = doc.createElement("tig");
		tig.setAttribute("xml:id", TIG_ID_PREFIX + term.getAddress());
		langSet.appendChild(tig);
		Element termElmt = doc.createElement("term");
		termElmt.setTextContent(term.getCoveredText());
		tig.appendChild(termElmt);

		FSArray forms = term.getForms();
		String pilot = term.getCoveredText();
		if (forms != null) {
			pilot = term.getForms(0).getForm();
			addNote(doc, langSet, tig, "termPilot", pilot);
		}

		this.addNote(doc, langSet, tig, "termType", isVariant ? "variant"
				: "termEntry");
		this.addNote(
				doc,
				langSet,
				tig,
				"partOfSpeech",
				(term instanceof MultiWordTermAnnotation) ? "noun" : term
						.getCategory());
		this.addNote(doc, langSet, tig, "termPattern", term.getCategory());
		this.addNote(doc, langSet, tig, "termComplexity",
				this.getComplexity(term));
		this.addDescrip(doc, langSet, tig, "termSpecificity",
				NUMBER_FORMATTER.format(term.getSpecificity()));
		this.addDescrip(doc, langSet, tig, "nbOccurrences",
				term.getOccurrences());
		this.addDescrip(doc, langSet, tig, "relativeFrequency",
				NUMBER_FORMATTER.format(term.getFrequency()));
		if (forms != null)
			addDescrip(doc, langSet, tig, "formList",
					buildFormListJSON(term, forms.size()));
		// this.addDescrip(document, langSet, tig, "domainSpecificity",
		// annotation.getSpecificity());

		if (tsvEnabled) {
			if (!isVariant)
				tsv.startTerm(pilot);
			else
				tsv.addVariant(pilot);
		}
	}

	private String buildFormListJSON(TermAnnotation term, int size) {
		StringBuilder sb = new StringBuilder("[");
		FormAnnotation form;
		for (int i = 0; i < size; i++) {
			form = term.getForms(i);
			if (i > 0)
				sb.append(", ");
			sb.append("{term=\"").append(form.getForm());
			sb.append("\", count=").append(form.getOccurrences()).append("}");
		}
		sb.append("]");
		return sb.toString();
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

	private void addDescrip(Document document, Element lang, Element element,
			String type, Object value) {
		Element descrip = document.createElement("descrip");
		element.appendChild(descrip);
		descrip.setAttribute("type", type);
		descrip.setTextContent(value.toString());
	}

	private void addTermBase(Document document, Element lang, String target,
			Object value) {
		Element descrip = document.createElement("descrip");
		lang.appendChild(descrip);
		descrip.setAttribute("type", "termBase");
		descrip.setAttribute("target", target);
		if (value != null) {
			descrip.setTextContent(value.toString());
		}
	}

	private void addTermVariant(Document document, Element lang, String target,
			Object value) {
		Element descrip = document.createElement("descrip");
		lang.appendChild(descrip);
		descrip.setAttribute("type", "termVariant");
		descrip.setAttribute("target", target);
		if (value != null) {
			descrip.setTextContent(value.toString());
		}
	}

	private void addNote(Document document, Element lang, Element element,
			String type, Object value) {
		Element termNote = document.createElement("termNote");
		element.appendChild(termNote);
		termNote.setAttribute("type", type);
		termNote.setTextContent(value.toString());
	}

}