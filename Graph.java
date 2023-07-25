package hw4;

import java.util.*;

/** 
 * Graph<String,String> represents an immutable directed multi graph of string parent and string child
 * It includes the edge connection between the nodes and make sures the edges have a positive weight to them of integer type
 * It also cannot have disconnected nodes and could be acyclical or cyclical.
 
 * The implementation is similar to that of the conventional adjacent list way of representing a graph but with more 
 * emphasis on the edges. It does not expect for identical edges with the same value connecting the same parent and child nodes
 */

public class Graph<T,V> {
    /**
	 * 
	 * Abstraction Function: TreeMap<str1, TreeMap<str2, TreeSet<int1>>>
	 * str1: parent node
	 * str2: child node
	 * int1: Edges between parent and child node (directed graph)
	 * The outer map has a representation of <parent node, child node> and the inner map has a representation of <child node, edge(s)>
	 * 
	 * Rep Invariant: The graph cannot have any null values, in any functions when data is NULL, nothing w. The edge weights must also be postive 
	 *  
	 *
	 * 
	 **/

    private TreeMap<T, TreeMap<T, TreeSet<V>>> direct_graph;

    /** Creates new graph object.
	    @modifies Makes new Graph object
    */
    public Graph() {
        direct_graph = new TreeMap<T, TreeMap<T, TreeSet<V>>>();
    }

    /** Iterator class that returns the iterator of child nodes.
	    @requires Valid parent node
	    @return Iterator to child nodes with edges
    */
    private class EdgeRunner implements Iterator<String> {
        private Iterator<T> nodeIt;
		private Iterator<V> labelIt;
		private T parentN;
		private T currentN;
		private V label;

		public EdgeRunner(T parent) {
            parentN = parent;
			nodeIt = direct_graph.get(parent).keySet().iterator(); 
            labelIt = null;
		}
		 
		@Override
		public boolean hasNext() {
		 	return nodeIt.hasNext() || (labelIt != null && labelIt.hasNext());
		} 
		@Override
		public String next() {
			 //moving on to next child
			if (labelIt == null || !labelIt.hasNext()) {
				//no more next child
				if (!nodeIt.hasNext()) {
					throw new NoSuchElementException();
				}
				 
				//Store the new child node
				currentN = nodeIt.next();
				labelIt = direct_graph.get(parentN).get(currentN).iterator();
			}
			  
	 		label = labelIt.next();
			return (currentN.toString() + "(" + label.toString() + ")");
		} 
    }

    /** Adds a node to the graph
        @param n A string node to add to the graph
	    @requires The string n to not be null
        @modifies Graph size and connections 
    */
    public void addNode(T n) {
        checkRep(n,n, null);
        if(n != null) {
			TreeMap<T, TreeSet<V>> child_tree = new TreeMap<T, TreeSet<V>>();
			if(direct_graph.containsKey(n) == false) {
				direct_graph.put(n, child_tree);
			}
		}
    }

    /** Adds a directed edge between a parent and child node
        @param parentN,childN,edge A string node to add an edge to 
	    @requires The parent and child node to be valid
        @modifies Graph by adding an edge between the parent and child
    */
    public void addEdge(T parentN, T childN, V edge) {
        checkRep(parentN, childN, edge);
        if (parentN != null && childN != null) {
            if(direct_graph.containsKey(parentN) == false) {
				this.addNode(parentN);
			}
			if (direct_graph.containsKey(childN) == false) {
				this.addNode(childN);
			}
            if(direct_graph.get(parentN).containsKey(childN) == false) {
				TreeSet<V> e = new TreeSet<V>();
				e.add(edge);
				direct_graph.get(parentN).put(childN, e);
            }
            else {
                direct_graph.get(parentN).get(childN).add(edge);
            }
        }
    }

    /** Outputs the number of nodes in the graph
        @requires a valid graph object
        @return number of nodes in the graph
    */
    public int getSize() {
        return direct_graph.size();
    }

    /** Outputs all the parent's node child nodes
        @param parentN The parent node 
	    @requires parent is in the graph
        @return all child nodes of the parent
    */
    public TreeMap<T,TreeSet<V>> getKids(T parentN) {
        checkRep(parentN,parentN,null);
        TreeMap<T, TreeSet<V>> children = new TreeMap<T, TreeSet<V>>();  
		children.putAll(direct_graph.get(parentN));
		return children;
    }

    /** Gets all the edges between a parent and source node
	    @param parentN childN The parent and child node 
        @requires parent and child node are not null and in the graph
        @return a new set containing the edges between the nodes
    */
    public TreeSet<V> getEdge(T parentN, T childN) {
        checkRep(parentN,childN, null);
        if(direct_graph.size() == 0) {
			return new TreeSet<V>();
		}
		
		TreeSet<V> edges = new TreeSet<V>(); 
		edges.addAll(direct_graph.get(parentN).get(childN));
		return edges;
	}

    /** returns a copy of the graph 
	    @requires the graph to be not null 
	    @return a clone of the graph 
    */
    public TreeMap<T,TreeMap<T, TreeSet<V>>> clone() {
        TreeMap<T,TreeMap<T,TreeSet<V>>> new_graph = new TreeMap<T,TreeMap<T, TreeSet<V>>>();
		new_graph.putAll(direct_graph);
		return new_graph;
    }

    /** checks if a node is present in the graph
        @param n String node to check if its in the graph
	    @requires string value to not be null 
	    @return a boolean value true or false
    */
    public boolean NodePresent(T n) {
        checkRep(n,n,null);
        if(direct_graph.size() == 0) {
			return false;
		}
		return direct_graph.containsKey(n);
    }

    /** Output an iterator of the parent's node children and the edges 
	    @param parentN Node to traverse its children through the edges 
        @requires graph and parent node to not be null
        @return iterator of child nodes from the parent 
    */
    public Iterator<String> listChildren(T parentN) {
        checkRep(parentN,parentN,null);
        return new EdgeRunner(parentN);
    }

    /** Outputs an iterator of all nodes 
	    @requires graph not to be null
        @return Iterator of nodes in the graph
	    
    */
    public Iterator<T> listNodes() {
        TreeSet<T> children = new TreeSet<>(direct_graph.keySet());
		ArrayList<T> nodes = new ArrayList<T>();
		
		for(T i : children) {
			nodes.add(i);
		}
		return nodes.iterator();
    }

    /** checks the integrity of the graph
        @param parent,child,e 
	    @throws RuntimeException if the rep invariant is violated 
    */
    private void checkRep(T parent, T child, V e) throws RuntimeException {
        if (parent == null || child == null) {
            if (parent == null) {
                throw new RuntimeException("Parent string cannot be null");
            }
            else if (child == null) {
                throw new RuntimeException("Child string cannot be null");
            }
        }
    }
}

