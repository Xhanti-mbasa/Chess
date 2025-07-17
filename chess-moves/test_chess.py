import unittest
import chess


class TestChessMoves(unittest.TestCase):
    def test_opening_moves(self):
        board = chess.parse_fen(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        )

        moves = chess.generate_moves(board)
        self.assertEqual(len(moves), 20, f"Expected 20 moves, but got {len(moves)}")

    def test_zukertort_1(self):
        board = chess.parse_fen(
            "rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1"
        )

        moves = chess.generate_moves(board)
        self.assertEqual(len(moves), 20, f"Expected 20 moves, but got {len(moves)}")

    def test_zukertort_2(self):
        board = chess.parse_fen(
            "rnbqkbnr/pppp1ppp/4p3/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 0 2"
        )

        moves = chess.generate_moves(board)
        self.assertEqual(len(moves), 22, f"Expected 22 moves, but got {len(moves)}")


if __name__ == "__main__":
    unittest.main()