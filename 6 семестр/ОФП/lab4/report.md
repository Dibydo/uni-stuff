% Лабораторная работа № 4 «Case-классы и сопоставление с образцом в Scala»
% 9 июня 2023 г.
% Ярослав Жовтяк, ИУ9-61Б

# Цель работы
Целью данной работы является приобретение навыков разработки case-классов на языке Scala для представления
абстрактных синтаксических деревьев.

# Индивидуальный вариант
Абстрактный синтаксис арифметических выражений:

Expr → Expr + Expr | Expr - Expr | Expr * Expr | Expr / Expr
  Expr ^ Expr | NUMBER | VARNAME
Требуется написать функцию asString : Expr => String, записывающую выражение в текстовой форме с
минимумом круглых скобок. Считаем, что наивысшим приоритетом обладает возведение в степень ^,
затем умножение * и деление /, затем сложение + и вычитание -.
Степень правоассоциативна, остальные операции левоассоциативны.

Вместо функции asString допустимо переопределить метод toString.

# Реализация и тестирование

```scala
sealed trait Expr {
  override def toString: String = this match {
    case Add(e1, e2) => s"${addParentheses(e1)} + ${addParentheses(e2)}"
    case Sub(e1, e2) => s"${addParentheses(e1)} - ${addParentheses(e2)}"
    case Mul(e1, e2) => s"${addParenthesesMul(e1)} * ${addParenthesesMul(e2)}"
    case Div(e1, e2) => s"${addParentheses(e1)} / ${addParenthesesMul(e2)}"
    case Pow(e1, e2) => s"${addParenthesesPow(e1)} ^ ${addParenthesesPow(e2)}"
    case Number(value) => value.toString
    case VarName(name) => name
  }

  private def addParentheses(expr: Expr): String = expr match {
    case Sub(_, _) => s"($expr)"
    case _ => expr.toString
  }

  private def addParenthesesMul(expr: Expr): String = expr match {
    case Add(_, _) | Sub(_, _) | Div(_, _) => s"($expr)"
    case _ => expr.toString
  }
  
  private def addParenthesesPow(expr: Expr): String = expr match {
    case _: Mul | _: Div | _: Pow | _: Add | _: Sub => s"($expr)"
    case _ => expr.toString
  }
}

case class Add(expr1: Expr, expr2: Expr) extends Expr
case class Sub(expr1: Expr, expr2: Expr) extends Expr
case class Mul(expr1: Expr, expr2: Expr) extends Expr
case class Div(expr1: Expr, expr2: Expr) extends Expr
case class Pow(expr1: Expr, expr2: Expr) extends Expr
case class Number(value: Double) extends Expr
case class VarName(name: String) extends Expr

object Main {
  def main(args: Array[String]): Unit = {
    val expr = Add(Number(1), Mul(VarName("x"), Pow(Number(2), Number(3))))
    val expr1 = Mul(VarName("x"), Add(Number(1), Number(2)))
    val expr2 = Add(Mul(VarName("x"), Number(1)), Number(2))
    println(expr)
    println(expr1)
    println(expr2)
    println(Div(VarName("x"), Div(VarName("y"), VarName("z"))))
    println(Sub(VarName("x"), Sub(VarName("y"), VarName("z"))))
    println(Div(Div(VarName("x"), VarName("y")), VarName("z")))
    println(Pow(Add(VarName("x"), VarName("y")), VarName("z")))
  }
}
```

# Вывод
В ходе выполнения данной лабораторной работы приобрел навыки разработки case-классов
на языке Scala для представления абстрактных синтаксических деревьев.
