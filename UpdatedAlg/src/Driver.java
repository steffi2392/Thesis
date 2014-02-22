import java.util.ArrayList; 
import java.util.Deque; 

public class Driver {
	public static void main(String[] args) {
		ArrayList<Double> cyclesNeeded = new ArrayList<Double>(); 
		ArrayList<Integer> numLookedAtList = new ArrayList<Integer>(); 
		ArrayList<Integer> numTraversedList = new ArrayList<Integer>(); 
		
		System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", "N", "d", "numCycles", 
				"lookedAt", "ratio", "traversed", "ratio");
		
		int runNumber = 0; 
		for (int N = 100; N < 200000; N = N * 2) {
			for (int d = 3; d < 17; d += 2) {
				for (int i = 0; i < 100; i++) {
					BipartiteNode[] nodeList = new BipartiteNode[N * 2];
					BipartiteNode root = BipartiteNode.generateGraph(N, d, nodeList);
					
					Graph<BipartiteNode> bipartiteGraph = new Graph<BipartiteNode>(nodeList, d);
					bipartiteGraph.addStraightAcrossDummies(); 
					
					numLookedAtList.add(0); 
					numTraversedList.add(0);
					ArrayList<ArrayList<Double[]>> edgesPerDummy = new ArrayList<ArrayList<Double[]>>(); 
					ArrayList<Deque<Edge>> cycles = bipartiteGraph.findAllCycles(numLookedAtList, 
						numTraversedList, edgesPerDummy, runNumber, N, false /* goHome */);
					ArrayList<Double> traversedRatioList = new ArrayList<Double>(); 
					ArrayList<Double> lookedAtRatioList = new ArrayList<Double>(); 
					
					int totalEdges = N * (d + 1);
					int lookedAt = numLookedAtList.get(numLookedAtList.size() - 1);
					double lookedAtRatio = ((double) lookedAt) / totalEdges; 
					lookedAtRatioList.add(lookedAtRatio);
					int traversed = numTraversedList.get(numTraversedList.size() - 1);
					double traversedRatio = ((double) traversed) / totalEdges; 
					traversedRatioList.add(traversedRatio);
					
					System.out.printf("%-10d %-10d %-10d %-10d %-10f %-10d %-10f\n", N, d, cycles.size(), 
							lookedAt, lookedAtRatio, traversed, traversedRatio);
					
					runNumber++; 
				}
			}
			
		}
		
		int N = 100; 
		int d = 11; 

		
		/*System.out.println("\nN = " + N + ", d = " + d);
		System.out.println("Cycles: " + cycles.size());
		System.out.println("Total Edges: " + N * (d + 1));
		System.out.println("Looked at: " + lookedAt + "   Ratio: " + lookedAtRatio);
		System.out.println("Traversed: " + traversed + "   Ratio: " + traversedRatio);
		System.out.print("(cycleLength, dummies, cycleLength / dummies): ");
		for (Double[] array : edgesPerDummy.get(edgesPerDummy.size() - 1)) {
			System.out.print("(");
			for (Double doub : array) {
				System.out.print(doub + ", ");
			}
			System.out.print("), ");
		}*/
		
		
		/*for (Deque<Edge> cycle : cycles) {
			System.out.println("\nCycle " + cycleNum);
			for (Edge e : cycle) {
				System.out.println(e);
			}
			cycleNum++; 
		}*/
		
		/*Edge first = bipartiteGraph.getNode(0).getDummyEdge();
		System.out.println("\nFirst edge: " + first);
		Deque<Edge> cycle = new LinkedList<Edge>(); 
		System.out.println(first.findCycle(cycle, Edge.Direction.L_TO_R));
		
		System.out.println("\nPrinting the cycle:");
		for (Edge e : cycle) {
			System.out.println(e);
		}*/
		
		
		/*TreeNode treeRoot = TreeNode.createDfsTree(root);
		System.out.println("\nDFS Tree:");
		treeRoot.prettyPrint(); 
		Set<TreeNode> unmatched = treeRoot.assignDummies();
		System.out.println("\nAfter assigning dummies:");
		treeRoot.prettyPrint(); 
		System.out.print(unmatched.size() + " unmatched nodes:"); 
		for (TreeNode n : unmatched) {
			System.out.print(n.getId() + " ");
		}
		
		Graph<TreeNode> dfsTree = new Graph<TreeNode>(N * 2, treeRoot);
		Set<Edge> dummyEdges = dfsTree.getDummyEdges(); 
		
		Graph<BipartiteNode> bipartiteGraph = new Graph<BipartiteNode>(N * 2, root);
		bipartiteGraph.addEdges(dummyEdges);
		System.out.println("\nAdded Dummies to Bipartite");
		root.prettyPrint(); */
		
		
	}
}