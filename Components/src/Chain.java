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
	
	public int getId() {
		return id;
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
	
	public void print() {
		
	}
	
	/**
	 * Breaks the chain after index -- returns a new chain
	 */
	public Chain breakChain(int index) {
		System.out.println("initial vertexList of chain " + getCharId() + ": " + vertexList);
		
		List<Integer> newVertexList = new ArrayList<Integer>(); 
		for (int i = index + 1; i < vertexList.size(); i++) {
			newVertexList.add(vertexList.get(i));
		}
		while (index + 1 < vertexList.size()) {
			vertexList.remove(index + 1);
		}
		System.out.println("index: " + index);
		System.out.println("newVertexList: " + newVertexList);
		System.out.println("vertexList: " + vertexList);
		return new Chain(newVertexList);
	}
}