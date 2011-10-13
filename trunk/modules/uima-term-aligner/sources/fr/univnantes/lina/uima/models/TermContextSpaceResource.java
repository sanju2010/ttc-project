package fr.univnantes.lina.uima.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import fr.univnantes.lina.metrics.AssociationRate;
import fr.univnantes.lina.metrics.SimilarityDistance;

public class TermContextSpaceResource implements TermContextSpace {

	/************************************** 0) Evaluating **********************************/

	private boolean checked = false;
	
	private void enableChecked(boolean enabled) {
		this.checked = enabled;
	}
	
	private boolean hasBeenChecked() {
		return this.checked;
	}
	
	private int total;
	
	private void setTotal(int num) {
		this.total = num;
	}
	
	private int getTotal() {
		return this.total;
	}
	
	private int[] table;
	
	private void setTable() {
		this.table = new int[20];
	}
	
	private int getTable(int index) {
		return this.table[index];
	}
		
	private void doDisplay(boolean display) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("TOP ");
		builder.append("\t");
		builder.append("FOUND");
		builder.append("\t");
		builder.append("TOTAL");
		builder.append("\t");
		builder.append("RATE");
		builder.append("\n");
		if (display) {
			for (int i = 0; i < 20; i++) {
				builder.append((i + 1) * 5);
				builder.append("\t");
				builder.append(this.getTable(i));
				builder.append("\t");
				builder.append(this.getTotal());
				builder.append("\t");
				builder.append((this.getTable(i) * 100) / this.getTotal());
				builder.append("%\n");
			}
		}
		Date date = new Date(); 
		DateFormat format = new SimpleDateFormat("y-M-d-H-m-s",Locale.ENGLISH);// DateFormat.getDateTimeInstance();
		String timestamp = format.format(date);
		File home = new File(System.getProperty("user.home"));
		FileOutputStream outStream = new FileOutputStream(home + File.separator + "alignment-" + timestamp + ".top");
		outStream.write(builder.toString().getBytes());
	}
	
	private int positionOf(String term,Map<String, Double> occurrences) {
		int index = 0;
		for (String key : occurrences.keySet()) {
			index++;
			if (key.equals(term)) {
				return index;
			} 
		}
		return -1;
	}
	
	private int positionOf(Set<String> terms,Map<String, Double> occurrences) {
		int position = -1;
		for (String term : terms) {
			int pos = this.positionOf(term, occurrences);
			if (position < 0) {
				position = pos;
			} else if (pos > 0 && pos < position) {
				position = pos;
			}
		}
		return position;
	}
	
	@Override
	public void doCheck(String indexID, Map<String, Set<String>> translations) throws IOException {
		if (!this.hasBeenChecked()) {
			this.enableChecked(true);
			UIMAFramework.getLogger().log(Level.CONFIG,"Checking Term Context Index '" + indexID + "'");
			TermContextIndex index = this.getIndex(indexID);
			boolean display = false;
			for (String term : translations.keySet()) {
				display = true;
				// UIMAFramework.getLogger().log(Level.INFO,"Checking  '" + term + "'");
				Set<String> references = translations.get(term);
				TermContext context = index.getTermContexts().get(term);
				if (context != null) {
					total++;
					int position = this.positionOf(references, context.getSortedCoOccurrences());
					if (position > 100) {
						UIMAFramework.getLogger().log(Level.WARNING,"Skiping  '" + term + "' at " + position + " > 100");
					} else {
						// UIMAFramework.getLogger().log(Level.INFO,"Scoring  '" + term + "' at " + position);
						for (int i = 0; i < 20; i++) {
							if (position != -1 && position <= (i + 1) * 5) {
								table[i] = table[i] + 1;
								for (int j = i + 1; j < 20; j++) {
									table[j] = table[j] + 1;	
								}
								break;
							}
						}
					}
				}
			}
			this.doDisplay(display);
		}
	}
		
	private String getName(String indexID) {
		StringBuilder string = new StringBuilder();
		char[] characters = indexID.toCharArray();
		int length = characters.length;
		for (int index = 0; index < length; index++) {
			char character = characters[index];
			if (Character.isUpperCase(character)) {
				if (index != 0) {
					string.append('-');
				}
				string.append(Character.toLowerCase(character));
			} else {
				string.append(character);
			}
		}
		return string.toString();
	}
	
	/************************************** X) Filtering **********************************/
	
	private Set<String> filtered;
	
	private void setFiltered() {
		this.filtered = new HashSet<String>();
	}
	
	private void enabledFiltered(String indexID) {
		this.filtered.add(indexID);
	}
	
	private boolean hasBeenFiltered(String indexID) {
		return this.filtered.contains(indexID);
	}
	
	@Override
	public void doFilter(String indexID,Set<String> terms) {
		TermContextIndex index = this.getIndex(indexID);
		if (!this.hasBeenFiltered(indexID)) {
			this.enabledFiltered(indexID);
			if (!terms.isEmpty()) {
				UIMAFramework.getLogger().log(Level.INFO,"Filtering " + " '" + indexID + "'");
				index.doFilter(terms);
			}
		}
	}
	
	private Set<String> shrinked;
	
	private void setShrinked() {
		this.shrinked = new HashSet<String>();
	}
	
	private void enabledShrinked(String indexID) {
		this.shrinked.add(indexID);
	}
	
	private boolean hasBeenShrinked(String indexID) {
		return this.shrinked.contains(indexID);
	}
	
	@Override
	public void doShrink(String indexID,Set<String> terms) {
		TermContextIndex index = this.getIndex(indexID);
		if (!this.hasBeenShrinked(indexID)) {
			this.enabledShrinked(indexID);
			UIMAFramework.getLogger().log(Level.INFO,"Shrinking " + " '" + indexID);
			index.doShrink(terms);
		}
	}
	
	/************************************** 2) Indexing **********************************/
	
	private Map<String,TermContextIndex> indexes;
	
	private void setIndexes() {
		this.indexes = new HashMap<String, TermContextIndex>();
	}
	
	@Override
	public void addIndex(String indexID) {
		TermContextIndex index = this.indexes.get(indexID);
		if (index == null) {
			this.indexes.put(indexID, new TermContextIndex());
			UIMAFramework.getLogger().log(Level.CONFIG,"Adding Term Context Index '" + indexID  + "'");
		}
	}
	
	@Override
	public void delIndex(String indexID) {
		TermContextIndex index = this.indexes.remove(indexID);
		if (index != null) {
			UIMAFramework.getLogger().log(Level.CONFIG,"Removing Term Context Index '" + indexID  + "'");
		}
	}

	@Override
	public TermContextIndex getIndex(String anIndexID) {
		return this.indexes.get(anIndexID);
	}
	
	
	public TermContextSpaceResource() {
		super();
		this.setIndexes();
		this.setFiltered();
		this.setShrinked();
		this.setNormalized();
		this.enableTransfered(false);
		this.enableAligned(false);
		this.enableSpecified(false);
		this.enableDefined(false);
		this.enableChecked(false);
		this.setTotal(0);
		this.setTable();
		UIMAFramework.getLogger().log(Level.CONFIG,"Building Term Context Space Resource " + this);
	}
	
	/************************************** 3) ALIGNEMENT **********************************/
	
	private boolean defined;
	
	private void enableDefined(boolean enabled) {
		this.defined = enabled;
	}
	
	private boolean isDefined() {
		return this.defined;
	}
	
	private SimilarityDistance similarityDistance;
	
	public void setSimilarityDistance(String name) throws Exception {
		if (!this.isDefined()) {
			this.enableDefined(true);
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof SimilarityDistance) {
				this.similarityDistance = (SimilarityDistance) obj;
				UIMAFramework.getLogger().log(Level.CONFIG,"Setting Similarity Distance: " + this.similarityDistance.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + SimilarityDistance.class.getCanonicalName());
			}
		}
	}
	
	private SimilarityDistance getSimilarityDistance() {
		return this.similarityDistance;
	}
	
	private boolean aligned;
	
	private void enableAligned(boolean enabled) {
		this.aligned = enabled;
	}
	
	private boolean hasBeenAligned() {
		return this.aligned;
	}
	
	@Override
	public void doAlign(String sourceID,String targetID,String resultID) {
		if (!this.hasBeenAligned()) {
			this.enableAligned(true);
			UIMAFramework.getLogger().log(Level.INFO,"Aligning " + " '" + sourceID + "' against '" + targetID + "' to '" + resultID + "'");
			TermContextIndex sourceIndex = this.getIndex(sourceID);
			TermContextIndex targetIndex = this.getIndex(targetID);
			TermContextIndex resultIndex = this.getIndex(resultID);
			for (String sourceTerm : sourceIndex.getTermContexts().keySet()) {
				UIMAFramework.getLogger().log(Level.INFO,"Translating " + " '" + sourceTerm + "'");
				resultIndex.setOccurrences(sourceTerm, sourceIndex.getOccurrences().get(sourceTerm));
				TermContext sourceContext = sourceIndex.getTermContexts().get(sourceTerm);
				for (String targetTerm : targetIndex.getTermContexts().keySet()) {
					TermContext targetContext = targetIndex.getTermContexts().get(targetTerm);
					double score = this.getSimilarityDistance().getValue(sourceContext.getCoOccurrences(),targetContext.getCoOccurrences());
					if (!Double.isInfinite(score) && !Double.isNaN(score)) {						
						resultIndex.setCoOccurrences(sourceTerm,targetTerm,new Double(score), TermContext.DEL_MODE);
					}
				}
			}
			try {
				File home = new File(System.getProperty("user.home"));
				FileOutputStream outStream = new FileOutputStream(home + File.separator + this.getName(sourceID) + "-aligned.index");
				resultIndex.doStore(outStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.delIndex(sourceID);
		}
	}
	
	/************************************** 2) Transfering **********************************/
		
	private boolean transfered;
	
	private void enableTransfered(boolean enabled) {
		this.transfered = enabled;
	}
	
	private boolean hasBeenTransfered() {
		return this.transfered;
	}
	
	@Override
	public void doTransfer(String sourceID,String targetID,String resultID,Map<String, Set<String>> translations) {
		if (!this.hasBeenTransfered()) {
			this.enableTransfered(true);
			UIMAFramework.getLogger().log(Level.INFO,"Transfering Term Context Index '" + sourceID + "' to '" + resultID + "'");
			TermContextIndex sourceIndex = this.getIndex(sourceID);
			TermContextIndex targetIndex = this.getIndex(targetID);
			TermContextIndex resultIndex = this.getIndex(resultID);
			resultIndex.setLanguage(sourceIndex.getLanguage());
			for (String sourceTerm : sourceIndex.getTermContexts().keySet()) {
				Integer sourceOcc = sourceIndex.getOccurrences().get(sourceTerm);
				resultIndex.setOccurrences(sourceTerm, sourceOcc);
				TermContext sourceContext = sourceIndex.getTermContexts().get(sourceTerm);
				for (String sourceCoTerm : sourceContext.getCoOccurrences().keySet()) {
					Double sourceCoOcc = sourceContext.getCoOccurrences().get(sourceCoTerm);
					Set<String> resultTerms = translations.get(sourceCoTerm);
					if (resultTerms == null) { 
						// TODO
					} else {
						int totalOcc = 0;
						for (String resultTerm : resultTerms) {
							Integer occ = targetIndex.getOccurrences().get(resultTerm);
							totalOcc += occ == null ? 0 : occ.intValue();
						}
						for (String resultTerm : resultTerms) {
							Integer resultOcc = targetIndex.getOccurrences().get(resultTerm);
							int candidateOcc = resultOcc == null ? 0 : resultOcc.intValue(); 
							double score = totalOcc == 0.0 ? 0.0 : (sourceCoOcc * candidateOcc) / totalOcc;
							if (score != 0.0) {				
								resultIndex.setCoOccurrences(sourceTerm, resultTerm, score, TermContext.MAX_MODE);
							}
						}
					}
				}
			}
			try {
				File home = new File(System.getProperty("user.home"));
				FileOutputStream outStream = new FileOutputStream(home + File.separator + this.getName(sourceID) + "-transfered.index");
				resultIndex.doStore(outStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.delIndex(sourceID);
		}
	}
	
	/************************************** X) NORMALIZE **********************************/
	
	private boolean specified;
	
	private void enableSpecified(boolean enabled) {
		this.specified = enabled;
	}
	
	private boolean isSpecified() {
		return this.specified;
	}
	
	private AssociationRate associationRate;
	
	@Override
	public void setAssociationRate(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (!this.isSpecified()) {
			this.enableSpecified(true);
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof AssociationRate) {
				this.associationRate = (AssociationRate) obj;
				UIMAFramework.getLogger().log(Level.CONFIG,"Setting Association Rate: " + this.associationRate.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + AssociationRate.class.getCanonicalName());
			}
		}
	}
	
	private AssociationRate getAssociationRate() {
		return this.associationRate;
	}
	
	private Set<String> normalized;
	
	private void setNormalized() {
		this.normalized = new HashSet<String>();
	}
	
	private void enableNormalized(String indexID,String resultID) {
		this.normalized.add(indexID + "-" + resultID);
	}
	
	private boolean hasBeenNormalized(String indexID,String resultID) {
		return this.normalized.contains(indexID + "-" + resultID);
	}
	
	@Override
	public void doNormalize(String indexID,String resultID) throws Exception {
		if (!this.hasBeenNormalized(indexID,resultID)) {
			this.enableNormalized(indexID,resultID);
			UIMAFramework.getLogger().log(Level.INFO,"Normalizing Term Context Index '" + indexID + "'");
			TermContextIndex index = this.getIndex(indexID);
			TermContextIndex resultIndex = this.getIndex(resultID);
			resultIndex.setLanguage(index.getLanguage());
			CrossTable crossTable = new CrossTable();
			crossTable.setIndex(index);
			for (String term : index.getTermContexts().keySet()) {
				Integer occ = index.getOccurrences().get(term);
				resultIndex.setOccurrences(term, new Integer(occ));
				TermContext context = index.getTermContexts().get(term);
				crossTable.setTerm(term);
				for (String coTerm : context.getCoOccurrences().keySet()) {
					try {
						crossTable.setCoTerm(coTerm);	
						crossTable.compute();
						int a = crossTable.getA();
						int b = crossTable.getB();
						int c = crossTable.getC();
						int d = crossTable.getD();
						double norm = this.getAssociationRate().getValue(a, b, c, d);
						resultIndex.setCoOccurrences(term, coTerm, new Double(norm), TermContext.DEL_MODE);
					} catch (Exception e) {
						UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
					}
				}
			}
			try {
				File home = new File(System.getProperty("user.home"));
				FileOutputStream outStream = new FileOutputStream(home + File.separator + this.getName(indexID) + "-normalized.index");
				resultIndex.doStore(outStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.delIndex(indexID);
		}
	}	

	/************************************** X) I/O **********************************/

	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void doCompare(String sourceID, String targetID) {
		TermContextIndex sourceIndex = this.getIndex(sourceID);
		TermContextIndex targetIndex = this.getIndex(targetID);
		int diff = sourceIndex.compareTo(targetIndex);
		String message = "Term Context Indexes '" + sourceID + "' and '" + targetID + "' ";
		if (diff == 0) {
			diff = targetIndex.compareTo(sourceIndex);
			if (diff == 0) {
				UIMAFramework.getLogger().log(Level.INFO,message + "are equal");
			} else {
				UIMAFramework.getLogger().log(Level.INFO,message + "aren't equal");	
			}
		} else {
			UIMAFramework.getLogger().log(Level.INFO,message + "aren't equal");
		}
	}

}
