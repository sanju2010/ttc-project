package eu.project.ttc.resources;

import com.sleepycat.je.Environment;
import eu.project.ttc.models.Component;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.WordAnnotation;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A partial berkely db based TermFrequency storage for multiword term annotations, term components are not stored in
 * the embedded db.
 * Created by sebastian on 31/01/14.
 */
public class BdbComplexTermFrequency extends AbstractBdbTermFrequency<MultiWordTermAnnotation> {

    private Map<String, List<Component>> components = new HashMap<String, List<Component>>();

    public BdbComplexTermFrequency(Environment environment) throws ResourceInitializationException {
        super("CTermFrequencies", environment);
    }

    public Map<String, List<Component>> getComponents() {
        return this.components;
    }

    @Override
    public void addEntry(MultiWordTermAnnotation annotation) {
        String term = this.add(annotation);
        if (term == null) {
            return;
        } else {
            if (annotation.getComponents() == null) {
                this.addSubComponents(annotation, term);
            } else {
                this.addComponents(annotation, term);
            }
        }
    }

    @Override
    public void close() {
        super.close();
        for(List<Component> compo : components.values())
            compo.clear();
        components.clear();
    }

    private void addForm(MultiWordTermAnnotation annotation, String term) {
        String coveredText = annotation.getCoveredText().toLowerCase()
                .replaceAll("\\s+", " ");
        int size = annotation.getComponents().size();
        StringBuilder form = new StringBuilder();
        for (int index = 0; index < size; index++) {
            TermComponentAnnotation component = annotation.getComponents(index);
            if (component.getBegin() == annotation.getBegin()
                    && component.getEnd() == annotation.getEnd()) {
                continue;
            } else {
                form.append(component.getCoveredText());
                if (index < size - 1) {
                    form.append(' ');
                }
            }
        }

        String formText = (coveredText.length() < form.length()) ? coveredText : form.toString();
        increment("[" + term + "-" + formText + "]");
    }

    private void add(MultiWordTermAnnotation annotation, String term) {
        increment(term);
        setValue("\0" + term + "\0-\0cat\0", annotation.getCategory());
        this.addForm(annotation, term);
    }

    private String add(MultiWordTermAnnotation annotation) {
        if (annotation.getComponents() == null) {
            return addGeneric(annotation);
        } else {
            int size = annotation.getComponents().size();
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < size; index++) {
                TermComponentAnnotation component = annotation
                        .getComponents(index);
                builder.append(component.getLemma());
                if (index < size - 1) {
                    builder.append(' ');
                }
            }
            String term = builder.toString();
            this.add(annotation, term);
            return term;
        }
    }

    private void addComponents(MultiWordTermAnnotation annotation, String term) {
        int size = annotation.getComponents().size();
        List<Component> components = new ArrayList<Component>();
        for (int index = 0; index < size; index++) {
            TermComponentAnnotation component = annotation.getComponents(index);
            if (component.getBegin() == annotation.getBegin()
                    && component.getEnd() == annotation.getEnd()) {
                continue;
            } else {
                Component c = new Component();
                c.update(component);
                components.add(c);
            }
        }
        this.getComponents().put(term, components);
    }

    private void addSubComponents(MultiWordTermAnnotation annotation,
                                  String term) {
        try {
            JCas cas = annotation.getCAS().getJCas();
            AnnotationIndex<Annotation> index = cas
                    .getAnnotationIndex(WordAnnotation.type);
            FSIterator<Annotation> iterator = index.subiterator(annotation);
            List<Component> components = new ArrayList<Component>();
            while (iterator.hasNext()) {
                WordAnnotation component = (WordAnnotation) iterator.next();
                if (component.getBegin() == annotation.getBegin()
                        && component.getEnd() == annotation.getEnd()) {
                    continue;
                } else {
                    Component c = new Component();
                    c.update(component);
                    components.add(c);
                }
            }
            this.getComponents().put(term, components);
        } catch (NullPointerException e) {
            UIMAFramework.getLogger().log(Level.SEVERE, term);
            UIMAFramework.getLogger().log(Level.SEVERE, annotation.toString());
            // ignore throw e;
        } catch (CASException e) {
            UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
        }
    }

}
