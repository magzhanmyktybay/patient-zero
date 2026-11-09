# Assignment 4. Patient Zero

Student ID: 251628
Name: Magzhan Myktybay
Group: SE-2540

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 82
    Shared core: 895
    Marker hits: 6
    First hit: 3601
    Border length: 4
    Shared words: 317
    Tests needed: 30
    Cheapest testing: 128
    Longest chain: 25

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?
d[i][j] - the minimum number of edits for the first i and j characters; the minimum of ↖ (substitution or match), ↑ (deletion), and ← (insertion).
Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?
A subsequence can skip characters, whereas a substring cannot; LCS accounts for shifts that occur following insertions and deletions.
Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?
pi[i] represents the longest border; in the event of a mismatch, we backtrack using the pi array rather than moving backward through the text itself. The border length of your marker corresponds to the value in the output; this means that the first $n$ characters of the marker (where $n$ is that value) are identical to its last $n$ characters.
Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?
Different words can have the same hash (a collision); when there are many collisions, it becomes necessary to frequently compare individual letters, and the time complexity increases to O(n·m·k).
Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?
Two states: tested / not tested; if we are not testing [a given node], all its children must be tested. A greedy choice fails, for instance, when a node with a large number of connections has a very expensive test.## AI use

## AI use
The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.
```
Claude (Anthropic) was used as a helper:
- to explain the ideas of edit distance, LCS, the prefix function and KMP, Rabin-Karp and the DP on a tree;
- to review my code and point out errors (off-by-one, missing +1, wrong loop bounds, rolling hash step);
- it gave code skeletons with TODOs that I completed myself, and helped with comments and the complexity table.
  No code or text was taken from books or websites.
```

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 73.8 / 139.4 / 301.6 ms|
| KMP | 20 000 / 40 000 / 80 000 | 2.3 / 1.6 / 0.5 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 6.6 / 10.9 / 33.0 / 135.4 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.
Naive: When we double text size it's time grows 2 times. Marker is fixed, length 2000, so it's linear growth n. But every position costs till m, O(n*m), so it's 100 times slower than KMP.
KMP: Time's different is almost didnt change. Lower time is not real it's Java(JIT) warmup. By growth theory the growth linear O(n + m). 
Edit distance: when size doubled time growth 4 times. Because table n*m when doubled becomes 2(n*m) or 2n * 2m. It's quadratic growth o(n + m). First step is changed by Java(JIT) warmuo.
## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(n·m) | O(n·m) | full (n+1)×(m+1) table, every cell in O(1) |
| LCS | O(n·m) | O(n·m) | same kind of table; two rows would be enough for the length only |
| Prefix function and KMP | O(n + m) | O(m) | prefix function O(m), search O(n): the text pointer never goes back |
| Rabin-Karp (shared words) | O(n + m + h·k) on average, O(n·m·k) worst | O(n + m) | each next window hash in O(1); h = number of hash matches; worst case when many windows have equal hashes |
| Tree DP | O(v) | O(v) | every person visited once in DFS (and once in rebuild); recursion depth up to v |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
