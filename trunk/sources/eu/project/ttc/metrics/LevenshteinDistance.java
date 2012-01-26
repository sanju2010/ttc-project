package eu.project.ttc.metrics;

public class LevenshteinDistance implements EditDistance {
	
	@Override 
	public int compute(String s1, String s2) {
		int[][] dp = new int[s1.length() + 1][s2.length() + 1];

		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[i].length; j++) {

				dp[i][j] = i == 0 ? j : j == 0 ? i : 0;

				if (i > 0 && j > 0) {

					if (s1.charAt(i - 1) == s2.charAt(j - 1))
						dp[i][j] = dp[i - 1][j - 1];

					else
						dp[i][j] = Math.min(dp[i][j - 1] + 1, Math.min(
								dp[i - 1][j - 1] + 1, dp[i - 1][j] + 1));

				}
			}
		}
		return dp[s1.length()][s2.length()];
	}

}