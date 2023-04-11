package ru.videtskikh.contest;

import ru.videtskikh.contest.first.Solution;

public class Util {

    private Util() {
    }

    public static long getTiming(Solution solution, String fileInput, String fileOutput) {
        long start = System.currentTimeMillis();
        solution.doTask(fileInput, fileOutput);
        return System.currentTimeMillis() - start;
    }
}
