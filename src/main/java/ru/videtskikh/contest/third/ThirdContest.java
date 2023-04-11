package ru.videtskikh.contest.third;

import ru.videtskikh.contest.first.Solution;

import static java.text.MessageFormat.format;
import static ru.videtskikh.contest.Util.getTiming;


public class ThirdContest {
    public static void main(String[] args) {
        String src = "src/main/resources/contest3/";
        String fileInput1 = src + "example1.txt";
        String fileOutput1 = src + "example1-out.txt";

        String fileInput2 = src + "example2-generated.txt";
        String fileOutput2 = src + "example2-out.txt";

        Solution solution = new SolutionJson();

        System.out.println("SolutionJson:");
//        System.out.println(format("Первый тест: {0}мс", getTiming(solution, fileInput1, fileOutput1)));
        System.out.println(format("Второй тест: {0}мс", getTiming(solution, fileInput2, fileOutput2)));
    }
}
