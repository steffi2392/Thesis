import java.util.List;

/**
 * This class provides a way to store connected vertices
 * as well as the chains that they are a part of. 
 */
public class Connection {
	private Vertex lVert; 
	private Vertex rVert;
	private int lIndex;
	private int rIndex;
	private Chain lChain; 
	private Chain rChain;
	
	public Connection(int lIndex, int rIndex, Chain lChain, Chain rChain) {
		this.lVert = null;
		this.rVert = null;
		this.lIndex = lIndex;
		this.rIndex = rIndex;
		this.lChain = lChain;
		this.rChain = rChain;
	}
	
	public Connection(Vertex lVert, Vertex rVert, int lIndex, int rIndex, Chain lChain, Chain rChain) {
		this.lVert = lVert;
		this.rVert = rVert;
		this.lIndex = lIndex;
		this.rIndex = rIndex;
		this.lChain = lChain;
		this.rChain = rChain;
	}
	
	public Vertex getLVert() {
		return lVert;
	}
	
	public Vertex getRVert() {
		return rVert; 
	}
	
	public int getLIndex() {
		return lIndex;
	}
	
	public int getRIndex() {
		return rIndex; 
	}
	
	public Chain getLChain() {
		return lChain; 
	}
	
	public Chain getRChain() {
		return rChain;
	}
	
	public String toString() {
		return "index " + lIndex + " of\n" + lChain.toString() + "\nconnected to index " + rIndex
				+ " of\n" + rChain.toString();
	}
}