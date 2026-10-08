package com.liverpool.appsales.exam.search.application;

import org.springframework.stereotype.Component;

@Component
public class FuzzyMatcher {

    public boolean matches(String value, String query) {

        if (value == null || query == null) {
            return false;
        }

        if (value.contains(query)) {
            return true;
        }

        String[] valueTokens = value.split(" ");
        String[] queryTokens = query.split(" ");

        for (String queryToken : queryTokens) {
            for (String valueToken : valueTokens) {
                int distance = levenshtein(valueToken, queryToken);

                int threshold = Math.max(1, queryToken.length() / 5 );

                if (distance <= threshold) {
                    return true;
                }
            }
        }

        return false;
    }

    private int levenshtein(String first, String second) {

        int[][] matrix = new int[first.length() + 1][second.length() + 1];

        for (int i = 0; i <= first.length(); i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j <= second.length(); j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                int cost = first.charAt(i - 1) == second.charAt(j - 1)
                                ? 0
                                : 1;

                matrix[i][j] = Math.min(
                        Math.min(
                                matrix[i - 1][j] + 1,
                                matrix[i][j - 1] + 1
                        ),
                        matrix[i - 1][j - 1] + cost
                );
            }
        }

        return matrix[first.length()][second.length()];
    }
}