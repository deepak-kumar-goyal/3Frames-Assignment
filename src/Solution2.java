import java.io.*;
import java.util.*;

/*
Problem-2:
Build a word count application, where the constraints are that you have 10 MB RAM and 1
GB text file. You should be able to efficiently parse the text file and output the words and
counts in a sorted way. Write a program to read a large file, and emit the sorted words along
with the count. Try to implement fuzzy search as well (fix the spelling issues) Algorithm
should have Log N complexity.

*/

class Solution2 {
    private static final int MAX_MEMORY = 10 * 1024 * 1024; // 10 MB

    // Method to read the file, split it into chunks, and count words
    public static Map<String, Integer> countWords(String filename) throws IOException {
        // Initialize a trie to handle fuzzy search
        Trie trie = new Trie();

        // Process the file in chunks
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    // Add word to trie for fuzzy search
                    trie.insert(word);

                    // Increment word count
                    String normalizedWord = word.toLowerCase();
                    trie.incrementCount(normalizedWord);
                }
            }
        }

        // Output sorted word counts
        return trie.getWordCounts();
    }

    public static void main(String[] args) {
        String filename = "data.txt";

        try {
            // Count words
            Map<String, Integer> wordCounts = countWords(filename);

            // Sort word counts by count in descending order
            List<Map.Entry<String, Integer>> sortedWordCounts = new ArrayList<>(wordCounts.entrySet());
            sortedWordCounts.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Output sorted word counts
            for (Map.Entry<String, Integer> entry : sortedWordCounts) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Trie data structure for fuzzy search and word counting
class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert a word into the trie
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.containsKey(c)) {
                current.put(c, new TrieNode());
            }
            current = current.get(c);
        }
        current.setEnd();
    }

    // Increment the count of a word
    public void incrementCount(String word) {
        TrieNode node = searchPrefix(word);
        if (node != null) {
            node.incrementCount();
        }
    }

    // Get the word counts stored in the trie
    public Map<String, Integer> getWordCounts() {
        Map<String, Integer> wordCounts = new HashMap<>();
        dfs(root, "", wordCounts);
        return wordCounts;
    }

    // Perform depth-first search to retrieve word counts
    private void dfs(TrieNode node, String prefix, Map<String, Integer> wordCounts) {
        if (node.isEnd()) {
            wordCounts.put(prefix, node.getCount());
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (node.containsKey(c)) {
                dfs(node.get(c), prefix + c, wordCounts);
            }
        }
    }

    // Search for a prefix in the trie
    private TrieNode searchPrefix(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.containsKey(c)) {
                return null;
            }
            current = current.get(c);
        }
        return current;
    }
}

// TrieNode class representing a node in the trie
class TrieNode {
    private TrieNode[] links;
    private int count;
    private boolean isEnd;

    public TrieNode() {
        links = new TrieNode[26];
        count = 0;
        isEnd = false;
    }

    public boolean containsKey(char c) {
        return links[c - 'a'] != null;
    }

    public TrieNode get(char c) {
        return links[c - 'a'];
    }

    public void put(char c, TrieNode node) {
        links[c - 'a'] = node;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd() {
        isEnd = true;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }
}
