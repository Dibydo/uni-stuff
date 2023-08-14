%{
#include <iostream>

extern "C" int yylex();

const int TAG_LATITUDE = 1;
const int TAG_LONGITUDE = 2;

union attribute {
    double degree;
};

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
    int type;
    attribute attr;
    Position start, finish;

    Fragment(int _type, attribute _attr, Position _start, Position _finish)
            : type(_type)
    , attr(_attr)
    , start(_start)
    , finish(_finish)
    {}
    void print() {
        switch (type) {
            case TAG_LATITUDE:
                std::cout << "LATITUDE " << start << "-" << finish << " " << attr.degree << std::endl;
                break;
            case TAG_LONGITUDE:
                std::cout << "LONGITUDE " << start << "-" << finish << " " << attr.degree << std::endl;
                break;
        }
    }
};

double powneg10(int degree){
    double a = 1;
    for (int i = 0; i<degree; ++i)
        a /= 10;
    return a;
}

double toDegrees(char* s){
    int i = 1;
    double accum = 0;
    do{
        accum *= 10;
        accum += s[i] - 48;
        ++i;
    } while (s[i]> 47 && s[i] < 58);

    double intpart = accum;
    double realpart = 0;

    if (s[i++] == '.'){
        accum = 0;
        int j = 1;
        do{
            accum += (s[i]-48)*powneg10(j);
            ++j;
            ++i;
        } while (s[i]> 47 && s[i] < 58);
        realpart = accum;
    }else{
        double minutes = 0;
        double seconds = 0;
        accum = 0;
        if (s[i] != 0){
            do{
                accum *= 10;
                accum += s[i] - 48;
                ++i;
            } while (s[i]> 47 && s[i] < 58);
            if (s[i++] == '\''){
                minutes = accum;
                accum = 0;
                do{
                    accum *= 10;
                    accum += s[i] - 48;
                    ++i;
                } while (s[i]> 47 && s[i] < 58);
                seconds = accum;
            }else{
                seconds = accum;
            }
        }
        realpart = minutes/60 + seconds/3600;
    }

    return intpart + realpart;
}

Position start, finish;
Position current(1, 1);

std::vector<Fragment> fragments;

#define YY_USER_ACTION { \
    start = current; \
    current.column += strlen(yytext); \
    finish = current; \
}
%}
LATITUDE [SN]([1-9][0-9]*|0)(\.[0-9]+|D([1-5]?[0-9]\')?([1-5]?[0-9]\")?)
LONGITUDE [EW]([1-9][0-9]*|0)(\.[0-9]+|D([1-5]?[0-9]\')?([1-5]?[0-9]\")?)
%%
{LATITUDE} {
attribute attr;
double degree = toDegrees(yytext);
if (degree > 90 || degree < -90){
std::cout << "Too big value: " << start.line << ", " << start.column << std::endl;
}else{
attr.degree =degree;
fragments.push_back(Fragment(TAG_LATITUDE, attr, start, finish));
}
}
{LONGITUDE} {
attribute attr;
double degree = toDegrees(yytext);
if (degree > 180 || degree < -180){
std::cout << "Too big value: " << start.line << ", " << start.column << std::endl;
}else{
attr.degree =degree;
fragments.push_back(Fragment(TAG_LONGITUDE, attr, start, finish));
}
}
" "
"\n" {
current.line++;
current.column=1;
}
. {
std::cout << "Unexpected char at line: " << start.line << ", column: " << start.column << std::endl;
}
%%
int main() {
    while (yylex()) {}

    for (auto f : fragments) {
        f.print();
    }

    return 0;
}