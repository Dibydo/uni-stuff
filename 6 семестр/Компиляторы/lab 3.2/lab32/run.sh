#!/bin/bash

flex lexer.l
bison -d parser.y
g++ -o test lex.yy.c parser.tab.c
./test
