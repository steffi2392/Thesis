import java.util.ArrayList; 
import java.util.Deque; 

public class Driver3 {
	public static void main(String[] args) {
		
		System.out.printf("%-10s %-10s %-10s %-10s\n", "N", "d", "No Go Home", "Go Home");
		
		int runNumber = 0; 
		for (int N = 100; N < 200000; N = N * 2) {
			for (int d = 3; d < 17; d += 2) {
				for (int i = 0; i < 100; i++) { 

					ArrayList<Double> cyclesNeeded = new ArrayList<Double>(); 
					ArrayList<Integer> numLookedAtList = new ArrayList<Integer>(); 
					ArrayList<Integer> numTraversedList = new ArrayList<Integer>(); 
					
					BipartiteNode[] nodeList = BipartiteNode.populateNodeList(N);
					BipartiteNode[] nodeListCopy = BipartiteNode.populateNodeList(N);
					int[] rightSide = BipartiteNode.getShuffledRightSide(N, d, nodeList);
					BipartiteNode root = BipartiteNode.generateGraph(N, d, nodeList, rightSide);
					BipartiteNode rootCopy = BipartiteNode.generateGraph(N, d, nodeListCopy, rightSide);
					
					Graph<BipartiteNode> bipartiteGraph = new Graph<BipartiteNode>(nodeList, d);
					Graph<BipartiteNode> bipartiteGraphCopy = new Graph<BipartiteNode>(nodeListCopy, d);
					bipartiteGraph.addStraightAcrossDummies();
					bipartiteGraphCopy.addStraightAcrossDummies(); 
					
					//root.prettyPrint(); 
					//System.out.println();
					//rootCopy.prettyPrint(); 
					
					//System.out.println("**No goHome**");
					numLookedAtList.add(0); 
					numTraversedList.add(0);
					ArrayList<ArrayList<Double[]>> edgesPerDummy = new ArrayList<ArrayList<Double[]>>(); 
					ArrayList<Deque<Edge>> cycles = bipartiteGraph.findAllCycles(numLookedAtList, 
						numTraversedList, edgesPerDummy, 0, N, false /* go home */ );
					/*if (cycles == null) {
						System.out.println("Didn't find cycle");
						return; 
					}*/
					ArrayList<Double> traversedRatioList = new ArrayList<Double>(); 
					ArrayList<Double> lookedAtRatioList = new ArrayList<Double>(); 
					
					int totalEdges = N * (d + 1);
					int lookedAt = numLookedAtList.get(numLookedAtList.size() - 1);
					int noGoHome = lookedAt; 
					double lookedAtRatio = ((double) lookedAt) / totalEdges; 
					lookedAtRatioList.add(lookedAtRatio);
					int traversed = numTraversedList.get(numTraversedList.size() - 1);
					double traversedRatio = ((double) traversed) / totalEdges; 
					traversedRatioList.add(traversedRatio);
			
					/*if (cycles != null) {
						System.out.println("\nN = " + N + ", d = " + d);
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
						}
					}*/
					
					//System.out.println("\n\n**With goHome**");
					cyclesNeeded = new ArrayList<Double>(); 
					numLookedAtList = new ArrayList<Integer>(); 
					numTraversedList = new ArrayList<Integer>();
					
					numLookedAtList.add(0); 
					numTraversedList.add(0);
					edgesPerDummy = new ArrayList<ArrayList<Double[]>>(); 
					cycles = bipartiteGraphCopy.findAllCycles(numLookedAtList, 
						numTraversedList, edgesPerDummy, 0, N, true /* go home */ );
					if (cycles == null) {
						System.out.println("Didn't find cycle");
						return; 
					}
					
					traversedRatioList = new ArrayList<Double>(); 
					lookedAtRatioList = new ArrayList<Double>(); 
					
					totalEdges = N * (d + 1);
					lookedAt = numLookedAtList.get(numLookedAtList.size() - 1);
					int goHome = lookedAt; 
					lookedAtRatio = ((double) lookedAt) / totalEdges; 
					lookedAtRatioList.add(lookedAtRatio);
					traversed = numTraversedList.get(numTraversedList.size() - 1);
					traversedRatio = ((double) traversed) / totalEdges; 
					traversedRatioList.add(traversedRatio);
			
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
					}
					
					System.out.println("\n\n--------------\n");*/
					System.out.printf("%-10d %-10d %-10d %-10d\n", N, d, noGoHome, goHome);
				}
			}
		}
	}
}