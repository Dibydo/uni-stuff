package main

//var global_counter: map[string]:int

func main() {
	fmt.Println("Hello")
	for i := 0; i < 20; i++ {
    		fmt.Println(i)
			foo()
	}
	foo()
	bar()
	foo()
	// for k, v := range global_counter....
}

func foo() {
	// global_counter["foo"]++
	fmt.Println("Foo")
}

func bar() {
	// global_counter["bar"]++
	fmt.Println("Bar")
}
