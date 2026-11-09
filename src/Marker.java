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

    // Polynomial hash: a window is read as a number in base BASE, modulo a big prime MOD
    static final long BASE = 31;
    static final long MOD = 1_000_000_007L;   // prime -> fewer collisions

    /**
     * Rolling hash: h[i] = hash of s[i..i+k-1] for every window of length k.
     * The first window is computed in O(k), every next one in O(1).
     */
    static long[] windowHashes(String s, int k) {
        int n = s.length();
        if (n < k) return new long[0];          // no window fits
        long[] h = new long[n - k + 1];

        // pow = BASE^(k-1): the "weight" of the first letter of a window
        long pow = 1;
        for (int t = 0; t < k - 1; t++) pow = pow * BASE % MOD;

        // Hash of the first window: add letters one by one (like 1 -> 12 -> 123)
        long cur = 0;
        for (int t = 0; t < k; t++) {
            cur = (cur * BASE + s.charAt(t)) % MOD;
        }
        h[0] = cur;

        // Slide the window one letter to the right in O(1)
        for (int i = 1; i <= n - k; i++) {
            cur = (cur - s.charAt(i - 1) * pow % MOD + MOD) % MOD;  // remove the old first letter (+MOD: never negative)
            cur = cur * BASE % MOD;                                  // shift left
            cur = (cur + s.charAt(i + k - 1)) % MOD;                 // add the new last letter
            h[i] = cur;
        }
        return h;
    }

    /**
     * Rabin-Karp: count positions i in a whose k-letter word also occurs in b.
     */
    public static int sharedWords(String a, String b, int k) {
        long[] ha = windowHashes(a, k);
        long[] hb = windowHashes(b, k);

        // Remember every window of b: hash -> list of start positions with this hash
        Map<Long, List<Integer>> map = new HashMap<>();
        for (int j = 0; j < hb.length; j++) {
            map.computeIfAbsent(hb[j], x -> new ArrayList<>()).add(j);
        }

        int count = 0;
        for (int i = 0; i < ha.length; i++) {
            List<Integer> list = map.get(ha[i]);
            if (list == null) continue;          // hash not in b -> this word is surely not in b
            for (int j : list) {
                // Equal hashes do not prove equal words (collision) -> compare the letters
                boolean same = true;
                for (int t = 0; t < k; t++) {
                    if (a.charAt(i + t) != b.charAt(j + t)) {
                        same = false;
                        break;
                    }
                }
                if (same) {
                    count++;
                    break;
                }    // count position i once, even if the word is in b many times
            }
        }
        return count;
    }
}
