(* Exercise 1a *)
let rec stirling (n, m) =
  if m = 0 || m = 1 || n = m then 1
  else stirling (n - 1, m - 1) + m * stirling (n - 1, m);;

(* print_int (stirling (100, 6));; *)

(* Exercise 1b *)
let memoized_stirling (n, m) = 
  let cache = Hashtbl.create 10 in
  let rec f_mem (n, m) = 
    try Hashtbl.find cache (n, m)
    with Not_found -> 
      if m = 0 || m = 1 || n = m then 1
      else
        let res = f_mem (n - 1, m - 1) + m * f_mem (n - 1, m) in
        Hashtbl.add cache (n , m) res;
        res
  in
  f_mem (n, m);;

print_newline ();;
(* print_int (memoized_stirling (100, 6));; *)

(* Exercise 2 *)
let make_memoize f =
  let cache = Hashtbl.create 16 in
  let rec f_mem x =
    try Hashtbl.find cache x
    with Not_found ->
        let res = f f_mem x in
        Hashtbl.add cache x res;
        res
  in
  f_mem;;

let rec fibo n = match n with
| 0 -> 1
| 1 -> 1
| n -> fibo (n - 1) + fibo (n - 2);;

let fib self = function
    0 -> 1
  | 1 -> 1
  | n -> self (n - 1) + self (n - 2);;

print_newline ();;
(* print_int (fibo 40);; *)

let memoized_fib = make_memoize fib;;
print_newline ();;
(* print_int (memoized_fib 40);; *)

(* Exercise 4a *)
type 'a sequence = Cons of 'a * (unit -> 'a sequence);;

let calc_bell_num n = 
  if n = 0 then 1
  else
    let rec calc_bell_num_rec n k acc =
      if n = k then acc
      else
        let value = memoized_stirling (n, k) in 
        calc_bell_num_rec n (k + 1) (acc + value) 
    in calc_bell_num_rec n 0 0;;

let rec from n = Cons (calc_bell_num n, fun () -> from (n + 1));;

(* Exercise 4b *)
let stream_head = function
Cons (x, _) -> x;;

let stream_tail = function
Cons (_, xf) -> xf ();;

(* Exercise 4c *)
(* i *)
let rec take (n, lxs) =
  match (n, lxs) with
  | (0, _) -> []
  | (n, Cons (x, xf)) -> x :: take (n - 1, xf ());;

(* ii *)
let take2 (n, lxs) =
  let rec take2_rec (n, lxs, counter) =
    match (n, lxs, counter) with
    | (0, _, _) -> []
    | (n, Cons (x, xf), counter) -> if counter mod 2 = 0 then x :: take2_rec (n - 1, xf (), counter + 1) else take2_rec (n, xf (), counter + 1)
  
  in take2_rec (n, lxs, 0);;

(* iii *)
let rec skip (n, lxs) =
  match (n, lxs) with
  | (0, _) -> lxs
  | (n, Cons (x, xf)) -> skip (n - 1, xf ());;

(* iv *)
let rec from_natural n = Cons (n, fun () -> from_natural (n + 1));;

let rec zip (s1, s2, n) =
  match (s1, s2, n) with
  | (_, _, 0) -> []
  | (Cons (x, xf), Cons (y, yf), n) -> (x, y) :: zip (xf (), yf (), n - 1);;

(* v *)
let rec map f s n =
  match (s, n) with
  | (_, 0) -> []
  | (Cons (x, xf), n) -> (f x) :: map f (xf ()) (n - 1);; 


(* Tests *)
(* Helper *)
let print_list list =
  List.map (fun x -> print_int x; print_char ' ') list;;

let print_list_of_tuples list =
  List.map (fun tup -> match tup with (x, y) -> print_string "("; print_int x; print_char ' '; print_int y; print_string ") ") list;;

(* 4a *)
print_string "\n\n4a\n";;
print_list (take (10, from 0));;

(* 4b *)
print_string "\n\n4b - head\n";;
print_int (stream_head (from 9));;

print_string "\n\n4b - tail\n";;
print_list (take (10, stream_tail (from 0)));;

(* 4c *)
(* ii *)
print_string "\n\n4c - ii\n";;
print_list (take2 (5, from 0));;

(* iii *)
print_string "\n\n4c - iii\n";;
print_list (take (5, skip (5, from 0)));;

(* iv *)
print_string "\n\n4c - iv\n";;
print_list_of_tuples (zip (from_natural 0, from 0, 10));;

(* v *)
print_string "\n\n4c - v\n";;
print_list (map (fun x -> x * (-1)) (from 0) 10);;