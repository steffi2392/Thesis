public class VertexLinkedList<T> {
	private VertexLinkedListNode<T> head; 
	
	public VertexLinkedList() {
		head = null;
	}
	
	/**
	 * Inserts node into the front of the list
	 * @param node The node to insert
	 */
	public void insert(VertexLinkedListNode<T> node) {
		if (head == null) {
			head = node;
			return;
		}
		
		VertexLinkedListNode<T> temp = head;
		head = node;
		temp.setPrev(head);
		head.setNext(temp);
	}
	
	/**
	 * Removes a node from the list
	 * @param node The node to remove
	 */
	public void remove(VertexLinkedListNode<T> node) {
		if (node == head) {
			head = null;
			return;
		}
		
		node.getPrev().setNext(node.getNext());
		if (node.getNext() != null) {
			node.getNext().setPrev(node.getPrev());
		}
	}
	
	public static class VertexLinkedListNode<V> {
		private VertexLinkedListNode<V> next; 
		private VertexLinkedListNode<V> prev; 
		private V element; 
		
		public VertexLinkedListNode(V element) {
			this.element = element; 
			next = null; 
			prev = null;
		}
		
		public VertexLinkedListNode<V> getNext() {
			return next;
		}
		
		public void setNext(VertexLinkedListNode<V> next) {
			this.next = next;
		}
		
		public VertexLinkedListNode<V> getPrev() {
			return prev; 
		}
		
		public void setPrev(VertexLinkedListNode<V> prev) {
			this.prev = prev;
		}
		
		public V getElement() {
			return element;
		}
	}
}