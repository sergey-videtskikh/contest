package ru.videtskikh.contest.first;

import static java.text.MessageFormat.format;
import static ru.videtskikh.contest.Util.getTiming;
import static ru.videtskikh.contest.first.TestingDataCreator.createTestFile;

public class FirstContest {
    public static void main(String[] args) {
        String src = "src/main/resources/contest1/";
        String fileInput1 = src + "example3-n1000-m1000.txt";
        String fileInput2 = src + "example3-n1_000_000-m1.txt";
        String fileInput3 = src + "example3-n1-m1_000_000.txt";
        String fileOutput = src + "example3-out.txt";
        createTestFile(fileInput1, 1000, 1000, 100_000);
        createTestFile(fileInput2, 1_000_000, 1, 100_000);
        createTestFile(fileInput3, 1, 1_000_000, 100_000);

//        Solution solutionPlainHeavy = new SolutionPlainHeavy();
//        System.out.println("Решение не оптимальное:");
//        System.out.println(format("Первый тест: {0}мс", getTiming(solutionPlainHeavy, fileInput1, fileOutput)));
//        System.out.println(format("Второй тест: {0}мс", getTiming(solutionPlainHeavy, fileInput2, fileOutput)));
//        System.out.println(format("Третий тест: {0}мс", getTiming(solutionPlainHeavy, fileInput3, fileOutput)));

        Solution aSolution = new A();
        System.out.println("Решение на нодах:");
        System.out.println(format("Первый тест: {0}мс", getTiming(aSolution, fileInput1, fileOutput)));
        System.out.println(format("Второй тест: {0}мс", getTiming(aSolution, fileInput2, fileOutput)));
        System.out.println(format("Третий тест: {0}мс", getTiming(aSolution, fileInput3, fileOutput)));
        System.out.println("Решение на нодах:");
        System.out.println(format("Первый тест: {0}мс", getTiming(aSolution, fileInput1, fileOutput)));
        System.out.println(format("Второй тест: {0}мс", getTiming(aSolution, fileInput2, fileOutput)));
        System.out.println(format("Третий тест: {0}мс", getTiming(aSolution, fileInput3, fileOutput)));

        Solution solutionOptimized = new SolutionOptimized();
        System.out.println("Решение оптимальное:");
        System.out.println(format("Первый тест: {0}мс", getTiming(solutionOptimized, fileInput1, fileOutput)));
        System.out.println(format("Второй тест: {0}мс", getTiming(solutionOptimized, fileInput2, fileOutput)));
        System.out.println(format("Третий тест: {0}мс", getTiming(solutionOptimized, fileInput3, fileOutput)));
        System.out.println("Решение оптимальное:");
        System.out.println(format("Первый тест: {0}мс", getTiming(solutionOptimized, fileInput1, fileOutput)));
        System.out.println(format("Второй тест: {0}мс", getTiming(solutionOptimized, fileInput2, fileOutput)));
        System.out.println(format("Третий тест: {0}мс", getTiming(solutionOptimized, fileInput3, fileOutput)));
        System.out.println("Решение оптимальное:");
        System.out.println(format("Первый тест: {0}мс", getTiming(solutionOptimized, fileInput1, fileOutput)));
        System.out.println(format("Второй тест: {0}мс", getTiming(solutionOptimized, fileInput2, fileOutput)));
        System.out.println(format("Третий тест: {0}мс", getTiming(solutionOptimized, fileInput3, fileOutput)));
    }
}
