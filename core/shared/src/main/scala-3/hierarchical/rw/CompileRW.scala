package hierarchical.rw

import hierarchical.*

import scala.deriving.*
import scala.compiletime.*

trait CompileRW {
  inline def ccRW[T <: Product](using Mirror.ProductOf[T]): ReadableWritable[T] = new ClassRW[T] {
    override protected def t2Map(t: T): Map[String, Value] = toMap(t)
    override protected def map2T(map: Map[String, Value]): T = fromMap[T](map)
  }

  inline def toMap[T <: Product](t: T)(using p: Mirror.ProductOf[T]): Map[String, Value] = {
    toMapElems[T, p.MirroredElemTypes, p.MirroredElemLabels](t, 0)
  }

  inline def toMapElems[A <: Product, T <: Tuple, L <: Tuple](a: A, index: Int): Map[String, Value] = {
    inline erasedValue[T] match
      case _: (hd *: tl) =>
        inline erasedValue[L] match
          case _: (hdLabel *: tlLabels) =>
            import hierarchical.rw.given
            val hdLabelValue = constValue[hdLabel].asInstanceOf[String]
            val hdValue = a.productElement(index).asInstanceOf[hd]
            val hdReadable = summonInline[Readable[hd]]
            val value = hdReadable.read(hdValue)
            toMapElems[A, tl, tlLabels](a, index + 1) ++ Map(hdLabelValue -> value)
          case EmptyTuple => sys.error("Not possible")
      case EmptyTuple => Map.empty
  }

  inline def fromMap[T <: Product](map: Map[String, Value])(using p: Mirror.ProductOf[T]): T = {
    ???
  }
}