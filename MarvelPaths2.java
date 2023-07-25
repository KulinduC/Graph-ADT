package hw6;

import java.io.IOException;

import hw4.Graph;
import hw5.MarvelParser;

import java.util.*;

public class MarvelPaths2 {
	//This class does not represent an ADT and doesn't need an abstraction function and rep invariant 
	
	private Graph<String,Double> graph;
	
	public MarvelPaths2() {
		graph = new Graph<String,Double>(); //using double to represent the weight 
	}
	
	public class Paths implements Comparable<Paths> { //custom paths class
        private double weight; 
        private ArrayList<String> nodes;
        private String s;
        private String e;
        
        public Paths(String start, String end) {
            weight = 0.000;
            nodes = new ArrayList<String>();
            s = new String(start);
            e = new String(end);
            nodes.add(start);
        }
        
        public Paths(Paths o) { //copy constructor
        	weight = o.getWeight();
        	nodes = new ArrayList<String>(o.getNodes());
        	s = new String(o.getStart());
        	e = new String(o.getEnd());
        }
        
        public void addNode(String node) { //adding a node to the path and updating its path
        	nodes.add(node);
        	weight += graph.getEdge(e, node).first();
        	e = node; 
        }
        
        
        public double getWeight() { //getters for the private variables
        	return weight;
        }
        
        public ArrayList<String> getNodes() {
        	return nodes;
        }
        
        public String getStart() {
        	return s;
        }
        
        public String getEnd() {
        	return e;
        }
        
        @Override
        public int compareTo(Paths o) { //overriding compareTo function for the priority queue
        	if (getWeight() > o.getWeight()) {
        		return 1;
        	}
        	else if (getWeight() < o.getWeight()) {
        		return -1;
        	}
        	return 0;
        }
    }
	
	public void createNewGraph(String filename)  {
		Map<String, Set<String>> charsBooks = new HashMap<String ,Set<String>>();
		Set<String> chars = new HashSet<String>();
		Graph<String, String> string_graph = new Graph<String, String>();
		
		try {
		MarvelParser.readData(filename, charsBooks, chars);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		for (String t : chars) {
			graph.addNode(t);
		}
		for (String book : charsBooks.keySet()) { //reads through the key values of the map
			for (String characters : charsBooks.get(book)) { //reads through the set of characters in each book 
				for (String charz : charsBooks.get(book)) {
					if (characters != charz) {
						string_graph.addEdge(characters, charz, book); //creates a connection between the first character and the rest
					}
					else {
						graph.addEdge(characters, charz, 0.0);
					}
				}
			}
		}
		
		Iterator<String> nodeIt = string_graph.listNodes(); //iterating through the graph of strings and then converting the edges to weights 
		
	    while (nodeIt.hasNext()) {
	      String n = nodeIt.next();
	      for (String child : string_graph.getKids(n).keySet()) {
	        double weight = (double) 1 / string_graph.getEdge(n, child).size();
	        graph.addEdge(n, child, weight);
	      }
	    }
	}
	
	public String findPath(String node1, String node2) {
		if (!graph.NodePresent(node1)) {
			String e = "unknown character " + node1 + "\n"; 
			if (!graph.NodePresent(node2) && !node1.equals(node2)) {
				String e2 = "unknown character " + node2 + "\n";
				return e + e2;
			}
			return e;
		}
		
		if (!graph.NodePresent(node2)) {
			return "unknown character " + node2 + "\n";
		}
		String start = node1;
		String dest = node2; 
		String no_path = "path from " + start + " to " + dest + ":\nno path found\n";
		
		Set<String> finished = new HashSet<String>();
		PriorityQueue<Paths> pq = new PriorityQueue<>();
        Paths begin = new Paths(start, start); //creating the path object
        pq.add(begin);
        
        while (!pq.isEmpty()) {
        	Paths minPath = pq.poll(); //removing the min path
        	String minDest = minPath.getEnd(); //getting the end of the path 
        			
        	if (minDest.equals(dest)) {
        		return pathFinder(minPath,start,dest);
        	}
        	
        	if (finished.contains(minDest)) {
        		continue;
        	}
        	for (String child : graph.getKids(minDest).keySet()) { //iterating through all children of the end node in the path 
        		if (!finished.contains(child)) {
        			Paths p1 = new Paths(minPath); //using copy constructor 
        			p1.addNode(child);
        			pq.add(p1);
        		}
        	}
        	finished.add(minDest);
        }
        return no_path;
	}
	
	public String pathFinder(Paths path, String start, String dest) {
		String lines = "path from " + start + " to " + dest + ":\n"; //starts returning the path
		for (int i = 0; i < path.getNodes().size()-1; i++) { //iterates through the minimum cost path and outputs it along with its weight
			String one = path.getNodes().get(i);
			String two = path.getNodes().get(i+1);
			double weight = graph.getEdge(one, two).first();
			lines += one + " to " + two;
			lines += String.format(" with weight %.3f\n", weight);
		}
		
		lines += String.format("total cost: %.3f\n",path.getWeight());
        return lines;
    }
	
	
	
	public static void main(String[] args) {
		String file = args[0];

		
		MarvelPaths2 mp = new MarvelPaths2();
		
		mp.createNewGraph(file);
		System.out.print(mp.findPath("STEVE","BATMAN"));
	}
	
	
	
}