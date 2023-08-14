from tok import Tag


class Alternative:
    def __init__(self, concat, alt=None):
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
    def __init__(self, quant, concat=None):
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
    def __init__(self, t, alt=None, lit=None):
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
        if self.type=='*':
            return '{' + self.alt.__str__() + '}'
        elif self.type=='?':
            return f'[{self.alt.__str__()}]'
        elif self.type=='lit':
            return self.lit.__str__()


class Lit:
    def __init__(self, t, symbol=None, alt=None):
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
    def __init__(self, scanner):
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

        while self.__Sym.tag == Tag.COMMENT or self.__Sym.tag == Tag.NTERM:
            if self.__Sym.tag == Tag.COMMENT:
                self.__Sym = self.__scanner.next_token()
            else:
                k, v = self.rule()
                trees[k] = v

        if self.__Sym.tag != Tag.EOF:
            self.print_error(self.__Sym)
            return False
        return trees

    # rule ::= NTERM LPAREN alt RPAREN
    def rule(self):
        if self.__Sym.tag == Tag.NTERM:
            nterm = self.__Sym.text
            self.__Sym = self.__scanner.next_token()
        else:
            self.print_error(self.__Sym)
            return False
        if self.__Sym.tag == Tag.LPAREN:
            self.__Sym = self.__scanner.next_token()
        else:
            self.print_error(self.__Sym)
            return False
        tree = self.alt()

        if self.__Sym.tag == Tag.RPAREN:
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

        if self.__Sym.tag == Tag.COMMA:
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

        if self.__Sym.tag == Tag.LFPAREN \
                or self.__Sym.tag == Tag.LSPAREN \
                or self.__Sym.tag == Tag.LPAREN \
                or self.__Sym.tag == Tag.TERM \
                or self.__Sym.tag == Tag.NTERM:
            c = self.concat()
            if not c:
                return False
            return Concat(q, c)
        return Concat(q)

    # quant ::= LFPAREN alt RFPAREN | LSPAREN alt RSPAREN | lit
    def quant(self):
        if self.__Sym.tag == Tag.LFPAREN:
            self.__Sym = self.__scanner.next_token()
            a = self.alt()
            if not a:
                return False
            if self.__Sym.tag == Tag.RFPAREN:
                self.__Sym = self.__scanner.next_token()
            else:
                self.print_error(self.__Sym)
                return False
            return Quant('*', alt=a)
        elif self.__Sym.tag == Tag.LSPAREN:
            self.__Sym = self.__scanner.next_token()
            a = self.alt()
            if not a:
                return False
            if self.__Sym.tag == Tag.RSPAREN:
                self.__Sym = self.__scanner.next_token()
            else:
                self.print_error(self.__Sym)
                return False
            return Quant('?', alt=a)
        elif self.__Sym.tag == Tag.TERM \
                or self.__Sym.tag == Tag.NTERM \
                or self.__Sym.tag == Tag.LPAREN:
            l = self.lit()
            if not l:
                return False
            return Quant('lit', lit=l)
        else:
            self.print_error(self.__Sym)
            return False

    # lit ::= TERM | NTERM | LPAREN alt RPAREN
    def lit(self):
        if self.__Sym.tag == Tag.TERM \
                or self.__Sym.tag == Tag.NTERM:
            s = self.__Sym.text
            t = 'term' if self.__Sym.tag == Tag.TERM else 'nterm'
            self.__Sym = self.__scanner.next_token()
            return Lit(t, symbol=s)
        elif self.__Sym.tag == Tag.LPAREN:
            self.__Sym = self.__scanner.next_token()
            a = self.alt()
            if self.__Sym.tag == Tag.RPAREN:
                self.__Sym = self.__scanner.next_token()
            else:
                self.print_error(self.__Sym)
                return False
            return Lit('alt', alt=a)
        else:
            self.print_error(self.__Sym)
            return False
