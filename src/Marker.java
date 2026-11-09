import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m]; // pi[0] = 0: one letter has no proper border
        for (int i = 1; i < m; i++) {
            int k = pi[i - 1]; // start from the border of the previous prefix
            // Next letter does not extend the border -> fall back to the next shorter border
            while (k > 0 && p.charAt(k) != p.charAt(i)) {
                k = pi[k - 1];
            }
            // Next letter matches -> the border grows by one
            if (p.charAt(k) == p.charAt(i)) k++; // border grows by one letter
            pi[i] = k;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        int n = text.length(), m = p.length();
        int[] pi = prefixFunction(p); // pi[0] = 0: one letter has no proper border
        List<Integer> hits = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < n; i++) {
            // Mismatch: keep the matched part that is still useful (a border), do not go back in text
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            // Match: one more letter of p is matched
            if (text.charAt(i) == p.charAt(k)) k++;
            // Whole marker matched: save the start position
            if (k == m) {
                hits.add(i - m + 1);
                k = pi[m - 1]; // not 0: the next hit may overlap this one
            }
        }
        // List<Integer> -> int[]
        int[] res = new int[hits.size()];
        for (int t = 0; t < res.length; t++) res[t] = hits.get(t);
        return res;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        // TODO Mission 4
        return -1;
    }
}
