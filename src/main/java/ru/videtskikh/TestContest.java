package ru.videtskikh;

import java.io.*;

public class TestContest {
    public static void main(String[] args) {
        readFromFileWriteToFile();
    }


    private static void readFromFileWriteToConsole() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int a = Integer.parseInt(s1[0]);
            int b = Integer.parseInt(s1[1]);

            System.out.println(a + b);
        } catch (IOException ex) {
        }
    }

    private static void readFromFileWriteToFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/input.txt"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/output.txt"))) {
            String s = br.readLine();
            if (s != null) {
                String[] s1 = s.split(" ");
                int a = Integer.parseInt(s1[0]);
                int b = Integer.parseInt(s1[1]);

                bw.write(String.valueOf(a + b));
            }
        } catch (IOException ex) {
        }
    }

    private static void readFromConsoleWriteToConsole() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int a = Integer.parseInt(s1[0]);
            int b = Integer.parseInt(s1[1]);

            System.out.println(a + b);
        } catch (IOException ex) {
        }
    }
}
