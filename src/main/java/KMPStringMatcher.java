import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KMPStringMatcher {

    // Compute failure function for KMP algorithm
    private static int[] computeFailureFunction(String pattern) {
        int m = pattern.length();
        int[] failure = new int[m];

        failure[0] = 0; // First element is always 0
        int j = 0; // Length of previous longest prefix suffix

        for (int i = 1; i < m; i++) {
            // Backtrack using failure function if characters don't match
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = failure[j - 1];
            }

            // Extend prefix if characters match
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
                failure[i] = j;
            } else {
                failure[i] = 0;
            }
        }

        return failure;
    }

    // Find all occurrences of pattern in text using KMP algorithm
    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();

        // Check for invalid inputs
        if (text == null || pattern == null || pattern.isEmpty()) {
            return occurrences;
        }

        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return occurrences; // Pattern longer than text
        }

        // Precompute failure function
        int[] failure = computeFailureFunction(pattern);

        int i = 0; // Text index
        int j = 0; // Pattern index

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            // Pattern found
            if (j == m) {
                occurrences.add(i - j);
                j = failure[j - 1];
            }
            // Mismatch after j matches
            else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = failure[j - 1]; // Use failure function
                } else {
                    i++; // Move to next character in text
                }
            }
        }

        return occurrences;
    }

    // Read test cases from input file
    private static List<TestCase> readTestCases(String filename) {
        List<TestCase> testCases = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                // Parse: text|pattern|expected_indices
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String text = parts[0];
                    String pattern = parts[1];
                    String expectedStr = parts.length > 2 ? parts[2] : "[]";

                    testCases.add(new TestCase(text, pattern, expectedStr));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }

        return testCases;
    }

    // Display search results for a test case
    public static void searchAndDisplay(String text, String pattern, int testNumber) {
        System.out.println("TEST " + testNumber + ":");
        System.out.println("Text: \"" + text + "\"");
        System.out.println("Pattern: \"" + pattern + "\"");

        long startTime = System.nanoTime();
        List<Integer> occurrences = kmpSearch(text, pattern);
        long endTime = System.nanoTime();

        System.out.println("Occurrences: " + occurrences);
        System.out.println("Count: " + occurrences.size());
        System.out.println("Execution time: " + (endTime - startTime) + " nanoseconds");
        System.out.println();
    }

    // Test case container class
    static class TestCase {
        String text;
        String pattern;
        String expected;

        TestCase(String text, String pattern, String expected) {
            this.text = text;
            this.pattern = pattern;
            this.expected = expected;
        }
    }

    public static void main(String[] args) {
        System.out.println("KMP String Matching Algorithm - Test Results");
        System.out.println("=============================================\n");

        // Read test cases from file
        List<TestCase> testCases = readTestCases("data/input.txt");

        if (testCases.isEmpty()) {
            System.out.println("No test cases found in data/input.txt");
            return;
        }

        // Execute each test case
        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);
            searchAndDisplay(testCase.text, testCase.pattern, i + 1);
        }

        // Complexity analysis
        System.out.println("COMPLEXITY ANALYSIS:");
        System.out.println("Time Complexity: O(n + m)");
        System.out.println("Space Complexity: O(m)");
    }
}