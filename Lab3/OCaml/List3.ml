(* Exercise 1 *)
let rec getLastElem list =
  match list with
  | [] -> None
  | elem :: [] -> Some(elem)
  | elem :: rest -> getLastElem(rest);;

(* Exercise 2 *)
let rec getLastTwoElem list = 
  match list with
  | [] -> None
  | elem1 :: [] -> None
  | elem1 :: elem2 :: [] -> Some((elem1, elem2))
  | elem1 :: elem2 :: rest -> getLastTwoElem(elem2 :: rest);;

(* Exercise 3 *)
let getLength list = 
  let rec getLengthRec (list, length) =
    match list with
    | [] -> length
    | elem :: rest -> getLengthRec(rest, length + 1)

  in getLengthRec(list, 0);;

(* Exercise 4 *)
let reverse list =
  let rec reverseRec (list, reversed) =
    match list with
    | [] -> reversed
    | elem :: rest -> reverseRec(rest, elem :: reversed)

  in reverseRec(list, []);;

(* Exercise 5 *)
let isPalindrome list =
  let rec isPalindromeRec (list, originalList, reversed) =
    match list with
    | [] -> reversed = originalList
    | elem :: rest -> isPalindromeRec(rest, originalList, elem :: reversed)

  in isPalindromeRec(list, list, []);;

let isPalindromeSmart list =
  list = reverse(list);;

(* Exercise 6 *)
let rec checkPresence (list, elem) =
  match list with
  | [] -> false
  | elem1 :: rest -> if elem1 = elem then true else checkPresence(rest, elem);;

let removeDuplicates list =
  let rec removeDuplicatesRec (list, removed) =
    match list with
    | [] -> reverse removed
    | elem :: rest -> removeDuplicatesRec(rest, if checkPresence(removed, elem) then removed else elem :: removed)

  in removeDuplicatesRec(list, []);;

(* Exercise 7 *)
let getEvenIndexes list =
  let rec getEvenIndexesRec (list, even) =
    match list with
    | [] -> reverse even
    | elem1 :: [] -> reverse (elem1 :: even)
    | elem1 :: elem2 :: rest -> getEvenIndexesRec(rest, elem1 :: even)

  in getEvenIndexesRec(list, []);;

(* Exercise 8 *)
let isPrime number =
  let rec isPrimeRec (number, divisor) =
    if divisor = 1 then true
    else if number mod divisor = 0 then false
    else isPrimeRec(number, divisor - 1)

  in if number = -1 || number = 0 || number = 1 then false
  else if number < 0 then isPrimeRec(number * -1, (number * -1) / 2)
  else isPrimeRec(number, number / 2);;


(* ------------------------------------------------------------------------------------------------------- *)


let intOfIntOpt = function None -> 0 | Some n -> n;;
let stringOfIntOpt = function None -> "None" | Some n -> string_of_int n;;
let stringOfStringOpt = function None -> "None" | Some n -> n;;
let stringOfIntTupleOpt = function None -> "None" | Some(a, b) -> string_of_int a ^ " " ^ string_of_int b;;
let stringOfStringTupleOpt = function None -> "None" | Some(a, b) -> a ^ " " ^ b;;
let rec printIntList = function
[] -> ()
| elem :: rest -> print_int elem ; print_string " " ; printIntList rest;;
let rec printStringList = function
[] -> ()
| elem :: rest -> print_string (elem ^ " "); printStringList rest;;

let rec readIntList arr =
  try
    let i = read_int_opt () in
    if i <> None then readIntList ((intOfIntOpt i) :: arr) else reverse arr
  with End_of_file -> reverse arr;;

let rec readStringList arr =
  try 
    let i = read_line () in
    if i <> "exit 0;;" then readStringList (i :: arr) else reverse arr
with End_of_file -> reverse arr;;

let rec getListFromUser () : int * int list * string list =
  print_string "Please select a type of list:\n";
  print_string "1. Int\n";
  print_string "2. String\n";
  print_string "0. Exit\n\n";

  let input = read_line () in
    match input with
    | "1" -> 
      print_string "Please enter a list of Ints (separate elements by new line - enter string to finish):\n";
      (1, readIntList [], [])

    | "2" ->
      print_string "Please enter a list of Strings (separate elements by new line - type \"exit 0;;\" to finish):\n";
      (2, [], readStringList [])

    | "0" ->
      print_string "Exiting...\n";
      (0, [], [])

    | _ ->
      print_string "Invalid input\n";
      getListFromUser ();;

let rec menu () =
  print_string "\nPlease select an operation:\n";
  print_string "1. Get last element from list\n";
  print_string "2. Get last two elements from list\n";
  print_string "3. Get length of list\n";
  print_string "4. Reverse list\n";
  print_string "5. Check if list is palindrome\n";
  print_string "6. Check if list is palindrome (use reverse method)\n";
  print_string "7. Remove duplicates from list\n";
  print_string "8. Get elements from even indexes\n";
  print_string "9. Check if number is prime\n";
  print_string "0. Exit\n\n";

  let input = read_line () in
  match input with
  | "1" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then
      print_string ("Last element: " ^ stringOfIntOpt (getLastElem intList) ^ "\n")
    else if pos = 2 then
      print_string ("Last element: " ^ stringOfStringOpt (getLastElem stringList) ^ "\n");

    menu ()

  | "2" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then
      print_string ("Last two elements: " ^ stringOfIntTupleOpt (getLastTwoElem intList) ^ "\n")
    else if pos = 2 then
      print_string ("Last two elements: " ^ stringOfStringTupleOpt (getLastTwoElem stringList) ^ "\n");

    menu ()

  | "3" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then
      print_string ("Length of list: " ^ string_of_int (getLength intList) ^ "\n")
    else if pos = 2 then
      print_string ("Length of list: " ^ string_of_int (getLength stringList) ^ "\n");

    menu ()

  | "4" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then (
      print_string "Reversed list: "; 
      printIntList (reverse intList);
      print_newline ();
      )
    else if pos = 2 then 
      print_string "Reversed list: ";
      printStringList (reverse stringList);
      print_newline ();

    menu ()

  | "5" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then
      print_string ("Is palindrome: " ^ string_of_bool (isPalindrome intList) ^ "\n")
    else if pos = 2 then
      print_string ("Is palindrome: " ^ string_of_bool (isPalindrome stringList) ^ "\n");

    menu ()

  | "6" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then
      print_string ("Is palindrome: " ^ string_of_bool (isPalindromeSmart intList) ^ "\n")
    else if pos = 2 then
      print_string ("Is palindrome: " ^ string_of_bool (isPalindromeSmart stringList) ^ "\n");
  
    menu ()

  | "7" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then (
      print_string "List without duplicates: "; 
      printIntList (removeDuplicates intList);
      print_newline ();
      )
    else if pos = 2 then 
      print_string "List without duplicates: ";
      printStringList (removeDuplicates stringList);
      print_newline ();

    menu ()

  | "8" ->
    let (pos, intList, stringList) = getListFromUser () in
    if pos = 1 then (
      print_string "Elements from even indexes: "; 
      printIntList (getEvenIndexes intList);
      print_newline ();
      )
    else if pos = 2 then 
      print_string "Elements from even indexes: ";
      printStringList (getEvenIndexes stringList);
      print_newline ();

    menu ()

  | "9" ->
    print_string "Please enter a number: ";
    let input = read_int_opt () in
    print_string ("Is prime: " ^ string_of_bool (isPrime (intOfIntOpt input)));
    print_newline ();

    menu ()

  | "0" ->
    print_string "Goodbye!"

  | _ ->
    print_string "Invalid input. Please try again.\n";
    menu ();;
    
menu ();;