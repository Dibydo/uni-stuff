from enum import Enum
from coords import Fragment, Position


class Tag(Enum):
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


class Token:
    def __init__(self, tag: Tag, start: Position, end: Position, text: str = ''):
        self.tag = tag
        self.coords = Fragment(start, end)
        self.text = text
