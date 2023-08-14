val sublists: (List[Int], Int) => List[List[Int]] = {
    def rec: (List[Int], Int, List[Int]) => List[List[Int]] = {
        case (Nil, p, Nil) => Nil
        case (Nil, p, acc) => List(acc)
        case (x :: xs, p, acc) if ((sum(acc) >= p) || (sum(acc) + x > p)) => List(acc) ::: rec(xs, p, List(x))
        case (x :: xs, p, acc) => rec(xs, p, (acc ::: List(x)))
    }
    (list, a) => rec(list, a, Nil)
}

val sum: List[Int] => Int = {
    case (Nil) => 0
    case (x::xs) => x + sum(xs)
}



val list = List(1, 2, 3, -10, 5, 6, -8)
val test1 = sublists(list, 5)
val test2 = sublists(list, 10)
val test3 = sublists(list, 1)