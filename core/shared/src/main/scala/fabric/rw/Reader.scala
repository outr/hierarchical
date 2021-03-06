package fabric.rw

import fabric._

/**
 * Reader provides a simple T => Value wrapper functionality
 */
trait Reader[T] {
  def read(t: T): Value
}

object Reader {
  import ReaderWriter._

  implicit def unitR: Reader[Unit] = unitRW
  implicit def valueR: Reader[Value] = valueRW

  implicit def boolR: Reader[Boolean] = boolRW

  implicit def shortR: Reader[Short] = shortRW
  implicit def intR: Reader[Int] = intRW
  implicit def longR: Reader[Long] = longRW
  implicit def floatR: Reader[Float] = floatRW
  implicit def doubleR: Reader[Double] = doubleRW
  implicit def bigIntR: Reader[BigInt] = bigIntRW
  implicit def bigDecimalR: Reader[BigDecimal] = bigDecimalRW

  implicit def stringR: Reader[String] = stringRW
  implicit def stringMapR: Reader[Map[String, String]] = stringMapRW

  implicit def listR[T](implicit r: Reader[T]): Reader[List[T]] = apply[List[T]] { list =>
    Arr(list.map(r.read).toVector)
  }
  implicit def setR[T](implicit r: Reader[T]): Reader[Set[T]] = apply[Set[T]] { set =>
    Arr(set.map(r.read).toVector)
  }
  implicit def optionR[T](implicit r: Reader[T]): Reader[Option[T]] = apply[Option[T]] {
    case Some(t) => r.read(t)
    case None => Null
  }

  def apply[T](f: T => Value): Reader[T] = new Reader[T] {
    override def read(t: T): Value = f(t)
  }
}