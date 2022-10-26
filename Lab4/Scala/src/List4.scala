import java.util.Date

object List4 {
  def log(prefix: "DEBUG" | "WARN" | "INFO" | "CRITICAL")(datetime: Date)(text: String): Unit = {
    prefix match
      case "DEBUG" => println(Console.BLUE + s"[$prefix] $datetime \t $text")
      case "WARN" => println(Console.YELLOW + s"[$prefix] $datetime \t $text")
      case "INFO" => println(Console.GREEN + s"[$prefix] $datetime \t $text")
      case "CRITICAL" => println(Console.RED + s"[$prefix] $datetime \t $text")
  }

  def main(args: Array[String]): Unit = {
    val warnLog = log("WARN")
    val dailyWarnLog = warnLog(new Date())
    dailyWarnLog("Hello")

    log("DEBUG")(new Date())("Hello")
    log("INFO")(new Date())("Hello")
    log("CRITICAL")(new Date())("Hello")
  }
}
