from collections import deque

def bfs(graph, start):
    """
    Perform Breadth-First Search on a graph
    
    Args:
    graph: dict - adjacency list representation of the graph
    start: starting node for BFS
    
    Returns:
    tuple: (visited nodes in order, parent dictionary)
    """
    visited = []  # List to keep track of visited nodes in order
    queue = deque()  # Initialize a queue
    parent = {start: None}  # To keep track of the parent of each node
    
    queue.append(start)
    
    while queue:
        node = queue.popleft()
        
        if node not in visited:
            visited.append(node)
            
            # Explore all neighbors
            for neighbor in graph[node]:
                if neighbor not in parent:  # If not visited yet
                    parent[neighbor] = node
                    queue.append(neighbor)
    
    return visited, parent

# Example usage:
if __name__ == "__main__":
    # Sample graph represented as adjacency list
    graph = {
        'A': ['B', 'C'],
        'B': ['A', 'D', 'E'],
        'C': ['A', 'F'],
        'D': ['B'],
        'E': ['B', 'F'],
        'F': ['C', 'E']
    }
    
    start_node = 'A'
    visited_nodes, parent_dict = bfs(graph, start_node)
    
    print("BFS Visited Order:", visited_nodes)
    print("Parent Relationships:", parent_dict)
    
    # To reconstruct path from start to any node:
    def get_path(parent, node):
        path = []
        while node is not None:
            path.append(node)
            node = parent[node]
        return path[::-1]  # Reverse to get start->end order
    
    print("Path from A to F:", get_path(parent_dict, 'F'))