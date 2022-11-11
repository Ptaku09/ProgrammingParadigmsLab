type point2D = float * float;;

let distance (p1: point2D) (p2: point2D) =
  let (x1, y1) = p1 in
  let (x2, y2) = p2 in

  sqrt ((x1 -. x2) ** 2.0 +. (y1 -. y2) ** 2.0);;
  
print_string "Exercise 1\n";;
print_float (distance (0.0, 0.0) (2.0, 0.0));;
print_newline ();;
print_newline ();;

(* Exercise 2 *)
(* Tuples - impossible to change fields *)
type personTuple = string * string * int * char * float;;
type partnershipTuple = personTuple * personTuple;;

let olderPerson (partnership: partnershipTuple) =
  let (person1, person2) = partnership in
  let (_, _, age1, _, _) = person1 in
  let (_, _, age2, _, _) = person2 in

  if (age1 > age2) then
    person1
  else
    person2;;

let personTuple1 = ("John", "Doe", 24, 'M', 1.80);;
let personTuple2 = ("Jane", "Doe", 28, 'F', 1.60);;
let partnershipTuple1 = (personTuple1, personTuple2);;

let (resName, _, _, _, _) = olderPerson partnershipTuple1;;

print_string "Exercise 2\n";;
print_string resName;;
print_newline ();;

(* Records *)
type personRecord = {
  mutable name: string;
  mutable surname: string;
  mutable age: int;
  mutable gender: char;
  mutable height: float;
};;

type partnershipRecord = {
  mutable person1: personRecord;
  mutable person2: personRecord;
};;

let youngerPerson partnership = 
  if partnership.person1.age < partnership.person2.age then
    partnership.person1
  else 
    partnership.person2;;

let personRecord1: personRecord = {
  name = "John";
  surname = "Doe";
  age = 30;
  gender = 'M';
  height = 1.80;
};;

let personRecord2: personRecord = {
  name = "Jane";
  surname = "Doe";
  age = 32;
  gender = 'F';
  height = 1.60;
};;

let partnershipRecord1: partnershipRecord = {
  person1 = personRecord1;
  person2 = personRecord2;
};;

(* Change age *)
partnershipRecord1.person2.age <- 28;;

print_string (youngerPerson partnershipRecord1).name;;
print_newline ();;
print_newline ();;

(* Exercise 3 *)
type weekDay = Mon | Tue | Wed | Thu | Fri | Sat | Sun;;

let weekDayToString = function
  | Mon -> "Poniedzialek"
  | Tue -> "Wtorek"
  | Wed -> "Sroda"
  | Thu -> "Czwartek"
  | Fri -> "Piatek"
  | Sat -> "Sobota" 
  | Sun -> "Niedziela";;

let nextDay = function
  | Mon -> Tue
  | Tue -> Wed
  | Wed -> Thu
  | Thu -> Fri
  | Fri -> Sat
  | Sat -> Sun
  | Sun -> Mon;;

print_string "Exercise 3\n";;
print_string (weekDayToString Mon);;
print_newline ();;
print_string (weekDayToString (nextDay Mon));;
print_newline ();;
print_newline ();;

(* Exericse 4 *)
type 'a maybe = Just of 'a | Nothing;;

let safeHead list = match list with
  | [] -> Nothing
  | head :: rest -> Just head;;

(* Exercise 5 *)
type solidFigure =
  | Cuboid of { a: float; b: float; h: float }
  | Cone of { r: float; h: float }
  | Sphere of { r: float }
  | Cylinder of { r: float; h: float };;

let volume = function
  | Cuboid { a; b; h } -> a *. b *. h
  | Cone { r; h } -> (1. /. 3.) *. Float.pi *. h *. r ** 2.
  | Sphere { r } -> (4. /. 3.) *. Float.pi *. r ** 3.
  | Cylinder { r; h } -> Float.pi *. h *. r ** 2.;;

let cuboid = Cuboid { a = 2.; b = 2.; h = 2.};;
let cone = Cone { r = 4.; h = 4.5 };;
let sphere = Sphere { r = 5. };;
let cylinder = Cylinder { r = 2.; h = 12.};;

print_string "Exercise 5\n";;
print_float (volume cuboid);;
print_newline ();;
print_float (volume cone);;
print_newline ();;
print_float (volume sphere);;
print_newline ();;
print_float (volume cylinder);;