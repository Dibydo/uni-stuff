#include <string>
#include <iostream>
#include <sstream>

unsigned int hexToInt(char* str){
    std::string s(str);

    unsigned int i;
    std::istringstream iss(s.substr(1, 4));
    iss >> std::hex >> i;

    return i;
}

int main(){
    char* str = "\\00FF";
    hexToInt(str);
    return 0;
}