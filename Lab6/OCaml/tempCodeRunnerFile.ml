let calc_bell_num n = 
  let rec calc_bell_num_rec n k acc =
    if n = k then acc
    else
      let value = memoized_stirling (n, k) in 
      calc_bell_num_rec n (k + 1) (acc + value) 
  in calc_bell_num_rec n 0 0;;

let rec from n = Cons (calc_bell_num n, fun () -> from (calc_bell_num (n + 1)));;

let rec ltake (n, lxs) =
  match (n, lxs) with
  (0, _) -> []
  | (n, Cons(x,xf)) -> x::ltake(n-1, xf())
  ;;

let l = ltake (5,from 30);;

List.map print_int l;;