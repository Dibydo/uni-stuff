%option noyywrap bison-bridge bison-locations

%{
#include <iostream>
#include <unordered_map>
#include <vector>
#include <sstream>
#include <string>

extern "C" int yylex();

const int CHAR_TOKEN = 4;
const int IDENT_TOKEN = 2;
const int KEYWORD_TOKEN = 3;


union attribute {
    int identCode;
    int ch;
};
typedef union attribute YYSTYPE;

struct Position {
    int line, column;

    Position() {}

    Position(int _line, int _column): line(_line), column(_column) {}

    void print() {
        std::cout << line << ":" << column;
    }
};

std::ostream& operator <<(std::ostream &out, const Position &pos) {
return (out << pos.line << ":" << pos.column);
}

struct Fragment {
    struct Position start, finish;
};

std::ostream& operator <<(std::ostream &out, const Fragment &frag) {
    return (out << frag.start << " - " << frag.finish);
}

typedef struct Fragment YYLTYPE;

unsigned int hexToInt(char* str){
    std::string s(str);

    unsigned int i;
    std::istringstream iss(s.substr(1, 4));
    iss >> std::hex >> i;

    return i;
}

bool continued = 0;
int curid = 1;
std::unordered_map<std::string, int> idents;
std::vector<std::string> revidents;
Position start, finish;
Position current(1, 1);


#define YY_USER_ACTION { \
    if (!continued)           { \
        yylloc->start = current; \
    }          \
    continued = 0; \
    current.column += strlen(yytext); \
    yylloc->finish = current;    \
}
%}
IDENT [a-zA-Z][a-zA-Z0-9]{0,8}[a-zA-Z]
KEYWORD z|forward|for
%x CHAR1 CHAR2
%%
\' {
    continued = 1;
    BEGIN(CHAR1);
}
<CHAR1,CHAR2>\n {
    std::cout << "Unexpected newline at line: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
    BEGIN(0);
    yylval->ch = 0;
    return CHAR_TOKEN;
}
<CHAR1>\\n {
    yylval->ch = '\n';
    BEGIN(CHAR2);
    continued = 1;
}
<CHAR1>\\\' {
    yylval->ch = '\'';
    BEGIN(CHAR2);
    continued = 1;
}
<CHAR1>\\\\ {
    yylval->ch = '\\';
    BEGIN(CHAR2);
    continued = 1;
}
<CHAR1>\\[0-9a-f]{4} {
    yylval->ch = hexToInt(yytext);
    BEGIN(CHAR2);
    continued = 1;
}
<CHAR1>\\. {
    std::cout << "Unexpected escape sequence at line: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
    BEGIN(CHAR2);
    yylval->ch = 0;
    continued = 1;
}
<CHAR1>\' {
    std::cout << "Empty character: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
    BEGIN(0);
    yylval->ch = 0;
    continued = 1;
    return CHAR_TOKEN;
}
<CHAR1>. {
    yylval->ch = yytext[0];
    BEGIN(CHAR2);
    continued = 1;
}
<CHAR2>\' {
    BEGIN(0);
    return CHAR_TOKEN;
}
<CHAR2>[^\n\']* {
    std::cout << "Too many characters: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
    continued = 1;
}
<CHAR1,CHAR2><<EOF>> {
    std::cout << "Unexpected EOF at line: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
    BEGIN(0);
    yylval->ch = 0;
    return CHAR_TOKEN;
}
{KEYWORD} {
    return KEYWORD_TOKEN;
}
{IDENT} {
    if (idents.find(yytext) == idents.end()) {
        revidents.push_back(yytext);
        idents[yytext] = curid++;
    }
    yylval->identCode = idents[yytext];
    return IDENT_TOKEN;
}
<<EOF>> {
    return 0;
}
" "
"\n" {
current.line++;
current.column=1;
}
. {
std::cout << "Unexpected char at line: " << yylloc->start.line << ", column: " << yylloc->start.column << std::endl;
}
%%
int main() {
    int tag;
    attribute attr;
    struct Fragment coords;
    do{
        tag = yylex(&attr, &coords);
        if (tag == CHAR_TOKEN)
            std::cout <<  "CHAR " << coords << ": " << (int)attr.ch << std::endl;
        else if (tag == IDENT_TOKEN)
            std::cout << "IDENT " << coords << ": " << attr.identCode << std::endl;
        else if (tag == KEYWORD_TOKEN)
            std::cout << "KEYWORD "<< coords << std::endl;
    }while(tag!=0);

    return 0;
}