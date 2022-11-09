//Exercise 1
//  Point
type Point2D = (Double, Double)

def distance(p1: Point2D, p2: Point2D) = {
  Math.sqrt(Math.pow(p1._1 - p2._1, 2) + Math.pow(p1._2 - p2._2, 2))
}

distance((0, 0), (2, 0))

//Exercise 2
//  Person - object
class PersonObject(var name: String, var surname: String, var age: Int, var gender: "M" | "F", var height: Double)
class PartnershipObject(var person1: PersonObject, var person2: PersonObject)

val personObj1 = new PersonObject("John", "Doe", 30, "M", 1.80)
val personObj2 = new PersonObject("Jane", "Doe", 32, "F", 1.60)
val partnershipObj = new PartnershipObject(personObj1, personObj2)

//  Change age
partnershipObj.person2.age = 28

def youngerPerson(partnership: PartnershipObject) = {
  if (partnership.person1.age < partnership.person2.age) partnership.person1 else partnership.person2
}

youngerPerson(partnershipObj).name

//  Person - tuple
type PersonTuple = (String, String, Int, "M" | "F", Double)
type PartnershipTuple = (PersonTuple, PersonTuple)

val personTuple1: PersonTuple = ("John", "Doe", 24, "M", 1.80)
val personTuple2: PersonTuple = ("Jane", "Doe", 28, "F", 1.60)
val partnershipTuple: PartnershipTuple = (personTuple1, personTuple2)

//  Change age
//partnershipTuple._1._3 = 30

def olderPerson(partnership: PartnershipTuple) = {
  if (partnership._1._3 > partnership._2._3) partnership._1 else partnership._2
}

olderPerson(partnershipTuple)._1

//Exercise 3
enum WeekDay:
  case Mon, Tue, Wed, Thu, Fri, Sat, Sun
end WeekDay

def weekDayToString(day: WeekDay) = {
  day match {
    case WeekDay.Mon => "Poniedzialek"
    case WeekDay.Tue => "Wtorek"
    case WeekDay.Wed => "Sroda"
    case WeekDay.Thu => "Czwartek"
    case WeekDay.Fri => "Piatek"
    case WeekDay.Sat => "Sobota"
    case WeekDay.Sun => "Niedziela"
  }
}

weekDayToString(WeekDay.Mon)

def nextDay(day: WeekDay) = {
  day match {
    case WeekDay.Mon => WeekDay.Tue
    case WeekDay.Tue => WeekDay.Wed
    case WeekDay.Wed => WeekDay.Thu
    case WeekDay.Thu => WeekDay.Fri
    case WeekDay.Fri => WeekDay.Sat
    case WeekDay.Sat => WeekDay.Sun
    case WeekDay.Sun => WeekDay.Mon
  }
}

nextDay(WeekDay.Mon)

//Exercise 4
enum Maybe[+T]:
  case Just(value: T)
  case Nothing
end Maybe

def safeHead[A](list: List[A]): Maybe[A] = {
  if (list.isEmpty) Maybe.Nothing else Maybe.Just(list.head)
}

safeHead(List(1, 2, 3))
safeHead(List())

//Exercise 5
sealed trait SolidFigure
case class Cuboid(var a: Double, var b: Double, var h: Double) extends SolidFigure
case class Cone(var r: Double, var h: Double) extends SolidFigure
case class Sphere(var r: Double) extends SolidFigure
case class Cylinder(var r: Double, var h: Double) extends SolidFigure

def volume(block: SolidFigure) = {
  block match {
    case Cuboid(a, b, h) => a * b * h
    case Cone(r, h) => (1/3) * Math.PI * Math.pow(r, 2) * h
    case Sphere(r) => (4/3) * Math.PI * Math.pow(r, 3)
    case Cylinder(r, h) => Math.PI * Math.pow(r, 2) * h
  }
}

val cuboid = Cuboid(2, 2, 2)
val cone = Cone(4, 4.5)
val sphere = Sphere(5)
val cylinder = Cylinder(2, 12)

volume(cuboid)
volume(cone)
volume(sphere)
volume(cylinder)
