package main

import (
	"fmt"
	"go/ast"
	"go/format"
	"go/parser"
	"go/token"
	"os"
)

func getGlobalVar() *ast.GenDecl {
	return &ast.GenDecl{
		Doc: nil,
		Tok: token.VAR,
		Specs: []ast.Spec{
			&ast.ValueSpec{
				Names: []*ast.Ident{
					{
						Name: "globalFunctionCallsCount",
						Obj: &ast.Object{
							Kind: ast.Var,
							Name: "globalFunctionCallsCount",
						},
					},
				},
				Type: &ast.Ident{
					Name: "int",
				},
				Values: []ast.Expr{
					&ast.BasicLit{
						Kind:  token.INT,
						Value: "0",
					},
				},
			},
		},
	}
}

func addGlobalVar(file *ast.File) {
	var insertionIndex = 0
	for i, decl := range file.Decls {
		if genDecl, ok := decl.(*ast.GenDecl); ok {
			if genDecl.Tok != token.IMPORT {
				insertionIndex = i
				break
			}
		}
	}
	file.Decls = append(file.Decls[:insertionIndex+1], file.Decls[insertionIndex:]...)
	file.Decls[insertionIndex] = getGlobalVar()
}

func modifyGlobalFunction(funcDecl *ast.FuncDecl) {
	funcDecl.Body.List = append(
		[]ast.Stmt{
			&ast.IncDecStmt{
				X: &ast.Ident{
					Name: "globalFunctionCallsCount",
				},
				Tok: token.INC,
			},
		},
		funcDecl.Body.List...)
}

func modifyMainFunction(mainFuncDecl *ast.FuncDecl) {
	mainFuncDecl.Body.List = append(mainFuncDecl.Body.List,
		&ast.ExprStmt{
			X: &ast.CallExpr{
				Fun: &ast.SelectorExpr{
					X:   ast.NewIdent("fmt"),
					Sel: ast.NewIdent("Printf"),
				},
				Args: []ast.Expr{
					&ast.BasicLit{
						Kind:  token.STRING,
						Value: "\"Вызовов глобальных функций: %d\"",
					},
					&ast.Ident{
						Name: "globalFunctionCallsCount",
					},
				},
			},
		},
	)
}

func countCalls(file *ast.File) {
	addGlobalVar(file)
	for _, decl := range file.Decls {
		if funcDecl, ok := decl.(*ast.FuncDecl); ok {
			if funcDecl.Name.Name == "main" {
				modifyMainFunction(funcDecl)
			} else if funcDecl.Recv == nil {
				modifyGlobalFunction(funcDecl)
			}
		}
	}
}

func main() {
	if len(os.Args) != 2 {
		fmt.Printf("usage: count_calls <filename.go>\n")
		return
	}

	fset := token.NewFileSet()

	if file, err := parser.ParseFile(
		fset,
		os.Args[1],
		nil,
		parser.ParseComments,
	); err == nil {
		countCalls(file)
		err := ast.Fprint(os.Stdout, fset, file, nil)
		if err != nil {
			return
		}
		err = format.Node(os.Stdout, fset, file)
		if err != nil {
			return
		}
	} else {
		fmt.Printf("Error: %v", err)
	}
}
