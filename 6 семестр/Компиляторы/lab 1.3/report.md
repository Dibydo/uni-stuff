% Лабораторная работа № 1.3 «Объектно-ориентированный
  лексический анализатор»
% 9 июня 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является приобретение навыка реализации лексического анализатора на
объектно-ориентированном языке без применения каких-либо средств автоматизации
решения задачи лексического анализа.

# Индивидуальный вариант
Идентификаторы переменных: последовательности буквенных символов Unicode и цифр,
начинающиеся на знаки «$», «@», «%». Имена функций: последовательности буквенных символов Unicode и цифр,
начинающиеся на букву. Ключевые слова «sub», «if», «unless».

# Реализация

```python
class LexicalAnalyzer:
    def __init__(self, input_file):
        self.input_file = input_file
        self.current_line = 1
        self.current_column = 1
        self.keywords = {'sub', 'if', 'unless'}
        self.identifier_table = {}
        self.identifier_count = 1

    def analyze(self):
        with open(self.input_file, 'r', encoding='utf-8') as file:
            for line in file:
                yield from self.process_line(line.rstrip())

        yield 'EOF', '', (self.current_line, self.current_column), (self.current_line, self.current_column)

    def process_line(self, line):
        word = ''

        for char in line:
            if char.isspace():
                if word:
                    yield from self.process_token(word)
                    word = ''
                continue

            word += char

        if word:
            yield from self.process_token(word)

        self.current_line += 1
        self.current_column = 1

    def process_token(self, token):
        start_pos = (self.current_line, self.current_column)
        end_pos = (self.current_line, self.current_column + len(token) - 1)
        token_type = self.get_token_type(token)

        if token_type == 'VARIABLE':
            token_id = self.get_identifier_id(token)
            yield token_type, token_id, start_pos, end_pos
        else:
            yield token_type, token, start_pos, end_pos

        self.current_column += len(token)

    def get_token_type(self, token):
        if token.startswith('$') or token.startswith('@') or token.startswith('%'):
            return 'VARIABLE'
        elif token.lower() in self.keywords:
            return 'KEYWORD'
        else:
            return 'IDENT'

    def get_identifier_id(self, identifier):
        if identifier not in self.identifier_table:
            self.identifier_table[identifier] = self.identifier_count
            self.identifier_count += 1
        return self.identifier_table[identifier]


analyzer = LexicalAnalyzer('in.txt')
for token_type, token, start_pos, end_pos in analyzer.analyze():
    if token_type == 'VARIABLE':
        print(f'{token_type} {start_pos}-{end_pos}: {token}')
    else:
        print(f'{token_type} {start_pos}-{end_pos}: {token}')
```

# Тестирование

Входные данные

```
новая unless     яяя       $переменная1 @переменная2 %переменная3
dffd
if
@переменная2
@переменная2

конец
sub
```

Вывод на `stdout`

```
IDENT (1, 1)-(1, 5): новая
KEYWORD (1, 6)-(1, 11): unless
IDENT (1, 12)-(1, 14): яяя
VARIABLE (1, 15)-(1, 26): 1
VARIABLE (1, 27)-(1, 38): 2
VARIABLE (1, 39)-(1, 50): 3
IDENT (2, 1)-(2, 4): dffd
KEYWORD (3, 1)-(3, 2): if
VARIABLE (4, 1)-(4, 12): 2
VARIABLE (5, 1)-(5, 12): 2
IDENT (7, 1)-(7, 5): конец
KEYWORD (8, 1)-(8, 3): sub
EOF (9, 1)-(9, 1):
```

# Вывод
В ходе данной лабораторной работы приобрел навыки реализации лексического анализатора на
объектно-ориентированном языке без применения каких-либо средств автоматизации для
решения задачи лексического анализа.
