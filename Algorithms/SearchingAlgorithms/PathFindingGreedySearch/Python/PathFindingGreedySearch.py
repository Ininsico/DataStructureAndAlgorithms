import heapq
from math import sqrt

# City data with coordinates (latitude, longitude approximations)
cities = {
    'Islamabad': (33.6844, 73.0479),
    'Rawalpindi': (33.5651, 73.0169),
    'Lahore': (31.5204, 74.3587),
    'Multan': (30.1575, 71.5249),
    'Faisalabad': (31.4180, 73.0790),
    'Sukkur': (27.7132, 68.8482),
    'Hyderabad': (25.3969, 68.3778),
    'Karachi': (24.8607, 67.0011)
}

# Road connections between cities (simplified)
roads = {
    'Islamabad': ['Rawalpindi', 'Lahore'],
    'Rawalpindi': ['Islamabad', 'Lahore', 'Faisalabad'],
    'Lahore': ['Islamabad', 'Rawalpindi', 'Faisalabad', 'Multan'],
    'Faisalabad': ['Rawalpindi', 'Lahore', 'Multan'],
    'Multan': ['Lahore', 'Faisalabad', 'Sukkur'],
    'Sukkur': ['Multan', 'Hyderabad'],
    'Hyderabad': ['Sukkur', 'Karachi'],
    'Karachi': ['Hyderabad']
}

def heuristic(city1, city2):
    """Calculate straight-line distance between two cities (simplified)"""
    x1, y1 = cities[city1]
    x2, y2 = cities[city2]
    return sqrt((x2-x1)**2 + (y2-y1)**2)

def greedy_path(start, goal):
    visited = set()
    priority_queue = []
    heapq.heappush(priority_queue, (heuristic(start, goal), start, [start]))
    
    while priority_queue:
        _, current, path = heapq.heappop(priority_queue)
        
        if current == goal:
            return path
            
        if current not in visited:
            visited.add(current)
            
            for neighbor in roads.get(current, []):
                if neighbor not in visited:
                    new_path = path + [neighbor]
                    priority = heuristic(neighbor, goal)
                    heapq.heappush(priority_queue, (priority, neighbor, new_path))
    
    return None  # No path found

# Find path from Islamabad to Karachi
path = greedy_path('Islamabad', 'Karachi')
print("Greedy Path:", path)