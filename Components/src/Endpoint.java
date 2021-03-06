import java.util.ArrayList;
import java.util.List;

public class Endpoint {
	private static int numEndpoints = 0; 
	
	private Integer id; 
	private Integer value; 
	private Endpoint otherEnd;
	private List<Endpoint> adjList; 
	private Chain chain; 
	private boolean start; 
	
	public Endpoint(Chain chain, Integer value, boolean start) {
		this.id = numEndpoints;
		numEndpoints++; 
		this.value = value;
		this.chain = chain; 
		otherEnd = null; 
		adjList = new ArrayList<Endpoint>(); 
		this.start = start; 
	}
	
	public Endpoint(Chain chain, Integer value, Endpoint otherEnd, boolean start) {
		this.id = numEndpoints;
		numEndpoints++; 
		this.value = value;
		this.chain = chain; 
		this.otherEnd = otherEnd;
		adjList = new ArrayList<Endpoint>(); 
		this.start = start; 
	}
	
	public int getId() {
		return id; 
	}
	
	public int getChainId() {
		return chain.getId(); 
	}
	
	public Chain getChain() {
		return chain;
	}
	
	public void setChain(Chain chain) {
		this.chain = chain;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public boolean isSingle() {
		return !(otherEnd == null);
	}
	
	public boolean hasOtherEnd() {
		return (otherEnd != null);
	}
	
	public Endpoint getOtherEnd() {
		return otherEnd; 
	}
	
	public void setOtherEnd(Endpoint e) {
		otherEnd = e; 
	}
	
	public List<Endpoint> getAdjList() {
		return adjList; 
	}
	
	public void addAdj(Endpoint e) {
		adjList.add(e);
	}
	
	public boolean isIsolatedOdd() {
		return ((chain.length() % 2 != 0) && adjList.size() == 0 
				&& (otherEnd == null || otherEnd.getAdjList().size() == 0));
	}
	
	public boolean isOdd() {
		return (chain.length() % 2 != 0);
	}
	
	public boolean isDisconnected() {
		if (!adjList.isEmpty() || (hasOtherEnd() && !otherEnd.getAdjList().isEmpty())) {
			return false;
		}
		return true;
	}
	
	public static void connect(Endpoint e1, Endpoint e2) {
		e1.addAdj(e2);
		e2.addAdj(e1);
	}
	
	public static boolean listIsOdd(List<Endpoint> list) {
		int numOdd = 0; 
		Endpoint prev = null; 
		for (Endpoint e : list) {
			if ((prev == null || prev.getChainId() != e.getChainId()) && e.isOdd()) {
				numOdd++; 
			}
			prev = e;
		}
		
		return (numOdd % 2) != 0;
	}
	
	/**
	 * Adds the vertices from supplied endpoint to this
	 */
	public void addToEnd(Endpoint e, boolean connectToStart) {
		Endpoint oldOtherEnd = hasOtherEnd() ? otherEnd : this;
		
		if (connectToStart) {
			chain.getVertexList().addAll(e.getChain().getVertexList());
			otherEnd = e.hasOtherEnd() ? e.getOtherEnd() : e;
		} else {
			for (int i = e.getChain().getVertexList().size() - 1; i >= 0; i--) {
				chain.getVertexList().add(e.getChain().getVertexList().get(i));
			}
			otherEnd = e; 
		}
		chain.getVertexSet().addAll(e.getChain().getVertexList()); 
		otherEnd.setOtherEnd(this);
		
		// update adjacency lists. 
		// Things that were connected to oldOtherEnd no longer are!
		for (Endpoint adj : oldOtherEnd.getAdjList()) {
			adj.getAdjList().remove(oldOtherEnd);
		}
	}
	
	public void addToStart(Endpoint e, boolean connectToStart) {
		if (connectToStart) {
			List<Integer> newVertexList = new ArrayList<Integer>(e.getChain().getVertexList());
			newVertexList.addAll(chain.getVertexList());
			chain.setVertexList(newVertexList);
		} else {
			List<Integer> newVertexList = new ArrayList<Integer>(); 
			for (int i = e.getChain().getVertexList().size() - 1; i >= 0; i--) {
				newVertexList.add(e.getChain().getVertexList().get(i));
			}
			newVertexList.addAll(chain.getVertexList()); 
			chain.setVertexList(newVertexList);
		}
		chain.getVertexSet().addAll(e.getChain().getVertexList());
	}
	
	/** 
	 * Breaks the chain of this endpoint at index.
	 * Returns one of the endpoints of the chain that was broken off.
	 * Properly sets this's otherEnd
	 */
	public Endpoint breakChain(int index) {
		Endpoint oldEnd = otherEnd;
		
		//Chain newChain = chain.breakChain(index);
		Chain newChain = null; 
		otherEnd = new Endpoint(this.chain, chain.getEnd(), this, false /* end */);
		setOtherEnd(otherEnd);
		
		Endpoint newStartEndpoint = new Endpoint(newChain, newChain.getStart(), oldEnd, true /* start */); 
		oldEnd.setChain(newChain);
		oldEnd.setOtherEnd(newStartEndpoint);
		
		return newStartEndpoint;
	}
	
	public static List<Integer> getFullList(List<Endpoint> endpointList) {
		List<Integer> fullList = new ArrayList<Integer>(); 
		Endpoint prev = null;
		for (Endpoint e : endpointList) {
			if ((prev == null) || !(prev.getChainId() == e.getChainId())) {
				for (Integer v : e.getChain().getVertexList()) {
					fullList.add(v);
				}
			}
			prev = e;
		}
		return fullList; 
	}
	
	public String toString() {
		String endMarker = (start) ? "" : "*";
		return (chain.getCharId() + endMarker);
	}
}