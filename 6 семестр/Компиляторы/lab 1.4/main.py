class Token:
    def __init__(self, tag, start_pos, end_pos, value):
        self.tag = tag
        self.start_pos = start_pos
        self.end_pos = end_pos
        self.value = value

    def __repr__(self):
        return f"{self.tag} ({self.start_pos}:{self.end_pos}): {self.value}"


class Lexer:
    def __init__(self, input_string):
        self.input_string = input_string
        self.current_pos = 0

    def get_next_token(self):
        while self.current_pos < len(self.input_string):
            # Пропускаем пробелы
            if self.input_string[self.current_pos].isspace():
                self.current_pos += 1
                continue

            start_pos = self.current_pos

            # Распознавание идентификаторов
            if self.input_string[self.current_pos].isalpha():
                while (self.current_pos < len(self.input_string) and
                       (self.input_string[self.current_pos].isalpha() or self.input_string[self.current_pos].isdigit())):
                    self.current_pos += 1
                value = self.input_string[start_pos:self.current_pos]
                return Token("идентификатор", start_pos, self.current_pos - 1, value)

            # Распознавание целочисленных литералов
            if self.input_string[self.current_pos].isdigit():
                while (self.current_pos < len(self.input_string) and
                       self.input_string[self.current_pos].isdigit()):
                    self.current_pos += 1
                value = self.input_string[start_pos:self.current_pos]
                return Token("целочисленный литерал", start_pos, self.current_pos - 1, value)

            # Распознавание ключевых слов
            keywords = ["fun", "let", "in"]
            for keyword in keywords:
                if self.input_string.startswith(keyword, self.current_pos):
                    self.current_pos += len(keyword)
                    return Token("ключевое слово", start_pos, self.current_pos - 1, keyword)

            # Распознавание знаков операций
            operators = [":", "::"]
            for operator in operators:
                if self.input_string.startswith(operator, self.current_pos):
                    self.current_pos += len(operator)
                    return Token("знак операции", start_pos, self.current_pos - 1, operator)

            # Распознавание строковых литералов
            if self.input_string[self.current_pos] == "`":
                self.current_pos += 1
                while (self.current_pos < len(self.input_string) and
                       self.input_string[self.current_pos] != "`"):
                    self.current_pos += 1
                if self.input_string[self.current_pos] == "`":
                    self.current_pos += 1
                    value = self.input_string[start_pos + 1:self.current_pos - 1]
                    return Token("строковый литерал", start_pos, self.current_pos - 1, value)

            # Если ни один токен не распознан, генерируем ошибку
            raise ValueError(f"Нераспознанный токен: {self.input_string[self.current_pos]} at position {self.current_pos}")

        # Достигнут конец строки, возвращаем None
        return None


# Пример использования лексического анализатора
lexer = Lexer('fun let in : :: `строковый литерал`')
token = lexer.get_next_token()
while token is not None:
    print(token)
    token = lexer.get_next_token()

