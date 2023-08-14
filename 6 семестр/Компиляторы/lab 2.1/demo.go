package main

import (
	"fmt"
	"go/ast"
	"go/parser"
	"go/token"
	"io/ioutil"
)

func main() {
	filename := "example.go"
	sourceCode, err := ioutil.ReadFile(filename)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	fset := token.NewFileSet()
	node, err := parser.ParseFile(fset, "", string(sourceCode), parser.ParseComments)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	functionCalls := make(map[string]int)
	ast.Inspect(node, func(n ast.Node) bool {
		if funcDecl, ok := n.(*ast.FuncDecl); ok && funcDecl.Name.Name == "main" {
			ast.Inspect(funcDecl.Body, func(n ast.Node) bool {
				switch stmt := n.(type) {
				case *ast.ExprStmt:
					if callExpr, ok := stmt.X.(*ast.CallExpr); ok {
						switch fun := callExpr.Fun.(type) {
						case *ast.Ident:
							functionName := fun.Name
							functionCalls[functionName]++
						case *ast.SelectorExpr:
							switch x := fun.X.(type) {
							case *ast.Ident:
								packageName := x.Name
								functionName := fun.Sel.Name
								functionCalls[packageName+"."+functionName]++
							}
						}
					}
				case *ast.ForStmt:
					ast.Inspect(stmt.Body, func(n ast.Node) bool {
						if innerStmt, ok := n.(*ast.ExprStmt); ok {
							if callExpr, ok := innerStmt.X.(*ast.CallExpr); ok {
								switch fun := callExpr.Fun.(type) {
								case *ast.Ident:
									functionName := fun.Name
									functionCalls[functionName]++
								case *ast.SelectorExpr:
									switch x := fun.X.(type) {
									case *ast.Ident:
										packageName := x.Name
										functionName := fun.Sel.Name
										functionCalls[packageName+"."+functionName]++
									}
								}
							}
						}
						return true
					})
				}
				return true
			})
			return false
		}
		return true
	})

	for functionName, callCount := range functionCalls {
		fmt.Printf("%s called %d times\n", functionName, callCount)
	}
}
