package main

import (
	"fmt"
	"go/ast"
	"go/format"
	"go/parser"
	"go/token"
	"golang.org/x/tools/go/ast/astutil"
	"os"
)

func generateIncDec(expr ast.Expr, op bool) *ast.AssignStmt {
	var opToken token.Token
	if op {
		opToken = token.ADD
	} else {
		opToken = token.SUB
	}
	return &ast.AssignStmt{
		Lhs: []ast.Expr{
			expr,
		},
		Tok: token.ASSIGN,
		Rhs: []ast.Expr{
			&ast.BinaryExpr{
				X:  expr,
				Op: opToken,
				Y: &ast.BasicLit{
					Kind:  token.INT,
					Value: "1",
				},
			},
		},
	}
}

func replaceIncDec(file *ast.File) {
	astutil.Apply(file, nil, func(c *astutil.Cursor) bool {
		node := c.Node()
		if incDecStmt, ok := node.(*ast.IncDecStmt); ok {
			var op bool
			if incDecStmt.Tok == token.INC {
				op = true
			} else {
				op = false
			}
			c.Replace(generateIncDec(incDecStmt.X, op))
		}
		return true
	})
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
		replaceIncDec(file)
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
