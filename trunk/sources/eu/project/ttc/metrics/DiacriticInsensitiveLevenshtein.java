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
package eu.project.ttc.metrics;

import java.text.Collator;
import java.util.Locale;

/**
 * The {@link Levenshtein} {@link EditDistance} insensitive to diacritics, i.e.
 * pairs of words such as <code>café</code> and <code>cafe</code>,
 * <code>joão</code> and <code>joao</code> will be considered to have a
 * <code>0</code> edit distance or <code>1</code> similarity.
 * 
 * @author Sebastián Peña Saldarriaga
 */
public class DiacriticInsensitiveLevenshtein implements EditDistance {

	/**
	 * Normalizes the specified <code>distance</code> by
	 * <code>max(|str|, |rst|)</code>. For historical reasons this method
	 * actually returns 1 - normalized distance, making a similarity.
	 * 
	 * @param distance
	 *            The edit distance between <code>str</code> and
	 *            <code>rst</code>.
	 * @param str
	 *            A string
	 * @param rst
	 *            Another string
	 * @return A [1, 0] value determined by
	 *         <code>1 - distance/max(|str|, |rst|)</code>.
	 */
	@Override
	public double normalize(int distance, String str, String rst) {
		return 1.0 - ((double) distance / Math.max(str.length(), rst.length()));
	}

	@Override
	public int compute(String str, String rst) {
		int[][] dp = new int[str.length() + 1][rst.length() + 1];
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[i].length; j++) {
				dp[i][j] = i == 0 ? j : j == 0 ? i : 0;
				if (i > 0 && j > 0) {
					if (diacriticInsensitiveEquals(str.charAt(i - 1),
							rst.charAt(j - 1))) {
						dp[i][j] = dp[i - 1][j - 1];
					} else {
						dp[i][j] = Math.min(dp[i][j - 1] + 1, Math.min(
								dp[i - 1][j - 1] + 1, dp[i - 1][j] + 1));
					}
				}
			}
		}
		return dp[str.length()][rst.length()];
	}

	/** Locale sensitive string comparator */
	private static final Collator StrCollator;

	static {
		// Might be modified depending on the language
		StrCollator = Collator.getInstance(Locale.getDefault());
		StrCollator.setStrength(Collator.PRIMARY);
	}

	/**
	 * Determines whether <code>char1</code> and <code>char2</code> are equals
	 * independent of the presence of diacritic marks.
	 * 
	 * @param char1
	 *            The first char
	 * @param char2
	 *            The second char
	 * @return <code>true</code> if <code>char1</code> and <code>char2</code>
	 *         are equals, or <code>false</code> otherwise.
	 */
	public static boolean diacriticInsensitiveEquals(char char1, char char2) {
		return StrCollator.equals(Character.toString(char1),
				Character.toString(char2));
	}

}
