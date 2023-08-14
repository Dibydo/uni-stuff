% Лабораторная работа № 3 «Обобщённые классы в Scala»
% 23 марта 2023 г.
% Жовтяк Ярослав, ИУ9-61Б

# Цель работы
Целью данной работы является приобретение навыков разработки обобщённых классов
на языке Scala с использованием неявных преобразований типов.

# Индивидуальный вариант
Класс *Matrix[T]*, представляющий неизменяемую квадратную матрицу с элементами типа *T*,
для которой реализованы две операции: получение значения элемента на *i*-й строке в *j*-м столбце,
а также перестановка двух строк матрицы. В случае, если *T* — числовой тип,
для *Matrix[T]* должна быть доступна дополнительная операция *power*,
возвращающая результат возведения матрицы в указанную степень.

# Реализация и тестирование

```scala
class Matrix[T](val data: Vector[Vector[T]]) {
  def apply(i: Int, j: Int): T = data(i)(j)

  def swapRows(i: Int, j: Int): Matrix[T] = {
    val newData = data.updated(i, data(j)).updated(j, data(i))
    new Matrix(newData)
  }

  def power(n: Int)(implicit num: Numeric[T]): Matrix[T] = {
    require(data.length == data.head.length, "Matrix must be square")
    if (n == 0) {
      val identity = Vector.tabulate(data.length, data.length) { (i, j) =>
        if (i == j) num.one else num.zero
      }
      new Matrix(identity)
    } else if (n % 2 == 0) {
      val halfPower = power(n / 2)
      halfPower * halfPower
    } else {
      this * power(n - 1)
    }
  }

  def *(other: Matrix[T])(implicit num: Numeric[T]): Matrix[T] = {
    require(data.length == other.data.length, "Matrices must have the same size")
    val newData = Vector.tabulate(data.length, data.length) { (i, j) =>
      val row = data(i)
      val col = other.data.map(_(j))
      row.zip(col).map { case (a, b) => num.times(a, b) }.sum
    }
    new Matrix(newData)
  }

  override def toString: String = {
    val sb = new StringBuilder
    val size = data.length
    for (i <- 0 until size) {
      for (j <- 0 until size) {
        sb.append(data(i)(j))
        sb.append("\t")
      }
      sb.append("\n")
    }
    sb.toString()
  }
}

val testmatrix = new Matrix(Vector(Vector(1, 2), Vector(3, 4)))
val test1 = testmatrix(0,0)
val test2 = testmatrix(0,1)
val swapMatrix = testmatrix.swapRows(0, 1)
val powMatrix = testmatrix.power(2)
```

# Вывод
В ходе выполнения данной лабораторной работы приобрел навыки разработки обобщенных классов
на языке Scala с использованием неявных преобразований типов.
Были изучены основные принципы работы с обобщенными классами и неявными преобразованиями.