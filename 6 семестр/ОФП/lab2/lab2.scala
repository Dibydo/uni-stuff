class RealNumberSet(val intervals: List[(Double, Double)]) {
  def this(a: Double, b: Double) = this(List((a, b)))
  
  def +(other: RealNumberSet): RealNumberSet = {
  val combinedIntervals = (intervals ++ other.intervals).sorted
  val mergedIntervals = combinedIntervals.foldLeft(List.empty[(Double, Double)]) { (result, interval) =>
    result match {
      case Nil => List(interval)
      case (prevA, prevB) :: tail =>
        if (interval._1 <= prevB) {
          (prevA, prevB.max(interval._2)) :: tail
        } else {
          interval :: result
        }
    }
  }
  new RealNumberSet(mergedIntervals.reverse)
}
  
  def *(other: RealNumberSet): RealNumberSet = {
    val newIntervals = for {
      (a1, b1) <- intervals
      (a2, b2) <- other.intervals
      if !(b1 < a2 || b2 < a1)
    } yield (a1 max a2, b1 min b2)
    new RealNumberSet(newIntervals)
  }
  
  def unary_! : RealNumberSet = {
  val sortedIntervals = intervals.sortBy(_._1)
  val newIntervals = sortedIntervals.zip(sortedIntervals.tail).flatMap { case ((_, b1), (a2, _)) =>
    if (b1 < a2) {
      Some((b1, a2))
    } else {
      None
    }
  }
  val min = intervals.map(_._1).min
  val max = intervals.map(_._2).max
  val invertedIntervals = {
    val lowerInterval =
      if (min == Double.NegativeInfinity) {
        Nil
      } else {
        List((Double.NegativeInfinity, min))
      }
    val upperInterval =
      if (max == Double.PositiveInfinity) {
        Nil
      } else {
        List((max, Double.PositiveInfinity))
      }
    lowerInterval ::: newIntervals ::: upperInterval
  }
  new RealNumberSet(invertedIntervals)
}

  def in(x: Double): Boolean = intervals.exists(i => i._1 <= x && x <= i._2)

  override def toString: String = {
  if (intervals.isEmpty) {
    "{}"
  } else {
    val intervalStrings = intervals.map {
      case (a, b) if a == Double.NegativeInfinity && b == Double.PositiveInfinity => "(-∞, ∞)"
      case (a, b) if a == Double.NegativeInfinity => "(-∞, " + b + "]"
      case (a, b) if b == Double.PositiveInfinity => "[" + a + ", ∞)"
      case (a, b) => "[" + a + ", " + b + "]"
    }
    intervalStrings.mkString("{", " ∪ ", "}")
  }
}
  def removeInnerIntervals(intervals: List[(Double, Double)]): List[(Double, Double)] = {
  intervals.filterNot { case (a1, b1) =>
    intervals.exists { case (a2, b2) =>
      (a2 < a1 && b1 < b2) || (a1 == a2 && b1 < b2) || (a2 < a1 && b1 == b2)
    }
  }
}

}

val set1 = new RealNumberSet(1, 5)

val set2 = new RealNumberSet(List((1, 2), (4, 7)))

println(set1.in(3.5))
println(set2.in(4))

val unionSet = set1 + set2
println(unionSet.in(6))
println(unionSet)

val intersectSet = set1 * set2
println(intersectSet.in(2))
println(intersectSet)

val complementSet = !set1
println(complementSet.in(10))

val setset = new RealNumberSet(1,3)
val setset2 = new RealNumberSet(3,5)
println(setset + setset2)

val i12 = new RealNumberSet(1, 2)
println(i12)
println(! i12)
println(! (! i12))
