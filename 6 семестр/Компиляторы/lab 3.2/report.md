% "Лабораторная работа 3.2 «Форматтер исходных текстов»"
% 20 июня 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является приобретение навыков использования генератора синтаксических анализаторов bison.

# Индивидуальный вариант
Слабый форматтер, составной оператор языка Java (без вложенных анонимных классов, объявлений переменных
обобщённых типов, break’ов на метку и оператора switch).

# Реализация

parser.y
```
%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "lexer.h"

#define MEM(size) ((char *)malloc( (size + 1) * sizeof(char)));

%}

%define api.pure
%locations
%lex-param {yyscan_t scanner}  /* параметр для yylex() */
/* параметры для yyparse() */
%parse-param {yyscan_t scanner}
%parse-param {long env[26]}

%union {
    char* ident;
    char* number;
    char* newline;
}

%token MUL DIV GREATER LESS NOT LPAREN RPAREN SEMICOLON LBRACE RBRACE DOT INTEGER COMMENT
%token PLUS MINUS AND_ST OR_ST NONEQ_ST EQUAL_ST DECL_ST PLUS_ST
%token IF_ST ELSE_ST FOR_ST

%token <ident> IDENT
%token <number> NUMBER
%token <newline> NEWLINE

%type <ident> statement if_statement expr logic_operation expr_decl ident Start Object newline formatter
else_statement for_statement comments expr_dec

%{
int yylex(YYSTYPE *yylval_param, YYLTYPE *yylloc_param, yyscan_t scanner);
void yyerror(YYLTYPE *loc, yyscan_t scanner, long env[26], const char *message);
%}

%{
int afterNewline = 0;
%}

%%

Start:  Object formatter
	{
		printf("%s\n",$Object);
		free($2);
	};

Object:
	LBRACE formatter statement RBRACE formatter
	{
		$$ = MEM(strlen($3) + 2 + strlen($2));
		sprintf($$, "{%s%s}", $2, $3);
		free($3);
		free($2);
		free($5);
	};
	
formatter:
	newline
	| { $$ = MEM(0); $$[0] = '\0'; }
	;
	
newline: 
	NEWLINE 
        {
        	$$ = MEM(strlen("\n"));
            	sprintf($$, "\n");
            	afterNewline = 1;
        };

statement:
        if_statement LBRACE formatter statement RBRACE formatter
        {
        	if (afterNewline == 1) 
        	{
        		$$ = MEM(strlen($1) + strlen($4) + 8 + strlen($3) + strlen($6));
			sprintf($$, "\t%s{%s%s\t}%s", $1, $3, $4, $6);
			free($1);
			free($3);
			free($4);
			free($6);
			afterNewline = 0;
        	} else {
        		$$ = MEM(strlen($1) + strlen($4) + 2 + strlen($3) + strlen($6));
			sprintf($$, "%s{%s%s}%s", $1, $3, $4, $6);
			free($1);
			free($3);
			free($4);
			free($6);
        	}
        	
    	};
      | if_statement LBRACE formatter statement RBRACE else_statement
        {
        	if (afterNewline == 1) 
        	{
        		$$ = MEM(strlen($1) + strlen($4) + 8 + strlen($3) + strlen($6));
			sprintf($$, "\t%s{%s%s\t  }%s", $1, $3, $4, $6);
			free($1);
			free($3);
			free($4);
			free($6);
        	} else {
        		$$ = MEM(strlen($1) + strlen($4) + 2 + strlen($3) + strlen($6));
			sprintf($$, "%s{%s%s}%s", $1, $3, $4, $6);
			free($1);
			free($3);
			free($4);
			free($6);
        	}
        	
    	};
      | for_statement LBRACE formatter statement RBRACE formatter
        {
        	if (afterNewline == 1) 
        	{
        		$$ = MEM(strlen($1) + strlen($4) + 8 + strlen($3) + strlen($6));
			sprintf($$, "\t%s{%s\t%s\t}%s", $1, $3, $4, $6);
			free($3);
			free($6);
			afterNewline = 0;
        	} else {
        		$$ = MEM(strlen($1) + strlen($4) + 2 + strlen($3) + strlen($6));
			sprintf($$, "%s{%s%s}%s", $1, $3, $4, $6);
			free($3);
			free($6);
        	}
        	free($4);
    	};
      | for_statement LBRACE formatter statement RBRACE formatter statement
        {
        	if (afterNewline == 1) 
        	{
        		$$ = MEM(strlen($1) + strlen($4) + 8 + strlen($3) + strlen($6) + strlen($7));
			sprintf($$, "\t%s{%s\t%s\t}%s%s", $1, $3, $4, $6, $7);
			free($3);
			free($6);
			free($7);
			afterNewline = 0;
        	} else {
        		$$ = MEM(strlen($1) + strlen($4) + 2 + strlen($3) + strlen($6) + strlen($7));
			sprintf($$, "%s{%s%s}%s%s", $1, $3, $4, $6, $7);
			free($3);
			free($6);
			free($7);
        	}
        	free($4);
    	};
      | expr_decl SEMICOLON formatter
        {
        	if (afterNewline == 1)
        	{
        		$$ = MEM(strlen($1) + 3 + strlen($3));
			sprintf($$, "%s;%s", $1, $3);
			free($1);
			free($3);
        	} else {
        		$$ = MEM(strlen($1) + 1 + strlen($3));
			sprintf($$, "%s;%s", $1, $3);
			free($1);
			free($3);
        	}
        	
    	};
      | expr_decl SEMICOLON formatter statement
        {
        	if (afterNewline == 1)
        	{
        		size_t l1 = strlen($1);
        		size_t l3 = strlen($3);
        		size_t l4 = strlen($4);
        		$$ = MEM(l1 + 3 + l3 + l4);
			sprintf($$, "%s;%s%s", $1, $3, $4);
			free($1);
			free($4);
        	} else {
        		$$ = MEM(strlen($1) + 1 + strlen($3) + strlen($4));
			sprintf($$, "%s;%s%s", $1, $3, $4);
			free($1);
			free($4);
        	}
        	free($3);
        	
    	};
      | comments formatter statement
        {
        	$$ = MEM(strlen($1) + strlen($2) + strlen($3));
        	sprintf($$, "%s%s%s", $1, $2, $3);
        	free($1);
        	free($2);
        	free($3);
        };
    	
comments:
	COMMENT ident
	{
		$$ = MEM(strlen($2) + 3);
		sprintf($$, "// %s", $2);
		free($2);
	};
    
if_statement:
	IF_ST LPAREN logic_operation RPAREN
	{
		$$ = MEM(strlen($3) + 6);
        	sprintf($$, "if (%s) ", $3);
        	free($3);
    	};
    	
else_statement:
	ELSE_ST LBRACE formatter statement RBRACE formatter
	{
		if (afterNewline == 1)
		{
			$$ = MEM(strlen(" else {") + 2 + strlen($3) + strlen($4) + strlen($6));
			sprintf($$, " else {%s\t%s\t}%s", $3, $4, $6);
		} else {
			$$ = MEM(strlen("else") + 5 + strlen($3) + strlen($4) + strlen($6));
			sprintf($$, " else {%s%s}%s", $3, $4, $6);
		}
		free($3);
		free($4);
		free($6);
	};
	
for_statement:
	FOR_ST LPAREN expr_dec SEMICOLON logic_operation SEMICOLON expr RPAREN
	{
		$$ = MEM(strlen("for") + 9 + strlen($3) + strlen($5) + strlen($7));
		sprintf($$, "for ( %s; %s; %s) ", $3, $5, $7);
		free($3);
		free($5);
		free($7);
	};
	
expr_dec:
	ident DECL_ST expr 
	{
		$$ = MEM(strlen($1) + strlen($3) + 3);
        	sprintf($$, "%s = %s", $1, $3);
        	free($1);
        	free($3);

    	};	
    
expr_decl:
	ident DECL_ST expr 
	{
		$$ = MEM(strlen($1) + strlen($3) + 4);
        	sprintf($$, "\t%s = %s", $1, $3);
        	free($1);
		free($3);

    	};
      | INTEGER ident DECL_ST expr
        {
        	$$ = MEM(strlen($2) + strlen($4) + 9);
        	sprintf($$, "\tint %s = %s", $2, $4);
        	free($2);
        	free($4);
        	afterNewline = 0;
        };
      | ident DECL_ST ident
	{
		$$ = MEM(strlen($1) + strlen($3) + 4);
        	sprintf($$, "\t%s = %s", $1, $3);
		free($1);
		free($3);
    	};

expr:
	NUMBER 
	{
		$$ = MEM(strlen($1));
		sprintf($$, "%s", $1);
		free($1);
	};
      | IDENT PLUS_ST
        {
        	$$ = MEM(strlen($1) + 2);
        	sprintf($$, "%s++", $1);
        	free($1);
        };

logic_operation:
	expr LESS expr 
	{
		$$ = MEM(strlen($1) + strlen($3) + 3);
        	sprintf($$, "%s < %s", $1, $3);
        	free($1);
        	free($3);
    	};
      | IDENT LESS expr
        {
        	$$ = MEM(strlen($1) + strlen($3) + 3);
        	sprintf($$, "%s < %s", $1, $3);
        	free($1);
        	free($3);
        };

ident:
	IDENT 
	{
		$$ = MEM(strlen($1));
		sprintf($$, "%s", $1);
		free($1);
	};
	| IDENT ident
	{
		$$ = MEM(strlen($1) + strlen($2) + 1);
		sprintf($$, "%s %s", $1, $2);
		free($1);
		free($2);
	};

%%

int main(int argc, char *argv[])
{
yyscan_t scanner;
	struct Extra extra;
	long env[26];
    env[0] = 0;
    // env[1] = 20;
    char * buffer = 0;
    FILE * f = fopen ("input.txt", "rb");

    if (argc > 1) {
        printf("Read flag %s\n", argv[1]);
        if (strcmp(argv[1],"-d") == 0) {
            env[1] = 20;
        }
        // env[1] = atoi(argv[1]);
    }

	init_scanner(f, &scanner, &extra);
	yyparse(scanner, env);
	destroy_scanner(scanner);
    	free(buffer);

    return 0;
}
```

lexer.l
```
%option reentrant noyywrap bison-bridge bison-locations
%option extra-type="struct Extra *"

/* Подавление предупреждений для -Wall */
%option noinput nounput

%{

#include <stdio.h>
#include <stdlib.h>
#include "lexer.h"
#include "parser.tab.h"  /* файл генерируется Bison’ом */

#define YY_USER_ACTION \
  { \
    int i; \
    struct Extra *extra = yyextra; \
    if (! extra->continued ) { \
      yylloc->first_line = extra->cur_line; \
      yylloc->first_column = extra->cur_column; \
    } \
    extra->continued = false; \
    for (i = 0; i < yyleng; ++i) { \
      if (yytext[i] == '\n') { \
        extra->cur_line += 1; \
        extra->cur_column = 1; \
      } else { \
        extra->cur_column += 1; \
      } \
    } \
    yylloc->last_line = extra->cur_line; \
    yylloc->last_column = extra->cur_column; \
  }

void yyerror(YYLTYPE *loc, yyscan_t scanner, long env[26], const char *message) {
    printf("Error (%d,%d): %s\n", loc->first_line, loc->first_column, message);
}

%}

%%
[\n]+	  return NEWLINE;
[\t\r ]+

\+	  return PLUS;
\-	  return MINUS;
\*	  return MUL;
\/	  return DIV;
\> 	  return GREATER;
\<	  return LESS;
\!	  return NOT;
\(	  return LPAREN;
\)	  return RPAREN;
\;	  return SEMICOLON;
\.	  return DOT;
\{	  return LBRACE;
\}	  return RBRACE;


[0-9]+	  {
    		yylval->number = strdup(yytext);
    		return NUMBER;
	  }

&&	  return AND_ST;
\|\|      return OR_ST;
!=        return NONEQ_ST;
==        return EQUAL_ST;
=         return DECL_ST;
[+][+]	  return PLUS_ST;
if        return IF_ST;
else      return ELSE_ST;
for	  return FOR_ST;
int	  return INTEGER;
[\/][\/]  return COMMENT;

[a-z]*	  {
    		yylval->ident = strdup(yytext);
    		return IDENT;
	  }


%%

void init_scanner(FILE *input, yyscan_t *scanner, struct Extra *extra) {
    extra->continued = false;
    extra->cur_line = 1;
    extra->cur_column = 1;

    yylex_init(scanner);
    yylex_init_extra(extra, scanner);
    yyset_in(input, *scanner);
}

void destroy_scanner(yyscan_t scanner) {
    yylex_destroy(scanner);
}
```

lexer.h
```
#ifndef LEXER_H
#define LEXER_H

#include <stdbool.h>
#include <stdio.h>

#ifndef YY_TYPEDEF_YY_SCANNER_T
#define YY_TYPEDEF_YY_SCANNER_T
typedef void *yyscan_t;
#endif /* YY_TYPEDEF_YY_SCANNER_T */

struct Extra
{
    bool continued;
    int cur_line;
    int cur_column;
};

void init_scanner(FILE *input, yyscan_t *scanner, struct Extra *extra);
void destroy_scanner(yyscan_t);

#endif /* LEXER_H */
```

run.sh
```
#!/bin/bash

flex lexer.l
bison -d parser.y
g++ -o test lex.yy.c parser.tab.c
./test
```

# Тестирование

Входные данные

```
{
int a=5;
int b=10;
int x=1;
for(x=0;x<10;x++){
int y=8;x=y;
}
if(a<3){a = 1;} else {
b = 3;

}

}
```

Вывод на `stdout`

```
{
        int a = 5;
        int b = 10;
        int x = 1;
        for ( x = 0; x < 10; x++) {
                int y = 8;      x = y;
        }
        if (a < 3) {    a = 1;    } else {
                b = 3;
        }
}
```

# Вывод
В ходе выполнения данной лабораторной работы приобрел навыки использования генератора синтаксических
анализаторов bison.