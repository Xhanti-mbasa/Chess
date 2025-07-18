
grid = [[" " if (row + col) % 2 == 0 else "#" for col in range(8)] for row in range(8)]

# Place pieces (uppercase = white, lowercase = black)
grid[0] = ["R", "N", "B", "Q", "K", "B", "N", "R"]
grid[1] = ["P"] * 8
grid[6] = ["p"] * 8
grid[7] = ["r", "n", "b", "q", "k", "b", "n", "r"]

columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

def print_board():
    print("  " + " ".join(columns))
    for i in range(8):
        print(f"{8 - i} " + " ".join(grid[i]))

def pos_to_index(pos):
    col = columns.index(pos[0].upper())
    row = 8 - int(pos[1])
    return row, col

def move_pawn(start, end):
    sr, sc = pos_to_index(start)
    er, ec = pos_to_index(end)
    piece = grid[sr][sc]

    if piece == "P" and sc == ec and grid[er][ec] in [" ", "#"] and sr - 1 == er:
        grid[er][ec] = "P"
        # replacing the block of the block we just moved from.
        grid[sr][sc] = " " if (sr + sc) % 2 == 0 else "#"
    elif piece == "p" and sc == ec and grid[er][ec] in [" ", "#"] and sr + 1 == er:
        grid[er][ec] = "p"
        grid[sr][sc] = " " if (sr + sc) % 2 == 0 else "#"
    else:
        print("Invalid pawn move")

print_board()
move_pawn("B2", "B3")
move_pawn("B7", "B6")
print("\nAfter moves:\n")
print_board()