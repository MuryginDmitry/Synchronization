package ru.netology;

import java.util.Map;
import java.util.Random;

import static ru.netology.DeliveryRobot.*;

public class Main {
    public static void main(String[] args) {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute(INSTRUCTIONS, ROUTE_LENGTH);
                int rCount = countR(route);
                synchronized (sizeToFreq) {
                    sizeToFreq.compute(rCount, (key, value) -> value != null ? value + 1 : 1);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printResult();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countR(String route) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void printResult() {
        int maxFreq = 0;
        for (int freq : sizeToFreq.values()) {
            if (freq > maxFreq) {
                maxFreq = freq;
            }
        }

        int maxFreqCount = 0;
        for (int freq : sizeToFreq.values()) {
            if (freq == maxFreq) {
                maxFreqCount++;
            }
        }

        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " + maxFreqCount + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() != maxFreq) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }
}