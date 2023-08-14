public class Main {
    public static void main(String[] args) {
        Multiplicator a = new Multiplicator(new int[]{2000, 2000}, new int[]{2000, 2000});
//        int[][] result1 = a.defaultMultiply();
//        int[][] result2 = a.defaultMultiplyByColumns();
        int threadNum = 12;
        int[][] result_min_priority = ThreadCreator.multiplyParallel(a.matrix1, a.matrix2, threadNum, Thread.MIN_PRIORITY);
        int[][] result_mid_priority = ThreadCreator.multiplyParallel(a.matrix1, a.matrix2, threadNum, Thread.NORM_PRIORITY);
        int[][] result_max_priority = ThreadCreator.multiplyParallel(a.matrix1, a.matrix2, threadNum, Thread.MAX_PRIORITY);
//        Multiplicator.compareMatrix(result1, result_min_priority);
//        Multiplicator.compareMatrix(result2, result_mid_priority);
//        Multiplicator.compareMatrix(result_min_priority, result_max_priority);
    }
}
