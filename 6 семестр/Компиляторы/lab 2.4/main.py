from comp import Compiler
from tok import Tag
from tree import Parser

if __name__ == '__main__':
    with open('input.txt', 'r') as file:
        text = file.read()

    compiler = Compiler()
    scanner = compiler.get_scanner(text)
    parser = Parser(scanner)
    parser.parse()
    parser.print_first()
    