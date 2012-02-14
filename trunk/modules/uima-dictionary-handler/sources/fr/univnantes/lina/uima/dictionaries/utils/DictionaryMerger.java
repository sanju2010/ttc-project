package fr.univnantes.lina.uima.dictionaries.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import fr.univnantes.lina.uima.dictionaries.DictionaryResource;

public class DictionaryMerger {

	public static void main(String[] arguments) throws Exception {
		DictionaryMerger merger = new DictionaryMerger();
		String out = null;
		String from = null;
		String to = null;
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].equals("--from")) {
				from = arguments[i+1];
				i++;
			} else if (arguments[i].equals("--to")) {
				to = arguments[i+1];
				i++;
			} else if (arguments[i].equals("--out")) {
				out = arguments[i+1];
				i++;
			} else {
				throw new Exception(arguments[i]);
			}
		}
		merger.merge(from, to, out);
	}
	
	private DictionaryResource from;
	
	private DictionaryResource to;
	
	private DictionaryResource out;
	
	private void merge(String from, String to, String out) throws Exception {
		this.from = new DictionaryResource();
		System.out.println("Loading " + from);
		this.from.doLoad(URI.create("file:" + from));
		System.out.println("Loaded " + from);
		this.to = new DictionaryResource();
		System.out.println("Loading " + to);
		this.to.doLoad(URI.create("file:" + to));
		System.out.println("Loaded " + to);
		String src = this.from.source();
		String tgt = this.to.target();
		System.out.println("Merging from " + src + " to " + tgt);
		this.out = new DictionaryResource();
		for (String source : this.from.map().keySet()) {
			for (String trans : this.from.map().get(source)) {
				Set<String> targets = this.to.map().get(trans);
				if (targets == null) {
					continue;
				} else {
					for (String target : targets) {
						this.out.add(src, tgt, source, target);						
					}
				}
			}
		}
		OutputStream stream = new FileOutputStream(out);
		try {
			for (String key : this.out.map().keySet()) {
				for (String value : this.out.map().get(key)) {
					String line = key + "::unkown::" + this.out.source() + "-" + this.out.target() + "::" + value + "::unknown\n";
					stream.write(line.getBytes());
				}
			}
			System.out.println("Merged " + src + "-" + tgt + " with " + this.out.map().size() + " entries");
		} finally {
			stream.close();
		}
	}
	
}
