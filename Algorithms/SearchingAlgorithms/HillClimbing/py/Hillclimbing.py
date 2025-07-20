import random

def hill_climbing(max_iter=1000, step_size=0.1):
    # Start at a random x
    current_x = random.uniform(-10, 10)
    current_val = -current_x**2 + 4*current_x
    
    for _ in range(max_iter):
        # Generate a neighboring solution
        neighbor_x = current_x + random.uniform(-step_size, step_size)
        neighbor_val = -neighbor_x**2 + 4*neighbor_x
        
        # Move to the neighbor if it's better
        if neighbor_val > current_val:
            current_x, current_val = neighbor_x, neighbor_val
    
    return current_x, current_val

# Run Hill Climbing
best_x, best_val = hill_climbing()
print(f"Best x: {best_x:.2f}, Best value: {best_val:.2f}")