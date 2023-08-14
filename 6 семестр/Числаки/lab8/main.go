package main

import (
	"fmt"
	"math"
)

// Функция для интегрирования
func f(u, v float64) float64 {
	x := u
	y := math.Sin(u) * v
	return math.Cos(x) / (1 + y)
}

// Метод ячеек
func CellMethod(a, b float64) float64 {
	eps := 0.001
	h0 := math.Sqrt(eps)
	n := int(math.Ceil((b - a) / h0))
	h := (b - a) / float64(n)

	var integral float64

	for i := 0; i < n; i++ {
		x1 := a + float64(i)*h
		x2 := a + float64(i+1)*h
		y1 := f(x1, 0)
		y2 := f(x2, 0)

		integral += h * (y1 + y2) / 2
	}

	return integral
}

// Метод повторного интегрирования
func RepeatIntegrationMethod(a, b float64) float64 {
	eps := 0.001
	h0 := math.Sqrt(eps)
	n := int((b - a) / h0)
	h := (b - a) / float64(n)

	var integral float64

	for {
		if n > 1 && h < eps {
			n *= 2
			h /= 2
			continue
		}

		var sum float64

		for i := 0; i < n; i++ {
			x1 := a + float64(i)*h
			x2 := a + float64(i+1)*h
			y1 := f(x1, 0)
			y2 := f(x2, 0)

			sum += (y1 + y2) / 2
		}

		integral += h * sum

		if n <= 1 || h >= eps {
			break
		}

		n *= 2
		h /= 2
	}

	return integral
}


func main() {
	a, b := math.Pi/6, math.Pi/2

	// вычисление интеграла методом ячеек
	integral1 := CellMethod(a, b)

	// вычисление интеграла методом повторного интегрирования
	integral2 := RepeatIntegrationMethod(a, b)

	// сравнение значений
	fmt.Println("Метод ячеек:", integral1)
	fmt.Println("Метод повторного интегрирования:", integral2)
	fmt.Println("Разница:", math.Abs(integral1-integral2))
}

