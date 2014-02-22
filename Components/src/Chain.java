import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Chain {
	private static int numChains = 0;
	
	private int id; 
	private List<Integer> vertexList;
	
	public Chain(List<Integer> vertexList) {
		this.id = numChains;
		this.vertexList = new ArrayList<Integer>(vertexList);
		numChains++; 
	}
	
	public static void resetNumChains() {
		numChains = 0; 
	}
	
	public int getId() {
		return id;
	}
	
	public Integer get(int i) {
		return vertexList.get(i);
	}
	
	public Integer getStart() {
		return vertexList.get(0);
	}
	
	public Integer getEnd() {
		return vertexList.get(vertexList.size() - 1);
	}
	
	public List<Integer> getVertexList() {
		return vertexList;
	}
	
	public void setVertexList(List<Integer> vertexList) {
		this.vertexList = vertexList;
	}
	
	public Set<Integer> getVertexSet() {
		return new HashSet<Integer>(vertexList);
	}
	
	public boolean contains(Vertex v) {
		return vertexList.contains(v);
	}
	
	public int length() {
		return vertexList.size(); 
	}
	
	public boolean isOdd() {
		return (vertexList.size() % 2 != 0); 
	}
	
	public char getCharId() {
		return (char) (65 + id);
	}
	
	public void reverse() {
		Collections.reverse(vertexList);
	}
	
	public void print() {
		System.out.println(vertexList);
	}
	
	public String toString() {
		return vertexList.toString(); 
	}
	
	/**
	 * Breaks the chain after index -- returns a new chain with the bit that was "broken off"
	 * (All the stuff AFTER index)
	 */
	public Chain breakAfter(int index) {
		List<Integer> newVertexList = new ArrayList<Integer>(); 
		for (int i = index + 1; i < vertexList.size(); i++) {
			newVertexList.add(vertexList.get(i));
		}
		while (index + 1 < vertexList.size()) {
			vertexList.remove(index + 1);
		}
		return new Chain(newVertexList);
	}
	
	/**
	 * Breaks the chain BEFORE index -- returns a new chain with the bit that was "broken off"
	 * (All the stuff BEFORE index)
	 */
	public Chain breakBefore(int index) {
		List<Integer> newVertexList = new ArrayList<Integer>(); 
		/*for (int i = 0; i < index; i++) {
			newVertexList.add(vertexList.get(i));
		}*/

		while (newVertexList.size() < index) {
			newVertexList.add(vertexList.get(0)); 
			vertexList.remove(0);
		}
		return new Chain(newVertexList); 
	}
	
	/**
	 * Connects c to this by adding it to the end of this.
	 */
	public void connect(Chain c) {
		vertexList.addAll(c.getVertexList());
	}
	
	public static List<Chain> convertLists(List<List<Integer>> chainLists) {
		List<Chain> chains = new ArrayList<Chain>(); 
		
		for (List<Integer> chainL : chainLists) {
			chains.add(new Chain(chainL));
		}
		
		return chains;
	}
}