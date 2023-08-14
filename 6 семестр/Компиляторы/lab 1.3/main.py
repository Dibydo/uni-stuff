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
