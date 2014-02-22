import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class TreeNode extends Node {
	private Node parent;
	private int height; 
	
	public TreeNode(int id) {
		super(id);
		parent = null;
		height = 0; 
	}
	
	public TreeNode(int id, Node parent) {
		super(id);
		this.parent = parent;
	}
	
	public TreeNode(Node n) {
		super(n.getId());
		parent = null;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setHeight(int height) {
		this.height = height; 
	}
	
	public int getHeight() {
		return height; 
	}
	
	public boolean isLeaf() {
		return getNeighbors().size() == 1; 
	}
	
	public ArrayList<Node> getChildren() {
		ArrayList<Node> childList = new ArrayList<Node>(); 
		ArrayList<Edge> edges = getEdges(); 
		for (Edge e : edges) {
			if (e.getLeft().equals(this)) {
				if (!e.getRight().equals(parent)) {
					childList.add((TreeNode) e.getRight());
				}
			} else {
				if (!e.getLeft().equals(parent)) {
					childList.add((TreeNode) e.getLeft());
				}
			}
		}
		return childList;
	}
	
	// makes a DFS tree based off the provided root
	public static TreeNode createDfsTree(Node root) {
		if (root == null) {
			return null;
		}
		
		Stack<Node> stack = new Stack<Node>();
		Set<Node> visited = new HashSet<Node>(); 
		Map<Node, TreeNode> newNodeMap = new HashMap<Node, TreeNode>();
		TreeNode newRoot = new TreeNode(root);
		newNodeMap.put(root, newRoot);
		
		stack.add(root);
		while (!stack.isEmpty()) {
			Node curr = stack.pop();
			
			if (!visited.contains(curr)) {
				TreeNode currTreeNode = newNodeMap.get(curr);
				
				// create an edge between the current node and its parent
				if (currTreeNode.getParent() != null) {
					TreeNode left = (currTreeNode.getId() < currTreeNode.getParent().getId()) 
							? (TreeNode) currTreeNode : (TreeNode) currTreeNode.getParent(); 
					TreeNode right = (currTreeNode.getId() < currTreeNode.getParent().getId())
							? (TreeNode) currTreeNode.getParent() : (TreeNode) currTreeNode;
					Edge edge = new Edge(left, right, false, 0);
					left.addEdge(edge);
					right.addEdge(edge);
				}
				
				for (Node child : curr.getNeighbors()) {
					if (!visited.contains(child)) {
						stack.add(child);
						if (!newNodeMap.containsKey(child)) {
							TreeNode treeNodeChild = new TreeNode(child);
							newNodeMap.put(child, treeNodeChild);
						}
						TreeNode currTreeChild = newNodeMap.get(child);
						currTreeChild.setParent(currTreeNode);
					}
				}
				visited.add(curr);
			}
		}
		return newRoot;
	}
	
	// performs bfs on the dfs tree and assigns dummies as it goes. 
	// Dummy assignment: assign wherever you can. Whenever it branches, 
	// choose a child with an even height first. 
	// This one just does the assignment stuff, go through and do the 
	// weights in a later function.
	public Set<TreeNode> assignDummies() {
		calculateHeight();
		Set<TreeNode> visited = new HashSet<TreeNode>(); 
		Set<TreeNode> unmatched = new HashSet<TreeNode>(); 
		Queue<TreeNode> queue = new LinkedList<TreeNode>(); 
		queue.add(this);
		
		while (!queue.isEmpty()) {
			TreeNode curr = queue.poll();
			if (!visited.contains(curr)) {
				ArrayList<Node> children = curr.getChildren(); 
				if (!curr.hasDummy()) {
					unmatched.add(curr);
					for (Node child : children) {
						if (((TreeNode) child).getHeight() % 2 == 0 && !child.hasDummy()) {
							assignDummy(curr, (TreeNode) child, unmatched);
							break;
						}
					}
					// if we haven't matched it yet (no child has even height), 
					// just match with the first unmatched child. 
					if (!curr.hasDummy()) {
						for (Node child : children) {
							if (!child.hasDummy()) {
								assignDummy(curr, (TreeNode) child, unmatched);
							}
						}
					}
				}
				for (Node child : children) {
					queue.add((TreeNode) child);
				}
				visited.add(curr);
			}
		}
		return unmatched; 
	}
	
	// START HERE! deep copy the node. PARENTS ARE NOT SET... does that matter?
	// YOU HAVEN'T FINISHED THIS YET.
	public TreeNode deepCopy() {
		Stack<TreeNode> stack = new Stack<TreeNode>(); 
		Set<Node> visited = new HashSet<Node>(); 
		Map<Integer, TreeNode> map = new HashMap<Integer, TreeNode>(); 
		TreeNode rootCopy = new TreeNode(this.getId(), this.getParent());
		map.put(this.getId(), rootCopy);
		stack.add(this);
		
		while (!stack.isEmpty()) {
			TreeNode curr = stack.pop(); 
			if (!visited.contains(curr)) {
				TreeNode currCopy = map.get(curr.getId());
				if (currCopy == null) {
					currCopy = new TreeNode(curr.getId(), null);
				}
				for (Edge edge : curr.getEdges()) {
					Node left = map.get(edge.getLeft().getId());
					if (left == null) {
						left = new TreeNode(edge.getLeft().getId(), null);
					}
					Node right = map.get(edge.getRight().getId());
					if (right == null) {
						right = new TreeNode(edge.getRight().getId(), null);
					}
					Edge copiedEdge = new Edge(left, right, edge.isDummy(), edge.getWeight());
					left.addEdge(copiedEdge);
					right.addEdge(copiedEdge);
					
				}
				for (Node child : curr.getChildren()) {
					if (!visited.contains(child)) {
						stack.add((TreeNode) child);
					}
				}
				visited.add(curr);
			}
		}
		return rootCopy; 
	}
	
	private void assignDummy(TreeNode n1, TreeNode n2, Set<TreeNode> unmatched) {
		Edge dummy = new Edge(n1, n2, true, 0);
		n1.addEdge(dummy);
		n2.addEdge(dummy);
		unmatched.remove(n1);
		unmatched.remove(n2);
	}
	
	private int calculateHeight() {
		if (getChildren().size() == 0) {
			return 0;
		}
		int maxHeight = 0; 
		for (Node child : getChildren()) {
			((TreeNode) child).setHeight(((TreeNode) child).calculateHeight()); 
			if (((TreeNode) child).getHeight() + 1 > maxHeight) {
				maxHeight = ((TreeNode) child).getHeight() + 1; 
			}
		}
		setHeight(maxHeight);
		return maxHeight; 
	}
}