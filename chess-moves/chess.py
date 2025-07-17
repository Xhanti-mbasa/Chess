def parse_fen(fen):
    fen_pieces, to_move, castling_rights, ep, hm, fm = fen.split(" ")
    pieces = [[]]
    for char in fen:
        if char.isdigit():
            pieces[-1].extend(["."] * int(char))
        elif char == "/":
            pieces.append([])
        else:
            pieces[-1].append(char)

    return ...


def generate_moves(board):
    raise NotImplementedError("This function is not implemented yet.")


def apply_move(board, move):
    raise NotImplementedError("This function is not implemented yet.")
