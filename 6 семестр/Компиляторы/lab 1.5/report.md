% Лабораторная работа № 1.5 «Порождение лексического анализатора с помощью flex»
% 20 апреля 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является изучение генератора лексических анализаторов flex.

# Индивидуальный вариант
Регулярные выражения: ограничены знаками «/», не могут пересекать границы текста,
содержат escape-последовательности «\\n», «\\/», «\\\». Строковые литералы:
ограничены тремя кавычками («"""»), могут занимать несколько строчек текста,
не могут содержать внутри более двух кавычек подряд.

# Реализация

```lex
%option noyywrap bison-bridge bison-locations

%{
    #include <stdio.h>
    #include <stdlib.h>

    #define TAG_REGEX 1
    #define TAG_STRING 2
    #define TAG_ERROR 3

    char *tag_names[] = {
        "END_OF_PROGRAM", "REGEX", "STRING", "ERROR"
    };

    typedef struct Position Position; 

    struct Position {
        int line, pos, index;
    };

    void print_pos(Position * p) {
        printf("(%d,%d)", p->line, p->pos);
    }

    struct Fragment {
        Position starting, following;
    };

    typedef struct Fragment YYLTYPE;
    typedef struct Fragment Fragment; 

    void print_frag(Fragment *f) {
        print_pos(&(f->starting));
        printf(" - ");
        print_pos(&(f->following));
    }

    union Token {
        int symbolToken;
        int regexToken;
        char *keywordToken;  
    };

    typedef union Token YYSTYPE;

    int continued;
    struct Position cur;

    #define YY_USER_ACTION           \
    {                                \
        int i;                       \
        if (!continued)              \
            yylloc->starting = cur;  \
        continued = 0;               \
        for (i = 0; i < yyleng; i++) \
        {                            \
            if (yytext[i] == '\n')   \
            {                        \
                cur.line++;          \
                cur.pos = 1;         \
            }                        \
            else                     \
                cur.pos++;           \
            cur.index++;             \
        }                            \
        yylloc->following = cur;     \
    }

    typedef struct {
        char** names;
        size_t codes;
        size_t size;
    } Dictionary;

    void initDict(Dictionary*  d, int initSize) {
        d->names = malloc(initSize * sizeof(char*));
        d->codes = 0;
        d->size = initSize;
    }

    void addName(Dictionary* d, char* name) {
        if (d->codes == d->size) {
            d->size = d->size * 2 + 1;
            d->names = realloc(d->names, d->size * sizeof(char*));
        }
        d->names[d->codes++] = name;
    }

    int findName(Dictionary * d, char *name) {
        for (int i = 0; i < d->codes; i++) {
            if (strcmp(name, d->names[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    void clearDict(Dictionary * d) {
        free(d->names);
        d->names = NULL;
        d->codes = 0;
        d->size = 0;
    }

    Dictionary name_codes;
    Dictionary syms;  

    void init_scanner(char *program) {
        continued = 0;
        cur.line = 1;
        cur.pos = 1;
        cur.index = 0;
        initDict(&name_codes, 2);
        yy_scan_string(program);
    }

    void err(char *msg)
    {
        printf("Error ");
        print_pos(&cur);
        printf(": %s\n", msg);
    }
    char* remove_all_quotes_and_interpret_escapes(char* str) {
        int len = strlen(str);
        while (len >= 2 && str[0] == '"' && str[len-1] == '"') {
            // Remove quotes
            str[len-1] = '\0';
            str++;

        // Interpret escape sequences
        char* result = (char*) malloc((len+1) * sizeof(char));
        int i, j;
        for (i = 0, j = 0; i < len-2; i++, j++) {
            if (str[i] == '\\') {
                // Escape sequence detected
                switch (str[i+1]) {
                    case 'n':
                        result[j] = '\n';
                        break;
                    case 't':
                        result[j] = '\t';
                        break;
                    case '\\':
                        result[j] = '\\';
                        break;
                    case '\"':
                        result[j] = '\"';
                        break;
                    default:
                        // Unsupported escape sequence
                        free(result);
                        return NULL;
                }
                i++;  // Skip next character
            } else {
                result[j] = str[i];
            }
        }
        result[j] = '\0';
        str = result;
        len = strlen(str);
        }
    return str;    
    }

%}

STRING          \"\"\"([[:space:]]*[^\"\"\"]+(\"|\"\")?)+\"\"\"
REGEX           "/"((\\n|\\\\|\\\/|[^\/\n])+)"/"

%% 

[\n\t ]+

{REGEX}     {
                addName(&syms, yytext);
                yylval->symbolToken = findName(&syms, yytext);
                return TAG_REGEX;
            }


{STRING}    {
                addName(&syms, yytext);
                yylval->symbolToken = findName(&syms, yytext);
                return TAG_STRING;
            }

<<EOF>>     return 0;

.           {
                err("unexpected character");
                return TAG_ERROR;
            }

%%

    int main() {
    int tag;
    YYSTYPE value;
    YYLTYPE coords;
    // union Token token;

    FILE *file = fopen("test.txt", "r");
    if (file == NULL)
    {
        fputs("File not found", stderr);
        exit(1);
    }

    char buf[1001];
    char c;
    int i = 0;
    while((c = fgetc(file)) != EOF && i < 1000){
        buf[i++] = c;
    }
    fclose(file);
    buf[1000] = '\0';
    init_scanner(buf);
    do {
        tag = yylex(&value, &coords);
        if (tag != 0 && tag != TAG_ERROR) {
            printf("%s ", tag_names[tag]);
            print_frag(&coords);
            if (strcmp(tag_names[tag], "REGEX") == 0) {
                printf(": %s\n", remove_all_quotes_and_interpret_escapes(syms.names[value.symbolToken]));
            } else if (strcmp(tag_names[tag], "STRING") == 0) {
                printf(": %s\n", remove_all_quotes_and_interpret_escapes(syms.names[value.symbolToken]));
            } else {
                printf(": \n");
            }
        }
    } while (tag != 0);

    clearDict(&name_codes);
    clearDict(&syms);
    return 0;
}
```

# Тестирование

Входные данные

```
""" he  llo123"""
/123/
asd$

"""with"quote""" """with""two""quotes"""


"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
"""!@#$%^&*()"""
"""aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"""
```

Вывод на `stdout`

```
STRING (1,1) - (1,18):  he  llo123
REGEX (2,1) - (2,6): /123/
Error (3,2): unexpected character
Error (3,3): unexpected character
Error (3,4): unexpected character
Error (3,5): unexpected character
STRING (5,1) - (5,17): with"quote
STRING (5,18) - (5,41): with""two""quotes
STRING (8,1) - (8,77): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (9,1) - (9,76): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (10,1) - (10,75): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (11,1) - (11,74): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (12,1) - (12,73): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (13,1) - (13,72): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (14,1) - (14,71): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (15,1) - (15,70): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
STRING (16,1) - (16,17): !@#$%^&*()
STRING (17,1) - (17,69): aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
```

# Вывод
В результате выполнения данной лабораторной работы получил навыки работы с генератором
лексических анализаторов flex, научился создавать правила лексического анализа,
а также научился отлаживать и тестировать свои лексические анализаторы.