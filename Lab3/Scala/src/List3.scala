import scala.annotation.tailrec

object List3 {
  // Exercise 1
  @tailrec
  def getLastElem[A](list: List[A]): Option[A] = list match {
    case Nil => None
    case elem :: Nil => Some(elem)
    case elem :: rest => getLastElem(rest)
  }

  // Exercise 2
  @tailrec
  def getLastTwoElem[A](list: List[A]): Option[(A, A)] = list match {
    case Nil => None
    case elem1 :: Nil => None
    case elem1 :: elem2 :: Nil => Some((elem1, elem2))
    case elem1 :: elem2 :: rest => getLastTwoElem(elem2 :: rest)
  }

  // Exercise 3
  def getLength[A](list: List[A]): Int = {
    @tailrec
    def getLengthRec[A](list: List[A], length: Int): Int = list match {
      case Nil => length
      case elem :: rest => getLengthRec(rest, length + 1)
    }

    getLengthRec(list, 0)
  }

  // Exercise 4
  def reverse[A](list: List[A]): List[A] = {
    @tailrec
    def reverseRec[A](list: List[A], reversed: List[A]): List[A] = list match {
      case Nil => reversed
      case elem :: rest => reverseRec(rest, elem :: reversed)
    }

    reverseRec(list, List())
  }

  // Exercise 5
  def isPalindrome[A](list: List[A]): Boolean = {
    @tailrec
    def isPalindromeRec[A](list: List[A], originalList: List[A], reversed: List[A]): Boolean = list match {
      case Nil => reversed == originalList
      case elem :: rest => isPalindromeRec(rest, originalList, elem :: reversed)
    }

    isPalindromeRec(list, list, List())
  }

  def isPalindromeSmart[A](list: List[A]): Boolean = {
    list == reverse(list)
  }

  // Exercise 6
  @tailrec
  private def checkPresence[A](list: List[A], elem: A): Boolean = list match {
    case Nil => false
    case elem1 :: rest => if (elem1 == elem) true else checkPresence(rest, elem)
  }

  def removeDuplicates[A](list: List[A]): List[A] = {
    @tailrec
    def removeDuplicatesRec[A](list: List[A], removed: List[A]): List[A] = list match {
      case Nil => removed
      case elem :: rest => removeDuplicatesRec(rest, if (checkPresence(removed, elem)) removed else elem :: removed)
    }

    reverse(removeDuplicatesRec(list, List()))
  }

  // Exercise 7
  def getEvenIndexes[A](list: List[A]): List[A] = {
    @tailrec
    def getEvenIndexesRec[A](list: List[A], even: List[A]): List[A] = list match {
      case Nil => even
      case elem1 :: Nil => elem1 :: even
      case elem1 :: elem2 :: rest => getEvenIndexesRec(rest, elem1 :: even)
    }

    reverse(getEvenIndexesRec(list, List()))
  }

  // Exercise 8
  def isPrime(number: Int): Boolean = {
    @tailrec
    def isPrimeRec(number: Int, divisor: Int): Boolean = {
      if (divisor == 1) true
      else if (number % divisor == 0) false
      else isPrimeRec(number, divisor - 1)
    }

    if (number == -1 || number == 0 || number == 1) false
    else if (number < 0) isPrimeRec(number * -1, (number * -1) / 2)
    else isPrimeRec(number, number / 2)
  }
}
