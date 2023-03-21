package ru.videtskikh;

import java.io.*;
import java.util.Arrays;
import java.util.function.BiPredicate;

public class FirstContestSecondSolution {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/contest1/example1.txt"));
             PrintWriter bw = new PrintWriter(new OutputStreamWriter(System.out))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int dataCenterNumbers = Integer.parseInt(s1[0]); //N
            int serversNumber = Integer.parseInt(s1[1]); //M
            int actionNumbers = Integer.parseInt(s1[2]); //Q

            int[] resetServersCount = new int[dataCenterNumbers];
            int[][] runningServers = instantiateDataCenter(dataCenterNumbers, serversNumber);
            int[] ra = new int[dataCenterNumbers];

            for (int i = 0; i < actionNumbers; i++) {
                s = br.readLine();
                String[] s2 = s.split(" ");
                switch (Action.valueOf(s2[0])) {
                    case RESET -> {
                        int r = Integer.parseInt(s2[1]);
                        resetServersCount[r - 1] += 1;
                        ra[r - 1] = resetServersCount[r - 1] * serversNumber;
                        resetDataCenter(runningServers, r);
                    }
                    case DISABLE -> {
                        int dN = Integer.parseInt(s2[1]);
                        int dM = Integer.parseInt(s2[2]);
                        if (runningServers[dN - 1][dM - 1] == 1 && ra[dN - 1] != 0) {
                            ra[dN - 1] = ra[dN - 1] - resetServersCount[dN - 1];
                        }
                        runningServers[dN - 1][dM - 1] = 0;
                    }
                    case GETMAX -> bw.println(getMaxIndex(ra) + 1);
                    case GETMIN -> bw.println(getMinIndex(ra) + 1);
                }
            }
        } catch (IOException ex) {
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

    private static int[][] instantiateDataCenter(int n, int m) {
        int[][] arr = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(arr[i], 1);
        }
        return arr;
    }

    private static void resetDataCenter(int[][] arr, int r) {
        Arrays.fill(arr[r - 1], 1);
    }

    enum Action {
        RESET, DISABLE, GETMAX, GETMIN
    }
}