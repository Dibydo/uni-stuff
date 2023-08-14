from sympy import *
import math

eps = 0.001

x, y = symbols('x y')

def f():
    return sin(x**2) + x**4 + 2 * x**2 * y**4 + ln(1 + 0.1 * x)

def analytical_min():
    return -0.0500014, 0.0

print('f: ', f())
fx = diff(f(), x)
fy = diff(f(), y)
print('df/dx: ', fx)
print('df/dy: ', fy)
print('d^2f/dx^2: ', diff(fx, x))
print('d^2f/dy^2: ', diff(fy, y))
print()

k = 0
xk, yk = 0.0, 0.0

while max(fx.subs({x: xk, y: yk}), fy.subs({x: xk, y: yk})) >= eps:
    phi1 = - (fx.subs({x: xk, y: yk}))**2 - (fy.subs({x: xk, y: yk}))**2
    phi2 = diff(fx, x).subs({x: xk, y: yk}) * (fx.subs({x: xk, y: yk}))**2 + \
           2 * diff(fx, y).subs({x: xk, y: yk}) * fx.subs({x: xk, y: yk}) * fy.subs({x: xk, y: yk}) + \
           diff(fy, y).subs({x: xk, y: yk}) * (fy.subs({x: xk, y: yk}))**2
    t_star = - phi1 / phi2
    xk = xk - t_star * fx.subs({x: xk, y: yk})
    yk = yk - t_star * fy.subs({x: xk, y: yk})
    k += 1

print(f'methods min {xk, yk}')
print(f'analytical min: {analytical_min()}')
print(f'difference: {math.fabs(xk - analytical_min()[0]), math.fabs(yk - analytical_min()[1])}')
