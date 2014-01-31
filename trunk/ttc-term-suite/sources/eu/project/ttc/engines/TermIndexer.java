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
package eu.project.ttc.engines;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import eu.project.ttc.metrics.AssociationRate;
import eu.project.ttc.models.Component;
import eu.project.ttc.models.Context;
import eu.project.ttc.models.CrossTable;
import eu.project.ttc.resources.BdbComplexTermFrequency;
import eu.project.ttc.resources.BdbSimpleTermFrequency;
import eu.project.ttc.resources.ITermFrequency;
import eu.project.ttc.types.*;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;
import uima.sandbox.indexer.engines.Indexer;

import java.io.File;
import java.util.*;

public class TermIndexer extends Indexer {

    /* Berkeley db environment */
    private Environment environ;

    private ITermFrequency<SingleWordTermAnnotation> singleWordTermFrequency;

    private BdbComplexTermFrequency multiWordTermFrequency;

    private AssociationRate associationRate;

    private String language;

    private void setAssociationRate(String name) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (this.associationRate == null) {
            Class<?> cl = Class.forName(name);
            Object obj = cl.newInstance();
            if (obj instanceof AssociationRate) {
                this.associationRate = (AssociationRate) obj;
                UIMAFramework.getLogger().log(
                        Level.INFO,
                        "Setting Association Rate: "
                                + this.associationRate.getClass()
                                .getSimpleName());
            } else {
                throw new ClassCastException("Class name '" + name
                        + "' doesn't comply "
                        + AssociationRate.class.getCanonicalName());
            }
        }
    }

    @Override
    public void initialize() throws Exception {
        if (environ != null)
            throw new Error("AE cannot be initialized twice.");

        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setAllowCreate(true);
        environmentConfig.setCacheSize(500 * 1024 * 1024);
        environ = new Environment(new File(System.getProperty("java.io.tmpdir")), environmentConfig);

        singleWordTermFrequency = new BdbSimpleTermFrequency(environ);
        multiWordTermFrequency = new BdbComplexTermFrequency(environ);

        language = (String) this.getContext().getConfigParameterValue("Language");

        String className = (String) this.getContext()
                .getConfigParameterValue("AssociationRateClassName");
        this.setAssociationRate(className);

    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        super.collectionProcessComplete();
        try {
            environ.close();
            singleWordTermFrequency.close();
            multiWordTermFrequency.close();
        } catch (DatabaseException e) {
            throw new AnalysisEngineProcessException(e);
        }
    }

    private void process(JCas cas, boolean mode) {
        AnnotationIndex<Annotation> index = cas
                .getAnnotationIndex(SourceDocumentInformation.type);
        FSIterator<Annotation> iterator = index.iterator();
        if (iterator.hasNext()) {
            SourceDocumentInformation sdi = (SourceDocumentInformation) iterator
                    .next();
            if (mode) {
                this.getContext().getLogger()
                        .log(Level.INFO, "Indexing " + sdi.getUri());
            } else {
                this.getContext().getLogger()
                        .log(Level.INFO, "Releasing " + sdi.getUri());
            }
        }
    }

    @Override
    public void update(JCas cas) throws Exception {
        this.process(cas, true);
        AnnotationIndex<Annotation> index = cas
                .getAnnotationIndex(TermAnnotation.type);
        FSIterator<Annotation> iterator = index.iterator();
        while (iterator.hasNext()) {
            TermAnnotation annotation = (TermAnnotation) iterator.next();
            if (annotation instanceof SingleWordTermAnnotation) {
                singleWordTermFrequency.addEntry(
                        (SingleWordTermAnnotation) annotation);
                this.update(annotation);
            } else if (annotation instanceof MultiWordTermAnnotation) {
                multiWordTermFrequency.addEntry(
                        (MultiWordTermAnnotation) annotation);
            }
        }
    }

    private void update(TermAnnotation annotation) throws Exception {
        FSArray array = annotation.getContext();
        if (array == null) {
            return;
        } else {
            String term = annotation.getLemma().toLowerCase();
            int freq = singleWordTermFrequency.getTermFrequency(term);
            if (freq < 2) {
                return;
            } else {
                for (int index = 0; index < array.size(); index++) {
                    TermAnnotation entry = annotation.getContext(index);
                    String context = entry.getLemma().toLowerCase();
                    singleWordTermFrequency.addCoOccurrences(term, context);
                }
            }
        }
    }

    @Override
    public void release(JCas cas) throws Exception {
        this.process(cas, false);
        cas.setDocumentLanguage(language);
        StringBuilder builder = new StringBuilder();
        this.release(cas, builder, singleWordTermFrequency);
        this.release(cas, builder, multiWordTermFrequency);
        cas.setDocumentText(builder.toString());
        SourceDocumentInformation sdi = new SourceDocumentInformation(cas);
        sdi.setUri("http://" + language + "-terminology.xmi");
        sdi.setBegin(0);
        sdi.setEnd(builder.length());
        sdi.setOffsetInSource(0);
        sdi.addToIndexes();
        this.normalize(associationRate);
        this.annotate(cas);
    }

    private void annotate(JCas cas) throws Exception {
        for (String item : singleWordTermFrequency.getTerms()) {
            int freq = singleWordTermFrequency.getTermFrequency(item);
            JCas jcas = cas.createView(item);
            jcas.setDocumentLanguage(language);
            StringBuilder builder = new StringBuilder();
            Context context = singleWordTermFrequency.getContext(item);
            builder.append(context.toString());
            jcas.setDocumentText(builder.toString());
            TermAnnotation annotation = new TermAnnotation(jcas, 0, jcas
                    .getDocumentText().length());
            annotation.setOccurrences(freq);
            annotation.addToIndexes();
        }
    }

    private void normalize(AssociationRate rate) throws Exception {
        CrossTable crossTable = new CrossTable();
        Map<String, Context> contexts = singleWordTermFrequency.getContexts();
        crossTable.setContexts(contexts);
        for (String term : contexts.keySet()) {
            Context context = contexts.get(term);
            crossTable.setTerm(term);
            for (String coTerm : context.getCoocurringTerms()) {
                crossTable.setCoTerm(coTerm);
                crossTable.compute();
                int a = crossTable.getA();
                int b = crossTable.getB();
                int c = crossTable.getC();
                int d = crossTable.getD();
                double norm = rate.getValue(a, b, c, d);
                singleWordTermFrequency.setCoOccurrences(term,
                        coTerm, new Double(norm), Context.DEL_MODE);
            }
        }
    }

    private void release(JCas cas, StringBuilder builder,
                         ITermFrequency<SingleWordTermAnnotation> frequency) {
        for (String entry : frequency.getTerms()) {
            int freq = frequency.getTermFrequency(entry);
            String category = frequency.getCategory(entry);
            Set<String> forms = frequency.getForms(entry);
            int begin = builder.length();
            builder.append(entry);
            int end = builder.length();
            builder.append('\n');
            TermAnnotation annotation = new SingleWordTermAnnotation(cas,
                    begin, end);
            annotation.setOccurrences(freq);
            annotation.setFrequency(freq);
            annotation.setCategory(category);
            annotation.setLemma(entry);
            annotation.addToIndexes();
            handleForms(entry, begin, forms, frequency, cas, annotation);
        }
    }

    private void release(JCas cas, StringBuilder builder,
                         BdbComplexTermFrequency frequency) {
        for (String entry : frequency.getTerms()) {
            int freq = frequency.getTermFrequency(entry);
            String category = frequency.getCategory(entry);
            List<Component> components = frequency.getComponents().get(entry);
            Set<String> forms = frequency.getForms(entry);
            int begin = builder.length();
            builder.append(entry);
            int end = builder.length();
            builder.append('\n');
            MultiWordTermAnnotation annotation = new MultiWordTermAnnotation(
                    cas, begin, end);
            annotation.setOccurrences(freq);
            annotation.setFrequency(freq);
            annotation.setCategory(category);
            annotation.addToIndexes();
            if (components != null) {
                FSArray array = new FSArray(cas, components.size());
                annotation.setComponents(array);
                int i = 0;
                int start = begin;
                for (Component component : components) {
                    int stop = start + component.length();
                    TermComponentAnnotation c = new TermComponentAnnotation(
                            cas, start, stop);
                    component.release(c);
                    c.addToIndexes();
                    annotation.setComponents(i, c);
                    i++;
                    start = stop + 1;
                }
            }
            handleForms(entry, begin, forms, frequency, cas, annotation);
        }
    }

    private void handleForms(String entry, int begin, Set<String> forms,
                             ITermFrequency<? extends TermAnnotation> frequency, JCas cas, TermAnnotation annotation) {
        if (forms != null && !forms.isEmpty()) {
            // forms.add(entry);
            Map<String, Integer> newForms = gatherForms(entry, forms, frequency);
            newForms = sortByValue(newForms);
            FSArray array = new FSArray(cas, newForms.size());
            annotation.setForms(array);
            int i = 0;
            // System.out.println("Forms of \\" + entry + "\\");
            FormAnnotation formAnnot;
            for (String form : newForms.keySet()) {
                formAnnot = new FormAnnotation(cas, begin, begin
                        + form.length());
                formAnnot.setForm(form);
                formAnnot.setOccurrences(newForms.get(form));
                formAnnot.addToIndexes();
                annotation.setForms(i, formAnnot);
                i++;
                // System.out.println("\t" + form + " = " + newForms.get(form));
            }
            // System.out.println();
        }
    }

    private Map<String, Integer> gatherForms(String entry, Set<String> forms,
                                             ITermFrequency<? extends TermAnnotation> frequency) {
        HashMap<String, Integer> newForms = new HashMap<String, Integer>();
        int mainFreq;
        int freq;
        int curr;
        for (String k : forms) {
            String term = k.toLowerCase().replaceAll("\\s+", " ").trim();
            mainFreq = frequency.getTermFrequency(k);
            freq = frequency.getFormFrequency(entry, k);
            curr = freq == 0 ? mainFreq : freq;
            if (newForms.containsKey(term))
                newForms.put(term, newForms.get(term) + curr);
            else
                newForms.put(term, curr);
        }
        return newForms;
    }


    private static Map<String, Integer> sortByValue(final Map<String, Integer> map) {
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int comp = map.get(o2).compareTo(map.get(o1));
                return comp == 0 ? o1.compareTo(o2) : comp;
            }
        });
        result.putAll(map);
        return result;
    }
}
