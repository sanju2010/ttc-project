package eu.project.ttc.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

public class RootBankResource implements RootBank {
	
	private class HiddentRoot implements Root {
		
		private String origin;
		
		public void setOrigin(String origin) {
			this.origin = origin;
		}

		@Override
		public String getOrigin() {
			return this.origin;
		}

		private String root;
		
		public void setRoot(String root) {
			this.root = root;
		}

		@Override
		public String getRoot() {
			return this.root;
		}
		
		public String toString() {
			return "[" + this.getOrigin() + ":" + this.getRoot() + "]";
		}
		
	}
	
	private class Tree implements RootTree {

		private Map<Character,Tree> childs;
		
		private void setChilds() {
			this.childs = new HashMap<Character,Tree>();
		}
		
		public Map<Character,Tree> getChilds() {
			return this.childs;
		}
		
		@Override
		public Tree getChild(Character c) {
			return this.getChilds().get(c);
		}

		private HiddentRoot root;
		
		public void setRoot(HiddentRoot root) {
			this.root = root;
		}
		
		@Override
		public HiddentRoot getRoot() {
			return this.root;
		}
		
		public Tree() {
			this.setChilds();
		}
		
		public String toString() {
			return this.toString(0);
		}

		private String toString(int level) {
			String string = "";
			for (Character c : this.getChilds().keySet()) {
				for (int index = 0; index < level; index++) {
					string += "  ";
				}
				string += "'" + c.toString() + "'" + "\n";
				string += this.getChilds().get(c).toString(level + 1);
			}
			if (this.getRoot() != null) {
				for (int index = 0; index < level; index++) {
					string += "  ";
				}
				string += " * " + this.getRoot().toString() + "\n";
			}
			return string + "\n";
		}

		
	}
	
	public RootBankResource() {
		this.setPrefixTree();
		this.setSuffixTree();
		this.setDelimiter();
		this.setSplitter();
	}
	
	private Tree prefixTree;
	
	private void setPrefixTree() {
		this.prefixTree = new Tree();
	}
	
	@Override
	public Tree getPrefixTree() {
		return this.prefixTree;
	}
	
	private Tree suffixTree;
	
	private void setSuffixTree() {
		this.suffixTree = new Tree();
	}
	
	@Override
	public Tree getSuffixTree() {
		return this.suffixTree;
	}
	
	private void addRoot(Tree tree,String word,HiddentRoot root) {
		Tree current = tree;
		int length = word.length() - 1;
		for (int index = 0; index <= length; index++) {
			char c = word.charAt(index);
			Tree child = current.getChild(c);
			if (child == null) {
				Tree t = new Tree();
				current.getChilds().put(c,t);
				current = t;
			} else {
				current = child;
			}
			if (index == length) {
				current.setRoot(root);
			}
		}
	}
	
	private HiddentRoot getClassicalRoot(String element) throws IOException {
		String elem = element.substring(1,element.length() -1);
		String[] elements = elem.split(":");
		if (elements.length == 2) {
			HiddentRoot root = new HiddentRoot();
			root.setOrigin(elements[0]);
			root.setRoot(elements[1]);
			return root;
		} else {
			throw new IOException("Wrong classical root format: " + element);
		}
	}
	
	private String delimiter;
	
	private void setDelimiter() {
		this.delimiter = java.lang.System.getProperty("line.separator");
	}

	private String getDelimiter() {
		return this.delimiter;
	}
	
	private String last;
	
	public void doLoad(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		if (this.last == null) {
			this.last = file.getAbsolutePath();
		} else if (file.getAbsoluteFile().equals(this.last)) {
			return;
		}
		this.doLoad(inputStream);
		/*
		System.out.println("Prefixes:\n");
		System.out.println(this.getPrefixTree().toString());
		System.out.println("Suffixes:\n");
		System.out.println(this.getSuffixTree().toString());
		*/
	}
	

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		try {
			this.doLoad(aData.getInputStream());
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

	private void doLoad(InputStream inputStream) throws IOException {
		Scanner scanner = new Scanner(inputStream);
		scanner.useDelimiter(this.getDelimiter());
		// scanner.skip("#(.)*");
		while (scanner.hasNext()) {
			this.doParse(scanner.next());
		}
		scanner.close();
	}
	
	private String splitter;
	
	private void setSplitter() {
		this.splitter = "\t";
	}

	private String getSplitter() {
		return this.splitter;
	}
	
    private void doParse(String line) throws IOException {
    	String[] items = line.split(this.getSplitter());
    	if (items.length >= 2) {
    		HiddentRoot root = this.getClassicalRoot(items[0]);
    		for (int index = 1; index < items.length; index++) {
    			String form = items[index];
    			int length = form.length();
    			if (length >= 2) {
    				int first = form.indexOf("-");
    				int last = form.lastIndexOf("-");
    				if (first == last) {
    					if (first == 0) {
    						String suffix = form.substring(1);
    						String reverse = new StringBuffer(suffix).reverse().toString();
    						this.addRoot(this.getSuffixTree(),reverse,root);
    						// this.addEntry(suffix,root.toString());
    					} else if (last == length - 1) {
    						String prefix = form.substring(0,last);
    						this.addRoot(this.getPrefixTree(),prefix,root);
    						// this.addEntry(prefix,root.toString());
    					}
    				} else {
    					throw new IOException("Wrong classical root resource format at: " + line);
    				}
    			} else {
    				throw new IOException("Wrong classical root resource format at: " + line);
    			}
    		}
    	} else {
    		throw new IOException("Wrong classical root resource format at: " + line);
    	}
    }


}
