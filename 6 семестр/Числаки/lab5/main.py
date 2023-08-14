import numpy as np

m = 4

l = 1
r = 5
n = 8

h = (r - l) / n

def f(x):
    return np.exp(x)

def find_lambda(A, b):
    T = np.zeros((m,m))
    x = np.empty(m)
    y = np.empty(m)

    for i in range(m):
        for j in range(i):
            T[i][j] = (A[i][j] - sum([T[i][k] * T[j][k] for k in range(j)])) / T[j][j]
        T[i][i] = np.sqrt(A[i][i] - sum(T[i][k] ** 2 for k in range(i)))
        
    # прямой ход
    for i in range(m):
        y[i] = (b[i] - sum([T[i,k]*y[k] for k in range(i)])) / T[i,i] 
    
    # обратный ход
    for i in range(m-1, -1, -1):
        x[i] = (y[i] - sum([T[k,i]*x[k] for k in range(i+1, m)])) / T[i,i] 

    return x
    
xs = np.linspace(l, r, n + 1, True)

mids = np.linspace(l + 0.5 * h, r - 0.5 * h, n, True)
ys = np.array([1.55, 1.80, 1.66, 0.73, 0.69, 1.30, 0.38, 0.72, 0.70])
print("xs: \n", xs)
print("ys: \n", ys)
print("mids: \n", mids)

A = np.array([[sum(xs[k] ** (i+j) for k in range(0,n+1))  for j in range(0, m)] for i in range(0,m)])
b = np.array([sum(ys[k] * (xs[k] ** i) for k in range(0,n+1))  for i in range(0, m)])
print("A: \n", A)
print("b: \n", b)

lambda1 = find_lambda(A,b)
print("Лямбда: \n", lambda1)

def z(x):
    return sum([lambda1[i] * x**i for i in range(m)])

D = sum([(ys[k] - z(xs)[k]) for k in range(m+1)])**2 
D = np.sqrt(D) / np.sqrt(n)
print("Отклонение: \n", D)

d = sum([ys[k]**2 for k in range(n+1)])
d = D / np.sqrt(d)
print("Относительная погрешность δ: \n", d)

tab = np.linspace(l, r, n+n+1, True)
counted = np.vectorize(z)(tab)
given = np.vectorize(f)(tab)

for k in range(n + 1):
    print("x:", xs[k], "  f(x):", ys[k], "  z(x):", z(xs)[k], "  |f - z|:", abs(ys[k] - z(xs)[k]))
    if k != n:
        print("x:", mids[k], "      z(x):", z(mids)[k])
