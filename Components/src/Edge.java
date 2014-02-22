public class Edge {
	Vertex left; 
	Vertex right; 
	boolean isDummy; 
	Direction direction; 
	
	public Edge(Vertex left, Vertex right) {
		this.left = left; 
		this.right = right;
		isDummy = false; 
		direction = null;
	}
	
	public Edge(Vertex left, Vertex right, boolean isDummy, Direction direction) {
		this.left = left; 
		this.right = right; 
		this.isDummy = isDummy; 
		this.direction = direction; 
	}
	
	public Vertex getLeft() {
		return left; 
	}
	
	public Vertex getRight() {
		return right; 
	}
	
	public boolean isDummy() {
		return isDummy; 
	}
	
	public Direction getDirection() {
		return direction; 
	}
	
	public void flipDirection() {
		direction = (direction == Direction.L_TO_R) ? Direction.R_TO_L : Direction.L_TO_R; 
	}
	
	public String toString() {
		String dummy = isDummy ? "D" : ""; 
		return String.format("%s(%d, %d)", dummy, left.getId(), right.getId());
	}
	
	public static enum Direction {
		L_TO_R, R_TO_L;
		
		public Direction flipDirection() {
			if (this == L_TO_R) {
				return R_TO_L;
			} else {
				return L_TO_R;
			} 
		}
		 
	}
}