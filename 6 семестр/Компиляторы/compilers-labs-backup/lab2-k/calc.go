package main

import "fmt"

type array []int

func (a array) print() {
	for _, el := range a {
		fmt.Println(el)
	}
}

func add(x int, y int) int {
	return x + y
}

func mul(x int, y int) int {
	ans := 0
	for i := 0; i < y; i++ {
		ans = add(ans, x)
	}
	return ans
}

func main() {
	var arr array = array{1, 2, 3}
	arr.print()
	x := 3
	y := 4
	x++
	y--
	fmt.Printf("%d\n", add(mul(x, y), y))
}
