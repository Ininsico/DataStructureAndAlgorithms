def ida_star(grid, start, goal):
    def search(path, g, bound):
        node = path[-1]
        f = g + heuristic(node, goal) # type: ignore
        if f > bound:
            return f
        if node == goal:
            return "FOUND"
        min_cost = float('inf')
        for i, j in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
            neighbor = (node[0] + i, node[1] + j)
            if (0 <= neighbor[0] < len(grid) and 0 <= neighbor[1] < len(grid[0]) 
                and grid[neighbor[0]][neighbor[1]] != 1 
                and neighbor not in path):
                path.append(neighbor)
                res = search(path, g + 1, bound)
                if res == "FOUND":
                    return "FOUND"
                if res < min_cost:
                    min_cost = res
                path.pop()
        return min_cost

    bound = heuristic(start, goal) # type: ignore
    path = [start]
    while True:
        res = search(path, 0, bound)
        if res == "FOUND":
            return path
        if res == float('inf'):
            return None  
        bound = res

grid = [
    [0, 0, 0, 0, 0],
    [0, 1, 0, 1, 0],  #
    [0, 0, 1, 0, 0],
    [0, 1, 0, 1, 0],
    [0, 0, 0, 0, 0]
]
start = (0, 0)
goal = (4, 4)
path = ida_star(grid, start, goal)
print("IDA* Path:", path)