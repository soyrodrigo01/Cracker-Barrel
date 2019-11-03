%Name:		Jose Rodrigo Arbaiza
%ID:		jra0224
%Date:	11/02/2019

%Description:
%This is a prolog "translation" from the python program
%located at https://github.com/ptarau/CrackerBarrelPegPuzzle

moves(0,1,3).
moves(0,2,5).
moves(1,3,6).
moves(1,4,8).
moves(2,4,7).
moves(2,5,9).
moves(3,6,10).
moves(3,7,12).
moves(4,7,11).
moves(4,8,13).
moves(5,8,12).
moves(5,9,14).
moves(3,4,5).
moves(6,7,8).
moves(7,8,9).
moves(10,11,12).
moves(11,12,13).
moves(12,13,14).

% generator for moves and their oposites
step(F, O, T):-
	moves(F, O, T);moves(T, O, F).

% builds cells, 1 if full 0 if empty
% returns as a pair a count k of the full ones and the cells
init(I, Board):-
	findall(J, (between(0,14,J), J=\=I),Board).

% performs, if possible, a move
% given the current occupancy of the cells
move(moves(F, O, T), B1,[T|B3]):-
	select(F, B1, B2),
	select(O, B2, B3),
	step(F, O, T),
	not(member(T, B1)).

% generator that yields all possible solutions
% given a cell configuration
solve([_], []).
solve(B1, [M | Moves]):-
	move(M, B1, B2),
	solve(B2, Moves).


% sets initial position with empty at i
% picks first solution
% collects path made of moves to a list
puzzle(I, Moves, Board):-
	init(I, Board),
	once(solve(Board, Moves)).


set(I, Board, X):-
	member(I, Board)->X=x ; X='.'.

% shows the result by printing out successive states
show(Board):-
	Lines=[[4,0,0], [3,1,2], [2,3,5], [1,6,9], [0,10,14]],
	member(Line, Lines),
	[T, A, B] = Line,
	nl,
	tab(T),
	between(A, B, I),
	set(I, Board, X),
	write(X),
	write(' '),
	fail.

show(_):-
	nl.

% replay a sequence of moves, showing the state of cells
replay([_],[]).
replay(B1, [M | Moves]):-
	move(M, B1, B2),
	!,
	show(B2),
	nl,
	replay(B2, Moves).

replay2([_],[],[]).
replay2(B1, [M | Moves], [B2 | BLT]):-
	move(M, B1, B2),
	!,
	replay2(B2, Moves, BLT).

% prints out a terse view of solutions for each missing peg
terse():-
	numlist(0, 14, I),
	terse(I).

terse([]).
terse(I):-
	[L | R] = I,
	puzzle(L, Moves, Board),
	print(Board),
	nl,
	print_elements(Moves),
	replay2(Board, Moves, BoardL),
	last(BoardL, FBoard),
	print(FBoard),
	nl,
	nl,
	terse(R).

print_elements([]).
print_elements(Li):-
	[L | R] = Li,
	print(L),nl,
	print_elements(R).

% visualizes a solution for each first 5 positions
% others look the same after 120 degrees rotations
go():-
	numlist(0, 4, I),
	go(I),

go([]).
go(I):-
	[L | R] = I,
	write('=== '),write(L),
	write(' ==='), nl,
	puzzle(L, Moves, Board),
	show(Board),
	nl,
	replay(Board, Moves),
	go(R).
