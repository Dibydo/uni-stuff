import copy


class Position:
    def __init__(self, text: str):
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


class Fragment:
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def __str__(self):
        return self.start.__str__() + "-" + self.end.__str__()

