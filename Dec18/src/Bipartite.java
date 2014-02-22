import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random; 
import java.util.Set;
import java.util.Stack;
import java.util.Collections;

public class Bipartite {
	private int N; 
	private int d; 
	private Vertex[] vertexList;
	
	public Bipartite(int N, int d) {
		this.N = N;
		this.d = d; 
		
		vertexList = new Vertex[N * 2];
		for (int i = 0; i < N * 2; i++) {
			vertexList[i] = new Vertex(i);
		}
		
		generateGraph(N, d, vertexList);
		for (Vertex v : vertexList) {
			Collections.sort(v.getAdjList());
		}
	}
	
	public Bipartite(int N, int d, boolean empty) {
		this.N = N;
		this.d = d;
		
		vertexList = new Vertex[N * 2];
		for (int i = 0; i < N * 2; i++) {
			vertexList[i] = new Vertex(i);
		}
		if (!empty) {
			generateGraph(N, d, vertexList);
		}
	}
	
	public void addEdge(int v1, int v2) {
		int left = (v1 < N) ? v1 : v2; 
		int right = (v1 < N) ? v2 : v1; 
		
		vertexList[left].addAdj(vertexList[right]);
		vertexList[right].addAdj(vertexList[left]);
	}
	
	public void addEdge(Vertex v1, Vertex v2) {
		addEdge(v1.getId(), v2.getId());
	}
	
	public void specialDfs(int start) {
		Vertex[] prevs = new Vertex[N * 2];
		Bipartite discard = new Bipartite(N, d, true /* empty */);
		Bipartite matching = new Bipartite(N, d, true /* empty */);
		Vertex prev = null; 
		ArrayList<Vertex> tree = new ArrayList<Vertex>(); 
		Set<Vertex> visited = new HashSet<Vertex>(); 
		Set<Vertex> popped = new HashSet<Vertex>();
		
		Stack<Vertex> stack = new Stack<Vertex>(); 
		stack.add(vertexList[start]);
		
		while (!stack.isEmpty()) {
			Vertex currVertex = stack.peek(); 
			System.out.println("currVertex: " + currVertex.getId());
			
			if (currVertex.getId() == start && !tree.isEmpty() && !currVertex.isTraversed(tree.get(tree.size() - 1))) {
				System.out.println("\nFound a cycle!");
				System.out.println("cycle: " + tree);
				
				// mark this edge as traversed
				currVertex.traverse(tree.get(tree.size() - 1));
				tree.get(tree.size() - 1).traverse(currVertex);
				
				// found a cycle!
				// put every other edge in a matching
				for (int i = 0; i < tree.size() - 1; i++) {
					if (i % 2 == 0) {
						matching.addEdge(tree.get(i), tree.get(i + 1));
					}
				}
				
				// clear the tree
				tree = new ArrayList<Vertex>(); 
				
				// start dfs from the next unvisited vertex (reset start!)
				stack = new Stack<Vertex>();
				for (Vertex v: vertexList) {
					if (!visited.contains(v)) {
						stack.add(v);
						start = v.getId(); 
						System.out.println("Starting next dfs from " + start);
						break;
					}
				}
			} else {
			
				if (!popped.contains(currVertex)) {
					visited.add(currVertex);
					
					if (!tree.isEmpty()) {
						//System.out.println("traversing (" + currVertex.getId() + ", " + (tree.size() - 1) + ")");
						currVertex.traverse(tree.get(tree.size() - 1));
						tree.get(tree.size() - 1).traverse(currVertex);
					}
					if (!tree.contains(currVertex)) {
						tree.add(currVertex);
					}
				
					boolean deadEnd = true;
					for (Vertex v : currVertex.getAdjList()) {
						if (!visited.contains(v) || (!currVertex.isTraversed(v) && v.getId() == start)) {
							deadEnd = false;
							stack.add(v);
						}
					}
					
					if (deadEnd) {
						// Dead end! Put the most recent edge on the discard graph
						// and remove it from the dfs tree
						System.out.println("deadend at " + currVertex.getId());
						
						if (tree.size() == 1) {
							// clear the tree
							tree = new ArrayList<Vertex>(); 
							
							// start dfs from the next unvisited vertex (reset start!)
							stack = new Stack<Vertex>();
							for (Vertex v: vertexList) {
								if (!visited.contains(v)) {
									stack.add(v);
									start = v.getId(); 
									System.out.println("Starting next dfs from " + start);
									break;
								}
							}
						} else {
							discard.addEdge(tree.get(tree.size() - 1), tree.get(tree.size() - 2));
							Vertex v = tree.remove(tree.size() - 1);
							System.out.println("removed: " + v.getId());
							v = stack.pop(); 
							popped.add(v); 
							System.out.println("popped: " + v.getId());
						}
					}
				} else {
					stack.pop(); 
				}
			}
		}
		
		System.out.println("DONE! \n");
		System.out.println("discard:");
		System.out.println(discard);
		System.out.println("matching:");
		System.out.println(matching);
	}
	
	private void generateGraph(int N, int d, Vertex[] vertexList) {
		int[] rightSide = new int[N * d];
		int k = 0; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < d; j++) {
				rightSide[k] = N + i;
				k++; 
			}
		}
		
		shuffleArray(rightSide);
		
		k = 0; 
		for (int left = 0; left < N; left++) {
			for (int adj = 0; adj < d; adj++) {
				Vertex leftV = vertexList[left];
				Vertex rightV = vertexList[rightSide[k]];
				leftV.addAdj(rightV);
				rightV.addAdj(leftV);
				k++;
			}
		}
	}
	
	private void shuffleArray(int[] ar) {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer(); 
		for (int i = 0; i < N * 2; i++) {
			buf.append(vertexList[i].toString());
		}
		
		return buf.toString(); 
	}
	
	public static void main(String[] args) {
		Bipartite graph = new Bipartite(10, 3);
		System.out.println(graph);
		graph.specialDfs(0);
	}
}