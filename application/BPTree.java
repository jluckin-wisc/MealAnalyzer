package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        
        //Assigns branching factor
        this.branchingFactor = branchingFactor;
        
        //Assigns root to null
        this.root = null;
        }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
    	System.out.println("Inserting: " + key);
    	//Root is null then this the first node
    	if(this.root == null) {
    		LeafNode node = new LeafNode();
    		node.insert(key, value);
    		node.parent = null;
    		this.root = node;
    		return;
    		
    	} else {
    		
    		root.insert(key, value);
    	}
    	
    	
        // TODO : Complete
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        // TODO : Complete
        return null;
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        InternalNode parent;
        
        /**
         * Package constructor
         */
        Node() {
            // TODO : Complete
        	keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
//        	if(this.getClass()== LeafNode.class) {
//            		
//        		return ((LeafNode)this).values.toString();
//        	}
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
            
        }
        
        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        K getFirstLeafKey() {
            //TODO
            return null;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
        	if(super.keys.size()>=BPTree.this.branchingFactor) {
            	return true;
            }
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
//System.out.println("Inserting into Internal Node: " + key);
        	if(value == null) {
	        	int keyPosition = -1;
	        	
	        	if(super.keys.size()==0) {
	        		super.keys.add(key);
	        		keyPosition = 0;
	        	}else {
	        		for(int i = 0; i<super.keys.size(); i++) {
	        			if(key.compareTo(super.keys.get(i))<0) {
	        				super.keys.add(i,key);
	        				keyPosition = i;
	        				break;
	        			}
	        		}
	        		if(keyPosition==-1) {
	        			super.keys.add(key);
	    				
	        		}
	        	}

	            
	            if(this.isOverflow()) {
	            	//TODO move children over to newSibling and remove them from this
	            	InternalNode newSibling = (InternalNode) this.split();
	            	
	            	
	            	//Changing keys/values
	            	int half = super.keys.size()/2 + (super.keys.size()%2)-1; 
	            	K promotedKey = this.keys.get(half);
	            	this.children = new ArrayList<Node>(this.children.subList(0,half+1));
	            	for(int i=0;i<this.children.size();i++) {
	            		this.children.get(i).parent= this;
	            	}
	       //System.out.println("Half: " + half);
	       //System.out.println("Size: " + this.keys.size());
	            	List<K> newKeys = new ArrayList<K>(this.keys.subList(0,half));
	
	            	this.keys= newKeys;
	
	            	
	            	//Promoting
	            	//Finding parent node
	            	if(this.parent == null) {
	            		InternalNode promotedNode = new InternalNode();

	            		promotedNode.children.add(this);
	            		promotedNode.children.add(newSibling);

	            		BPTree.this.root = promotedNode;
	            		this.parent = promotedNode;
	            		newSibling.parent=promotedNode;
	            		
	            	}
	            	else {
	            		newSibling.parent = this.parent;
	            		this.parent.children.add(this.parent.children.indexOf(this)+1,newSibling);
	            	}
	            	
	            	this.parent.insert(promotedKey, null);
	            	
	            }
        	} else {
        		//Find correct leaf node
        		for(int i=0; i<this.keys.size();i++) {
        			if(key.compareTo(this.keys.get(i))<=0){
        				if(children.size()==0) {
        					//TODO Remove this
        					System.out.println("Index too Big");
        				}
        				this.children.get(i).insert(key, value);
        				break;
        			} else if(i== this.keys.size()-1) {
System.out.println("Inserting into last childNode");
        				this.children.get(i+1).insert(key,value);
        				break;
        			}
        		}
        	}
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
//System.out.println("Splitting InternalNode");
            // TODO : Complete
        	
        	//Create a new node to store half of the current node's data
        	InternalNode newSibling = new InternalNode();
        	
        	//Find the halfway point of the keys
        	int half = super.keys.size()/2 + (super.keys.size()%2)-1;
        	
        	//Forming the left leafNode
        	newSibling.keys = new ArrayList<K>(this.keys.subList(half+1, keys.size()));
//        	newSibling.values = this.values.subList(half, keys.size());
        	
        	newSibling.children = new ArrayList<Node>(this.children.subList(half+1,children.size()));
        	for(int i=0;i<newSibling.children.size();i++) {
        		newSibling.children.get(i).parent= newSibling;
        	}
//        	for(int i=0; i<half; i++) {
//        		newSibling.insert(super.keys.get(i), this.values.get(i));
//        	}
        	
        	
            return newSibling;
            
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            values = new ArrayList<V>();
            next = null;
            previous = null;
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return null;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
        	if(super.keys.size()>=BPTree.this.branchingFactor) {
            	return true;
            }
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
 //System.out.println("Inserting into LeafNode");
        	int keyPosition = -1;
        	
        	if(super.keys.size()==0) {
        		super.keys.add(key);
        		keyPosition = 0;
        	}else {
        		for(int i = 0; i<super.keys.size(); i++) {
        			if(key.compareTo(super.keys.get(i))<0) {
        				super.keys.add(i,key);
        				keyPosition = i;
        				break;
        			}
        		}
        		if(keyPosition==-1) {
        			super.keys.add(key);
    				
        		}
        	}
            if(keyPosition>-1) {
            	this.values.add(keyPosition, value);
            } else {
            	this.values.add(value);
            }
            
            if(this.isOverflow()) {
            	LeafNode newSibling = (LeafNode) this.split();
            	newSibling.next = this.next;
            	if(newSibling.next!=null) {
            		newSibling.next.previous = newSibling;
            	}
            	
            	this.next = newSibling;
            	newSibling.previous = this;
            	//TODO Add previous to other next
            	
            	//Changing keys/values
            	int half = super.keys.size()/2 + (super.keys.size()%2)-1; 
            	K promotedKey = this.keys.get(half);

            	List<K> newKeys = new ArrayList<K>(this.keys.subList(0,half));
            	List<V> newValues = new ArrayList<V>(this.values.subList(0,half));
            	this.keys= newKeys;
            	this.values = newValues;
            	
            	//Promoting
            	//Finding parent node
            	if(this.parent == null) {
            		InternalNode promotedNode = new InternalNode();
            		//promotedNode.insert(promotedKey, null);
            		promotedNode.children.add(this);
            		promotedNode.children.add(this.next);
            		BPTree.this.root = promotedNode;
            		this.parent = promotedNode;
            		newSibling.parent=promotedNode;
            	}else {
            		newSibling.parent=this.parent;
            		this.parent.children.add(this.parent.children.indexOf(this)+1,newSibling);
            	}
            	
            	this.parent.insert(promotedKey, null);
            	
            }
        }
        

        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
//System.out.println("Splitting LeafNode");
        	
        	//Create a new node to store half of the current node's data
        	LeafNode newSibling = new LeafNode();
        	
        	//Find the halfway point of the keys
        	int half = super.keys.size()/2 + (super.keys.size()%2)-1;
        	
        	//Forming the left leafNode
        	newSibling.keys = new ArrayList<K>(this.keys.subList(half, keys.size()));
        	newSibling.values = new ArrayList<V>(this.values.subList(half, keys.size()));
        	       	
            return newSibling;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random(1);

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};
        
        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
//TODO Change back to 400
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
       //TODO System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class BPTree