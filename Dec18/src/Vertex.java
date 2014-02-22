import java.util.ArrayList; 

public class Vertex implements Comparable<Vertex> {
	private int id; 
	private ArrayList<Vertex> adjList; 
	private ArrayList<Boolean> traversedList; 
	
	public Vertex(int id) {
		this.id = id; 
		adjList = new ArrayList<Vertex>(); 
		traversedList = new ArrayList<Boolean>(); 
	}
	
	public void addAdj(Vertex v) {
		adjList.add(v);
		traversedList.add(false);
	}
	
	public ArrayList<Vertex> getAdjList() {
		return adjList; 
	}
	
	public int getId() {
		return id; 
	}
	
	public boolean isTraversed(int index) {
		return traversedList.get(index);
	}
	
	public boolean isTraversed(Vertex vert) {
		boolean traversed = true; 
		for (int i = 0; i < adjList.size(); i++) {
			if (adjList.get(i).getId() == vert.getId()) {
				if (traversedList.get(i) == false) {
					traversed = false;
				}
			}
		}
		if (!traversed)
			System.out.println("traversed (" + this.id + ", " + vert.getId() + "): " + traversed);
		return traversed;
	}
	
	public void traverse(Vertex vert) {
		for (int i = 0; i < adjList.size(); i++) {
			if (adjList.get(i).getId() == vert.getId() && traversedList.get(i) == false) {
				traversedList.set(i, true);
				break;
			}
		}
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
		/*if (!(obj instanceof Vertex)) {
			return -1; 
		}
		
		Vertex v = (Vertex) obj;*/
		
		if (this.getId() < v.getId()) {
			return -1; 
		} else if (this.getId() > v.getId()) {
			return 1; 
		} else {
			return 0;
		}
	}
}