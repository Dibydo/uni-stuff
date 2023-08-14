from coords import Fragment, Position
from scanner import Scanner

from collections import OrderedDict


class Message:
    def __init__(self, is_err, text):
        self.is_err = is_err
        self.text = text


class Compiler:
    def __init__(self):
        self.__messages = OrderedDict()

    def add_msg(self, err: bool, pos: Position, text: str):
        self.__messages[pos] = Message(err, text)

    def print_messages(self):
        for pos, msg in self.__messages.items():
            print("Error" if msg.is_err else "Warning", end="")
            print(f" {pos}: {msg.text}")

    def get_scanner(self, text):
        return Scanner(text, self)



