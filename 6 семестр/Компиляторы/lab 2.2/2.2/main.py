# Статически типизированный функциональный язык программирования с сопоставлением с образцом:
# Комментарии начинаются на знак @.
# Все функции в рассматриваемом языке являются функциями одного аргумента. Когда нужно вызвать функцию с несколькими аргументами, они передаются в виде кортежа.
# Круглые скобки служат для создания кортежа, фигурные — для создания списка, квадратные — для указания приоритета.
# Наивысший приоритет имеет операция вызова функции. Вызов функции правоассоциативен, т.е. выражение x y z трактуется как x [y z] (аргументом функции y является z, аргументом функции x — выражение y z.
# За вызовом функции следуют арифметические операции *, /, +, - с обычным приоритетом (у * и / он выше, чем у + и -) и ассоциативностью (левая).
# Наинизшим приоритетом обладает операция создания cons-ячейки :, ассоциативность — правая (т.е. x : y : z трактуется как x : [y : z]).
# Функция состоит из заголовка, в котором указывается её тип, и тела, содержащего несколько предложений. Предложения разделяются знаком ;.
# Предложение состоит из образца и выражения, разделяемых знаком =. В образце, в отличие от выражения, недопустимы арифметические операции и вызовы функций.
# Тип списка описывается с помощью одноместной операции *, предваряющей тип, тип кортежа — как перечисление типов элементов через запятую в круглых скобках.
# 1) проверка на совпадение количества аргументов в функции и ее паттернах
# 2) проверка на свопадение количества аргументов в функции и вызове сей функции
# 3) проверка, если в аргументах функции не массив, то в соответствующих местах в паттернах это2 функции тоже не должно быть массива
# 4) проверка интовых штук на int()

import abc
import parser_edsl as pe
import typing
from dataclasses import dataclass
from pprint import pprint
from typing import Any

@dataclass
class Type(abc.ABC):
    pass

@dataclass
class ListType(Type):
    listof: Type

@dataclass
class Pattern:
    pass

@dataclass
class ConsPattern(Pattern):
    head: Pattern
    tail: Pattern

@dataclass
class Expression(abc.ABC):
    pass

@dataclass
class BinaryExpression(Expression):
    left: Expression
    op: str
    right: Expression


@dataclass
class ConsExpression(Expression):
    head: Expression
    tail: Expression

@dataclass
class ListExpression(Expression):
    list_expr : list[Any]


@dataclass
class FunctionCall(Expression):
    name: str
    arguments: typing.List[Any]

@dataclass
class Statement:
    pattern: list[Pattern]
    expr: Expression

@dataclass
class Function:
    name: str
    functype: list[Type]
    returntype: list[Type]
    body: list[Statement]

@dataclass
class Program:
    functions: list[Function]


VARNAME = pe.Terminal('VARNAME', '[A-Za-z][A-Za-z0-9_]*', str, priority=6)
NUMBER = pe.Terminal('NUMBER', '[0-9]+', int, priority=6)
LISTSIGN = pe.Terminal('ListSign', '{}', str, priority=6)
BASICTYPE = pe.Terminal('BasicType', 'int|bool|str', str, priority=8)

def make_keyword(image):
    return pe.Terminal(image, image, lambda name: None, priority=10)

KW_IS, KW_END = map(make_keyword, 'is end'.split())

NProgram, NFunction, NType, NListType = map(pe.NonTerminal, ('Program', 'Function', 'Type', 'ListType'))
NBasicType, NMoreTypes, NStatement, NBody, NSt = map(pe.NonTerminal, ('BasicType', 'MoreTypes', 'Statement', 'Body', 'Statement'))
NPattern, NConsPattern, NList, NExpr = map(pe.NonTerminal, ('Pattern', 'ConsPattern', 'List', 'Expr'))
NMoreCons, NCons, NFuncCall, NVarList = map(pe.NonTerminal, ('MoreCons', 'Cons', 'FuncCall', 'VarList'))
NBinExpr, NBinOp, NTerms, NTerm = map(pe.NonTerminal, ('BinExpr', 'BinOp', 'Terms', 'Term'))
NConsExpr, NAddOp, NMulOp, NFactor = map(pe.NonTerminal, ('ConsExpr', 'AddOp', 'MulOp', 'Factor'))
NArgs, NArg, NArgList = map(pe.NonTerminal, ('Arguments', 'Argument', 'ArgList'))
NNumList, NNumL, NConsArgs = map(pe.NonTerminal, ('NumList', 'NumList', 'ConsArgs'))
NFuncArgs, NPatterns, NMorePat = map(pe.NonTerminal, ('FuncArgs', 'Patterns', 'MorePat'))

# Grammar
NProgram |= lambda: []
NProgram |= NFunction, NProgram, lambda f, pr: [f] + pr

NFunction |= VARNAME, NType, '::', NType, KW_IS, NBody, KW_END, lambda n, t, rt, b: Function(n, t, rt, b)

# Types
NType |= lambda: []
NType |= BASICTYPE
NType |= '*', NType, lambda t: ListType(t)
NType |= '(', NType, NMoreTypes, ')', lambda t, m: [t] + m if m else [t]
NMoreTypes |= lambda: []
NMoreTypes |= ',', NType, NMoreTypes, lambda t, m: [t] + m if m else [t]

# Body = Statements
NBody |= lambda: []
NBody |= NStatement, NSt, lambda s, b: [s] + b
NSt |= lambda: []
NSt |= ';', NStatement, NSt, lambda s, b: [s] + b
NStatement |= NPattern, '=', NExpr, lambda p, e: Statement(p, e)

# Pattern
NPattern |= lambda: []
NPattern |= VARNAME, NList, NConsPattern, lambda v, p, c: ConsPattern([v]+p, c) if c else [v] + p
NPattern |= LISTSIGN, NList, NConsPattern, lambda v, p, c: ConsPattern([v]+p, c) if c else [v] + p
NPattern |= '(', NPattern, ')', NConsPattern, lambda p, c: ConsPattern(p, c) if c else p

NList |= lambda: []
NList |= ',', VARNAME, NList, lambda v, l: [v] + l
NList |= ',', LISTSIGN, NList, lambda v, l: [v] + l

NConsPattern |= lambda: []
NConsPattern |= ':', NPattern

# Expression
NExpr |= NTerms
NExpr |= '+', NTerms
NExpr |= '-', NTerms
NExpr |= NExpr, NAddOp, NTerms, BinaryExpression

NAddOp |= '+', lambda: '+'
NAddOp |= '-', lambda: '-'

NTerms |= NFactor
NTerms |= NTerms, NMulOp, NFactor, BinaryExpression

NMulOp |= '*', lambda: '*'
NMulOp |= '/', lambda: '/'

NFactor |= NTerm
NFactor |= '(', NExpr, ')'

NTerm |= VARNAME, NList, NConsExpr, lambda v, n, c: ConsExpression([v] + n, c) if c else [v] + n
NTerm |= LISTSIGN, NConsExpr, lambda n, c: ConsExpression(n, c) if c else n
NTerm |= NUMBER, NConsExpr, lambda n, c: ConsExpression(n, c) if c else n
NTerm |= NFuncCall, NConsExpr, lambda n, c: ConsExpression(n, c) if c else n
NTerm |= NNumList, NConsExpr, lambda n, c: ConsExpression(n, c) if c else n

NConsExpr |= lambda: []
NConsExpr |= ':', NTerm

NFuncCall |= VARNAME, NArgs, FunctionCall
NFuncCall |= VARNAME, '[', NConsArgs, ']', FunctionCall

NConsArgs |= NArgs, ":", NArgs, ConsPattern
NArgs |= NArg
NArgs |= '(', NArg, NArgList, ')', lambda a, l: [a] + l
NArgList |= lambda: []
NArgList |= ',', NArg, NArgList, lambda v, l: [v] + l
NArg |= NFuncCall
NArg |= VARNAME
NArg |= NUMBER
NArg |= NNumList

NNumList |= '{', NUMBER, NNumL, '}', lambda n, l: ListExpression([n] + l)
NNumL |= lambda: []
NNumL |= ',', NUMBER, NNumL, lambda n, l: [n] + l if l else [n]


p = pe.Parser(NProgram)
print(p.is_lalr_one())
assert p.is_lalr_one()
p.add_skipped_domain('[ \t\n]+')
p.add_skipped_domain('@[^\n]*')


def main():
    try:
        with open("test.txt") as f:
            tree = p.parse(f.read())
            pprint(tree)
    except pe.Error as e:
        print(f'Ошибка {e.pos}: {e.message}')
    except Exception as e:
        print(e)


if __name__ == '__main__':
    main()
