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

import java.util.HashSet;
import java.util.Set;

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

import eu.project.ttc.types.TermAnnotation;

/**
 * This Analysis Engine is responsible for removing annotations of terms
 * based on a threshold parameter.
 */
public class TermCleaner extends JCasAnnotator_ImplBase {

    private static String PRM_THRESHOLD = "Threshold";

    // Threshold value for the filtering
    private Integer threshold;
    // Set of annotations
    private Set<Annotation> annotations;

    private void setAnnotations() {
        this.annotations = new HashSet<Annotation>();
    }

    private Set<Annotation> getAnnotations() {
        return this.annotations;
    }

	private void setThreshold(Integer threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("The threshold must be greater than 0!");
        }
		this.threshold = threshold;
	}

	private Integer getThreshold() {
		return threshold;
	}

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		try {
            super.initialize(context);
			if (this.getAnnotations() == null) {
				this.setAnnotations();
			}
			if (this.getThreshold() == null) {
				Integer threshold = (Integer) context
						.getConfigParameterValue(PRM_THRESHOLD);
				this.setThreshold(threshold);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	private void display(JCas cas) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation sdi = (SourceDocumentInformation) iterator
					.next();
			this.getContext().getLogger()
					.log(Level.INFO, "Cleaning terms of " + sdi.getUri());
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		this.display(cas);
		// this.clean(cas);
		this.select(cas);
		this.remove();
		this.adjust(cas);
	}

//	private void clean(JCas cas) {
//		AnnotationIndex<Annotation> index = cas
//				.getAnnotationIndex(SingleWordTermAnnotation.type);
//		FSIterator<Annotation> iterator = index.iterator();
//		while (iterator.hasNext()) {
//			SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator
//					.next();
//			this.clean(cas, annotation);
//		}
//	}

//	private void clean(JCas cas, SingleWordTermAnnotation annotation) {
//		Set<TermComponentAnnotation> delete = new HashSet<TermComponentAnnotation>();
//		AnnotationIndex<Annotation> index = cas
//				.getAnnotationIndex(TermComponentAnnotation.type);
//		FSIterator<Annotation> iterator = index.subiterator(annotation);
//		while (iterator.hasNext()) {
//			TermComponentAnnotation component = (TermComponentAnnotation) iterator
//					.next();
//			FSIterator<Annotation> subiterator = index.subiterator(component);
//			while (subiterator.hasNext()) {
//				delete.add((TermComponentAnnotation) subiterator.next());
//			}
//		}
//		for (TermComponentAnnotation del : delete) {
//			del.removeFromIndexes();
//		}
//	}

    /**
     * Collect the term annotations that should be removed from the CAS
     * as their number of occurrences is strictly lesser than the threshold.
     */
	private void select(JCas cas) {
		TermAnnotation annotation;
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();

		// Collect removable terms
		while (iterator.hasNext()) {
			annotation = (TermAnnotation) iterator.next();
			if (annotation.getOccurrences() < threshold.intValue()) {
				annotations.add(annotation);
			}
		}

		// Keep variants that may be removable but which term is accepted
		iterator = index.iterator();
		TermAnnotation variant;
		while (iterator.hasNext()) {
			annotation = (TermAnnotation) iterator.next();
			if (annotation.getOccurrences() >= threshold.intValue()) {
				FSArray variants = annotation.getVariants();
				if (variants != null) {
					for (int i = variants.size() - 1; i >= 0; i--) {
						variant = annotation.getVariants(i);
						if (annotations.contains(variant))
							annotations.remove(variant);
					}
				}
			}
		}
	}

	private void remove() {
		for (Annotation annotation : this.getAnnotations()) {
			annotation.removeFromIndexes();
		}
	}

	private void adjust(JCas cas) {
		AnnotationIndex<Annotation> index = cas
				.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (annotation.getVariants() != null) {
				int occ = annotation.getOccurrences();
				double freq = annotation.getFrequency();
				double spec = annotation.getSpecificity();
				for (int i = 0; i < annotation.getVariants().size(); i++) {
					occ += annotation.getVariants(i).getOccurrences();
					freq += annotation.getVariants(i).getFrequency();
					spec += annotation.getVariants(i).getSpecificity();
				}
				annotation.setOccurrences(occ);
				annotation.setFrequency(freq);
				annotation.setSpecificity(spec);
			}
		}
	}

}
