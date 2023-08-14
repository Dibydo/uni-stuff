def mpi(x, a, b):
    x1 = [0] * len(b)
    for i in range(len(x)):
        summ = 0
        for j in range(len(x)):
            if i != j:
                summ += a[i][j] * x[j]
        x1[i] = (b[i] - summ) / a[i][i]
    return x1


def fast_seidel(x, x1, a, b):
    for i in range(len(x)):
        summ = 0
        for j in range(len(x)):
            if i != j:
                if j < i:
                    summ += a[i][j] * x1[j]
                else:
                    summ += a[i][j] * x[j]
        x1[i] = (b[i] - summ) / a[i][i]
    return x1


def calc_err(xk_new, xk):
    big_delta_tmp = 0
    for i in range(len(xk_new)):
        big_delta_tmp = max(abs(xk_new[i] - xk[i]), big_delta_tmp)
    return big_delta_tmp


def zeidel(A, b, eps):
    N = len(A)
    F_t = [[0] * N for i in range(N)]
    F_b = [[0] * N for i in range(N)]
    c = [0] * N
    for i in range(N):
        for j in range(N):
            if i < j:
                F_t[i][j] = -A[i][j] / A[i][i]
            elif i > j:
                F_b[i][j] = -A[i][j] / A[i][i]
        c[i] = b[i] / A[i][i]

    x = c.copy()
    x1 = c.copy()

    iters = 0

    while True:
        iters += 1
        x1 = fast_seidel(x, x1, a, b)

        big_delta_tmp = 0
        for i in range(len(x)):
            big_delta_tmp = max(abs(x1[i] - x[i]), big_delta_tmp)

        another_big_delta_tmp = 0
        for i in range(len(x)):
            another_big_delta_tmp = max(abs(x1[i]), another_big_delta_tmp)

        small_delta_tmp = big_delta_tmp / another_big_delta_tmp

        if small_delta_tmp < eps:
            break
        else:
            x = x1
    print('zeidel method iters:', iters)

    return x1


if __name__ == "__main__":
    a = [[10.5, -1, 0.2, 2.0],
         [1, 11.5, -2.0, 0.1],
         [0.3, -4.0, 11.5, 1.0],
         [0.2, -0.3, -0.5, 7.5]]
    b = [1.5, 1.5, 3.0, 1.0]

    b_len = len(b)

    x = [0] * b_len
    c = [0] * b_len
    for i in range(b_len):
        x[i] = b[i] / a[i][i]
        c[i] = x[i]

    f = []
    for i in range(b_len):
        f.append([0] * b_len)
        for j in range(b_len):
            if i == j:
                f[i][j] = 0
            else:
                f[i][j] = -a[i][j] / a[i][i]

    print("F")
    for i in f:
        print(i)

    f_norm = -1
    for i in range(b_len):
        acc = 0
        for j in range(b_len):
            acc += abs(f[i][j])
        if acc < 1:
            f_norm = max(acc, f_norm)

    print("F_norm", f_norm)

    iter_count = 0
    while True:
        iter_count += 1
        x1 = mpi(x, a, b)

        big_delta_tmp = 0
        for i in range(len(x)):
            big_delta_tmp = max(abs(x1[i] - x[i]), big_delta_tmp)

        another_big_delta_tmp = 0
        for i in range(len(x)):
            another_big_delta_tmp = max(abs(x1[i]), another_big_delta_tmp)

        small_delta_tmp = big_delta_tmp / another_big_delta_tmp

        if small_delta_tmp < 0.0001:
            print("STOP", iter_count)
            break
        else:
            x = x1

    new_b = [0] * b_len
    for i in range(b_len):
        for j in range(b_len):
            new_b[i] += a[i][j] * x1[j]
    print(new_b)

    new_x = zeidel(a, b, 0.0001)

    new_b = [0] * b_len
    for i in range(b_len):
        for j in range(b_len):
            new_b[i] += a[i][j] * new_x[j]
    print(new_b)
