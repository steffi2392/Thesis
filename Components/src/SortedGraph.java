import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SortedGraph {
	private Map<Integer, Chain> chainMap;
	private List<Chain> lTerm; 
	private List<Chain> rTerm; 
	private List<Chain> even; 
	private Vertex[] vertexList;
	private int N;
	
	public SortedGraph(List<Chain> chains, Vertex[] vertexList, int N) {
		this.vertexList = vertexList;
		this.N = N; 
		lTerm = new ArrayList<Chain>();
		rTerm = new ArrayList<Chain>();
		even = new ArrayList<Chain>(); 
		
		chainMap = new HashMap<Integer, Chain>(); 
		for (Chain chain : chains) {
			if (chain.length() % 2 == 0) {
				even.add(chain);
				if (chain.get(0) >= N) {
					chain.reverse();
				}
			} else {
				if (chain.get(0) < N) {
					lTerm.add(chain);
				} else {
					rTerm.add(chain);
				}
			}
			chainMap.put(chain.getId(), chain);
		}
	}
	
	public List<Chain> getLTerm() {
		return lTerm;
	}
	
	public List<Chain> getRTerm() {
		return rTerm;
	}
	
	public List<Chain> getEven() {
		return even;
	}
	
	public Set<Integer> getLTermSet() {
		Set<Integer> lTermSet = new HashSet<Integer>(); 
		for (Chain chain : lTerm) {
			lTermSet.addAll(chain.getVertexList());
		}
		
		return lTermSet; 
	}
	
	public Set<Integer> getRTermSet() {
		Set<Integer> rTermSet = new HashSet<Integer>(); 
		for (Chain chain : rTerm) {
			rTermSet.addAll(chain.getVertexList()); 
		}
		
		return rTermSet; 
	}
	
	public Set<Integer> getEvenSet() {
		Set<Integer> evenSet = new HashSet<Integer>(); 
		for (Chain chain : even) {
			evenSet.addAll(chain.getVertexList()); 
		}
		
		return evenSet;
	}
	
	/**
	 * The connecting algorithm!
	 * While lTerm is not empty:
	 *    1. Look for / take odd connection
	 *    2. If none, look for shared even and process accordingly (crossed or bridge)
	 *    3. For now, just print in this case! Not sure how to handle this yet.
	 * @throws Exception 
	 */
	public void connect() throws Exception {
		int i = 0; 
		boolean elongated = false;
		while (!lTerm.isEmpty()) {
			if (!processOddConnection()) {
				Map<Integer, List<List<Connection>>> connectionMap = getEvenConnectionLists();
				if (!processSharedEven(connectionMap)) {
					//System.out.println("No odd connection or shared even!");
					//System.out.println(); 
					//printGraph(); 
					//System.out.println(); 
					//throw new Exception("No odd connection or shared even!");
					//return;
					elongateLeftChain(connectionMap);
					System.out.println("elongated left chain: " + i); 
					i++; 
					if (i > 1)
						elongated = true;
				}
			}
			System.out.println(); 
			printGraph(); 
			System.out.println(); 
		}
		
		if (elongated) {
			throw new Exception("elongated " + i + " times");
		}
	}
	
	// lolz this function.
	public Connection getOddConnection() {
		Set<Integer> rTermSet = getRTermSet(); 
		for (Chain lChain : lTerm) {
			for (int i = 0; i < lChain.getVertexList().size(); i++) {
				Vertex leftV = vertexList[lChain.get(i)];
				for (Vertex rightV : leftV.getAdjList()) {
					if (rTermSet.contains(rightV.getId())) {
						// Find the chain and index of this connection!
						for (Chain rChain : rTerm) {
							for (int j = 0; j < rChain.getVertexList().size(); j++) {
								Integer v = rChain.get(j); 
								if (v.equals(rightV.getId())) {
									return new Connection(leftV, rightV, i, j, lChain, rChain);
								}
							}
						}
					}
				}
			}
		}
		// No connection between lTerms and rTerms
		return null;
	}
	
	private Chain findRChain(Vertex rightV) {
		for (Chain rChain : rTerm) {
			for (Integer v : rChain.getVertexList()) {
				if (v.equals(rightV.getId())) {
					return rChain;
				}
			}
		}
		return null;
	}
	
	/**
	 * Processes a connection between 2 odd chains if one exists; returns false otherwise. 
	 * After this, all vertices from both odd chains are in Even.
	 */
	public boolean processOddConnection() {
		Connection c = getOddConnection(); 
		if (c == null) {
			System.out.println("No odd connection found.");
			return false; 
		}
		
		Chain lChain = c.getLChain(); 
		Chain rChain = c.getRChain(); 
		
		System.out.println(); 
		System.out.println("Processing odd connection.");
		System.out.println(c); 
		System.out.println(); 
		
		// remove the chains from the lTerm and rTerm lists
		lTerm.remove(lChain);
		rTerm.remove(rChain);
		
		// Break the chains
		Chain brokenOffL = lChain.breakAfter(c.getLIndex());
		Chain brokenOffR = rChain.breakBefore(c.getRIndex());
		
		// add the broken off bits to the even list
		if (brokenOffL.length() > 0) {
			even.add(brokenOffL);
		}
		if (brokenOffR.length() > 0) {
			even.add(brokenOffR); 
		}
		
		
		// connect the chains and add to even
		lChain.connect(rChain);
		even.add(lChain);
		
		return true;
	}
	
	/**
	 * This finds an occurrence of a crossed connection and returns a list of the two connectiosn: 
	 * [leftConnection, rightConnection]
	 * @return
	 * @throws Exception 
	 */
	public boolean processSharedEven(Map<Integer, List<List<Connection>>> connectionMap) throws Exception {
		Connection bridge = null;
		Connection bridgeLeft = null; 
		Connection bridgeRight = null;
		
		for (Integer chainId : connectionMap.keySet()) {
			List<List<Connection>> connectionLists = connectionMap.get(chainId);
			List<Connection> leftConnections = connectionLists.get(0);
			List<Connection> rightConnections = connectionLists.get(1);
			
			if (!leftConnections.isEmpty() && !rightConnections.isEmpty()) {
				Connection lastLeft = leftConnections.get(leftConnections.size() - 1);
				Connection firstRight = rightConnections.get(0); 
				if (lastLeft.getRIndex() > firstRight.getLIndex()) {
					// Crossed connection!
					processCrossedConnection(lastLeft, firstRight);
					return true; 
				} else if (bridge == null) {
					// there exists an uncrossed connection -- see if it has a bridge. 
					bridge = findBridgeConnection(lastLeft, firstRight);
					bridgeLeft = lastLeft;
					bridgeRight = firstRight;
				}
			} 
		}
		
		if (bridge != null) {
			processBridge(bridgeLeft, bridgeRight, bridge); 
			return true;
		}
		
		// No shared even
		System.out.println("No shared even found");
		return false;
	}
	
	private Connection findBridgeConnection(Connection leftToEven, Connection rightToEven) {
		Chain evenChain = leftToEven.getRChain(); 
		int lowerIndex = leftToEven.getRIndex(); 
		int upperIndex = rightToEven.getLIndex();
		
		Map<Integer, Integer> upperVertices = new HashMap<Integer, Integer>(); 
		for (int i = upperIndex + 1; i < evenChain.length(); i++) {
			upperVertices.put(evenChain.get(i), i); 
		}
		
		for (int i = 0; i < lowerIndex; i++) {
			Vertex lower = vertexList[evenChain.get(i)];
			for (Vertex v : lower.getAdjList()) {
				if (upperVertices.containsKey(v.getId())) {
					// found a bridge! 
					Vertex upper = vertexList[evenChain.get(upperVertices.get(v.getId()))];
					return new Connection(lower, upper, i, upperVertices.get(v.getId()), evenChain, evenChain);
				}
			}
		}
		
		return null;
	}
	
	private void processCrossedConnection(Connection leftToEven, Connection rightToEven) {
		Chain leftChain = leftToEven.getLChain(); 
		Chain rightChain = rightToEven.getRChain(); 
		Chain evenChain = leftToEven.getRChain(); 
		
		System.out.println(); 
		System.out.println("Processing crossed connection:");
		System.out.println("leftToEven\n" + leftToEven);
		System.out.println("rightToEven\n" + rightToEven);
		System.out.println(); 
		
		// Remove leftChain and rightChain from the odd chain list and 
		// evenChain from the even list
		lTerm.remove(leftChain);
		rTerm.remove(rightChain);
		even.remove(evenChain); 
		
		// Break the chains and add them all to the even list
		Chain brokenOffL = leftChain.breakAfter(leftToEven.getLIndex());
		Chain brokenOffR = rightChain.breakBefore(rightToEven.getRIndex());
		Chain brokenOffE1 = evenChain.breakBefore(rightToEven.getLIndex());
		Chain brokenOffE2 = evenChain.breakAfter(leftToEven.getRIndex() - rightToEven.getLIndex());
		
		if (brokenOffL.length() > 0) 
			even.add(brokenOffL);
		if (brokenOffR.length() > 0)
			even.add(brokenOffR);
		if (brokenOffE1.length() > 0)
			even.add(brokenOffE1);
		if (brokenOffE2.length() > 0)
			even.add(brokenOffE2);
		
		// connect the chains. Everything will end up in 
		evenChain.reverse(); 
		leftChain.connect(evenChain);
		leftChain.connect(rightChain); 
		
		// add this new, conglomerate chain to even
		even.add(leftChain);
	}
	
	private void processBridge(Connection leftToEven, Connection rightToEven, Connection bridge) {
		Chain leftChain = leftToEven.getLChain(); 
		Chain rightChain = rightToEven.getRChain(); 
		Chain evenChain = bridge.getLChain(); 
		
		// Remove leftChain and rightChain from the odd chain list and
		// evenChain from the even list
		lTerm.remove(leftChain);
		rTerm.remove(rightChain); 
		even.remove(evenChain); 
		
		System.out.println(); 
		System.out.println("Processing bridge connection:");
		System.out.println("leftToEven\n" + leftToEven);
		System.out.println("rightToEven\n" + rightToEven);
		System.out.println("bridge\n" + bridge);
		System.out.println();
		
		// Break the chains and add all pieces to even list
		Chain brokenOffL = leftChain.breakAfter(leftToEven.getLIndex());
		Chain brokenOffR = rightChain.breakBefore(rightToEven.getRIndex());
		Chain brokenOffE2 = evenChain.breakAfter(bridge.getRIndex());
		Chain brokenOffE1 = evenChain.breakBefore(bridge.getLIndex());
		
		if (brokenOffL.length() > 0)
			even.add(brokenOffL);
		if (brokenOffR.length() > 0)
			even.add(brokenOffR);
		if (brokenOffE1.length() > 0)
			even.add(brokenOffE1);
		if (brokenOffE2.length() > 0)
			even.add(brokenOffE2); 
		
		int numBrokenOffFront = bridge.getLIndex(); 
		// Break the middle out of the remaining chain -- the middle is in "evenChain"
		Chain connectToRight = evenChain.breakAfter(rightToEven.getLIndex() - numBrokenOffFront - 1);
		Chain connectToLeft = evenChain.breakBefore(leftToEven.getRIndex() - numBrokenOffFront + 1);
		even.add(evenChain);
		
		// Connect the chain!
		connectToLeft.reverse();
		leftChain.connect(connectToLeft);
		connectToRight.reverse(); 
		leftChain.connect(connectToRight); 
		leftChain.connect(rightChain);
		
		// add this new, conglomerate chain to the even list
		even.add(leftChain);
	}
	
	// Looking for the connection to the highest index in an even chain with a left chain.
	private void elongateLeftChain(Map<Integer, List<List<Connection>>> connectionMap) {
		Connection highestConnection = null;
		int highestIndex = -1;
		
		for (List<List<Connection>> connectionList : connectionMap.values()) {
			List<Connection> leftConnections = connectionList.get(0);
			if (!leftConnections.isEmpty()) {
				int currIndex = leftConnections.get(leftConnections.size() - 1).getRIndex();
				if (highestConnection == null || currIndex > highestIndex) {
					highestConnection = leftConnections.get(leftConnections.size() - 1);
					highestIndex = currIndex; 
				}
			}
		}
		
		Chain evenChain = highestConnection.getRChain(); 
		Chain connectToLeft = evenChain.breakBefore(highestConnection.getRIndex() + 1);
		connectToLeft.reverse(); 
		Chain leftChain = highestConnection.getLChain(); 
		leftChain.connect(connectToLeft);
	}
	
	/**
	 * Connection map has the form: 
	 * (evenChainId, [[connections between even chain and lTerm], [connections between even chain and rTerm]])
	 * @return
	 */
	public Map<Integer, List<List<Connection>>> getEvenConnectionLists() {
		Set<Integer> lTermSet = getLTermSet(); 
		Set<Integer> rTermSet = getRTermSet(); 
		
		Map<Integer, List<List<Connection>>> connectionMap = new HashMap<Integer, List<List<Connection>>>(); 
		for (int j = 0; j < even.size(); j++) {
			List<Connection> LConnections = new ArrayList<Connection>(); 
			List<Connection> RConnections = new ArrayList<Connection>(); 
			
			Chain evenChain = even.get(j);
			// Orient the chain! Left vertex first. 
			if (!(evenChain.length() == 0) && evenChain.get(0) >= N) {
				evenChain.reverse(); 
			}
		
			// looping through even indices = left vertices, so we're 
			// looking for connections to rTerm chains
			for (int i = 0; i < evenChain.length(); i += 2) {
				Vertex evenV = vertexList[evenChain.get(i)];
				for (Vertex adjV : evenV.getAdjList()) {
					if (rTermSet.contains(adjV.getId())) {
						// need to find adjV in its rChain!
						for (Chain rChain : rTerm) {
							for (int k = 0; k < rChain.length(); k++) {
								if (rChain.get(k).equals(adjV.getId())) {
									RConnections.add(new Connection(evenV, adjV, i, k, evenChain, rChain));
								}
							}
						}
					}
				}
			}
			
			// looping through odd indices = right vertices, so we're 
			// looking for connections to lTerm chains
			for (int i = 1; i < evenChain.length(); i += 2) {
				Vertex evenV = vertexList[evenChain.get(i)];
				for (Vertex adjV : evenV.getAdjList()) {
					if (lTermSet.contains(adjV.getId())) {
						// need to find adjV in its lChain!
						for (Chain lChain : lTerm) {
							for (int k = 0; k < lChain.length(); k++) {
								if (lChain.get(k).equals(adjV.getId())) {
									LConnections.add(new Connection(adjV, evenV, k, i, lChain, evenChain));
								}
							}
						}
					}
				}
			}
			
			List<List<Connection>> connectionLists = new ArrayList<List<Connection>>(); 
			connectionLists.add(LConnections);
			connectionLists.add(RConnections); 
			connectionMap.put(even.get(j).getId() , connectionLists); 
		}
		
		return connectionMap; 
	}
	
	public boolean hasOddChains() {
		return !lTerm.isEmpty(); 
	}
	
	public static boolean hasCrossingConnections(Map<Integer, List<List<Integer>>> connectionMap) {
		for (List<List<Integer>> connections : connectionMap.values()) {
			List<Integer> LConnections = connections.get(0);
			List<Integer> RConnections = connections.get(1); 
			
			if (LConnections.get(LConnections.size() - 1) > RConnections.get(0)) 
				return true;
		}
		return false;
	}
	
	public void printGraph() {
		System.out.println("L-term Chains");
		System.out.println(lTerm);
		System.out.println("\nR-term Chains");
		System.out.println(rTerm);
		System.out.println("\nEven Chains");
		System.out.println(even);
	}
}