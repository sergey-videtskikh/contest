package ru.videtskikh.contest.first;

import java.io.*;
import java.util.function.BiPredicate;

public class SolutionOptimized implements Solution {
    public static final String FILE_INPUT = "src/main/resources/contest1/example3-n1000-m1000.txt";
    public static final String FILE_OUTPUT = "src/main/resources/contest1/example3-out.txt";

    public static void main(String[] args) {
        new SolutionOptimized().doTask(FILE_INPUT, FILE_OUTPUT);
    }

    public void doTask(String fileInput, String fileOutput) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileInput));
             PrintWriter bw = new PrintWriter(new FileWriter(fileOutput))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int dataCenterNumbers = Integer.parseInt(s1[0]); //N
            int serversNumber = Integer.parseInt(s1[1]); //M
            int actionNumbers = Integer.parseInt(s1[2]); //Q

            int[] resetServersCount = new int[dataCenterNumbers];
            int[][] runningServers = new int[dataCenterNumbers][serversNumber]; //inversed values of server state; 0 - disable; 1 - enabled
            int[] ra = new int[dataCenterNumbers];

            for (int i = 0; i < actionNumbers; i++) {
                s = br.readLine();
                String[] s2 = s.split(" ");
                switch (Action.valueOf(s2[0])) {
                    case RESET -> {
                        int r = Integer.parseInt(s2[1]);
                        resetServersCount[r - 1] += 1;
                        ra[r - 1] = resetServersCount[r - 1] * serversNumber;
                        runningServers[r - 1] = new int[serversNumber];
                    }
                    case DISABLE -> {
                        int dN = Integer.parseInt(s2[1]);
                        int dM = Integer.parseInt(s2[2]);
                        if (runningServers[dN - 1][dM - 1] == 0 && ra[dN - 1] != 0) {
                            ra[dN - 1] = ra[dN - 1] - resetServersCount[dN - 1];
                        }
                        runningServers[dN - 1][dM - 1] = 1;
                    }
                    case GETMAX -> bw.println(getMaxIndex(ra) + 1);
                    case GETMIN -> bw.println(getMinIndex(ra) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getMaxIndex(int[] arr) {
        int min = 0;
        return getMaxOrMinIndexByBiFunction(arr, min, (a, b) -> a > b);
    }

    private static int getMinIndex(int[] arr) {
        int min = Integer.MAX_VALUE;
        return getMaxOrMinIndexByBiFunction(arr, min, (a, b) -> a < b);
    }

    private static int getMaxOrMinIndexByBiFunction(int[] arr, int maxOrMinElement, BiPredicate<Integer, Integer> predicate) {
        int maxOrMinIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (predicate.test(arr[i], maxOrMinElement)) {
                maxOrMinElement = arr[i];
                maxOrMinIndex = i;
            }
        }
        return maxOrMinIndex;
    }

    enum Action {
        RESET, DISABLE, GETMAX, GETMIN
    }
}
