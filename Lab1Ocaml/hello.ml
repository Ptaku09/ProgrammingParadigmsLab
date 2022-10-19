print_string("Hello world!\n");;

let double (x : int) : int = 2 * x;;
let twice ((f : int -> int), (x : int)) : int = f (f x);;
let quad (x : int) : int = twice (double, x);;


let addNumbersIf ((f : int -> bool), (x : int), (y : int), (z : int)) : int =
  let sum = ref 0 in

  if (f x = true) then sum := !sum + x;
  if (f y = true) then sum := !sum + y;
  if (f z = true) then sum := !sum + z;

  !sum;;

let test1 (x : int) : bool = x mod 2 = 0;;
let test2 (x : int) : bool = x mod 3 = 0;;
let test3 (x : int) : bool = x > 5;;
