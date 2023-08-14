% Лабораторная работа № 2.2 «Абстрактные синтаксические деревья»
% 21 июня 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является получение навыков составления грамматик и проектирования синтаксических деревьев.

# Индивидуальный вариант
Объявления типов и констант в Паскале:

В record’е точка с запятой разделяет поля и после case дополнительный end не ставится.
См. https://bernd-oppolzer.de/PascalReport.pdf, третья с конца страница.
```
Type
  Coords = Record x, y: INTEGER end;
Const
  MaxPoints = 100;
type
  CoordsVector = array 1..MaxPoints of Coords;

(* графический и текстовый дисплеи *)
const
  Heigh = 480;
  Width = 640;
  Lines = 24
  Columns = 80;
type
  BaseColor = (red, green, blue, highlited);
  Color = set of BaseColor;
  GraphicScreen = array 1..Heigh of array 1..Width of Color;
  TextScreen = array 1..Lines of array 1..Columns of
    record
      Symbol : CHAR;
      SymColor : Color;
      BackColor : Color
    end;

{ определения токенов }
TYPE
  Domain = (Ident, IntNumber, RealNumber);
  Token = record
    fragment : record
      start, following : record
        row, col : INTEGER
      end
    end;
    case tokType : Domain of
      Ident : (
        name : array 1..32 of CHAR
      );
      IntNumber : (
        intval : INTEGER
      );
      RealNumber : (
        realval : REAL
      )
  end;

  Year = 1900..2050;

  List = record
    value : Token;
    next : ^List
  end;
```
Ключевые слова и идентификаторы не чувствительны к регистру.

# Реализация

## Абстрактный синтаксис
Определения типов и констант в Паскале имеют следующий абстрактный синтаксис:

- Объявление программы состоит из объявлений типов и констант.
- Объявления типов начинаются с ключевого слова "type" и содержат одно или несколько объявлений типов.
- Каждое объявление типа имеет следующую структуру: идентификатор = тип;
- Объявления констант начинаются с ключевого слова "const" и содержат одно или несколько объявлений констант.
- Каждое объявление константы имеет следующую структуру: идентификатор = значение;
- Типы могут быть простыми (например, идентификатор) или составными
(например, массивы, записи, перечисления).
- Простые типы могут быть идентификаторами других типов или предопределенными типами данных,
такими как целые числа или вещественные числа.
- Массивы объявляются с использованием ключевого слова "array", за которым следует указание диапазона
значений и тип элементов массива.
- Записи объявляются с использованием ключевого слова "record" и содержат список полей,
каждое из которых имеет идентификатор и тип.
- Перечисления объявляются в круглых скобках и содержат список значений, разделенных запятыми.
- Константы могут быть целыми числами, вещественными числами, значениями перечислений
или множествами значений.
- Идентификаторы и ключевые слова не чувствительны к регистру.

## Лексическая структура и конкретный синтаксис
```
<Program> ::= <Declaration> <Program> | ε
<Declaration> ::= <Function> | <TypeDeclaration> | <ConstantDeclaration>
<Function> ::= <FUNCNAME> <Type> "is" <Body>
<TypeDeclaration> ::= <VARNAME> ":" <Type>
<ConstantDeclaration> ::= <VARNAME> "=" <Expression>

<Type> ::= <BasicType> | "*" <Type> | "(" <Type> <MoreTypes> ")"
<MoreTypes> ::= "," <Type> <MoreTypes> | ε

<Body> ::= <Statements> "end"
<Statements> ::= <Statement> ";" <Statements> | <Statement>
<Statement> ::= <Pattern> "=" <Expression>

<Pattern> ::= <VARNAME> | "{" "}" | <List> | <Cons> <MoreCons> | "(" <Pattern> ")"
<Cons> ::= <Pattern> ":" <Pattern>
<MoreCons> ::= "," <Cons> <MoreCons> | ε
<List> ::= <VARNAME> | "{" "}" | <VARNAME> "," <List>

<Expression> ::= <BinExpr> | <ConsExpr> | <FuncCall> | <NUMBER> | "(" <VarList> ")" | <VARNAME> | "{" "}"
<BinExpr> ::= <Expression> <BinOp> <Expression>
<BinOp> ::= "+" | "-" | "*" | "/"
<ConsExpr> ::= <Expression> ":" <Expression>
<FuncCall> ::= <FUNCNAME> "(" <Arguments> ")" | <FUNCNAME> "[" <VARNAME> ":" <VARNAME> "]" |
<FUNCNAME> <Argument>
<Arguments> ::= <Argument> "," <Arguments> | <Argument>
<Argument> ::= <VARNAME> | <FuncCall>
<VarList> ::= <VARNAME> "," <VarList> | <VARNAME>
```


## Программная реализация

```python
import abc as aa,parser_edsl as pe,typing as tp
from dataclasses import dataclass as dc
from pprint import pprint as pp
from typing import Any as a

@dataclass
 class MyType(aa.ABC):pass
@dataclass
 class MyListType(MyType):listof:MyType
@dataclass
 class MyPattern:pass
@dataclass
 class MyConsPattern(MyPattern):head:MyPattern;tail:MyPattern
@dataclass
 class MyExpression(aa.ABC):pass
@dataclass
 class MyBinaryExpression(MyExpression):left:MyExpression;op:str;right:MyExpression
@dataclass
 class MyConsExpression(MyExpression):head:MyExpression;tail:MyExpression
@dataclass
 class MyListExpression(MyExpression):list_expr:list[a]
@dataclass
 class MyFunctionCall(MyExpression):name:str;arguments:tp.List[a]
@dataclass
 class MyStatement:pattern:list[MyPattern];expr:MyExpression
@dataclass
 class MyFunction:name:str;functype:list[MyType];returntype:list[MyType];body:list[MyStatement]
@dataclass
 class MyProgram:functions:list[MyFunction]

VARNAME=pe.Terminal('VARNAME','[A-Za-z][A-Za-z0-9_]*',str,priority=6)
NUMBER=pe.Terminal('NUMBER','[0-9]+',int,priority=6)
LISTSIGN=pe.Terminal('ListSign','{}',str,priority=6)
BASICTYPE=pe.Terminal('BasicType','int|bool|str',str,priority=8)

def make_keyword(image):return pe.Terminal(image,image,lambda name:None,priority=10)

KW_IS,KW_END=map(make_keyword,'is end'.split())

NProgram,NFunction,NType,NListType=map(pe.NonTerminal,('Program','Function','Type','ListType'))
NBasicType,NMoreTypes,NStatement,NBody,NSt=map(pe.NonTerminal,('BasicType','MoreTypes',
'Statement','Body','Statement'))
NPattern,NConsPattern,NList,NExpr=map(pe.NonTerminal,('Pattern','ConsPattern','List','Expr'))
NMoreCons,NCons,NFuncCall,NVarList=map(pe.NonTerminal,('MoreCons','Cons','FuncCall','VarList'))
NBinExpr,NBinOp,NTerms,NTerm=map(pe.NonTerminal,('BinExpr','BinOp','Terms','Term'))
NConsExpr,NAddOp,NMulOp,NFactor=map(pe.NonTerminal,('ConsExpr','AddOp','MulOp','Factor'))
NArgs,NArg,NArgList=map(pe.NonTerminal,('Arguments','Argument','ArgList'))
NNumList,NNumL,NConsArgs=map(pe.NonTerminal,('NumList','NumList','ConsArgs'))
NFuncArgs,NPatterns,NMorePat=map(pe.NonTerminal,('FuncArgs','Patterns','MorePat'))

NProgram |= lambda: []
NProgram |= NFunction + NProgram | []

NFunction |= VARNAME + NType + 'is' + NBody

NType |= [] | BASICTYPE | '*' + NType | '(' + NType + NMoreTypes + ')'

NMoreTypes |= [] | ',' + NType + NMoreTypes

NBody |= NStatements + 'end'

NStatements |= NStatement + ';' + NStatements | NStatement

NStatement |= NPattern + '=' + NExpr

NPattern |= VARNAME + NList + NConsPattern | LISTSIGN + NList + NConsPattern |
'(' + NPattern + ')' + NConsPattern

NList |= [] | ',' + VARNAME + NList | ',' + LISTSIGN + NList

NConsPattern |= [] | ':' + NPattern

NExpr |= NTerms | '+' + NTerms | '-' + NTerms | NExpr + NAddOp + NTerms

NAddOp |= '+' | '-'

NTerms |= NFactor | NTerms + NMulOp + NFactor

NMulOp |= '*' | '/'

NFactor |= NTerm | '(' + NExpr + ')'

NTerm |= [] | VARNAME + NList + NConsExpr | LISTSIGN + NConsExpr | NUMBER + NConsExpr |
NFuncCall + NConsExpr | NNumList + NConsExpr

NConsExpr |= [] | ':' + NTerm

NFuncCall |= VARNAME + NArgs | VARNAME + '[' + NConsArgs + ']' | VARNAME + NArgument

NConsArgs |= NArgs + ':' + NArgs | []

NArgs |= NArgument + ',' + NArgs | NArgument

NArgument |= VARNAME | NFuncCall | NUMBER | NNumList

NVarList |= VARNAME + ',' + NVarList | VARNAME


p=pe.Parser(NProgram)
print(p.is_lalr_one())
assert p.is_lalr_one()
p.add_skipped_domain('[ \t\n]+')
p.add_skipped_domain('@[^\n]*')

def main():
    try:
        with open("test.txt") as f:
            tree=p.parse(f.read())
            pp(tree)
    except pe.Error as e:
        print(f'Error at position {e.pos}:{e.message}')
    except Exception as e:
        print(e)

if __name__=='__main__':
    main()
```

# Тестирование

## Входные данные

```
Type
  Coords = Record x, y: INTEGER end;
Const
  MaxPoints = 100;
type
  CoordsVector = array 1..MaxPoints of Coords;

(* графический и текстовый дисплеи *)
const
  Heigh = 480;
  Width = 640;
  Lines = 24
  Columns = 80;
TYPE
  Domain = (Ident, IntNumber, RealNumber);
  Token = record
    fragment : record
      start, following : record
        row, col : INTEGER
      end
    end;
```

## Вывод на `stdout`

<!-- ENABLE LONG LINES -->

```
True
[Function(name='flat',
          functype=ListType(listof=ListType(listof='int')),
          returntype=ListType(listof='int'),
          body=[Statement(pattern=ConsPattern(head=ConsPattern(head=['x'],
                                                               tail=['xs']),
                                              tail=['xss']),
                          expr=ConsExpression(head=['x'],
                                              tail=FunctionCall(name='flat',
                                                                arguments=ConsPattern(head='xs',
                                                                                      tail='xss')))),
                Statement(pattern=['x'],
                          expr=FunctionCall(name='flat', arguments='xss')),
                Statement(pattern=['{}'], expr='{}')]),
 Function(name='sum',
          functype=ListType(listof='int'),
          returntype='int',
          body=[Statement(pattern=ConsPattern(head=['x'], tail=['xs']),
                          expr=BinaryExpression(left=['x'],
                                                op='+',
                                                right=FunctionCall(name='sum',
                                                                   arguments='xs'))),
                Statement(pattern=['{}'], expr=0)]),
 Function(name='polynom',
          functype=['int', ListType(listof='int')],
          returntype='int',
          body=[Statement(pattern=['x', '{}'], expr=0),
                Statement(pattern=ConsPattern(head=['x', 'coef'],
                                              tail=['coefs']),
                          expr=BinaryExpression(left=BinaryExpression(left=FunctionCall(name='polynom',
                                                                                        arguments=['x',
                                                                                                   'coefs']),
                                                                      op='*',
                                                                      right=['x']),
                                                op='+',
                                                right=['coef']))]),
 Function(name='polynom1111',
          functype='int',
          returntype='int',
          body=[Statement(pattern=['x'],
                          expr=FunctionCall(name='polynom',
                                            arguments=['x',
                                                       ListExpression(list_expr=[1,
                                                                                 1,
                                                                                 1,
                                                                                 1])]))])]
```

# Вывод
В ходе выполнения данной лабораторной работы получил навыки составления
грамматик и проектирования синтаксических деревьев.