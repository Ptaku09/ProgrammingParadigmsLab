(* Exercise 1 *)
module type POINT_TYPE = sig
  type point_3D = MInt of int point | MFloat of float point and 'a point = {x : 'a; y : 'a; z : 'a}
  val distance : point_3D -> point_3D -> float
end;;

module Point : POINT_TYPE = struct
  type point_3D = MInt of int point | MFloat of float point and 'a point = {x : 'a; y : 'a; z : 'a}
  let distance (p1: point_3D) (p2: point_3D) =
    match (p1, p2) with
    | (MFloat p1, MFloat p2) ->
      sqrt ((p1.x -. p2.x) ** 2. +. (p1.y -. p2.y) ** 2. +. (p1.z -. p2.z) ** 2.)
    | (MInt p1, MInt p2) ->
      sqrt ((float_of_int (p1.x - p2.x)) ** 2. +. (float_of_int (p1.y - p2.y)) ** 2. +. (float_of_int (p1.z - p2.z)) ** 2.)
    | (MFloat p1, MInt p2) ->
      sqrt ((p1.x -. float_of_int p2.x) ** 2. +. (p1.y -. float_of_int p2.y) ** 2. +. (p1.z -. float_of_int p2.z) ** 2.)
    | (MInt p1, MFloat p2) ->
      sqrt ((float_of_int p1.x -. p2.x) ** 2. +. (float_of_int p1.y -. p2.y) ** 2. +. (float_of_int p1.z -. p2.z) ** 2.)    
end;;

let p1 = Point.MInt {x = 1; y = 2; z = 3};;
let p2 = Point.MFloat {x = 0.0; y = 0.0; z = 0.0};;
let d1 = Point.distance p1 p2;;

print_string "Exercise 1\n";;
print_float d1;;
print_newline ();;
print_newline ();;

(* Exercise 2 *)
module type SEGMENT_TYPE = sig
  open Point
  type segment = {p1 : point_3D; p2 : point_3D}
  val length : segment -> float
end;;

module Segment : SEGMENT_TYPE = struct
  open Point
  type segment = {p1 : point_3D; p2 : point_3D}
  let length (s: segment) = distance s.p1 s.p2
end;;

let s1 = Segment.{p1 = p1; p2 = p2};;

print_string "Exercise 2\n";;
print_float (Segment.length s1);;
print_newline ();;
print_newline ();;

(* Exercise 3 *)
module BTree : sig
  type 'a btree = Nil | Cons of 'a * 'a btree * 'a btree
  val add : 'a btree -> 'a -> 'a btree;;
  val remove : 'a btree -> 'a -> 'a btree;;
  val preorder : 'a btree -> 'a list;;
  val inorder : 'a btree -> 'a list;;
  val postorder : 'a btree -> 'a list;;
  val leaves : 'a btree -> 'a list;;
end = struct
  type 'a btree = Nil | Cons of 'a * 'a btree * 'a btree
  let rec add (t: 'a btree) (x: 'a) =
    match t with
    | Nil -> Cons (x, Nil, Nil)
    | Cons (y, l, r) ->
      if x < y then Cons (y, add l x, r)
      else if x > y then Cons (y, l, add r x)
      else t
  let rec remove (t: 'a btree) (x: 'a) =
    match t with
    | Nil -> Nil
    | Cons (y, l, r) ->
      if x < y then Cons (y, remove l x, r)
      else if x > y then Cons (y, l, remove r x)
      else
        match (l, r) with
        | (Nil, Nil) -> Nil
        | (Nil, _) -> r
        | (_, Nil) -> l
        | (_, _) ->
          let rec min (t: 'a btree) =
            match t with
            | Nil -> failwith "min"
            | Cons (y, Nil, _) -> y
            | Cons (_, l, _) -> min l
          in
          let y' = min r in
          Cons (y', l, remove r y')
  let rec preorder (t: 'a btree) =
    match t with
    | Nil -> []
    | Cons (y, l, r) -> y :: (preorder l) @ (preorder r)
  let rec inorder (t: 'a btree) =
    match t with
    | Nil -> []
    | Cons (y, l, r) -> (inorder l) @ (y :: inorder r)
  let rec postorder (t: 'a btree) =
    match t with
    | Nil -> []
    | Cons (y, l, r) -> (postorder l) @ (postorder r) @ [y]
  let rec leaves (t: 'a btree) =
    match t with
    | Nil -> []
    | Cons (y, Nil, Nil) -> [y]
    | Cons (_, l, r) -> (leaves l) @ (leaves r)
end;;

open Printf;;
let t1 = BTree.add (BTree.add (BTree.add (BTree.add (BTree.add (BTree.Cons (6, Nil, Nil)) 2) 12) 9) 15) 3;;

print_string "Exercise 3\n";;
List.iter (printf "%d ") (BTree.preorder t1);;
print_newline ();;
List.iter (printf "%d ") (BTree.inorder t1);;
print_newline ();;
List.iter (printf "%d ") (BTree.postorder t1);;
print_newline ();;

let t2 = BTree.add t1 13;;

List.iter (printf "%d ") (BTree.leaves t2);;
print_newline ();;

let t3 = BTree.remove t2 12;;
let t3 = BTree.remove t3 9;;

List.iter (printf "%d ") (BTree.inorder t3);;
print_newline ();;
print_newline ();;

(* Exercise 4 *)
(* a *)
module Make_Point (P: POINT_TYPE) = struct
  let create x y z = P.MInt {x; y; z} (* or P.MFloat {x; y; z} *)
end;;

module Point_Maker = Make_Point(Point);;

let s2 = Segment.{p1 = Point_Maker.create 1 2 3; p2 = Point_Maker.create 2 2 3};;

print_string "Exercise 4 a)\n";;
print_float (Segment.length s2);;
print_newline ();;
print_newline ();;

(* b *)
module Make_Segment (S: SEGMENT_TYPE) = struct
  let create p1 p2 = S.{p1; p2}
end;;

module Segment_Maker = Make_Segment(Segment);;

let s3 = Segment_Maker.create (Point_Maker.create 1 2 3) (Point_Maker.create 2 2 3);;

print_string "Exercise 4 b)\n";;
print_float (Segment.length s3);;
print_newline ();;
print_newline ();;

(* c *)
module type TRANSLATION_TYPE = sig
  type translation3D = TInt of int translation | TFloat of float translation and 'a translation = {dx : 'a; dy : 'a; dz : 'a}
end;;

module Translation : TRANSLATION_TYPE = struct
  type translation3D = TInt of int translation | TFloat of float translation and 'a translation = {dx : 'a; dy : 'a; dz : 'a}
end;;

module Translate_Point (P: POINT_TYPE) (T: TRANSLATION_TYPE) = struct
  let translate t p =
    match (t, p) with
    | (T.TInt t, P.MInt p) -> P.MInt { P.x = p.x + t.dx; P.y = p.y + t.dy; P.z = p.z + t.dz }
    | (T.TFloat t, P.MFloat p) -> P.MFloat { P.x = p.x +. t.dx; P.y = p.y +. t.dy; P.z = p.z +. t.dz }
    | (T.TInt t, P.MFloat p) -> P.MFloat { P.x = p.x +. float_of_int t.dx; P.y = p.y +. float_of_int t.dy; P.z = p.z +. float_of_int t.dz }
    | (T.TFloat t, P.MInt p) -> P.MFloat { P.x = float_of_int p.x +. t.dx; P.y = float_of_int p.y +. t.dy; P.z = float_of_int p.z +. t.dz }
end;;

module Translate_Segment (S: SEGMENT_TYPE) (T: TRANSLATION_TYPE) = struct
  open Point;;
  module Translate_Point = Translate_Point (Point) (T);;
  let translate t s = { S.p1 = Translate_Point.translate t s.S.p1; S.p2 = Translate_Point.translate t s.S.p2 }
end;;

let translate1 = Translation.TInt {dx = 10; dy = 0; dz = 0};;

module Translate_Point_Int = Translate_Point (Point) (Translation);;
module Translate_Segment_Int = Translate_Segment (Segment) (Translation);;

let p4 = Translate_Point_Int.translate translate1 (Point_Maker.create 0 0 0);;
let p5 = Point_Maker.create 0 0 0;;

let s4 = Translate_Segment_Int.translate translate1 (Segment_Maker.create p4 p5);;

print_string "Exercise 4 c)\n";;
print_float (Segment.length s4);;