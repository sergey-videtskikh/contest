package ru.videtskikh.contest.first;

import java.io.*;
import java.util.Arrays;

public class SolutionArrayMinMax implements Solution {
    public static final String FILE_INPUT = "src/main/resources/contest1/example3-n1000-m1000.txt";
    public static final String FILE_OUTPUT = "src/main/resources/contest1/example3-out.txt";

    public static void main(String[] args) {
        new SolutionArrayMinMax().doTask(FILE_INPUT, FILE_OUTPUT);
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
            ArrayMinMax ra = new ArrayMinMax(dataCenterNumbers);

            for (int i = 0; i < actionNumbers; i++) {
                s = br.readLine();
                String[] s2 = s.split(" ");
                switch (Action.valueOf(s2[0])) {
                    case RESET -> {
                        int r = Integer.parseInt(s2[1]);
                        resetServersCount[r - 1] += 1;
                        ra.add(resetServersCount[r - 1] * serversNumber, r - 1);
                        for (int j = 0; j < serversNumber; j++) {
                            runningServers[r - 1][j] = 0;
                        }
                        //runningServers[r - 1] = new int[serversNumber]; этот вариант сброса приводит к 12с при n = 1; m = 1_000_000
                    }
                    case DISABLE -> {
                        int dN = Integer.parseInt(s2[1]);
                        int dM = Integer.parseInt(s2[2]);
                        if (runningServers[dN - 1][dM - 1] == 0 && ra.get(dN - 1) != 0) {
                            ra.add(ra.get(dN - 1) - resetServersCount[dN - 1], dN - 1);
                        }
                        runningServers[dN - 1][dM - 1] = 1;
                    }
                    case GETMAX -> bw.println(ra.getMaxIndex() + 1);
                    case GETMIN -> bw.println(ra.getMinIndex() + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ArrayMinMax {
        private final int[] arr;

        private int maxElement = 0;
        private int minElement = 0;

        private int maxIndex = 0;
        private int minIndex = 0;

        private boolean isMaxAvailable = true;
        private boolean isMinAvailable = true;

        public ArrayMinMax(int size) {
            this.arr = new int[size];
        }

        public int getMaxIndex() {
            if (!isMaxAvailable) {
                resetMaxMin();
            }
            return maxIndex;
        }

        public int getMinIndex() {
            if (!isMinAvailable) {
                resetMaxMin();
            }
            return minIndex;
        }

        private void resetMaxMin() {
            int maxI = 0;
            int minI = 0;
            int maxE = 0;
            int minE = Integer.MAX_VALUE;
            for (int i = 0; i < arr.length; i++) {
                if (!isMaxAvailable && arr[i] > maxE) {
                    maxE = arr[i];
                    maxI = i;
                }
                if (!isMinAvailable && arr[i] < minE) {
                    minE = arr[i];
                    minI = i;
                }
            }
            if (!isMaxAvailable) {
                maxIndex = maxI;
                isMaxAvailable = true;
            }
            if (!isMinAvailable) {
                minIndex = minI;
                isMinAvailable = true;
            }
        }

        public void add(int value, int index) {
            if (value > maxElement) {
                maxElement = value;
                maxIndex = index;
            } else if (value == maxElement && index < maxIndex) {
                maxIndex = index;
            } else if (index == maxIndex && value < maxElement) {
                isMaxAvailable = false;
            }

            if (value < minElement) {
                minElement = value;
                minIndex = index;
            } else if (value == minElement && index < minIndex) {
                minIndex = index;
            } else if (index == minIndex && value > minElement) {
                isMinAvailable = false;
            }

            arr[index] = value;
        }

        public int get(int index) {
            return arr[index];
        }

        @Override
        public String toString() {
            return Arrays.toString(arr);
        }
    }

    private enum Action {
        RESET, DISABLE, GETMAX, GETMIN
    }
}
