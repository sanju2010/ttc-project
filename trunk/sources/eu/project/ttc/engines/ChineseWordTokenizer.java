package eu.project.ttc.engines;

import fr.univnantes.lina.uima.engines.WordTokenizer;

public class ChineseWordTokenizer extends WordTokenizer {

	@Override
	protected boolean hasChanged(String text,int index) {
		if (index == 0) {
			return false;
		} else {
			char previous = text.charAt(index - 1);
			char current = text.charAt(index);
			if (Character.isDigit(previous) && Character.isDigit(current)) {
				return false;
			} else if (Character.isWhitespace(previous) && Character.isWhitespace(current)) {
				return false;
			} else {
				return true;
			} 
		}
	}
	
}
