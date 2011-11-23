package eu.project.ttc.models;

import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;

import eu.project.ttc.metrics.SimilarityDistance;

public class ContextualDirectMethod implements AlignmentMethod {

	private TermContextIndex source;
	
	private TermContextIndex target;
	
	private TermContextIndex result;
	
	private SimilarityDistance distance;
	
	public ContextualDirectMethod(SimilarityDistance distance, TermContextIndex source, TermContextIndex target, TermContextIndex result) {
		this.distance = distance;
		this.source = source;
		this.target = target;
		this.result = result;
	}
	
	@Override
	public Map<String, Double> align(String term) {
		UIMAFramework.getLogger().log(Level.INFO,"Translating " + " '" + term + "'");
		this.result.setOccurrences(term, this.source.getOccurrences().get(term));
		TermContext sourceContext = this.source.getTermContexts().get(term);
		for (String targetTerm : this.target.getTermContexts().keySet()) {
			TermContext targetContext = this.target.getTermContexts().get(targetTerm);
			double score = this.distance.getValue(sourceContext.getCoOccurrences(),targetContext.getCoOccurrences());
			if (!Double.isInfinite(score) && !Double.isNaN(score)) {						
				this.result.setCoOccurrences(term,targetTerm,new Double(score), TermContext.DEL_MODE);
			}
		}
		return this.result.getTermContexts().get(term).getSortedCoOccurrences();
	}

}
