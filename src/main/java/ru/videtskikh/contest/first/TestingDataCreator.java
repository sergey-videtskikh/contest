package ru.videtskikh.contest.first;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class TestingDataCreator {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        createTestFile("src/main/resources/contest1/example3-n1000-m1000.txt", 1000, 1000, 100_000);
        createTestFile("src/main/resources/contest1/example3-n1_000_000-m1.txt", 1_000_000, 1, 100_000);
        createTestFile("src/main/resources/contest1/example3-n1-m1_000_000.txt", 1, 1_000_000, 100_000);
    }

    public static void createTestFile(String fileName, int n, int m, int q) {
        try (PrintWriter pr = new PrintWriter(new FileWriter(fileName))) {
            pr.println(n + " " + m + " " + q);
            for (int i = 0; i < q; i++) {
                Action action = Action.randomAction();
                switch (action) {
                    case RESET -> pr.println(Action.RESET + " " + getRandom(1, n));
                    case DISABLE -> pr.println(Action.DISABLE + " " + getRandom(1, n) + " " + getRandom(1, m));
                    case GETMAX -> pr.println(Action.GETMAX);
                    case GETMIN -> pr.println(Action.GETMIN);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRandom(int min, int max) {
        return RANDOM.nextInt(max + 1 - min) + min;
    }

    enum Action {
        RESET, DISABLE, GETMAX, GETMIN;

        private static final List<Action> VALUES = List.of(values());
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Action randomAction() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }
}
