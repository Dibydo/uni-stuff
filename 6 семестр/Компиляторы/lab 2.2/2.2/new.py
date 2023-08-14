import abc as aa,parser_edsl as pe,typing as tp
from dataclasses import dataclass as dc
from pprint import pprint as pp
from typing import Any as a

@dc class MyType(aa.ABC):pass
@dc class MyListType(MyType):listof:MyType
@dc class MyPattern:pass
@dc class MyConsPattern(MyPattern):head:MyPattern;tail:MyPattern
@dc class MyExpression(aa.ABC):pass
@dc class MyBinaryExpression(MyExpression):left:MyExpression;op:str;right:MyExpression
@dc class MyConsExpression(MyExpression):head:MyExpression;tail:MyExpression
@dc class MyListExpression(MyExpression):list_expr:list[a]
@dc class MyFunctionCall(MyExpression):name:str;arguments:tp.List[a]
@dc class MyStatement:pattern:list[MyPattern];expr:MyExpression
@dc class MyFunction:name:str;functype:list[MyType];returntype:list[MyType];body:list[MyStatement]
@dc class MyProgram:functions:list[MyFunction]

VARNAME=pe.Terminal('VARNAME','[A-Za-z][A-Za-z0-9_]*',str,priority=6)
NUMBER=pe.Terminal('NUMBER','[0-9]+',int,priority=6)
LISTSIGN=pe.Terminal('ListSign','{}',str,priority=6)
BASICTYPE=pe.Terminal('BasicType','int|bool|str',str,priority=8)

def make_keyword(image):return pe.Terminal(image,image,lambda name:None,priority=10)

KW_IS,KW_END=map(make_keyword,'is end'.split())

NProgram,NFunction,NType,NListType=map(pe.NonTerminal,('Program','Function','Type','ListType'))
NBasicType,NMoreTypes,NStatement,NBody,NSt=map(pe.NonTerminal,('BasicType','MoreTypes','Statement','Body','Statement'))
NPattern,NConsPattern,NList,NExpr=map(pe.NonTerminal,('Pattern','ConsPattern','List','Expr'))
NMoreCons,NCons,NFuncCall,NVarList=map(pe.NonTerminal,('MoreCons','Cons','FuncCall','VarList'))
NBinExpr,NBinOp,NTerms,NTerm=map(pe.NonTerminal,('BinExpr','BinOp','Terms','Term'))
NConsExpr,NAddOp,NMulOp,NFactor=map(pe.NonTerminal,('ConsExpr','AddOp','MulOp','Factor'))
NArgs,NArg,NArgList=map(pe.NonTerminal,('Arguments','Argument','ArgList'))
NNumList,NNumL,NConsArgs=map(pe.NonTerminal,('NumList','NumList','ConsArgs'))
NFuncArgs,NPatterns,NMorePat=map(pe.NonTerminal,('FuncArgs','Patterns','MorePat'))

NProgram |= lambda: []
NProgram |= NFunction + NProgram | []

NFunction |= VARNAME + NType + 'is' + NBody

NType |= [] | BASICTYPE | '*' + NType | '(' + NType + NMoreTypes + ')'

NMoreTypes |= [] | ',' + NType + NMoreTypes

NBody |= NStatements + 'end'

NStatements |= NStatement + ';' + NStatements | NStatement

NStatement |= NPattern + '=' + NExpr

NPattern |= VARNAME + NList + NConsPattern | LISTSIGN + NList + NConsPattern | '(' + NPattern + ')' + NConsPattern

NList |= [] | ',' + VARNAME + NList | ',' + LISTSIGN + NList

NConsPattern |= [] | ':' + NPattern

NExpr |= NTerms | '+' + NTerms | '-' + NTerms | NExpr + NAddOp + NTerms

NAddOp |= '+' | '-'

NTerms |= NFactor | NTerms + NMulOp + NFactor

NMulOp |= '*' | '/'

NFactor |= NTerm | '(' + NExpr + ')'

NTerm |= [] | VARNAME + NList + NConsExpr | LISTSIGN + NConsExpr | NUMBER + NConsExpr | NFuncCall + NConsExpr | NNumList + NConsExpr

NConsExpr |= [] | ':' + NTerm

NFuncCall |= VARNAME + NArgs | VARNAME + '[' + NConsArgs + ']' | VARNAME + NArgument

NConsArgs |= NArgs + ':' + NArgs | []

NArgs |= NArgument + ',' + NArgs | NArgument

NArgument |= VARNAME | NFuncCall | NUMBER | NNumList

NVarList |= VARNAME + ',' + NVarList | VARNAME


p=pe.Parser(NProgram)
print(p.is_lalr_one())
assert p.is_lalr_one()
p.add_skipped_domain('[ \t\n]+')
p.add_skipped_domain('@[^\n]*')

def main():
    try:
        with open("test.txt") as f:
            tree=p.parse(f.read())
            pp(tree)
    except pe.Error as e:
        print(f'Error at position {e.pos}:{e.message}')
    except Exception as e:
        print(e)

if __name__=='__main__':
    main()
