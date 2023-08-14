public class Multiplicator {
    public int[][] matrix1;
    public int[][] matrix2;

    public Multiplicator(int[] sizes1, int[] sizes2) {
        matrix1 = new int[sizes1[0]][sizes1[1]];
        matrix2 = new int[sizes2[0]][sizes2[1]];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                matrix1[i][j] = ((int) (Math.random() * 10));
            }
        }
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[i].length; j++) {
                matrix2[i][j] = ((int) (Math.random() * 10));
            }
        }
    }

    public int[][] defaultMultiply() {
        System.out.println("Default multiplication by rows");
        long startTime = System.currentTimeMillis();
        int[][] return_matrix = new int[matrix1.length][matrix2[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    return_matrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        long stopTime = System.currentTimeMillis();
        System.out.print("Default multiplication time in milliseconds: ");
        System.out.println(stopTime - startTime);
        return return_matrix;
    }

    public int[][] defaultMultiplyByColumns() {
        System.out.println("Default multiplication by columns");
        long startTime = System.currentTimeMillis();
        int[][] return_matrix = new int[matrix1.length][matrix2[0].length];
        for (int j = 0; j < matrix1.length; j++) {
            for (int i = 0; i < matrix2[0].length; i++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    return_matrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        long stopTime = System.currentTimeMillis();
        System.out.print("Default multiplication time in milliseconds: ");
        System.out.println(stopTime - startTime);
        return return_matrix;
    }

    public static void compareMatrix(int[][] matrix1, int[][] matrix2) {
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                if (matrix1[i][j] != matrix2[i][j]) {
                    System.out.println("Matrix not equal");
                    return;
                }
            }
        }
        System.out.println("Matrix equal");
    }
}
