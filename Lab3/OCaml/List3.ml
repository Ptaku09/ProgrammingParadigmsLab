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
    | [] -> 0
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
    | [] -> reversed == originalList
    | elem :: rest -> isPalindromeRec(rest, originalList, elem :: reversed)

  in isPalindromeRec(list, list, []);;

let isPalindromeSmart list =
  list == reverse(list);;

(* Exercise 6 *)
let rec checkPresence (list, elem) =
  match list with
  | [] -> false
  | elem1 :: rest -> if elem1 = elem then true else checkPresence(rest, elem);;

let removeDuplicates list =
  let rec removeDuplicatesRec (list, removed) =
    match list with
    | [] -> removed
    | elem :: rest -> removeDuplicatesRec(rest, if checkPresence(removed, elem) then removed else elem :: removed)

  in removeDuplicatesRec(list, []);;

(* Exercise 7 *)
let getEvenIndexes list =
  let rec getEvenIndexesRec (list, even) =
    match list with
    | [] -> even
    | elem1 :: [] -> elem1 :: even
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