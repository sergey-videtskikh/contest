package ru.videtskikh;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.function.BiPredicate;

public class FirstContest {
    public static void main(String[] args) {
        Date start = new Date();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/contest1/example3-n1000-m1000.txt"));
//             PrintWriter bw = new PrintWriter(new OutputStreamWriter(System.out))) {
             PrintWriter bw = new PrintWriter(new FileWriter("src/main/resources/contest1/example3-out.txt"))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int dataCenterNumbers = Integer.parseInt(s1[0]); //N
            int serversNumber = Integer.parseInt(s1[1]); //M
            int actionNumbers = Integer.parseInt(s1[2]); //Q

            int[] resetServersCount = new int[dataCenterNumbers];
            int[][] runningServers = instantiateDataCenter(dataCenterNumbers, serversNumber);

            for (int i = 0; i < actionNumbers; i++) {
                s = br.readLine();
                String[] s2 = s.split(" ");
                switch (Action.valueOf(s2[0])) {
                    case RESET -> {
                        int r = Integer.parseInt(s2[1]);
                        resetServersCount[r - 1] += 1;
                        resetDataCenter(runningServers, r);
                    }
                    case DISABLE -> {
                        int dN = Integer.parseInt(s2[1]);
                        int dM = Integer.parseInt(s2[2]);
                        disableServer(runningServers, dN, dM);
                    }
                    case GETMAX -> {
                        int max = getMax(runningServers, resetServersCount);
                        bw.println(max);
                    }
                    case GETMIN -> {
                        int min = getMin(runningServers, resetServersCount);
                        bw.println(min);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
    }

    private static int getMax(int[][] runningServers, int[] resetServersCount) {
        int min = 0;
        return getByBiFunction(runningServers, resetServersCount, min, (a, b) -> a > b);
    }

    private static int getMin(int[][] runningServers, int[] resetServersCount) {
        int min = Integer.MAX_VALUE;
        return getByBiFunction(runningServers, resetServersCount, min, (a, b) -> a < b);
    }

    private static int getByBiFunction(int[][] runningServers, int[] resetServersCount, int maxOrMinElement, BiPredicate<Integer, Integer> predicate) {
        int maxOrMinRaDataCenter = 1;
        for (int i = 0; i < runningServers.length; i++) {
            int ra = getRa(runningServers, resetServersCount, i);
            if (predicate.test(ra, maxOrMinElement)) {
                maxOrMinElement = ra;
                maxOrMinRaDataCenter = i + 1;
            }
        }
        return maxOrMinRaDataCenter;
    }

    private static int getRa(int[][] runningServers, int[] resetServersCount, int i) {
        int running = 0;
        for (int j = 0; j < runningServers[i].length; j++) {
            if (runningServers[i][j] == 1) {
                running++;
            }
        }
        return running * resetServersCount[i];
    }

    private static void disableServer(int[][] runningServers, int dN, int dM) {
        runningServers[dN - 1][dM - 1] = 0;
    }

    private static void resetDataCenter(int[][] arr, int r) {
        Arrays.fill(arr[r - 1], 1);
    }

    private static int[][] instantiateDataCenter(int n, int m) {
        int[][] arr = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(arr[i], 1);
        }
        return arr;
    }

    enum Action {
        RESET, DISABLE, GETMAX, GETMIN
    }
}
