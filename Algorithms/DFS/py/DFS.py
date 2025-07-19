def dfs(graph, start):

    visited = []
    parent = {start: None}
    stack = [start]  # Using stack for DFS
    
    while stack:
        node = stack.pop()
        if node not in visited:
            visited.append(node)
            # Push neighbors in reverse order to visit them in order
            for neighbor in reversed(graph[node]):
                if neighbor not in parent:
                    parent[neighbor] = node
                    stack.append(neighbor)
    
    return visited, parent

# Recursive version
def dfs_recursive(graph, node, visited=None, parent=None):
    if visited is None:
        visited = []
    if parent is None:
        parent = {node: None}
    
    if node not in visited:
        visited.append(node)
        for neighbor in graph[node]:
            if neighbor not in parent:
                parent[neighbor] = node
                dfs_recursive(graph, neighbor, visited, parent)
    
    return visited, parent

# Example usage:
if __name__ == "__main__":
    graph = {
        'A': ['B', 'C'],
        'B': ['A', 'D', 'E'],
        'C': ['A', 'F'],
        'D': ['B'],
        'E': ['B', 'F'],
        'F': ['C', 'E']
    }
    
    print("Iterative DFS:")
    visited, parent = dfs(graph, 'A')
    print("Visited order:", visited)
    print("Parent relationships:", parent)
    
    print("\nRecursive DFS:")
    visited_rec, parent_rec = dfs_recursive(graph, 'A')
    print("Visited order:", visited_rec)
    print("Parent relationships:", parent_rec)