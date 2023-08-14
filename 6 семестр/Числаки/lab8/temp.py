import math

# Функция для интегрирования
def f(u, v):
    x = u
    y = math.sin(u) * v
    return math.cos(x) / (1 + y)

# Метод ячеек
def cell_method(a, b):
    eps = 0.001
    h0 = math.sqrt(eps)
    n = int(math.ceil((b - a) / h0))
    h = (b - a) / n

    integral = 0

    for i in range(n):
        x1 = a + i * h
        x2 = a + (i + 1) * h
        y1 = f(x1, 0)
        y2 = f(x2, 0)

        integral += h * (y1 + y2) / 2

    return integral

# Метод повторного интегрирования
def repeat_integration_method(a, b):
    eps = 0.001
    h0 = math.sqrt(eps)
    n = int((b - a) / h0)
    h = (b - a) / n

    integral = 0
    count = 0
    while True:
        count += 1
        if n > 1 and h < eps:
            n *= 2
            h /= 2
            continue

        sum = 0

        for i in range(n):
            x1 = a + i * h
            x2 = a + (i + 1) * h
            y1 = f(x1, 0)
            y2 = f(x2, 0)

            sum += (y1 + y2) / 2

        integral += h * sum

        if n <= 1 or h >= eps:
            break

        n *= 2
        h /= 2
    print(count)
    return integral

if __name__ == '__main__':
    a, b = math.pi / 6, math.pi / 2

    # вычисление интеграла методом ячеек
    integral1 = cell_method(a, b)

    # вычисление интеграла методом повторного интегрирования
    integral2 = repeat_integration_method(a, b)

    # сравнение значений
    print("Метод ячеек:", integral1)
    print("Метод повторного интегрирования:", integral2)
    print("Разница:", abs(integral1 - integral2))
