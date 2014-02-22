import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList; 
import java.util.Set; 
import java.util.HashSet; 
import java.util.Deque;

public class Graph<T extends Node> {
	private ArrayList<BipartiteNode> nodes;
	private Set<Edge> edges; 
	private int d;
	int size; // number of total nodes in graph 
	int numEdges; // number of total edges (including dummies) in the graph
	
	/*public Graph(int size, T root) {
		this.size = size; 
		nodes = new ArrayList<T>(size);
		for (int i = 0; i < size; i++) {
			nodes.add(null);
		}
		
		// put each node in T into its spot in the arrayList 
		// (as indexed by node id)
		Queue<T> queue = new LinkedList<T>(); 
		Set<T> visited = new HashSet<T>(); 
		queue.add(root);
		
		while (!queue.isEmpty()) {
			T curr = queue.poll(); 
			if (!visited.contains(curr) && curr != null) {
				nodes.set(curr.getId(), curr);
				for (Node child : curr.getChildren()) {
					if (!visited.contains(child)) {
						queue.add((T) child);
					}
				}
			}
			visited.add(curr);
		}
		
		// If 2 nodes are only connected to each other, they wouldn't have been picked
		// up yet!
		for (T n : nodes) {
			if (n == null) {
				
			}
		}
		
		System.out.println("printing nodes");
		System.out.println("number of nodes = " + nodes.size());
		for (T n : nodes) {
			System.out.println(n);
		}
		edges = new HashSet<Edge>(); 
		for (T n : nodes) {
			for (Edge e : n.getEdges()) {
				edges.add(e);
			}
		}
	}*/
	
	public Graph(BipartiteNode[] nodeList, int d) {
		nodes = new ArrayList<BipartiteNode>(Arrays.asList(nodeList));
		edges = new HashSet<Edge>(); 
		for (BipartiteNode n : nodes) {
			for (Edge e : n.getEdges()) {
				edges.add(e);
			}
		}
		this.d = d; 
		size = nodes.size(); 
		numEdges = edges.size(); 
	}
	
	public BipartiteNode getNode(int id) {
		return nodes.get(id);
	}
	
	// Note that this adds *copies* of the edges, using the nodes in the node list
	// with IDs corresponding with the ids of the nodes in the list provided as a param.
	public void addEdges(Set<Edge> edges) {
		for (Edge e : edges) {
			Node left = getNode(e.getLeft().getId());
			Node right = getNode(e.getRight().getId()); 
			Edge newEdge = new Edge(left, right, e.isDummy(), e.getWeight());
			left.addEdge(newEdge);
			right.addEdge(newEdge);
		}
	}
	
	public Set<Edge> getDummyEdges() {
		Set<Edge> dummies = new HashSet<Edge>(); 
		for (Edge e : edges) {
			if (e.isDummy()) {
				dummies.add(e);
			}
		}
		return dummies;
	}
	
	public void addStraightAcrossDummies() {
		for (int i = 0; i < size / 2; i++) {
			Node left = nodes.get(i); 
			Node right = nodes.get(i + size / 2);
			Edge dummy = new Edge(left, right, true, 0);
			left.addEdge(dummy);
			right.addEdge(dummy); 
			edges.add(dummy);
		}
	}
	
	public ArrayList<Deque<Edge>> findAllCycles(ArrayList<Integer> numLookedAtList, 
			ArrayList<Integer> numTraversedList, ArrayList<ArrayList<Double[]>> edgesPerDummy, int index, 
			int N, boolean goHome) {
		Set<Edge> untraversedDummies = new HashSet<Edge>();
		for (Edge e : getDummyEdges()) {
			untraversedDummies.add(e);
		}
		ArrayList<Deque<Edge>> cycles = new ArrayList<Deque<Edge>>();
		ArrayList<Double[]> currEdgesPerDummy = new ArrayList<Double[]>(); 
		
		int numTraversed = 0; 
		while (!untraversedDummies.isEmpty()) {
			Edge currEdge = nextDummy(); 
			if (currEdge == null) {
				System.out.println("Error: no dummies left");
				return cycles;
			}
			
			
			Deque<Edge> cycle = new LinkedList<Edge>(); 
			int thisRun = currEdge.findCycle(cycle, Edge.Direction.L_TO_R, numLookedAtList, index, N, goHome);
			if (thisRun == -1) {
				// Did not find a cycle! Print info. 
				//nodes.get(0).prettyPrint(); 
				System.out.println();
				for (Edge e : edges) {
					if (e.getWeight() != 0) {
						System.out.println(e + ": " + e.getWeight());
					}
				}
				return null; 
			}
			
			//numTraversed += currEdge.findCycle(cycle, Edge.Direction.L_TO_R, numLookedAtList, index, N, goHome);
			numTraversed += thisRun; 
			
			Edge.Direction currDirection = Edge.Direction.L_TO_R;
			int numDummiesTraversed = 0; 
			for (Edge e : cycle) {
				if (e.isDummy()) {
					numDummiesTraversed++;
					untraversedDummies.remove(e);
					
					// UPDATED ALG: update the count of stuff
					for (Node n : e.getLeft().getNeighbors()) {
						if (n.incrementCount() == d) {
							for (Node leftNeighbor : n.getNeighbors()) {
								leftNeighbor.incrementCount(); 
							}
						}
					}
				}
				int oldWeight = e.getWeight(); 
				e.setWeight(e.getWeight() + currDirection.cost());
				e.setTempWeight(e.getWeight());
				e.getLeft().moveEdge(e, oldWeight);
				e.getRight().moveEdge(e, oldWeight);
				
				//System.out.println("Edge " + e + " assigned weight of " + e.getWeight());
				currDirection = currDirection.flipDirection();
			}
			cycles.add(cycle);
			//System.out.println("  Num Dummies: " + numDummiesTraversed);
			currEdgesPerDummy.add(new Double[] {Double.valueOf(cycle.size()), Double.valueOf(numDummiesTraversed), 
				Double.valueOf(cycle.size()) / numDummiesTraversed});
		}
		numTraversedList.add(numTraversed);
		edgesPerDummy.add(currEdgesPerDummy);
		return cycles;
	}
	
	private Edge nextDummy() {
		for (Node n : nodes) {
			if (n.hasDummy() && n.getDummyEdge().getWeight() == 0) {
				return n.getDummyEdge(); 
			}
		}
		return null;
	}
}
