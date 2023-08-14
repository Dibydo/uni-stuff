import numpy as np

def gauss_method(A, b):
    n = len(b)
    for i in range(n):
        maxEl = abs(A[i][i])
        maxRow = i
        for k in range(i + 1, n):
            if abs(A[k][i]) > maxEl:
                maxEl = abs(A[k][i])
                maxRow = k
        for k in range(i, n):
            tmp = A[maxRow][k]
            A[maxRow][k] = A[i][k]
            A[i][k] = tmp
        tmp = b[maxRow]
        b[maxRow] = b[i]
        b[i] = tmp
        for k in range(i + 1, n):
            c = -A[k][i] / A[i][i]
            for j in range(i, n):
                if i == j:
                    A[k][j] = 0
                else:
                    A[k][j] += c * A[i][j]
            b[k] += c * b[i]

    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = b[i]
        for j in range(i + 1, n):
            x[i] -= A[i][j] * x[j]
        x[i] /= A[i][i]

    return x

def newton_method():
    x0 = [0.0, 1]
    e = 1
    i = 0
    def f1(x, y):
        return 2 * y - np.cos(x + 1)

    def f2(x, y):
        return x + np.sin(y) + 0.4

    def d_dx_f1(x, y):
        return np.sin(x + 1)

    def d_dx_f2(x, y):
        return 1

    def d_dy_f1(x, y):
        return 2

    def d_dy_f2(x, y):
        return np.cos(y)

    while e > 0.01:
        df = np.zeros((2, 2))
        df[0][0] = d_dx_f1(x0[0], x0[1])
        df[0][1] = d_dy_f1(x0[0], x0[1])
        df[1][0] = d_dx_f2(x0[0], x0[1])
        df[1][1] = d_dy_f2(x0[0], x0[1])

        b = [-f1(x0[0], x0[1]), -f2(x0[0], x0[1])]

        res = gauss_method(df, b)

        x_prev = x0
        x0 = [x0[0] + res[0], x0[1] + res[1]]

        e = max(abs(x0[0] - x_prev[0]), abs(x0[1] - x_prev[1]))
        i += 1
    return x0, e, i

solution, error, iter = newton_method()
print("Решение: ", solution)
print("Погрешность: ", error)
print(iter)
