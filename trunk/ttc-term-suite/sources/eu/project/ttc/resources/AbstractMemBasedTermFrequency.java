package eu.project.ttc.resources;

import eu.project.ttc.types.TermAnnotation;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by sebastian on 30/01/14.
 */
public abstract class AbstractMemBasedTermFrequency<T extends TermAnnotation> extends ContextMemBasedTermFrequency<T> {

    protected final Map<String, Map<String, MutableInt>> forms = new HashMap<String, Map<String, MutableInt>>();
    protected final Map<String, String> categories = new HashMap<String, String>();
    protected final Map<String, MutableInt> frequencies = new HashMap<String, MutableInt>();

    public int getTermFrequency(String term) {
        MutableInt iNT = frequencies.get(term);
        return iNT == null ? 0 : iNT.intValue();
    }

    public String getCategory(String term) {
        return categories.get(term);
    }

    public int getFormFrequency(String term, String form) {
        try {
            return forms.get(term).get(form).intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public Set<String> getTerms() {
        return frequencies.keySet();
    }

    public Set<String> getForms(String term) {
        return forms.get(term).keySet();
    }

    protected String addGeneric(TermAnnotation annotation) {
        String term = annotation.getLemma().toLowerCase()
                .replaceAll("\\s+", " ").trim();
        String form = annotation.getCoveredText().trim();
        if (term == null) {
            return null;
        } else if (term.length() <= 2) {
            return null;
        } else if (term.length() > 50) {
            return null;
        } else if (term.startsWith("http:") || term.startsWith("www")) {
            return null;
        } else if (isAllowedTerm(term)) {

            // Increase term frequency
            if (!frequencies.containsKey(term))
                frequencies.put(term, new MutableInt(0));
            MutableInt frequency = frequencies.get(term);
            frequency.increment();

            categories.put(term, annotation.getCategory());

            // Handle forms
            Map<String, MutableInt> termForms = forms.get(term);
            if (termForms == null) {
                termForms = new TreeMap<String, MutableInt>();
                forms.put(term, termForms);
            }

            // Increse term forms frequencies
            // Increase term frequency
            if (!termForms.containsKey(form))
                termForms.put(form, new MutableInt(0));

            MutableInt formFreq = termForms.get(form);
            formFreq.increment();
            return term;
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        super.close();
        for (Map<String, MutableInt> map : forms.values())
            map.clear();
        forms.clear();
        categories.clear();
        frequencies.clear();
    }
}
