package eu.project.ttc.resources;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.TermAnnotation;
import org.apache.uima.resource.SharedResourceObject;

import java.util.Map;
import java.util.Set;

/**
 * Created by sebastian on 30/01/14.
 */
public interface ITermFrequency<T extends TermAnnotation> extends SharedResourceObject {

    public int getTermFrequency(String term);

    public String getCategory(String term);

    public int getFormFrequency(String term, String form);

    public void addEntry(T annotation);

    public Set<String> getTerms();

    public Set<String> getForms(String term);

    public void setCoOccurrences(String term, String coTerm, double occurrences, int mode);

    public void addCoOccurrences(String term, String coTerm);

    public Context getContext(String term);

    // TODO: Remove this method if easily possible
    public Map<String, Context> getContexts();

    /**
     * Disposes / releases any resources used by this instance
     */
    public void close();
}
