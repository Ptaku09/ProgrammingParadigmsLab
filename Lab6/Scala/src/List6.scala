import scala.collection.mutable

object List6 {
  //  Exercise 1a
  def stirling(n: Int, m: Int): BigInt = {
    if (m == 0 || m == 1 || n == m) 1
    else stirling(n - 1, m - 1) + m * stirling(n - 1, m)
  }

  //  Exercise 1b
  def memoized_stirling(n: Int, m: Int): BigInt = {
    val cache = mutable.HashMap[(Int, Int), BigInt]()

    def f_mem(n: Int, m: Int): BigInt = {
      cache.getOrElseUpdate((n, m), {
        if (m == 0 || m == 1 || n == m) 1
        else f_mem(n - 1, m - 1) + m * f_mem(n - 1, m)
      })
    }

    f_mem(n, m)
  }

  //  Exercise 2
  def make_memoize[A, B](f: (A => B) => A => B): A => B = {
    val cache = mutable.HashMap[A, B]()

    def f_mem(x: A): B = {
      cache.getOrElseUpdate(x, {
        f(f_mem)(x)
      })
    }

    f_mem
  }

  def fibonacci_default(n: Int): BigInt = {
    if (n == 0 || n == 1) 1
    else fibonacci_default(n - 1) + fibonacci_default(n - 2)
  }

  def fibonacci_for_memoization(self: Int => BigInt): Int => BigInt = {
    case 0 => 1
    case 1 => 1
    case n => self(n - 1) + self(n - 2)
  }

  //  EXTRA
  def memoize_better[A, B](f: A => B): A => B = new mutable.HashMap[A, B]() {
    override def apply(key: A): B = getOrElseUpdate(key, f(key))
  }

  val fibonacci_better: Int => BigInt = memoize_better {
    case 0 => 0
    case 1 => 1
    case n => fibonacci_better(n - 1) + fibonacci_better(n - 2)
  }

  val stirling_better: ((Int, Int)) => BigInt = memoize_better {
    case (n, m) =>
      if (m == 0 || m == 1 || n == m) 1
      else stirling_better(n - 1, m - 1) + m * stirling_better(n - 1, m)
  }

  def main(args: Array[String]): Unit = {
    println("Exercise 1a")
    println("stirling(100, 6)")
    var time = System.nanoTime()
    println(stirling(100, 6))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()

    println("Exercise 1b")
    println("stirling(100, 6)")
    time = System.nanoTime()
    println(memoized_stirling(100, 6))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()

    println("Exercise 2")
    println("fibonacci_default(40)")
    time = System.nanoTime()
    println(fibonacci_default(40))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()

    println("memoized_fibonacci(100)")
    time = System.nanoTime()
    println(make_memoize(fibonacci_for_memoization)(100))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()

    println("EXTRA")
    println("fibonacci_better(100)")
    time = System.nanoTime()
    println(fibonacci_better(100))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()

    println("stirling_better(100, 6)")
    time = System.nanoTime()
    println(stirling_better(100, 6))
    println((System.nanoTime() - time) / 1000000 + "ms")
    println()
  }
}
