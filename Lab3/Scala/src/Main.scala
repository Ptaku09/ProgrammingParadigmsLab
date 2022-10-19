import List3.*

import scala.annotation.tailrec

object Main {
  @tailrec
  def getListFromUser: List[String | Int | Double] = {
    println("Please select a type of list:")
    println("1. Int")
    println("2. String")
    println("3. Double")
    println("4. Empty list")
    println("0. Exit")

    val input = scala.io.StdIn.readLine()

    input match {
      case "1" =>
        println("Please enter a list of Ints (separate elements by space):")

        try {
          scala.io.StdIn.readLine().split(" ").map(_.toInt).toList
        } catch {
          case e: NumberFormatException =>
            println("Invalid input. Please try again.")
            getListFromUser
        }

      case "2" =>
        println("Please enter a list of Strings (separate elements by space):")
        val list = scala.io.StdIn.readLine().split(" ").toList
        list

      case "3" =>
        println("Please enter a list of Doubles (separate elements by space):")

        try {
          scala.io.StdIn.readLine().split(" ").map(_.toDouble).toList
        } catch {
          case e: NumberFormatException =>
            println("Invalid input. Please try again.")
            getListFromUser
        }

      case "4" => List()

      case "0" =>
        println("Exiting...")
        List()

      case _ =>
        println("Invalid input")
        getListFromUser
    }
  }

  @tailrec
  def menu(): Unit = {
    println()
    println("Please select an operation:")
    println("1. Get last element from list")
    println("2. Get last two elements from list")
    println("3. Get length of list")
    println("4. Reverse list")
    println("5. Check if list is palindrome")
    println("6. Check if list is palindrome (use reverse method)")
    println("7. Remove duplicates from list")
    println("8. Get elements from even indexes")
    println("9. Check if number is prime")
    println("0. Exit")
    println()

    val input = scala.io.StdIn.readLine()

    input match {
      case "1" =>
        val list = getListFromUser
        println("Last element: " + List3.getLastElem(list).getOrElse(None))
        menu()

      case "2" =>
        val list = getListFromUser
        println("Last two elements: " + List3.getLastTwoElem(list).getOrElse(None))
        menu()

      case "3" =>
        val list = getListFromUser
        println("Length of list: " + List3.getLength(list))
        menu()

      case "4" =>
        val list = getListFromUser
        println("Reversed list: " + List3.reverse(list))
        menu()

      case "5" =>
        val list = getListFromUser
        println("Is palindrome: " + List3.isPalindrome(list))
        menu()

      case "6" =>
        val list = getListFromUser
        println("Is palindrome: " + List3.isPalindromeSmart(list))
        menu()

      case "7" =>
        val list = getListFromUser
        println("List without duplicates: " + List3.removeDuplicates(list))
        menu()

      case "8" =>
        val list = getListFromUser
        println("Elements from even indexes: " + List3.getEvenIndexes(list))
        menu()

      case "9" =>
        println("Please enter a number:")
        val number = scala.io.StdIn.readInt()
        println("Is prime: " + List3.isPrime(number))
        menu()

      case "0" => println("Goodbye!")

      case _ => println("Invalid input. Please try again.")
        menu()
    }
  }

  def main(args: Array[String]): Unit = {
    menu()
  }
}