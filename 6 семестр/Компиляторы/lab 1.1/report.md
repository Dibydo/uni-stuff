% Лабораторная работа № 1.1. Раскрутка самоприменимого компилятора
% 23 марта 2023 г.
% Жовтяк Ярослав, ИУ9-61Б

# Цель работы
Целью данной работы является ознакомление с раскруткой самоприменимых компиляторов
на примере модельного компилятора.

# Индивидуальный вариант
Компилятор **BeRo**. Сделать так, чтобы символы .. и : перестали быть взаимозаменяемыми.

# Реализация

Различие между файлами `pcom.pas` и `pcom2.pas`:

```diff
--- btpc64.pas  2020-02-15 14:28:08.000000000 +0300
+++ btpc64-2.pas        2023-02-13 20:15:38.184137938 +0300
@@ -685,7 +685,7 @@
   ReadChar;
   if CurrentChar='.' then begin
    ReadChar;
-   CurrentSymbol:=TokColon;
+   CurrentSymbol:=TokPlus;
   end else begin
    CurrentSymbol:=TokPeriod
   end;
```

Различие между файлами `pcom2.pas` и `pcom3.pas`:

```diff
--- btpc64-2.pas        2023-02-13 20:15:38.184137938 +0300
+++ btpc64-3.pas        2023-02-14 14:27:55.307353926 +0300
@@ -229,15 +229,15 @@
     CurrentString:array[1:255] of char;
     CurrentStringLength:integer;
     FunctionDeclarationIndex:integer;
-    Keywords:array[SymBEGIN..SymPROC] of TAlfa;
+    Keywords:array[SymBEGIN:SymPROC] of TAlfa;
     LastOpcode:integer;
     CurrentLevel:integer;
     IsLabeled:boolean;
-    SymbolNameList:array[-1..MaximalList] of integer;
+    SymbolNameList:array[-1:MaximalList] of integer;
     IdentifierPosition:integer;
     TypePosition:integer;
-    Identifiers:array[0..MaximalIdentifiers] of TIdent;
-    Types:array[1..MaximalTypes] of TType;
+    Identifiers:array[0:MaximalIdentifiers] of TIdent;
+    Types:array[1:MaximalTypes] of TType;
     Code:array[0:MaximalCodeSize] of integer;
     CodePosition:integer;
     StackPosition:integer;
@@ -1604,9 +1604,9 @@
  end;
 end;
 
-procedure Block(L..integer); forward;
+procedure Block(L:integer); forward;
 
-procedure Constant(var c,t..integer);
+procedure Constant(var c,t:integer);
 var i,s:integer;
 begin
  if (CurrentSymbol=tOKsTRc) and (CurrentStringLength=1) then begin
@@ -1658,7 +1658,7 @@
 
 procedure TypeDefinition(var t:integer); forward;
 
-procedure ArrayType(var t..integer);
+procedure ArrayType(var t:integer);
 var x:integer;
 begin
  Types[t].Kind:=KindARRAY;
@@ -1988,7 +1988,7 @@
  EmitChar(chr(B));
 end;
 
-procedure EmitInt16(i..integer);
+procedure EmitInt16(i:integer);
 begin
  if i>=0 then begin
   EmitByte(i mod 256);
@@ -2000,7 +2000,7 @@
  end;
 end;
 
-procedure EmitInt32(i..integer);
+procedure EmitInt32(i:integer);
 begin
  if i>=0 then begin
   EmitByte(i mod 256);
@@ -2029,7 +2029,7 @@
  end;
 end;
 
-procedure OutputCodePutInt32(o,i..integer);
+procedure OutputCodePutInt32(o,i:integer);
 begin
  if i>=0 then begin
   OutputCodeData[o]:=chr(i mod 256);
@@ -2053,7 +2053,7 @@
  end;
 end;
 
-type TOutputCodeString=array[1..20] of char;
+type TOutputCodeString=array[1:20] of char;
 
 procedure OutputCodeString(s:TOutputCodeString);
 var i:integer;
```

# Тестирование

Тестовый пример:

```pascal
program Project1;
begin
	// Get the sum of two values
	Writeln('244 + 835 = ', 244 .. 835);

	Write('Press any key to continue...');
	Readln;
end.
```

Вывод тестового примера на `stdout`

```
244 + 835 = 1079
Press any key to continue...
```

# Вывод
В ходе данной лабораторной работы ознакомился с раскруткой самоприменимых компиляторов на 
примере компилятора BeRo, получил опыт программирования на языке Pascal и выполнил поставленная 
задачу. Также изучил особенности работы компилятора и его процесса раскрутки.
