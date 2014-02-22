import java.util.Random; 

public class BipartiteNode extends Node {
	public BipartiteNode(int id) {
		super(id);
	}
	
	public static BipartiteNode[] populateNodeList(int N) {
		BipartiteNode[] nodeList = new BipartiteNode[N * 2];
		for (int i = 0; i < N * 2; i++) {
			nodeList[i] = new BipartiteNode(i);
		}
		return nodeList; 
	}
	
	public static int[] getShuffledRightSide(int N, int d, BipartiteNode[] nodeList) {
		int[] rightSides = new int[N * d];
		int k = 0; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < d; j++) {
				rightSides[k] = N + i;
				k++; 
			}
		}
		
		shuffleArray(rightSides);
		return rightSides; 
	}
	
	public static BipartiteNode generateGraph(int N, int d, BipartiteNode[] nodeList) {
		for (int i = 0; i < N * 2; i++) {
			nodeList[i] = new BipartiteNode(i);
		}
		
		int[] rightSides = new int[N * d];
		int k = 0; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < d; j++) {
				rightSides[k] = N + i;
				k++; 
			}
		}
		
		shuffleArray(rightSides);
		
		k = 0;
		for (int left = 0; left < N; left++) {
			for (int e = 0; e < d; e++) {
				BipartiteNode leftNode = nodeList[left];
				BipartiteNode rightNode = nodeList[rightSides[k]];
				k++; 
				Edge edge = new Edge(leftNode, rightNode, false, 0);
				leftNode.addEdge(edge);
				rightNode.addEdge(edge);
			}
		}
		
		return nodeList[0]; 
	}
	
	// This one should take a deep copy of the original nodeList if you're using it for 
	// duplication purposes!! 
	public static BipartiteNode generateGraph(int N, int d, BipartiteNode[] nodeList, int[] rightSides) {
		int k = 0;
		for (int left = 0; left < N; left++) {
			for (int e = 0; e < d; e++) {
				BipartiteNode leftNode = nodeList[left];
				BipartiteNode rightNode = nodeList[rightSides[k]];
				k++; 
				Edge edge = new Edge(leftNode, rightNode, false, 0);
				leftNode.addEdge(edge);
				rightNode.addEdge(edge);
			}
		}
		
		return nodeList[0]; 
	}
	
	private static void shuffleArray(int[] ar) {
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
}
