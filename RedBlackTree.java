package RedBlackTree;


public class RedBlackTree implements TreeInterface {
	private RedBlackNode root;

	private final static int LLROTATE = 0;
	private final static int RRROTATE = 1;
	private final static int LRROTATE = 2;
	private final static int RLROTATE = 3;

	public boolean chooseSubstituteInLeftSubtreeForDelete = true;
	
	public RedBlackTree() {
		root = null;
	}

	public String find(int key) {

		RedBlackNode node = findNode(key);
		if (node != null)
			return node.getElement().getValue();
		else
			return null;

	}

	private RedBlackNode findNode(int key) {
		if (root == null)
			return null;

		RedBlackNode current = root;
		int _key;

		while (current != null) {
			_key = current.getElement().getKey();
			if (key > _key) {
				current = current.getRightChild();
			} else if (key < _key) {
				current = current.getLeftChild();
			} else {
				// found
				break;
			}
		}

		return current;
	}

	public RedBlackNode findMin(RedBlackNode start) {
		RedBlackNode left;

		if (start == null)
			return null;

		while ((left = start.getLeftChild()) != null) {
			start = left;
		}

		return start;
	}

	public RedBlackNode findMax(RedBlackNode start) {
		RedBlackNode right;

		if (start == null)
			return null;

		while ((right = start.getRightChild()) != null) {
			start = right;
		}

		return start;

	}

	public void insert(int key, String value) {

		if (root == null) {
			root = new RedBlackNode(new Element(key, value), null, null);
			root.setBlackColor();
			return;
		}

		// find the position of insertion
		RedBlackNode next = root;
		RedBlackNode current = root;
		int _key;

		while (next != null) {
			current = next;
			_key = current.getElement().getKey();

			if (key > _key) {
				next = current.getRightChild();
			} else if (key < _key) {
				next = current.getLeftChild();
			} else {
				// found, overwritten, and do nothing.
				current.getElement().setValue(value);
				return;
			}
		}

		// do real insertion.
		RedBlackNode newNode = new RedBlackNode(new Element(key, value), null,
				null);
		newNode.setRedColor();
		if (key > current.getElement().getKey()) {
			current.setRightChild(newNode);
		} else {
			current.setLeftChild(newNode);
		}

		// re-color
		recolorForInsertion(newNode);
	}

	/***
	 * do the re-color job after insertion.
	 * 
	 * @param newNode
	 */
	private void recolorForInsertion(RedBlackNode newNode) {
		boolean needBottomUpCheck = false;

		if (newNode == root) { // root.
			return;
		}

		RedBlackNode parent = newNode.getParent();
		RedBlackNode grandParent = parent.getParent();
		RedBlackNode uncle = null;
		if (parent != null)
			uncle = parent.getSibling();

		if (parent.isBlack()) { // include parent is root.
			// do nothing
			return;

		} else {

			if (uncle == null || uncle.isBlack()) {

				int rotateType = findRotationType(newNode);

				if (rotateType == LLROTATE || rotateType == RRROTATE) {
					parent.setBlackColor();
					grandParent.setRedColor();

				} else if (rotateType == LRROTATE || rotateType == RLROTATE) {
					grandParent.setRedColor();
					parent.setRedColor();
					newNode.setBlackColor();
				}

				rotateWithType(rotateType, newNode);
			} else if (uncle.isRed()) {
				grandParent.setRedColor();
				parent.setBlackColor();
				uncle.setBlackColor();

				if (grandParent == root) {
					grandParent.setBlackColor();
					needBottomUpCheck = false;
				} else
					needBottomUpCheck = true;

			}

		}

		if (needBottomUpCheck)
			recolorForInsertion(grandParent);

	}

	private void rotateWithType(int rotateType, RedBlackNode child) {
		RedBlackNode parent = child.getParent();
		RedBlackNode grandParent = parent.getParent();

		if (rotateType == LLROTATE) {
			rotateWithLeftChild(grandParent);
		} else if (rotateType == RRROTATE) {
			rotateWithRightChild(grandParent);
		} else if (rotateType == LRROTATE) {
			rotateWithRightChild(parent);
			rotateWithLeftChild(grandParent);
		} else if (rotateType == RLROTATE) {
			rotateWithLeftChild(parent);
			rotateWithRightChild(grandParent);
		}

	}

	private void rotateWithLeftChild(RedBlackNode node) {
		RedBlackNode left = node.getLeftChild();
		RedBlackNode parent = node.getParent();
		if (parent != null) {
			if (node.isLeftChild())
				parent.setLeftChild(left);
			else
				parent.setRightChild(left);
		} else {// node is root.
			root = left;
			root.setParent(null);
		}

		node.setLeftChild(left.getRightChild());
		left.setRightChild(node);
	}

	private void rotateWithRightChild(RedBlackNode node) {
		RedBlackNode right = node.getRightChild();
		RedBlackNode parent = node.getParent();
		if (parent != null) {
			if (node.isLeftChild())
				parent.setLeftChild(right);
			else
				parent.setRightChild(right);
		} else { // node is root.
			root = right;
			root.setParent(null);
		}

		node.setRightChild(right.getLeftChild());
		right.setLeftChild(node);

	}

	private int findRotationType(RedBlackNode child) {

		if (child.isLeftChild() && child.getParent().isLeftChild()) {
			return LLROTATE;
		} else if (child.isLeftChild() && child.getParent().isRightChild()) {
			return RLROTATE;
		} else if (child.isRightChild() && child.getParent().isLeftChild()) {
			return LRROTATE;
		} else if (child.isRightChild() && child.getParent().isRightChild()) {
			return RRROTATE;
		}

		return -1;
	}

	/***
	 * delete a node. 
	 */
	public void delete(int key) {
		remove(key);
	}
	
	
	/***
	 * delete a node and return its value. first delete as a binary search tree, then, check the
	 * rb-tree properties.
	 */
	public String remove(int key) {

		// find the node x containing key x.
		RedBlackNode nodex = findNode(key);
		if (nodex == null) {
			System.out.println("can't find the node!");
			return null;
		}
		String oldNodeVaule = nodex.getElement().getValue();
		
		// properties of node that is actually deleted.
		RedBlackNode child = null;
		RedBlackNode parent = null;
		boolean colorOfDeletedNodeIsBlack = false;
		
		int numOfChildren = nodex.numOfChildren();
		if (numOfChildren == 1) {
			// simple case, when node x has one child.
			// (must be a red child under black parent, do you know?)

			colorOfDeletedNodeIsBlack = nodex.isBlack();
			
			if ((child = nodex.getLeftChild()) != null){
				nodex.setLeftChild(null);
			}else{
				child = nodex.getRightChild();
				nodex.setRightChild(null);
			}
			nodex.cloneElementAndColor(child);
			child = nodex;
			parent = nodex.getParent();

		} else if (numOfChildren == 0) {
			
			if (nodex != root){
				colorOfDeletedNodeIsBlack = nodex.isBlack();
				child = null;
				parent = nodex.getParent();
				if (nodex.isLeftChild())
					parent.setLeftChild(null);
				else
					parent.setRightChild(null);
			
			}else
				root = null;
			
		} else { // 2

			// find the node that will replace node x:
			// ( find the min of the right subtree.)
			
			RedBlackNode rntd; // real node to be delete.
			if (chooseSubstituteInLeftSubtreeForDelete)
				rntd = findMax(nodex.getLeftChild());
			else
				rntd = findMin(nodex.getRightChild());
			
			// replace node x with rntd for the key and value
			if (rntd != null) {
				parent = rntd.getParent();
				
				if (chooseSubstituteInLeftSubtreeForDelete)
					child = rntd.getLeftChild();
				else
					child = rntd.getRightChild(); // do you know why?
				
				nodex.cloneElement(rntd);
			} else {
				// something is wrong here, because rntd is not null
				// since node x has 2 children.
				System.out.println("something is wrong here!");
			}

			// delete rntd.
			if (rntd.isLeftChild())
				parent.setLeftChild(child);
			else
				parent.setRightChild(child);
			
			colorOfDeletedNodeIsBlack = rntd.isBlack();

		}

		// only do makeup when the node actually deleted is black.
		if (colorOfDeletedNodeIsBlack)
			recolorForDelete(child, parent);
		
		return oldNodeVaule;
	}
	

	/**
	 * re-color for deletion of node rntd, whose relatives are following:
	 * 
	 * @param child
	 *            : of node rntd 's
	 * @param parent
	 *            : of node rntd 's
	 */
	private void recolorForDelete(RedBlackNode child, RedBlackNode parent) {

		RedBlackNode x_node, x_sibling;
		x_node = child;
		
		while ((x_node == null || x_node.isBlack()) && x_node != root)
		{
			if (parent.getLeftChild() == x_node)
			{
				// case 1. ( in Introduction.to.Algorithms.)
				//    auto rotate and re-color to become case 2, which is following.
				x_sibling = parent.getRightChild();
				if (x_sibling.isRed())
				{
					x_sibling.setBlackColor();
					parent.setRedColor();
					rotateWithRightChild(parent);
					x_sibling = parent.getRightChild();
				}
				
				// case 2. ( in Introduction.to.Algorithms.)
				// if (old parent) new x_node is red, loop is done. We win!
				if ((x_sibling.getLeftChild() == null || 
					x_sibling.getLeftChild().isBlack())
				    && (x_sibling.getRightChild() == null || 
				    	x_sibling.getRightChild().isBlack()))
				{
					x_sibling.setRedColor();
					x_node = parent;
					parent = x_node.getParent();
				}
				else
				{
					
					// case 3. ( in Introduction.to.Algorithms.)
					//    x_sibling's left is red. rotate x_sibling to fit for case 4.
					if (x_sibling.getRightChild() == null ||
						x_sibling.getRightChild().isBlack())
					{
						
						RedBlackNode x_siblingLeft = x_sibling.getLeftChild();
						if (x_siblingLeft != null)
							x_siblingLeft.setBlackColor();
						x_sibling.setRedColor();
						rotateWithLeftChild(x_sibling);
						x_sibling = parent.getRightChild();// == x_siblingLeft
					}
					
					// case 4. ( in Introduction.to.Algorithms.)
					//   rotate to makeup a missed black node. Another way to Win!
					x_sibling.setColor(parent.getColor());;
					parent.setBlackColor();
					if (x_sibling.getRightChild() != null)
						x_sibling.getRightChild().setBlackColor();
					rotateWithRightChild(parent);
					x_node = root; // make sure root is black, since we're done!
					break;
				}
			}
			else // the anti-direction case of 1,2,3,4 above.
			{
				x_sibling = parent.getLeftChild();
				// case 1'.
				if (x_sibling.isRed())
				{
					x_sibling.setBlackColor();
					parent.setRedColor();
					rotateWithLeftChild(parent);
					x_sibling = parent.getLeftChild();
				}
				
				 // case 2'
				if ((x_sibling.getLeftChild() == null || 
						x_sibling.getLeftChild().isBlack())
					&& (x_sibling.getRightChild() == null || 
					   	x_sibling.getRightChild().isBlack()))
				{
					x_sibling.setRedColor();
					x_node = parent;
					parent = x_node.getParent();
				}

				else
				{
					//case 3'
					if (x_sibling.getLeftChild() == null ||
						x_sibling.getLeftChild().isBlack())
					{
						RedBlackNode x_siblingRight = x_sibling.getRightChild();
						if (x_siblingRight != null)
							x_siblingRight.setBlackColor();
						x_sibling.setRedColor();
						rotateWithRightChild(x_sibling);
						x_sibling = parent.getLeftChild();
					}
					// case 4'
					x_sibling.setColor(parent.getColor());
					parent.setBlackColor();
					if (x_sibling.getLeftChild() != null)
						x_sibling.getLeftChild().setBlackColor();
					rotateWithLeftChild(parent);
					x_node = root;
					break;
				}
			}
		}
		if (x_node != null)
			x_node.setBlackColor();
	}


	

	@Override
	public String toString() { // pre-order
		if (root == null) {
			return "This tree is empty";
		} else {
			return preorder_traversal(root, "root");// + "\n";
		}

	}

	private String preorder_traversal(RedBlackNode start, String name) {
		// parent
		// System.out.println(name + ":" + (start.isBlack()?"BLACK":"RED") +
		// "("+ start.getElement().getKey()+ "," +
		// start.getElement().getValue()+")");

		String current = name + ":" + (start.isBlack() ? "BLACK" : "RED") + "("
				+ start.getElement().getKey() + ","
				+ start.getElement().getValue() + ")" + "\n";

		// left child, then right child
		if (start.getLeftChild() != null)
			current += preorder_traversal(start.getLeftChild(), name + "-left");
		if (start.getRightChild() != null)
			current += preorder_traversal(start.getRightChild(), name
					+ "-right");

		return current;
	}

}
