package spec

import fabric._
import fabric.rw._

class RWSpecManual extends munit.FunSuite {
  implicit val addressRW: ReaderWriter[Address] = new ClassRW[Address] {
    override protected def t2Map(t: Address): Map[String, Value] = Map(
      "city" -> t.city.toValue,
      "state" -> t.state.toValue
    )

    override protected def map2T(map: Map[String, Value]): Address = Address(
      city = map("city").as[String],
      state = map("state").as[String]
    )
  }
  implicit val personRW: ReaderWriter[Person] = new ClassRW[Person] {
    override protected def t2Map(t: Person): Map[String, Value] = Map(
      "name" -> t.name.toValue,
      "age" -> t.age.toValue,
      "address" -> t.address.toValue
    )

    override protected def map2T(map: Map[String, Value]): Person = Person(
      name = map("name").as[String],
      age = map("age").as[Int],
      address = map("address").as[Address]
    )
  }

  test("convert Person to Value and back") {
    val person = Person("Matt Hicks", 41, Address("San Jose", "California"))
    val value = person.toValue
    assertEquals(value, obj(
      "name" -> "Matt Hicks",
      "age" -> 41.0,
      "address" -> obj(
        "city" -> "San Jose",
        "state" -> "California"
      )
    ))
    val back = value.as[Person]
    assertEquals(back, person)
  }
}