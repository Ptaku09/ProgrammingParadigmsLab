import java.util.Date
import scala.annotation.tailrec

object List4 {
  // Exercise 0
  def log(prefix: "DEBUG" | "WARN" | "INFO" | "CRITICAL")(datetime: Date)(text: String): Unit = {
    prefix match
      case "DEBUG" => println(Console.BLUE + s"[$prefix] $datetime \t $text")
      case "WARN" => println(Console.YELLOW + s"[$prefix] $datetime \t $text")
      case "INFO" => println(Console.GREEN + s"[$prefix] $datetime \t $text")
      case "CRITICAL" => println(Console.RED + s"[$prefix] $datetime \t $text")
  }

  // Exercise 1
  def map[A](list: List[A], f: A => A): List[A] = list match
    case Nil => Nil
    case elem :: rest => f(elem) :: map(rest, f)

  // Exercise 2
  def filter[A](list: List[A], pred: A => Boolean): List[A] = list match
    case Nil => Nil
    case elem :: rest => if pred(elem) then elem :: filter(rest, pred) else filter(rest, pred)

  // Exercise 3
  @tailrec
  def reduce[A](list: List[A], op: (A, A) => A, acc: A): A = list match
    case Nil => acc
    case elem :: rest => reduce(rest, op, op(elem, acc))

  // Exercise 4
  def average(list: List[Int]): Double = reduce(list, (a, b) => a + b, 0) / list.length.toDouble

  // Exercise 5
  def acronym(str: String): String = {
    val listOfStrings = str.split(" ").toList
    val uppercaseChars = map(listOfStrings, (word: String) => word.slice(0, 1))
    reduce(uppercaseChars, (a: String, b: String) => b + a, "")
  }

  // Exercise 6
  def conditionalSquare(list: List[Int]): List[Int] = {
    val sum = reduce(list, (a, b) => a + b, 0)
    val filteredList = filter(list, (elem: Int) => elem * elem * elem <= sum)
    map(filteredList, (elem: Int) => elem * elem)
  }

  def main(args: Array[String]): Unit = {
    println("Exercise 0")
    log("WARN")(new Date())("Hello")
    log("DEBUG")(new Date())("Hello")
    log("INFO")(new Date())("Hello")
    log("CRITICAL")(new Date())("Hello")

    println(Console.RESET + "\nExercise 4")
    println(average(List(1, 2, 3, 4, 6)) + "\n")

    println("Exercise 5")
    println(acronym("Zaklad Ubezpieczen Spolecznych") + "\n")

    println("Exercise 6")
    println(conditionalSquare(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
  }
}
