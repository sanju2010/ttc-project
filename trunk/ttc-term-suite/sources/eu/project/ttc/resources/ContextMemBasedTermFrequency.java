package eu.project.ttc.resources;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.TermAnnotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 30/01/14.
 */
public abstract class ContextMemBasedTermFrequency<T extends TermAnnotation> implements  ITermFrequency<T> {

    protected final HashMap<String, Context> contexts = new HashMap<String, Context>();

    static boolean isAllowedTerm(String term) {
        char ch = term.charAt(0);
        int type = Character.getType(ch);
        Character.UnicodeBlock unicode = Character.UnicodeBlock.of(ch);
        if (type == Character.LOWERCASE_LETTER) {
            return true;
        } else if (unicode == Character.UnicodeBlock.CYRILLIC) {
            return true;
        } else if (unicode == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setCoOccurrences(String term, String coTerm, double occurrences, int mode) {
        Context termContext = contexts.get(term);
        if (termContext == null) {
            termContext = new Context();
            contexts.put(term, termContext);
        }
        termContext.setCoOccurrences(coTerm, occurrences, mode);
    }

    @Override
    public void addCoOccurrences(String term, String coTerm) {
        Context termContext = contexts.get(term);
        if (termContext == null) {
            termContext = new Context();
            contexts.put(term, termContext);
        }
        termContext.setCoOccurrences(coTerm, 1.0, Context.ADD_MODE);
    }

    @Override
    public Context getContext(String term) {
        return contexts.get(term);
    }

    @Override
    public Map<String, Context> getContexts() {
        return contexts;
    }

    @Override
    public void load(DataResource aData) throws ResourceInitializationException {
    }

    @Override
    public void close() {
        contexts.clear();
    }
}
