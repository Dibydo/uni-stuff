from coords import Position, Fragment
from tok import Token, Tag


class Scanner:
    def __init__(self, input, compiler):
        self.text = input
        self.__compiler = compiler
        self.__cur = Position(self.text)
        self.__comments = []

    def get_comments(self):
        return self.__comments
    
    def add_comment(self, start, end):
        self.__comments.append(Fragment(start, end))

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
                                self.__compiler.add_msg(False, self.__cur.copy(), 'Unexpected EOF')
                                self.__compiler.add_comment(start.copy(), self.__cur.copy())
                                return Token(Tag.COMMENT, start.copy(), self.__cur.copy())
                            self.__cur += 1
                        self.__cur += 1
                        if self.__cur.cp() == '/':
                            closed = True
                            self.__cur += 1

                    pos = self.__cur.copy()
                    self.add_comment(start.copy(), self.__cur.copy())
                    return Token(Tag.COMMENT, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == '"':
                self.__cur += 1

                text = self.__cur.cp()
                self.__cur += 1

                if self.__cur.is_eof():
                    self.__compiler.add_msg(False, self.__cur.copy(), 'Unexpected EOF')
                    return Token(Tag.TERM, start.copy(), self.__cur.copy(), text)
                if self.__cur.cp() != '"':
                    self.__compiler.add_msg(False, self.__cur.copy(), 'Expected close bracket')
                else:
                    self.__cur += 1
                return Token(Tag.TERM, start.copy(), self.__cur.copy(), text)
            elif self.__cur.is_upper_case():
                text = self.__cur.cp()
                self.__cur += 1
                return Token(Tag.NTERM, start.copy(), self.__cur.copy(), text)
            elif self.__cur.cp() == '(':
                self.__cur += 1
                return Token(Tag.LPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == ')':
                self.__cur += 1
                return Token(Tag.RPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == '{':
                self.__cur += 1
                return Token(Tag.LFPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == '}':
                self.__cur += 1
                return Token(Tag.RFPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == '[':
                self.__cur += 1
                return Token(Tag.LSPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == ']':
                self.__cur += 1
                return Token(Tag.RSPAREN, start.copy(), self.__cur.copy())
            elif self.__cur.cp() == ',':
                self.__cur += 1
                return Token(Tag.COMMA, start.copy(), self.__cur.copy())
            else:
                self.__compiler.add_msg(True, self.__cur.copy(), 'Unexpected symbol')
                self.__cur += 1
                break

        return Token(Tag.EOF, self.__cur.copy(), self.__cur.copy())
