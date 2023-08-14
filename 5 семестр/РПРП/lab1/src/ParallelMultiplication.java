public class ParallelMultiplication implements Runnable {

    public int[][] result;
    public int[][] a;
    public int[][] b;
    public int startRow, endRow, startCol, endCol, size;

    public ParallelMultiplication(int[][] result, int[][] a, int[][] b, int size, int startRow, int endRow, int startCol, int endCol) {
        this.result = result;
        this.a = a;
        this.b = b;
        this.size = size;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                result[i][j] = 0;
                for (int k = 0; k < size; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }
}
