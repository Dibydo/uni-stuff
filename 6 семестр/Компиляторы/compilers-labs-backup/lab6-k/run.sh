flex -o coords.yy.cpp coords.lex
g++ -g -lfl -o coords coords.yy.cpp
./coords < input.txt