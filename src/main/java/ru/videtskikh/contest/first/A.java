package ru.videtskikh.contest.first;

import java.io.*;
import java.util.*;

public class A implements Solution {
    //    public static final String INPUT_FILENAME = "src/main/resources/contest1/example3-n1000-m1000.txt";
    //    public static final String INPUT_FILENAME = "src/main/resources/contest1/example3-n1-m1_000_000.txt";
    //    public static final String INPUT_FILENAME = "src/main/resources/contest1/example3-n1_000_000-m1.txt";
    //public static final String INPUT_FILENAME = "src/main/resources/contest1/example1.txt";
    public static final String INPUT_FILENAME = "src/main/resources/contest1/example2.txt";

    public static final String OUTPUT_FILENAME = "src/main/resources/contest1/example3-out.txt";
    private MyScanner sc;
    static double EPS = 0.00000001;

    @Override
    public void doTask(String fileInput, String fileOutput) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileOutput))) {
            sc = new MyScanner(fileInput);
            doTask(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Node {
        Node(int index, int r, int a) {
            this.index = index;
            this.r = r;
            this.a = a;
        }

        int index;
        long r;
        long a;
    }

    static abstract class SegmentTree {
        int levels = 0;
        Node[][] nodes;

        SegmentTree(int n, int m) {
            levels = 0;
            while (n > (1 << levels)) {
                levels++;
            }
            levels++;

            nodes = new Node[levels][];
            nodes[0] = new Node[1];
            for (int i = 1; i < levels; i++) {
                nodes[i] = new Node[nodes[i - 1].length * 2];
            }

            for (int i = 0; i < n; i++) {
                nodes[levels - 1][i] = new Node(i, 0, m);
                if (i % 2 == 1) {
                    update(i - 1, i, levels - 1);
                } else {
                    update(i, i + 1, levels - 1);
                }
            }
        }

        abstract Node choose(Node left, Node right);

        void updated(int i, long r, long a) {
            nodes[levels - 1][i].r = r;
            nodes[levels - 1][i].a = a;

            if (i % 2 == 0) {
                update(i, i + 1, levels - 1);
            } else {
                update(i - 1, i, levels - 1);
            }
        }

        void update(int l, int r, int level) {
            if (level <= 0) return;

            Node selected = choose(nodes[level][l], nodes[level][r]);

            int i = l / 2;
            nodes[level - 1][i] = selected;
            if (i % 2 == 0) {
                update(i, i + 1, level - 1);
            } else {
                update(i - 1, i, level - 1);
            }
        }
    }

    static class MaxSegmentTree extends SegmentTree {

        MaxSegmentTree(int n, int m) {
            super(n, m);
        }

        @Override
        Node choose(Node left, Node right) {
            if (right == null) return left;

            long leftRA = left.r * left.a;
            long rightRA = right.r * right.a;
            if (leftRA == rightRA) {
                return (left.index < right.index) ? left : right;
            }
            return leftRA < rightRA ? right : left;
        }
    }

    static class MinSegmentTree extends SegmentTree {

        MinSegmentTree(int n, int m) {
            super(n, m);
        }

        @Override
        Node choose(Node left, Node right) {
            if (right == null) return left;

            long leftRA = left.r * left.a;
            long rightRA = right.r * right.a;
            if (leftRA == rightRA) {
                return (left.index < right.index) ? left : right;
            }
            return leftRA < rightRA ? left : right;
        }
    }


    private static void recalcMinAndMax(int i, long r, long a) {
        minSegmentTree.updated(i - 1, r, a);
        maxSegmentTree.updated(i - 1, r, a);
    }

    static MaxSegmentTree maxSegmentTree;
    static MinSegmentTree minSegmentTree;

    public void doTask(PrintWriter out) {
        int n = sc.nextInt();
        int m = sc.nextInt();
        int q = sc.nextInt();

        maxSegmentTree = new MaxSegmentTree(n, m);
        minSegmentTree = new MinSegmentTree(n, m);

        List<Set<Integer>> disabled = new ArrayList<>(n + 1);
        int i, j;
        for (i = 0; i <= n; i++) {
            disabled.add(new HashSet<>());
        }

        while (q-- > 0) {
            String command = sc.next();
            switch (command) {
                case "DISABLE":
                    i = sc.nextInt();
                    j = sc.nextInt();
                    if (!disabled.get(i).contains(j)) {
                        long r = minSegmentTree.nodes[minSegmentTree.levels - 1][i - 1].r;
                        long a = minSegmentTree.nodes[minSegmentTree.levels - 1][i - 1].a;
                        recalcMinAndMax(i, r, a - 1);
                        disabled.get(i).add(j);
                    }
                    break;
                case "GETMIN":
                    out.println(minSegmentTree.nodes[0][0].index + 1);
                    break;
                case "GETMAX":
                    out.println(maxSegmentTree.nodes[0][0].index + 1);
                    break;
                case "RESET":
                    i = sc.nextInt();
                    disabled.get(i).clear();
                    long r = minSegmentTree.nodes[minSegmentTree.levels - 1][i - 1].r;
                    recalcMinAndMax(i, r + 1, m);
                    break;
            }
        }
    }

    static long pow(long a, int p) {
        if (p == 1) return a;
        long h = pow(a, p / 2);
        long r = h * h;
        if (p % 2 == 1) r *= a;
        return r;
    }

    static long gcd(long a, long b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;

        public MyScanner(String fileInput) throws FileNotFoundException {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileInput))));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

    }
}
