import java.util.ArrayList;
import java.util.List;

public class ThreadCreator {
    public static int[][] multiplyParallel(int[][] A, int[][] B, int num, int priority) {
        int size = A.length;
        int[][] result = new int[size][size];
        long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        int stepRow = size / num;
        int stepCol = size / num;
        if (stepRow == 0) {
            stepRow = size;
        }
        if (stepCol == 0) {
            stepCol = size;
        }
        for (int i = 0; i < size; i += stepRow) {
            for (int j = 0; j < size; j += stepCol) {
                ParallelMultiplication subMatrix = new ParallelMultiplication(result, A, B, size, i, i + stepRow, j, j + stepCol);
                Thread thread = new Thread(subMatrix);
                if (0 <= priority && priority <= 10) {
                    thread.setPriority(priority);
                }
                thread.start();
                threads.add(thread);
                if (size - i - stepRow < stepRow) {
                    stepRow = size - i;
                }
                if (size - j - stepCol < stepCol) {
                    stepCol = size - j;
                }
                if (threads.size() % num == 0) {
                    waitForThreads(threads);
                }
            }
        }
        long stopTime = System.currentTimeMillis();
        long time = stopTime - startTime;
        System.out.println("Threads: " + num + "\nTime: " + time + " milliseconds");
        return result;
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }
}
