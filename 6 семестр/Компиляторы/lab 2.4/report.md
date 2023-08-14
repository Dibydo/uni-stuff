% Лабораторная работа № 2.4 «Множества FIRST для РБНФ»
% 21 июня 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является изучение алгоритма построения множеств FIRST для расширенной формы Бэкуса-Наура.

# Индивидуальный вариант
```
/* варианты разделяются запятыми */
E  ( T {("+","-") T} )
T  ( F {("*","/") F} )
F  ( "n", "-" F,
     "(" E ")" )
```

# Реализация

## Неформальное описание синтаксиса входного языка
Грамматика состоит из определенной последовательности комментариев или правил.
Каждое правило имеет специальную структуру, включающую символы, открывающие и закрывающие скобки.
Выражение, в свою очередь, состоит из конкатенаций, разделенных запятыми, и может повторяться несколько раз.
Конкатенация, в свою очередь, состоит из квантификатора и следующей за ним конкатенации.
Квантификатор может быть представлен выражением в круглых скобках,
выражением в квадратных скобках или литералом. Литерал может быть терминалом,
символом или выражением, заключенным в скобки.

## Лексическая структура
```
COM ::= /* .* */
T    ::= "[a-z]"
NT   ::= [A-Z]
LP   ::= (
RP   ::= )
LFP  ::= {
RFP  ::= }
LSP  ::= [
RSP  ::= ]
CM   ::= ,
```

## Грамматика языка
```
G ::= (C|R)* E
R ::= NT (LP EX RP)
E ::= C, E | C
C ::= Q C | Q
Q ::= {E} | [E] | L
L ::= T | NT | (E)
```

## Программная реализация

main.py
```
import my_comp as cmp
import my_tok as tk
import my_tree as tr

if name == 'main':
with open('input.txt', 'r') as myfile:
txt = myfile.read()
```

scanner.py
```
import my_coords as crd
import my_tok as tk

class MyScanner:
def init(self, input, cmp):
self.text = input
self.__cmp = cmp
self.__cur = crd.Position(self.text)
self.__cmts = []
def get_comments(self):
    return self.__cmts

def add_comment(self, start, end):
    self.__cmts.append(crd.Fragment(start, end))

def next_token(self):
    while self.__cur.cp():
        while self.__cur.is_white_space():
            self.__cur += 1
        start = self.__cur.copy()

        if self.__cur.cp() == '/':
            self.__cur += 1

            if self.__cur.cp() == '*':
                self.__cur += 1
                closed = False

                while not closed:
                    while self.__cur.cp() != '*':
                        if self.__cur.is_eof():
                            self.__cmp.add_msg(False, self.__cur.copy(), 'Unexpected EOF')
                            self.__cmp.add_comment(start.copy(), self.__cur.copy())
                            return tk.Token(tk.Tag.COMMENT, start.copy(), self.__cur.copy())
                        self.__cur += 1
                    self.__cur += 1
                    if self.__cur.cp() == '/':
                        closed = True
                        self.__cur += 1

                pos = self.__cur.copy()
                self.add_comment(start.copy(), self.__cur.copy())
                return tk.Token(tk.Tag.COMMENT, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == '"':
            self.__cur += 1

            text = self.__cur.cp()
            self.__cur += 1

            if self.__cur.is_eof():
                self.__cmp.add_msg(False, self.__cur.copy(), 'Unexpected EOF')
                return tk.Token(tk.Tag.TERM, start.copy(), self.__cur.copy(), text)
            if self.__cur.cp() != '"':
                self.__cmp.add_msg(False, self.__cur.copy(), 'Expected close bracket')
            else:
                self.__cur += 1
            return tk.Token(tk.Tag.TERM, start.copy(), self.__cur.copy(), text)
        elif self.__cur.is_upper_case():
            text = self.__cur.cp()
            self.__cur += 1
            return tk.Token(tk.Tag.NTERM, start.copy(), self.__cur.copy(), text)
        elif self.__cur.cp() == '(':
            self.__cur += 1
            return tk.Token(tk.Tag.LPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == ')':
            self.__cur += 1
            return tk.Token(tk.Tag.RPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == '{':
            self.__cur += 1
            return tk.Token(tk.Tag.LFPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == '}':
            self.__cur += 1
            return tk.Token(tk.Tag.RFPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == '[':
            self.__cur += 1
            return tk.Token(tk.Tag.LSPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == ']':
            self.__cur += 1
            return tk.Token(tk.Tag.RSPAREN, start.copy(), self.__cur.copy())
        elif self.__cur.cp() == ',':
            self.__cur += 1
            return tk.Token(tk.Tag.COMMA, start.copy(), self.__cur.copy())
        else:
            self.__cmp.add_msg(True, self.__cur.copy(), 'Unexpected symbol')
            self.__cur += 1
            break

    return tk.Token(tk.Tag.EOF, self.__cur.copy(), self.__cur.copy())
```

tok.py
```
class MyTag(Enum):
TERM = 1
NTERM = 2
LPAREN = 3
RPAREN = 4
LFPAREN = 5
RFPAREN = 6
LSPAREN = 7
RSPAREN = 8
COMMA = 9
COMMENT = 10
EOF = 0

class MyToken:
def init(self, tag: MyTag, start: MyPosition, end: MyPosition, text: str = ''):
self.tag = tag
self.coords = MyFragment(start, end)
self.text = text
```

comp.py
```
from my_coords import MyFragment, MyPosition
from my_scanner import MyScanner

from collections import OrderedDict

class MyMessage:
def init(self, is_err, text):
self.is_err = is_err
self.text = text

class MyCompiler:
def init(self):
self.__messages = OrderedDict()
def add_msg(self, err: bool, pos: MyPosition, text: str):
    self.__messages[pos] = MyMessage(err, text)

def print_messages(self):
    for pos, msg in self.__messages.items():
        print("Error" if msg.is_err else "Warning", end="")
        print(f" {pos}: {msg.text}")

def get_scanner(self, text):
    return MyScanner(text, self)
```

coords.py
```
import copy

class MyPosition:
def init(self, text: str):
self.__text = text
self.__line = 1
self.__pos = 1
self.__index = 0
def __eq__(self, other):
    return self.__index == other.__index

def __str__(self):
    return f"({self.__line}, {self.__pos})"

def __iadd__(self, other):
    if self.__index < len(self.__text):
        if self.is_new_line():
            if self.__text[self.__index] == '\r':
                self.__index += 1
            self.__line += 1
            self.__pos = 1
        else:
            self.__pos += 1
    self.__index += 1
    return self

def __hash__(self):
    return self.__index.__hash__()

def copy(self):
    return copy.copy(self)

def cp(self):
    if self.__index == len(self.__text):
        return False
    return self.__text[self.__index]

def is_eof(self):
    return self.__index == len(self.__text)

def is_white_space(self):
    return not self.is_eof() and self.cp().isspace()

def is_upper_case(self):
    return not self.is_eof() and self.cp().isupper()

def is_new_line(self):
    if self.__index == len(self.__text):
        return True
    if self.cp() == '\r' and self.__index + 1 < len(self.__text):
        return self.__text[self.__index + 1] == '\n'
    return self.cp() == '\n'
class MyFragment:
def init(self, start, end):
self.start = start
self.end = end
def __str__(self):
    return self.start.__str__() + "-" + self.end.__str__()
```

tree.py
```
import copy

class MyTag:
TERM = 1
NTERM = 2
LPAREN = 3
RPAREN = 4
LFPAREN = 5
RFPAREN = 6
LSPAREN = 7
RSPAREN = 8
COMMA = 9
COMMENT = 10
EOF = 0

class Alternative:
def init(self, concat, alt=None):
self.concat = concat
self.alt = alt
def first(self, firsts):
    if self.alt:
        return self.concat.first(firsts).union(self.alt.first(firsts))
    else:
        return self.concat.first(firsts)

def __str__(self):
    if self.alt:
        return f'{self.concat.__str__()} , {self.alt.__str__()}'
    return self.concat.__str__()
class Concat:
def init(self, quant, concat=None):
self.quant = quant
self.concat = concat

def first(self, firsts):
    if self.concat:
        quant_first = self.quant.first(firsts)
        if 0 in quant_first:
            quant_first.remove(0)
            return quant_first.union(self.concat.first(firsts))
        return quant_first
    else:
        return self.quant.first(firsts)

def __str__(self):
    if self.concat:
        return self.quant.__str__() + self.concat.__str__()
    return self.quant.__str__()
class Quant:
def init(self, t, alt=None, lit=None):
if t == 'lit':
self.lit = lit
self.type = 'lit'
self.alt = None
else:
self.lit = None
self.type = t
self.alt = alt
def first(self, firsts):
    if self.lit:
        return self.lit.first(firsts)
    else:
        return self.alt.first(firsts).union({0})

def __str__(self):
    if self.type == '*':
        return '{' + self.alt.__str__() + '}'
    elif self.type == '?':
        return f'[{self.alt.__str__()}]'
    elif self.type == 'lit':
        return self.lit.__str__()
class Lit:
def init(self, t, symbol=None, alt=None):
self.type = t
self.symbol = symbol
self.alt = alt
def first(self, firsts):
    if self.type == 'term':
        return {self.symbol}
    elif self.type == 'nterm':
        return firsts[self.symbol]
    else:
        return self.alt.first(firsts)

def __str__(self):
    if self.type == 'term':
        return f'"{self.symbol}"'
    elif self.type == 'nterm':
        return self.symbol
    elif self.type == 'alt':
        return f'({self.alt.__str__()})'
class Parser:
def init(self, scanner):
self.__scanner = scanner
self.__Sym = scanner.next_token()
self.rules = None
def print_error(self, token):
    print(f"Error while parsing token: {token.coords}")

def print_first(self):
    firsts = {}
    for nterm in self.rules.keys():
        firsts[nterm] = set()

    changed = True
    while changed:
        changed = False

        for nterm, rule in self.rules.items():
            old = firsts[nterm]
            new = rule.first(firsts)
            if not new.issubset(old):
                changed = True
            firsts[nterm] = firsts[nterm].union(new)

    for nterm, first in firsts.items():
        print(f'{nterm} - {first}')

#         GRAMMAR
# grammar ::= (COMMENT | rule)* EOF
# rule ::= NTERM LPAREN alt RPAREN
# alt ::= concat (COMMA alt)?
# concat ::= quant (concat)?
# quant ::= LFPAREN alt RFPAREN | LSPAREN alt RSPAREN | lit
# lit ::= TERM | NTERM | LPAREN alt RPAREN

def parse(self):
    self.rules = self.grammar()

# grammar ::= (COMMENT | rule)* EOF
def grammar(self):
    trees = {}

    while self.__Sym.tag == MyTag.COMMENT or self.__Sym.tag == MyTag.NTERM:
        if self.__Sym.tag == MyTag.COMMENT:
            self.__Sym = self.__scanner.next_token()
        else:
            k, v = self.rule()
            trees[k] = v

    if self.__Sym.tag != MyTag.EOF:
        self.print_error(self.__Sym)
        return False
    return trees

# rule ::= NTERM LPAREN alt RPAREN
def rule(self):
    if self.__Sym.tag == MyTag.NTERM:
        nterm = self.__Sym.text
        self.__Sym = self.__scanner.next_token()
    else:
        self.print_error(self.__Sym)
        return False
    if self.__Sym.tag == MyTag.LPAREN:
        self.__Sym = self.__scanner.next_token()
    else:
        self.print_error(self.__Sym)
        return False
    tree = self.alt()

    if self.__Sym.tag == MyTag.RPAREN:
        self.__Sym = self.__scanner.next_token()
    else:
        self.print_error(self.__Sym)
        return False

    return nterm, tree

# alt ::= concat (COMMA alt)?
def alt(self):
    c = self.concat()
    if not c:
        return False

    if self.__Sym.tag == MyTag.COMMA:
        self.__Sym = self.__scanner.next_token()
        a = self.alt()
        if not a:
            return False
        return Alternative(c, a)
    return Alternative(c)

# concat ::= quant (concat)?
def concat(self):
    q = self.quant()
    if not q:
        return False

    if self.__Sym.tag == MyTag.LFPAREN \
            or self.__Sym.tag == MyTag.LSPAREN \
            or self.__Sym.tag == MyTag.LPAREN \
            or self.__Sym.tag == MyTag.TERM \
            or self.__Sym.tag == MyTag.NTERM:
        c = self.concat()
        if not c:
            return False
        return Concat(q, c)
    return Concat(q)

# quant ::= LFPAREN alt RFPAREN | LSPAREN alt RSPAREN | lit
def quant(self):
    if self.__Sym.tag == MyTag.LFPAREN:
        self.__Sym = self.__scanner.next_token()
        a = self.alt()
        if not a:
            return False
        if self.__Sym.tag == MyTag.RFPAREN:
            self.__Sym = self.__scanner.next_token()
        else:
            self.print_error(self.__Sym)
            return False
        return Quant('*', alt=a)
    elif self.__Sym.tag == MyTag.LSPAREN:
        self.__Sym = self.__scanner.next_token()
        a = self.alt()
        if not a:
            return False
        if self.__Sym.tag == MyTag.RSPAREN:
            self.__Sym = self.__scanner.next_token()
        else:
            self.print_error(self.__Sym)
            return False
        return Quant('?', alt=a)
    elif self.__Sym.tag == MyTag.TERM \
            or self.__Sym.tag == MyTag.NTERM \
            or self.__Sym.tag == MyTag.LPAREN:
        l = self.lit()
        if not l:
            return False
        return Quant('lit', lit=l)
    else:
        self.print_error(self.__Sym)
        return False

# lit ::= TERM | NTERM | LPAREN alt RPAREN
def lit(self):
    if self.__Sym.tag == MyTag.TERM \
            or self.__Sym.tag == MyTag.NTERM:
        s = self.__Sym.text
        t = 'term' if self.__Sym.tag == MyTag.TERM else 'nterm'
        self.__Sym = self.__scanner.next_token()
        return Lit(t, symbol=s)
    elif self.__Sym.tag == MyTag.LPAREN:
        self.__Sym = self.__scanner.next_token()
        a = self.alt()
        if self.__Sym.tag == MyTag.RPAREN:
            self.__Sym = self.__scanner.next_token()
        else:
            self.print_error(self.__Sym)
            return False
        return Lit('alt', alt=a)
    else:
        self.print_error(self.__Sym)
        return False

```

# Тестирование

Входные данные

```
/* COMMENT */
A ( "x" {("x","y") B} , C)
B ("p" , "q" , ["r"])
C (B , "7")
D ({"4"} ["5"] "6", ["5"])
```

Вывод на `stdout`

```
A - {0, '7', 'x', 'q', 'p', 'r'}
B - {0, 'p', 'q', 'r'}
C - {0, '7', 'p', 'q', 'r'}
D - {0, '4', '5', '6'}
```

# Вывод
В ходе выполнения данной работы изучил алгоритм построения множеств FIRST для расширенной формы Бэкуса-Наура.
