# Project - Chess Moves

In this project, you'll implement some of the rules of the classic game of chess. You'll be given a chess position and asked to generate _pseudolegal_ moves.

## Project Overview

Chess is a two-player strategy board game played on an 8x8 grid. The game is played between two players, one controlling the white pieces and the other controlling the black pieces. Each player starts with 16 pieces: one king, one queen, two rooks, two knights, two bishops, and eight pawns.

The objective of the game is to defend your own king while attacking your opponent's. The game can end in several ways, including checkmate, stalemate, and draw.

Chess rules seem simple at first glance, but they can be quite complex. For example, it is illegal to make a move that puts your own king in check; this is called a _pseudolegal_ move. A pseudolegal move is a move that is legal according to the rules of chess, but does not necessarily result in a legal position.

For this project, you need to implement:

- Pawn moves and diagonal captures
- Knight moves
- Bishop moves
- Rook moves
- Queen moves
- King moves

You do not need to implement castling, en passant, or promotion. You also do not need to implement check or checkmate detection.

We'd also like a convenient way to represent a chess position. The most common way to represent a chess position is using the Forsyth-Edwards Notation (FEN). A FEN string consists of six fields separated by spaces:
1. Piece placement (from rank 8 to rank 1)
2. Active color (w or b)
3. Castling availability (KQkq)
4. En passant target square (if any)
5. Halfmove clock (for draw conditions)
6. Fullmove number (starting from 1)

The piece placement field is the most important for this project. It consists of 8 ranks, each containing 8 files. Each piece is represented by a single character, with uppercase letters representing white pieces and lowercase letters representing black pieces. The characters are as follows:

- P/p: Pawn
- N/n: Knight
- B/b: Bishop
- R/r: Rook
- Q/q: Queen
- K/k: King

The ranks are separated by slashes (/), and empty squares are represented by numbers (1-8) indicating the number of empty squares. For example, the FEN string for the starting position of a chess game is:

```
rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
```
This string represents the following position:

```
8  r n b q k b n r
7  p p p p p p p p
6  . . . . . . . .
5  . . . . . . . .
4  . . . . . . . .
3  . . . . . . . .
2  P P P P P P P P
1  R N B Q K B N R
   a b c d e f g h
```

The ranks are numbered from 8 to 1, and the files are labeled from a to h. The white pieces are represented by uppercase letters, and the black pieces are represented by lowercase letters. Empty squares are represented by dots (.), and the ranks are separated by spaces.

The active color field indicates which player's turn it is to move. The castling availability field indicates whether either player can castle. The en passant target square field indicates whether a pawn has just moved two squares forward and is available for capture. The halfmove clock field indicates the number of halfmoves since the last pawn move or capture, and the fullmove number field indicates the number of full moves in the game.

## Project Requirements

- Implement a function that parses a FEN string into a more convenient representation of a chess position.
- Implement a function that generates some basic pseudolegal moves, as described above.
- Implement a function that applies a move to a chess position and returns the new position.

## Notes on teamwork

Your team needs to decide how positions and moves will be represented. This is the core decision of the project, as everybody needs to agree on how it works, and the decisions you make here can considerably influence how difficult it is to write each function. Explore this with your team first, writing some test code together. It would be a mistake to split up the work before agreeing on the data model.

You can split the move-generation task into several smaller tasks, one for each piece type. I suggest starting with knights, as they have the simplest movement rules. You can write this one together before splitting up the rest of the work. Note that queen moves are a combination of rook and bishop moves, so you can implement them last. It's possible to structure your code so that you can reuse the rook and bishop code for the queen moves.

## How you'll be assessed

- **Correctness**: Your code should produce the correct output for all test cases.
- **Comprehension**: _Everybody_ should understand _all_ the code in the project.
- **Quality**: We'd like to see a data model that supports the functionality well, and sensible utilization of the Python features you learned throughout the bootcamp.

## How to run the tests

To run the tests, you can configure your IDE to run unittest tests in the "." directory. Alternatively, you can run the tests from the command line using the following command:

```bash
python -m unittest test_chess.py
```