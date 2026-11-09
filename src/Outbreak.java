import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */

    static long[] yes, no;

    // fills yes[v] and no[v] for the subtree of v (children first, then v)
    static void dfs(int v, int parent, int[][] nb, int[] cost) {
        yes[v] = cost[v];
        no[v] = 0;
        for (int c : nb[v]) {
            if (c == parent) continue;
            dfs(c, v, nb, cost);
            yes[v] += Math.min(yes[c], no[c]);
            no[v] += yes[c];
        }
    }

    // walks down from the root and decides who is tested
    static void rebuild(int v, int parent, boolean vTested, int[][] nb, boolean[] tested) {
        tested[v] = vTested;
        for (int c : nb[v]) {
            if (c == parent) continue;
            boolean cTested;
            if (vTested) cTested = yes[c] < no[c];
            else cTested = true;
            rebuild(c, v, cTested, nb, tested);
        }
    }

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        yes = new long[n];
        no = new long[n];
        dfs(0, -1, nb, cost);
        boolean[] tested = new boolean[n];
        rebuild(0, -1, yes[0] <= no[0], nb, tested);
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    static int best;

    static int down(int v, int parent, int[][] nb) {
        int top1 = 0, top2 = 0;
        for (int c : nb[v]) {
            if (c == parent) continue;
            int d = down(c, v, nb) + 1;
            if (d > top1) { top2 = top1; top1 = d; }
            else if (d > top2) { top2 = d; }
        }
        best = Math.max(best, top1 + top2);
        return top1;
    }

    public static int longestChain(int n, int[][] nb) {
        best = 0;
        down(0, -1, nb);
        return best;
    }
}