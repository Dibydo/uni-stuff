% Лабораторная работа № 2.3 «Синтаксический анализатор на основе
  предсказывающего анализа»
% 18 апреля 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является изучение алгоритма построения таблиц предсказывающего анализатора.

# Индивидуальный вариант
```
/* аксиома помечена звёздочкой */
  F  ("n") ("(" E ")")
  T  (F T')
  T' ("*" F T') ()
* E  (T E')
  E' ("+" T E') ()
```

# Реализация

## Неформальное описание синтаксиса входного языка
• Символ S представляет основное правило, которое содержит правила и аксиому.
• Правила состоят из правила и списка правил или могут быть пустыми.
• Аксиома состоит из символа аксиомы и правила.
• Правило состоит из нетерминального символа, знака равенства и правой части.
• Правая часть может содержать альтернативные варианты, состоящие из терминальных и нетерминальных символов,
а также может быть пустой.

## Лексическая структура
```
• комментарий = /*.*/
• T = “[a-z*+()]+”
• NonT = [A-Z]+\’?
• EqSign = \s\s
• AxSign = *
• NLSign = \n
• Alt_Start = (
• Alt_End = )
```

## Грамматика языка
• S ::= Rules  Axioma Rules
• Rules ::= Rule Rules | eps
• Axioma ::= AxSign Rule
• Rule ::= NonT EqSign RightPart
• RightPart ::= AltStart Altern AltEnd RightPart | eps
• Altern ::= T Altern | NonT Altern | eps

## Программная реализация

main.py
```
import io

def main():
    i = 0
    toks = []
    try:
        with open("test.txt", "r") as file:
            for line in file:
                i += 1
                ide = LexicAnalysis()
                t = ide.main(line, i)
                toks.extend(t)
    except IOError as ex:
        print(ex)

    t = Token()
    t.type = "ENDOFFILE"
    toks.append(t)

    parser = SyntaxAnalysis()
    parser.init_table()
    tree = parser.topDownParse(toks)
    tree.print("")
```

lexican.py
```
import re

class LexicAnalysis:
    def __init__(self):
        self.tokens = []

    def search(self, text, aaa):
        # Regular expressions
        T = r'\s?"[a-z+*()]*"'
        NonT = r" ?[A-Z]+'?"
        AxSign = r'\*\s'
        AltStart = r' *\('
        AltEnd = r'\)'
        Space = r' (?=[^(\\s])'
        comment = r'/\*.*\*/'
        pattern = f"(?P<T>{T})|(?P<NonT>{NonT})|" \
                  f"(?P<comment>{comment})|(?P<AxSign>{AxSign})|" \
                  f"(?P<Space>{Space})|(?P<AltStart>{AltStart}\\s*" + r")|" \
                  f"(?P<AltEnd>{AltEnd})"

        index = 0
        while True:
            if len(text) == 0:
                break
            else:
                # Match the text with the regular expression
                match = re.match(pattern, text)
                if match:
                    if match.group("comment"):
                        if match.group("comment") == text:
                            break
                        index += len(match.group("comment"))
                        text = text[len(match.group("comment")):]

                    elif match.group("AxSign"):
                        if match.group("AxSign") == text:
                            break
                        toks = Token()
                        toks.tok = match.group("AxSign")
                        toks.type = "AxSign"
                        index += len(match.group("AxSign"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("AxSign")):]

                    elif match.group("AltStart"):
                        if match.group("AltStart") == text:
                            break
                        toks = Token()
                        toks.tok = "("
                        toks.type = "AltStart"
                        index += len(match.group("AltStart"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("AltStart")):]

                    elif match.group("AltEnd"):
                        if match.group("AltEnd") == text:
                            break
                        toks = Token()
                        toks.tok = match.group("AltEnd")
                        toks.type = "AltEnd"
                        index += len(match.group("AltEnd"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("AltEnd")):]

                    elif match.group("T"):
                        if match.group("T") == text:
                            break
                        toks = Token()
                        toks.tok = match.group("T")
                        toks.type = "T"
                        index += len(match.group("T"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("T")):]

                    elif match.group("NonT"):
                        if match.group("NonT") == text:
                            break
                        toks = Token()
                        toks.tok = match.group("NonT")
                        toks.type = "NonT"
                        index += len(match.group("NonT"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("NonT")):]

                    elif match.group("Space"):
                        if match.group("Space") == text:
                            break
                        toks = Token()
                        toks.tok = match.group("Space")
                        toks.type = "Space"
                        index += len(match.group("Space"))
                        toks.index_str = index
                        toks.index_file = aaa
                        self.tokens.append(toks)
                        text = text[len(match.group("Space")):]
                else:
                    index += 1
                    if text[0] != ' ':
                        print(f"syntax error ({aaa},{index})")
                    text = text[1:]

    def main(self, text, aaa):
        self.search(text, aaa)
        return self.tokens
```

syntaxan.py
```
class Node:
    def print(self, indent):
        pass

class Leaf(Node):
    def __init__(self, a):
        self.tok = a

    def print(self, indent):
        if self.tok.type == "T" or self.tok.type == "NonT":
            print(indent + "Leaf: " + self.tok.type + " " + self.tok.tok)
        else:
            print(indent + "Leaf: " + self.tok.type)

class Inner(Node):
    def __init__(self):
        self.nterm = ""
        self.ruleId = 0
        self.children = []

    def print(self, indent):
        print(indent + "Inner node: " + self.nterm)
        for child in self.children:
            child.print(indent + "\t")

class SyntaxAnalysis:
    def __init__(self):
        self.crossing = {}

    def init_table(self):
        self.crossing = {
            "S AxSign": ["Axiom", "S"],
            "S NonT": ["Rules", "S"],
            "S ENDOFFILE": [],
            "Axiom AxSign": ["AxSign", "NonT", "RightPart"],
            "Rules NonT": ["Rule", "Rules"],
            "Rules AxSign": [],
            "Rule NonT": ["NonT", "RightPart"],
            "RightPart NonT": [],
            "RightPart AxSign": [],
            "RightPart AltStart": ["AltStart", "Altern", "AltEnd", "RightPart"],
            "Altern T": ["T", "Altern"],
            "Altern NonT": ["NonT", "Altern"],
            "Altern AltEnd": []
        }

    def is_term(self, s):
        return s not in ["S", "Axiom", "Rules", "Rule", "RightPart", "Altern"]

    def top_down_parse(self, toks):
        sparent = Inner()
        stack_in = [sparent]
        stack_str = ["S"]
        i = 0
        a = toks[i]
        i += 1
        while i < len(toks) and toks[i].type != "ENDOFPROGRAM":
            parent = stack_in.pop()
            X = stack_str.pop()
            if self.is_term(X):
                if X == a.type:
                    parent.children.append(Leaf(a))
                    a = toks[i]
                    i += 1
                else:
                    self.err("Expected " + X + ", received " + a.type, a)
            elif X + " " + a.type in self.crossing:
                inner = Inner()
                inner.nterm = X
                inner.children = []
                parent.children.append(inner)
                array = self.crossing[X + " " + a.type]
                for j in range(len(array) - 1, -1, -1):
                    stack_in.append(inner)
                    stack_str.append(array[j])
            else:
                self.err("Expected " + X + ", received " + a.type, a)
        return sparent.children[0]

    def err(self, err_str, tok):
        print("(" + str(tok.index_file) + "," + str(tok.index_str) + ") " + err_str)
```

token.py
```
class Token:
    def __init__(self):
        self.index_str = 0
        self.index_file = 0
        self.tok = ""
        self.type = ""
```

# Тестирование

Входные данные

```
F("n")
(
    "("
    E
    ")"
)
T (F T')
T' ("*" F T') (/* empty */) * E (T E')
E' ("+" T E') ()
```

Вывод на `stdout`

```
Inner node: S
	Inner node: Rules
		Inner node: Rule
			Leaf: NonT F
			Inner node: RightPart
				Leaf: AltStart
				Inner node: Altern
					Leaf: T "n"
					Inner node: Altern
				Leaf: AltEnd
				Inner node: RightPart
					Leaf: AltStart
					Inner node: Altern
						Leaf: T "("
						Inner node: Altern
							Leaf: NonT E
							Inner node: Altern
								Leaf: T ")"
								Inner node: Altern
					Leaf: AltEnd
					Inner node: RightPart
		Inner node: Rules
			Inner node: Rule
				Leaf: NonT T
				Inner node: RightPart
					Leaf: AltStart
					Inner node: Altern
						Leaf: NonT F
						Inner node: Altern
							Leaf: NonT  T'
							Inner node: Altern
					Leaf: AltEnd
					Inner node: RightPart
			Inner node: Rules
				Inner node: Rule
					Leaf: NonT T'
					Inner node: RightPart
						Leaf: AltStart
						Inner node: Altern
							Leaf: T "*"
							Inner node: Altern
								Leaf: NonT  F
								Inner node: Altern
									Leaf: NonT  T'
									Inner node: Altern
						Leaf: AltEnd
						Inner node: RightPart
							Leaf: AltStart
							Inner node: Altern
							Leaf: AltEnd
							Inner node: RightPart
				Inner node: Rules
	Inner node: S
		Inner node: Axiom
			Leaf: AxSign
			Leaf: NonT E
			Inner node: RightPart
				Leaf: AltStart
				Inner node: Altern
					Leaf: NonT T
					Inner node: Altern
						Leaf: NonT  E'
						Inner node: Altern
				Leaf: AltEnd
				Inner node: RightPart
		Inner node: S
			Inner node: Rules
				Inner node: Rule
					Leaf: NonT E'
					Inner node: RightPart
						Leaf: AltStart
						Inner node: Altern
							Leaf: T "+"
							Inner node: Altern
								Leaf: NonT  T
								Inner node: Altern
									Leaf: NonT  E'
									Inner node: Altern
						Leaf: AltEnd
						Inner node: RightPart
							Leaf: AltStart
							Inner node: Altern
							Leaf: AltEnd
```

# Вывод
В процессе выполнения данной лабораторной работы я ознакомился с алгоритмом, который используется для
создания таблиц предсказывающего анализатора, и углубил свои знания в этой области.