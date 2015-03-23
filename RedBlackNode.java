package RedBlackTree;

public class RedBlackNode {
	private Element element;
	private RedBlackNode left;
	private RedBlackNode right;
	private RedBlackNode parent;
	private int sideOfChild = 0; // 1 is left, 2 is right.
	private int color;
	
	public final static int REDCOLOR = 0;
	public final static int BLACKCOLOR = 1;

	
	public RedBlackNode(Element e, RedBlackNode l, RedBlackNode r) {
		element = e;
		left = l;
		right = r;
		parent = null;
	}

	public Element getElement() {
		return element;
	}

	public int getColor() {  return color; }
	public void setColor(int c) { color = c; }


	public boolean isRed(){ return (color == REDCOLOR); }
	public boolean isBlack(){ return (color == BLACKCOLOR); }
	
	public void setRedColor(){ color = REDCOLOR; }
	public void setBlackColor(){ color = BLACKCOLOR; }
	
	//  get relatives
	public RedBlackNode getLeftChild() { return left; }
	public RedBlackNode getRightChild() { return right; }
	public RedBlackNode getParent() { return parent; }
	public RedBlackNode getSibling(){
		if (parent == null)
			return null;
		
		if (sideOfChild == 1)
			return parent.getRightChild();
		else if (sideOfChild == 2)
			return parent.getLeftChild();
		else 
			return null;
	}

	// set relatives
	public void setLeftChild(RedBlackNode node) {
		left = node;
		
		if (node != null){
			node.parent = this;
			node.setAsLeftChild();
		}
	}
	
	public void setRightChild(RedBlackNode node) {
		right = node;
		if (node != null){
			node.parent = this;
			node.setAsRightChild();
		}
	}
	
	
	public void setParent(RedBlackNode node) {
		parent = node;
	}
	

	public void cloneElementAndColor(RedBlackNode src){
		element.setKey(src.getElement().getKey());
		element.setValue(src.getElement().getValue());
		color = src.getColor();
	}
	
	public void cloneElement(RedBlackNode src){
		element.setKey(src.getElement().getKey());
		element.setValue(src.getElement().getValue());
	}
	
	// child deciding
	public void setAsLeftChild() { sideOfChild = 1; }
	public void setAsRightChild() { sideOfChild = 2; }

	public boolean isLeftChild() { return (sideOfChild == 1); }
	public boolean isRightChild() { return (sideOfChild == 2); }

	public boolean isLeaf() { return ((left == null)&&(right == null) ); }
	
	// exist checking
	public boolean hasLeftChild() { return (left != null);	}
	public boolean hasRightChild() { return (right != null); }
	public boolean hasSibling(){ return (getSibling() !=null);	}
	
	public int numOfChildren(){
		return (((left == null)?0:1) + ((right == null)?0:1));
	}
}
