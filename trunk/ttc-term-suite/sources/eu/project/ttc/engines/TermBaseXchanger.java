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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.project.ttc.tools.indexer.IndexerBinding;
import eu.project.ttc.tools.utils.IndexerTSVBuilder;
import eu.project.ttc.tools.utils.IterableNodeList;
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

    /** Initial predicate */
    private TermPredicate initial;
    /** Filtering predicate */
    private TermPredicate filteringPredicate;
	/** Global filter for tbx output */
	private TermPredicate completePredicate;

	private File workingDir;

	/** Term sorter in TBX output */
	private Comparator<TermAnnotation> outputComparator;

	/** TSV output flag */
	private boolean tsvEnabled = false;

	private void setDirectory(String path) throws IOException {
		this.workingDir = new File(path);
	}

	private File getDirectory() {
		return this.workingDir;
	}

    private Set<TermAnnotation> variants;

    private Map<String, Set<TermAnnotation>> variantsOf;

    private Map<TermAnnotation, Set<String>> basesOf;

    private void setVariants() {
        this.variants = new HashSet<TermAnnotation>();
        this.variantsOf = new HashMap<String, Set<TermAnnotation>>();
        this.basesOf = new HashMap<TermAnnotation, Set<String>>();
    }

    private int getVariantCount(FSArray vars) {
        return vars == null ? 0 : vars.size();
    }


    /**
     * Initialize the engine: configure the various parameters and prepare the filtering.
     */
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		try {
            super.initialize(context);

            // FIXME why ?
			if (this.getDirectory() == null) {
                // Compute directory
				String path = (String) context
                        .getConfigParameterValue(IndexerBinding.PRM.OUTPUT.getParameter());
				this.setDirectory(path);
                // Save TSV export flag
                tsvEnabled = Boolean.TRUE.equals(context
                        .getConfigParameterValue(IndexerBinding.PRM.TSV.getParameter()));
                // Set up filtering
                initFiltering(
                        (String) context.getConfigParameterValue(IndexerBinding.PRM.FILTERRULE.getParameter()),
                        (Float) context.getConfigParameterValue(IndexerBinding.PRM.FILTERINGTLD.getParameter()),
                        Boolean.TRUE.equals(context
                                .getConfigParameterValue(IndexerBinding.PRM.KEEPVERBS.getParameter())) );
                // Set up variants
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

    /**
     * Initialize the terms filtering based on the parameters.
     * The initialization is done as a side effect of the method on the class instance.
     *
     * @param filterRule
     *      name of the filter rule to be applied
     * @param filteringThreshold
     *      value of the threshold for the filtering
     * @param isKeepVerbs
     *      do we keep verbs and adverbs or do we filter them out
     */
	private void initFiltering(String filterRule, Float filteringThreshold, boolean isKeepVerbs) {
        // Compute the initial predicate
        if (isKeepVerbs) {
            initial = TermPredicates.createOrPredicate(
                    TermPredicates.createNounAdjectivePredicate(),
                    TermPredicates.createVerbAdverbPredicate());
        } else {
            initial = TermPredicates.createNounAdjectivePredicate();
        }

        // Add the filtering rule
        IndexerBinding.FilterRules rule = IndexerBinding.FilterRules.valueOf(filterRule);
        int cutoff = (int) Math.floor(filteringThreshold.doubleValue());
        switch (rule) {
        case None:
            outputComparator = TermPredicates.ASCENDING_TEXT_ORDER;
            filteringPredicate = TermPredicates.TRIVIAL_ACCEPTOR;
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case OccurrenceThreshold:
            outputComparator = TermPredicates.DESCENDING_OCCURRENCE_ORDER;
            filteringPredicate = TermPredicates.createOccurrencesPredicate(cutoff);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case FrequencyThreshold:
            outputComparator = TermPredicates.DESCENDING_FREQUENCY_ORDER;
            filteringPredicate = TermPredicates.createFrequencyPredicate(filteringThreshold);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case SpecificityThreshold:
            outputComparator = TermPredicates.DESCENDING_SPECIFICITY_ORDER;
            filteringPredicate = TermPredicates.createSpecificityPredicate(filteringThreshold);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case TopNByOccurrence:
            outputComparator = TermPredicates.DESCENDING_OCCURRENCE_ORDER;
            filteringPredicate = TermPredicates.createTopNByOccurrencesPredicate(cutoff);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case TopNByFrequency:
            outputComparator = TermPredicates.DESCENDING_FREQUENCY_ORDER;
            filteringPredicate = TermPredicates.createTopNByFrequencyPredicate(cutoff);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        case TopNBySpecificity:
            outputComparator = TermPredicates.DESCENDING_SPECIFICITY_ORDER;
            filteringPredicate = TermPredicates.createTopNBySpecificityPredicate(cutoff);
            completePredicate = TermPredicates.createAndPredicate(initial, filteringPredicate);
            return;

        default:
            throw new IllegalArgumentException("Unknown filtering rule " + filterRule);
        }
	}

    /**
     * Extracts all the term annotations in the JCas in parameter in order to
     * export them applying the filtering rules defined in parameter.
     */
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
            // Compute TBX and TSV file paths
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
			File tbxFile = new File(this.getDirectory(), name);
            File tsvFile = new File(this.getDirectory(), name.replace(".tbx", ".tsv"));
            this.getContext().getLogger().log(Level.INFO, "TBX path: " + tbxFile.getAbsolutePath());
            if (tsvEnabled) {
                this.getContext().getLogger().log(Level.INFO, "TSV path: " + tsvFile.getAbsolutePath());
            }

            // Sorting terms
            List<TermAnnotation> terms = extractAndSortTermAnnotations(cas);

            // Build TBX
            Document tbxDoc = prepareTBXDocument();
            populateTBX(cas.getDocumentLanguage(), terms, tbxDoc);
            exportTBXDocument(tbxDoc, tbxFile);

            // Build TSV
            if (tsvEnabled) {
                exportTSV(tbxDoc, tsvFile);
            }
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

    /**
     * Extract and sort term annotations based on the filtering parametered.
     *
     * @param cas
     *      JCas from which the annotations are extracted
     * @return
     *      sorted list of the annotations
     */
	private List<TermAnnotation> extractAndSortTermAnnotations(JCas cas) {
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

		if (filteringPredicate instanceof ListBasedTermPredicate)
			((ListBasedTermPredicate) filteringPredicate).initialize(accepted);

		Collections.sort(termList, outputComparator);
		System.out.println("Terms in sorted term list : " + termList.size());
		return termList;
	}

    /**
     * Prepare the TBX document that will contain the terms.
     */
	private Document prepareTBXDocument() throws ParserConfigurationException {
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
		encodingP.setTextContent("http://ttc-project.googlecode.com/files/ttctbx.xcs");
		encodingDesc.appendChild(encodingP);

		Element sourceDesc = document.createElement("sourceDesc");
		Element p = document.createElement("p");
		p.setTextContent(workingDir.getAbsolutePath());
		sourceDesc.appendChild(p);
		fileDesc.appendChild(sourceDesc);

		Element text = document.createElement("text");
		martif.appendChild(text);

        Element body = document.createElement("body");
		text.appendChild(body);

        return document;
    }

    /**
     * Export the TBX document to a file specified in parameter.
     *
     * @param tbxDoc
     *      TBX document containing the terms
     * @param tbxFile
     *      File where to persist the TBX tree
     * @throws TransformerException
     */
    private void exportTBXDocument(Document tbxDoc, File tbxFile) throws TransformerException {
        // Prepare the transformer to persist the file
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
				"http://ttc-project.googlecode.com/files/tbxcore.dtd");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		try {
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (IllegalArgumentException e) {} // Ignore

        // Actually persist the file
		DOMSource source = new DOMSource(tbxDoc);
		StreamResult result = new StreamResult(tbxFile);
		transformer.transform(source, result);
	}

    /**
     * Populate the TBX tree with the filtered terms found in the document.
     *
     * @param lang
     *      language of the document (ISO two letters)
     * @param termList
     *      list of the terms sorted in the order of the export
     * @param document
     * @throws IOException
     */
    private void populateTBX(String lang, List<TermAnnotation> termList, Document document) throws IOException {
        int count = 0;
        for (TermAnnotation annotation : termList) {
            // If term matches the filtering rules, the we add it to output
            // FIXED: why not done in the extraction ?
        	// FIXME: Because top n by predicates are based in the whole list.
            if (completePredicate.accept(annotation)) {
                count++;
                // Add main term entry
                addTermEntry(document, annotation.getLangset(), annotation, lang, false);
                // Add term variants
                Set<TermAnnotation> tVars = variantsOf.get( annotation.getLangset() );
                if (tVars != null) {
                    for (TermAnnotation tVariant : tVars) {
                        addTermEntry(document, tVariant.getLangset(),
                                tVariant, lang, true);
                    }
                }
            }
        }
        getContext().getLogger().log(Level.INFO, "Terms added to TBX: " + count);
    }


    /**
     * Build a TSV of the list of term annotations based on the TBX tree.
     *
     * @param tbxDoc
     *      sorted list of the annotations according to the filtering rules
     * @param tsvFile
     *      file where the content will be exported
     */
    private void exportTSV(Document tbxDoc, File tsvFile) throws IOException, XPathExpressionException {
		IndexerTSVBuilder tsv = new IndexerTSVBuilder(new FileWriter(tsvFile,
				false));

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpathTerms = factory.newXPath();
		NodeList tEntries = (NodeList) xpathTerms.evaluate("/martif/text/body/termEntry/langSet/tig", tbxDoc, XPathConstants.NODESET);
		for (Node tNode : IterableNodeList.fromNodeList(tEntries)) {
			// Add the term
			XPath xpathTerm = factory.newXPath();
			String termPilot = (String) xpathTerm.evaluate("termNote[@type='termPilot']/text()", tNode, XPathConstants.STRING);
			tsv.startTerm(termPilot);
			// ... its variants
			NodeList variantNodes = (NodeList) xpathTerm.evaluate("descrip[@type='termVariant']", tNode, XPathConstants.NODESET);
			for (Node vNode : IterableNodeList.fromNodeList(variantNodes)) {
				tsv.addVariant(((Element) vNode).getTextContent());
			}
			// done with this term
			tsv.endTerm();
		}

		tsv.close();
	}

    /**
     * Add a term to the TBX document.
     *
     * @param doc
     * @param langsetId
     * @param term
     * @param language
     * @param isVariant
     * @throws IOException
     */
	private void addTermEntry(Document doc, String langsetId,
			TermAnnotation term, String language, boolean isVariant)
			throws IOException {
        Node body = doc.getElementsByTagName("body").item(0);

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

		this.addNote(doc, langSet, tig, "termType", isVariant ? "variant" : "termEntry");
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