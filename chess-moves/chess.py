grid = [["#"]*8 for _ in range(8)]
grid[0] = ["R", "N", "B", "Q", "K", "B", "N", "R"]
grid[1] = ["P"] * 8

grid[6] = ["p"] * 8
grid[7] = ["r", "n", "b", "q", "k", "b", "n", "r"]

columns = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
print(" "+" ".join(columns))

for i in range(8):
    row_num = 8 - i

    row = grid[i]
    print(f"{row_num}"+"".join(row))
