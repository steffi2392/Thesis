import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectedGraph {
	List<Chain> chainList;
	
	/**
	 * Creates a directed graph given a list of chains. 
	 * The default behavior is to keep all the chains in their default, "forward" 
	 * orientation, but it will flip them if necessary.
	 */
	public DirectedGraph(List<List<Integer>> chains, Bipartite graph) {
		chainList = new ArrayList<Chain>();
		for (int i = 0; i < chains.size(); i++) {
			chainList.add(new Chain(i, chains.get(i)));
		}
		
		initializeConnections(chains, graph); 
	}
	
	public void print() {
		for (Chain c : chainList) {
			c.print(); 
		}
	}
	
	/**
	 * Look at the endpoint of each chain. 
	 * curr.end -> next.start: curr.connect(next); 
	 * curr.end -> next.end: reverse curr; next.connect(curr)
	 */
	private void initializeConnections(List<List<Integer>> chains, Bipartite graph) {
		for (int i = 0; i < chainList.size(); i++) {
			Chain currChain = chainList.get(i);
			Vertex currStart = graph.getVertex(currChain.getStart());
			Vertex currEnd = graph.getVertex(currChain.getEnd());
			
			Set<Vertex> adjToStart = new HashSet<Vertex>(currStart.getAdjList());
			Set<Vertex> adjToEnd = new HashSet<Vertex>(currEnd.getAdjList());
			
			for (int j = i; j < chainList.size(); j++) {
				Chain nextChain = chainList.get(j);
				Vertex nextStart = graph.getVertex(nextChain.getStart());
				Vertex nextEnd = graph.getVertex(nextChain.getEnd());
				
				if (currChain.length() == 1 && nextChain.length() == 1 && adjToStart.contains(nextStart)) {
					// If they're both singles, connect both ways.
					nextChain.connect(currChain);
					currChain.connect(nextChain);
				} else if (adjToStart.contains(nextStart)) {
					if (nextChain.length() > 1) {
						nextChain.reverse(); 
					}
					nextChain.connect(currChain);
				} else if (adjToStart.contains(nextEnd)) {
					nextChain.connect(currChain);
				} else if (adjToEnd.contains(nextEnd)) {
					if (nextChain.length() > 1) {
						nextChain.reverse(); 
					}
					currChain.connect(nextChain);
				} else if (adjToEnd.contains(nextStart)) {
					currChain.connect(nextChain);
				}
			}
		}
	}
}