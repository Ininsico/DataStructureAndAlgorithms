# Adaptive Recommendation Engine

A JavaFX application implementing a hybrid recommendation system (Collaborative Filtering + Graph-Based) with custom Data Structures.

## Features
- **User-Based Collaborative Filtering**: Uses Cosine Similarity to find similar users.
- **Graph-Based Recommendations**: Uses BFS (Depth 2) on a custom Bipartite Graph.
- **Interactive Dashboard**:
    - **Simulator**: Add interactions in real-time and see recommendations update.
    - **Visualizer**: View the User-Item graph nodes and connections.
    - **Browser**: Search and filter items.
- **Custom Data Structures**:
    - `Graph`: Adjacency list implementation for efficient traversal.
    - `PriorityQueue`: Custom Binary Min-Heap for Top-K selection.

## Architecture
- **Language**: Java 21
- **UI Framework**: JavaFX
- **Build Tool**: Maven

## Complexity Analysis
- **Graph Traversal (BFS)**: `O(V + E)` - Efficient for local neighborhood search.
- **Top-K Selection**: `O(N log K)` using Priority Queue.
- **Similarity Calculation**: `O(N)` where N is number of items.
- **Lookups**: `O(1)` using HashMaps for User/Item retrieval.

## Setup & Running

### Prerequisites
- Java JDK 21+
- Maven 3.8+

### How to Run
1. Navigate to the project directory:
   ```bash
   cd AdaptiveRecommendationEngine
   ```
2. Build and Run using Maven:
   ```bash
   mvn javafx:run
   ```

### Troubleshooting
If `mvn` is not recognized:
- Ensure Maven is installed and added to your system PATH.
- OR open this project in IntelliJ IDEA / Eclipse and let it import the Maven project `pom.xml`.

## Project Structure
```
src/main/java/com/dsa/
├── algorithms/      # RecommendationEngine
├── core/           # Graph, User, Item, PriorityQueue
├── data/           # DataLoader
└── ui/             # JavaFX Views & Controllers
```
