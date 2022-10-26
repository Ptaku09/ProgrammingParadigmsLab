(* Exercise 1 *)
let rec map list f = 
  match list with
  | [] -> []
  | elem :: rest -> (f elem) :: map rest f;;

(* Exercise 2 *)
let rec filter list pred =
  match list with
  | [] -> []
  | elem :: rest -> if (pred elem) then elem :: filter rest pred else filter rest pred;;

(* Exericse 3 *)
let rec reduce list op acc =
  match list with
  | [] -> acc
  | elem :: rest -> reduce rest op (op elem acc);;

(* Exercise 4 *)
let average list = 
  let sum = reduce list (fun x acc -> x + acc) 0 in
  float_of_int sum /. float_of_int (List.length list);;

(* Exercise 5 *)
let acronym str = 
  let listOfStrings = String.split_on_char ' ' str in
  let uppercaseChars = map listOfStrings (fun s -> String.get s 0) in
  reduce uppercaseChars (fun c acc -> acc ^ (String.make 1 c)) "";;

(* Exercise 6 *)
let conditionalSquare list = 
  let sumOfElems = reduce list (fun x acc -> x + acc) 0 in
  let filteredList = filter list (fun x -> x * x * x <= sumOfElems) in
  map filteredList (fun x -> x * x);;

(* Testing *)
print_string "Exercise 4\n";;
print_float (average [1; 2; 3; 4; 6]);;
print_string "\n\n";;

print_string "Exercise 5\n";;
print_string (acronym "Zaklad Ubezpieczen Spolecznych");;
print_string "\n\n";;

print_string "Exercise 6\n";;
let ex6Example = conditionalSquare [1; 2; 3; 4];;
map (List.rev ex6Example) (fun x -> print_int x; print_string " ");;
