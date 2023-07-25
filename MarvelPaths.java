package hw5;
import java.io.IOException;
import java.util.*;

import hw4.Graph;

public class MarvelPaths {
	private Graph<String,String> graph;
	
	/** Creates new graph object.
    @modifies Makes a new Graph object
	*/
	
	public MarvelPaths() {
		graph = new Graph<String,String>();
	}
	/** Fills up the graph object using the csv file 
    @param n A string filename to open
    @requires The string filename to not be null
    @modifies graph object by filling it with data
	 */
	
	public void createNewGraph(String filename)  {
		Map<String, Set<String>> charsBooks = new HashMap<String ,Set<String>>();
		Set<String> chars = new HashSet<String>();
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
						graph.addEdge(characters, charz, book); //creates a connection between the first character and the rest
					}
				}
			}
		}
	}
	/** uses a BFS to see if there is a path existing from start to dest 
    @param n node1 and node2 are the start and destination nodes 
    @requires node1 and node2 
    @effects creates queue and map to track child and parents to trace backwards
    @modifies queue and map size of unvisited nodes while trying to find the shortest path 
    @returns calls pathFinder or returns no path if it doesn't exist 
	 */
	
	
	public String findPath(String node1, String node2) { //checks for invalid characters for both the start and destination nodes
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
		
		Deque<String> queue = new ArrayDeque<>();
		Map<String, String> map = new HashMap<String, String>(); //uses a backwards map of child and parent 
		
		queue.add(start);
		map.put(start, "");
		
		while (!queue.isEmpty()) { //BFS search
			String n = queue.poll();
			if (n.equals(dest)) { //calls pathFinder when there is a guaranteed path 
				ArrayList<String> path = new ArrayList<String>();
				String node21 = dest;
				while (node21 != start) {
					path.add(node21);
					node21 = map.get(node21);
				}
				return pathFinder(map, path, start, dest);
			}
			for (String child : graph.getKids(n).keySet()) { //looks through unvisited node
				if (!map.containsKey(child)) {
					map.put(child,n);
					queue.add(child);
				}
			}	
		}
		return no_path; //default returns no path if dest wasnt reached 
	}
	
	/** Prints out the shortest path from start to dest 
    @param n map of childs and parents, path arraylist,start and end nodes 
    @requires map, path, start and dest to not be null 
    @returns shortest path lexicographically from start to destination 
	 */
	public String pathFinder(Map<String, String> map, ArrayList<String> path, String start, String dest) {
		String lines = "path from " + start + " to " + dest + ":\n"; //starts returning the path
		Collections.reverse(path);
        for (String child : path) {
          String parent = map.get(child);
          lines += parent + " to " + child + " via ";
          lines += graph.getEdge(parent, child).first() + "\n";
        }
        return lines;
     }
	
	public static void main(String[] args) {
		String file = args[0];

		
		MarvelPaths mp = new MarvelPaths();
		
		mp.createNewGraph(file);
		System.out.print(mp.findPath("PETERS, SHANA TOC", "SEERESS"));
		System.out.print(mp.findPath("GOOM","HOFFMAN"));
		System.out.print(mp.findPath("BATMAN","CAPTAIN AMERICA"));
		System.out.print(mp.findPath("BATMAN","GREEN LANTERN"));
		System.out.print(mp.findPath("SEERESS","SEERESS"));
		System.out.print(mp.findPath("BATMAN","BATMAN"));


	}
}
