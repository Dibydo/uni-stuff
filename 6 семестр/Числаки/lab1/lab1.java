public class lab1 {
    private static float[] calculateAnswer(final float[] a, final float[] b, final float[] c, final float[] d) {
        int n = d.length;

        c[0] = c[0] / b[0];
        d[0] = d[0] / b[0];

        for (int i = 1; i < n; i++) {
            float temp = (float) 1.0 / (b[i] - a[i] * c[i - 1]);
            c[i] = c[i] * temp;
            d[i] = (d[i] - a[i] * d[i - 1]) * temp;
        }

        float[] x = new float[n];
        x[n - 1] = d[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            x[i] = d[i] - c[i] * x[i + 1];
        }

        return x;
    }

    private static float[] calculateError(final float[][] matrix, float[] d, final float[] answer) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("Incorrect value");

        float[] error = new float[matrix.length];

        float[] dzv = new float[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                dzv[i] += matrix[i][j] * answer[j];
            }
        }

        float[] r = new float[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            r[i] = d[i] - dzv[i];
        }

        float[][] invertMatrix = invert(matrix);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                error[i] += invertMatrix[i][j] * r[j];
            }
        }

        return error;
    }

    public static float[][] invert(float[][] a)
    {
        int n = a.length;
        float[][] x = new float[n][n];
        float[][] b = new float[n][n];
        int[] index = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    public static void gaussian(float[][] a, int[] index)
    {
        int n = index.length;
        float[] c = new float[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            float c1 = 0;
            for (int j=0; j<n; ++j)
            {
                float c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            float pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                float pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                float pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }

    public static void main(String[] args) {
        float[] a = {0, 1, 1, 1};
        float[] b = {4, 4, 4, 4};
        float[] c = {1, 1, 1, 0};
        float[] d = {5, 6, 6, 5};

        float[] x = calculateAnswer(a.clone(), b.clone(), c.clone(), d.clone());
        for (float value : x) {
            System.out.printf("%.10f ", value);
        }
        System.out.println();

        float[][] matrix = new float[][]{{4, 1, 0, 0}, {1, 4, 1, 0}, {0, 1, 4, 1}, {0, 0, 1, 4}};
        float[] error = calculateError(matrix, d.clone(), x);
        for (float value : error) {
            System.out.printf("%.10f ", value);
        }
    }
}
