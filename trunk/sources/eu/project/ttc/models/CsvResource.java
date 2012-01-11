package eu.project.ttc.models;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class CsvResource implements SharedResourceObject {

	private List<Line> lines;
		
	public CsvResource() {
		this.lines = new ArrayList<Line>();
	}
	
	public void index(String term, String complexity, String category, double frequency, double specificity) {
		Line line = new Line(term, complexity, category, frequency, specificity);
		this.lines.add(line);
	}
		
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Collections.sort(this.lines, new LineComparator());
		for (Line line : this.lines) {
			builder.append(line.term());
			builder.append('\t');
			builder.append(line.complexity());
			builder.append('\t');
			builder.append(line.category());
			builder.append('\t');
			builder.append(line.frequency());
			builder.append('\t');
			builder.append(line.specificity());
			builder.append('\t');
			builder.append('\n');
		}
		return builder.toString();
	}
	
	public void load(DataResource aData) throws ResourceInitializationException { }
	
	private class LineComparator implements Comparator<Line> {

		@Override
		public int compare(Line source,Line target) {
			if (CsvResource.sortBy.equals(CsvResource.SORT_BY_SPECIFICITY)) {
				if (source.specificity() < target.specificity()) {
					return 1;
				} else if (source.specificity() > target.specificity()) {
					return -1;
				} else {
					return 0;
				}				
			} else if (CsvResource.sortBy.equals(CsvResource.SORT_BY_FREQUENCY)) {
				if (source.frequency() < target.frequency()) {
					return 1;
				} else if (source.frequency() > target.frequency()) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return source.term().compareTo(target.term());
			}
		}
	}

	public void release(OutputStream stream) throws IOException {
		String string = this.toString();
		stream.write(string.getBytes());
	}

	private class Line {
		
		private String term;
		
		public String term() {
			return this.term;
		}
		
		private String complexity;
		
		public String complexity() {
			return this.complexity;
		}
		
		private String category;
		
		public String category() {
			return this.category;
		}
		
		private double frequency;
		
		public double frequency() {
			return this.frequency;
		}
		
		private double specificity;

		public double specificity() {
			return this.specificity;
		}
		
		public Line(String term, String complexity, String category, double frequency, double specificity) {
			this.term = term;
			this.complexity = complexity;
			this.category = category;
			this.frequency = frequency;
			this.specificity = specificity;
		}
		
	}
	
	public static String SORT_BY_SPECIFICITY = "specificity";
	
	public static String SORT_BY_FREQUENCY = "frequency";
			
	private static String sortBy;
	
	public void sortBy(String sortBy) {
		CsvResource.sortBy = sortBy.toLowerCase();
	}
	
}
