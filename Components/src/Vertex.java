import java.util.ArrayList;

public class Vertex implements Comparable<Vertex> {
	private int id; 
	private ArrayList<Vertex> adjList; 
	
	public Vertex(int id) {
		this.id = id; 
		adjList = new ArrayList<Vertex>(); 
	}
	
	public void addAdj(Vertex v) {
		adjList.add(v);
	}
	
	public void removeAdj(Vertex v) {
		adjList.remove(v);
	}
	
	public ArrayList<Vertex> getAdjList() {
		return adjList; 
	}
	
	public int getId() {
		return id; 
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer(); 
		buf.append(id + ": ");
		for (Vertex v : adjList) {
			buf.append(v.getId() + ", ");
		}
		buf.append("\n");
		
		return buf.toString();
	}
	
	public int compareTo(Vertex v) {
		if (this.getId() < v.getId()) {
			return -1; 
		} else if (this.getId() > v.getId()) {
			return 1; 
		} else {
			return 0;
		}
	}
}