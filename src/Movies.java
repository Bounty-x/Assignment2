// import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
/*
 * Downloaded JAR files from:
 *   http://commons.apache.org/proper/commons-csv/user-guide.html (Apache Commons CSV)
 *   http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm (JSON-Simple)
 *
 * Got them in my build path using:
 *   https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-%28Java%29
 */

public class Movies {
	public Graph graph;
	ArrayList<String> actors;//actors real index spots
	ArrayList<Integer> movieSizes;//number of actors per movie
	ArrayList<String> allActors;//list of actors with duplicates

	public Movies(int size, ArrayList<String> actors, ArrayList<Integer> movieSizes, ArrayList<String> allActors){
		//size of actors no duplicates
		this.actors = actors;
		graph = new Graph(size,actors);
		this.movieSizes = movieSizes;
		this.allActors = allActors;
		//sort();//sorts actors array to find index easier
		buildGraph();
	}

	//not used
	private void sort(){
		Collections.sort(actors);
	}


	//helper functions for buildGraph() in order to index properly
	//example if we are on second movie, index from movie1.numActors-1 to movie1.numActors + movie2.numActors
	private int sum(int end){
		int sum = 0;
		for(int i = 0; i <= end; i++){
			sum += movieSizes.get(i);
		}
		return sum;
	}
	private int prevSum(int movieNum){
		int sum = 0;
		for(int i = 0; i < movieNum; i++){
			sum+= movieSizes.get(i);
		}
		return sum;
	}


	//for actors in list within the same movie, make a relationship in graph
	public void buildGraph() {

		for(int i = 0; i < movieSizes.size(); i++) {
//			System.out.println("Movie num" + i);
			if (i == 0) {//starter case
				for (int j = 0; j < movieSizes.get(i); j++) {
					for (int k = j + 1; k < movieSizes.get(i); k++) {
						graph.add(find(allActors.get(j)), find(allActors.get(k)));
					}
				}
			} else {
				int start = prevSum(i);//starts from end of movie length (index)
				int end = sum(i);//ends at sum of previous movie lengths (index)
				for (int j = start; j < end; j++) {
//					System.out.println("From: " + j + " To: "+end);
					for (int k = j + 1; k < end; k++) {
//						graph.add(binsearch(actors,allActors.get(j),0,actors.size()),
//								binsearch(actors,allActors.get(k),0,actors.size()));
						graph.add(find(allActors.get(j)),find(allActors.get(k)));//finds actor's index then adds them to graph
					}
				}
			}
		}

	}

	//calls graph function and prints results here
	//TODO: path doesn't have first actor...
	public void findPath(String start, String end){
		ArrayList<Integer> path = graph.bfs(find(start),find(end));
		if(path != null) {
			System.out.print("Path between " + start + " and " + end + " : ");
			System.out.print(start);
			for (int i : path) {
				System.out.print(" --> ");
				System.out.print(actors.get(i));
			}
		}else{
			System.out.println("Path is NULL");
		}
	}



	//finds actor's real index spot in the graph
	//may be alot easier if list is sorted?
	public int find(String name){
		for(int i = 0; i < actors.size(); i++){
			if(actors.get(i).equals(name)){
				return i;
			}
		}
		return -1;
	}

	//got stackoverflow error alot from this
	int binsearch(ArrayList<String> arr,String target, int low, int high){
		int start = (high + low)/2;
		if(arr.get(start).equals(target)){
			return start;
		}else{
			if(arr.get(start).compareTo(target) > 0){//we need to look at left side
				return binsearch(arr,target,low,start);
			}else
				return binsearch(arr,target,start,high);
		}
	}

	public static void main (String[] args) {

		try {
			Reader reader = new FileReader(args[0]);
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			JSONParser jsonParser = new JSONParser();

			ArrayList<String> actors = new ArrayList<>();
			//number of actors in each movie
			ArrayList<Integer> movieSizes = new ArrayList<>();
			ArrayList<Integer> movieSizesDup = new ArrayList<>();
			ArrayList<String> allActors = new ArrayList<>();


			int movies = 0;
			int size =0;

			for (CSVRecord csvRecord : csvParser) {
				// && movies < 4
				if (movies >= 1) {
					size = 0;
					int numActors = 0;
					String title = csvRecord.get(1);
					String castJSON = csvRecord.get(2);
					// [] = array
					// { } = "object" / "dictionary" / "hashtable" -- key "name": value

//					System.out.println("Title: " + title);
					Object object = jsonParser.parse(castJSON);

					JSONArray jsonArray = (JSONArray) object;
					//System.out.println(title +" Size: "+  jsonArray.size());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//						System.out.println(" * " + jsonObject.get("name"));
						String name = jsonObject.get("name").toString().toLowerCase(Locale.ROOT);



						//creates a list of all actors with no duplicates
						if (!actors.contains(name)) {

							actors.add(name);
							numActors++;

						}//list with duplicate actor names
//						else {
//							System.out.println("already exists ***********");
//						}
						allActors.add(name);
						size++;

					}
					movieSizes.add(numActors);
					movieSizesDup.add(size);
				}

				++movies;
			}
//			System.out.println(actors.size());
//			System.out.println(Arrays.toString(movieSizes.toArray()));
//			System.out.println(Arrays.toString(movieSizesDup.toArray()));
//			System.out.println(Arrays.toString(allActors.toArray()));
			System.out.println("LOADING... may take more than 5 minutes");

			Movies m = new Movies(actors.size(),actors,movieSizesDup,allActors);



			Scanner sc = new Scanner(System.in);


			System.out.println("ENTER -1 TO EXIT:");
			String a1 = "";
			String a2 = "";
			boolean isRunning = true;
			while(isRunning){
				while(isRunning && a1 != "-1" && m.find(a1) == -1) {
					System.out.print("\nActor 1 name: ");
					a1 = sc.nextLine().toLowerCase(Locale.ROOT);
//					System.out.println(a1);
					if(a1.equals("-1")){
						isRunning = false;
					}
				}

				while(isRunning && a2 != "-1" && m.find(a2) == -1){
					System.out.print("Actor 2 name: ");
					a2 = sc.nextLine().toLowerCase(Locale.ROOT);
//					System.out.println(a2);
					if(a2.equals("-1")){
						isRunning = false;
					}
				}
				if(isRunning) {
					m.findPath(a1, a2);
					a1 = "";
					a2 = "";
				}
			}


			csvParser.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("File " + args[0] + " is invalid or is in the wrong format.");
		}

	}
}