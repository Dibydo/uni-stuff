#!/bin/sh
flex lab6.l
gcc lex.yy.c -lfl
./a.out
