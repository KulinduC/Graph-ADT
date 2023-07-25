package hw4;

import java.util.*;



public final class GraphWrapper
{
	private Graph<String, String> graph;
	
	public GraphWrapper() {
		graph = new Graph<String,String>();
	}

	public void addNode(String node) {
		graph.addNode(node);
	}
	
	public void addEdge(String parentN, String childN, String edge) {
		graph.addEdge(parentN, childN, edge);
	}

    public int getSize() {
		return graph.getSize();
	}
	
	public TreeSet<String> getEdge(String parentN, String childN) {
		return graph.getEdge(parentN, childN);	
	}
	
	public TreeMap<String, TreeMap<String, TreeSet<String>>> getGraph() {
		return graph.clone();
	}
	
	public TreeMap<String, TreeSet<String>> getChild(String parentN) {
		return graph.getKids(parentN);
	}
	
	
	public Iterator<String> listNodes() {
		return graph.listNodes();
	}
	
	public Iterator<String> listChildren(String parentN) {
		return graph.listChildren(parentN);
	}
	
	public boolean checkNode(String node) {
		return graph.NodePresent(node);
	}
}