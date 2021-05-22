//// import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.Reader;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Locale;
//import java.util.Queue;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//
//import org.json.simple.parser.JSONParser;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
///*
// * Downloaded JAR files from:
// *   http://commons.apache.org/proper/commons-csv/user-guide.html (Apache Commons CSV)
// *   http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm (JSON-Simple)
// *
// * Got them in my build path using:
// *   https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-%28Java%29
// */
//
//public class RoughDraft {
//    //start of Actor class creates a 'profile' for each actor including their hash key and name as String
//    public class Actor {
//        private String name;
//        private int hash;
//
//        public Actor(String name) {
//            this.name = name;
//            this.hash = hash(name,numBuckets);
//        }
//
//        //find a 'unique' hash by adding char values then modding to size of array
//        private static int hash(String name, int arrSize) {
//            int hash = 0;
//            for (char x : name.toCharArray()) {
//                hash += x;
//            }
//            hash *= hash;
//            hash %= arrSize;
//            System.out.println("Hash: " + hash);
//            return hash;
//        }
//
//    }
//
//    public int numActors;//number of actors added to data structure
//    private int numBuckets;
//    //public ArrayList<Actor> actors;
//    Actor[] actors;
//    int[][] arr;
//
//    Graph<Actor> graph;
//
//    public Movies(ArrayList<ArrayList<String>> movies,int size){
//        numBuckets = size;
//
//
//        //adjacency matrix
//        int[] row = new int[numBuckets];
//        for(int i = 0; i < numBuckets; i++){
//            row[i] = 0;
//        }
//        System.out.println(Arrays.toString(row));
//        arr = new int[numBuckets][];
//        for(int j = 0; j < numBuckets; j++){
//            arr[j] = Arrays.copyOf(row,numBuckets);
//        }
//        //print();
//
//        buildGraph(movies);
//
//    }
//
//    //each actor in String[] l works with each other we can assume
////	public void buildGraph(ArrayList<ArrayList<String>> l){
////		int hash;
////		for(ArrayList<String> movie:l) {
////			for (String a : movie) {
////				actors = new Actor[movie.size()];
////				Actor temp = new Actor(a);
////				hash = temp.hash;
////				System.out.println("HASH:" + hash);
////				hash = checkHash(hash);
////				if (numActors >= numBuckets)
////					growActors();
////				actors[temp.hash] = temp;
////				numActors++;
////			}
////		}
////	}
//
//    public void buildGraph(ArrayList<ArrayList<String>> l){
//        int hash;
//        for(ArrayList<String> movie :  l){
//            String actor = movie.get(0);
//            Actor temp = new Actor(actor);
//            int i =0;
//            for(String coworker : movie){
//                if(i > 0) {
//
//                }
//                i++;
//            }
//        }
//    }
//
//    //grows actors array by doubling its size
//    private void growActors(){
//        Actor[] temp = new Actor[actors.length*2];
//        for(int i = 0; i < actors.length; i++){
//            temp[i] = actors[i];
//        }
//        actors = temp;
//    }
//
//    //uses quadratic collision case (if hash is already occupied)
//    //TODO: may need a getHash function
//    private int checkHash(int hashNum){
//        int x = 0;
//        int y = 1;
//        while(actors[hashNum] != null){
//            if(hashNum + x > numBuckets){
//                hashNum -= numBuckets;
//            }
//            x += 2*y;
//            y++;
//        }
//        return hashNum + x;
//    }
//
//    private int getHash(int hash, Actor a){
//        int x = 0;
//        int y = 1;
//        //while there are still things in 'placeholders' keep looking for our actor
//        while(actors[hash].equals(a) != true && actors[hash] != null){
//            //if we have to wrap around list
//            if(hash + x > numBuckets){
//                hash -= numBuckets;
//            }
//            x+= 2*y;
//            y++;
//        }
//        if(actors[hash].equals(a) != true){
//            return hash + x;
//        }
//        //not found
//        return -1;
//
//    }
//
//    public void print(){
//
//        for(int i = 0; i < 1; i++){
//            System.out.print("[");
//            for(int j = 0; j < arr[i].length; j++){
//                System.out.print(" ");
//                System.out.print(arr[i][j]);
//            }
//            System.out.println("]");
//        }
//
//    }
//
//
//    public static void main (String[] args) {
//
//        try {
//
//            Reader reader = new FileReader(args[0]);
//            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
//            JSONParser jsonParser = new JSONParser();
//
//            //all actors for a specific movie
//            ArrayList<ArrayList<String>> movies = new ArrayList<>();
//
//            int numActors = 0;
//
//
//            //Graph<String> graph = new Graph(start);
//
//            int numMovies = 0;
//            ArrayList<String> allactors = new ArrayList<>();
//
//            for (CSVRecord csvRecord : csvParser) {
//                if (numMovies >= 1) {
//                    ArrayList<String> actors = new ArrayList<>();
//
//                    String title = csvRecord.get(1);
//                    String castJSON = csvRecord.get(2);
//                    // [] = array
//                    // { } = "object" / "dictionary" / "hashtable" -- key "name": value
//
//                    //System.out.println("Title: " + title);
//                    Object object = jsonParser.parse(castJSON);
//
//                    JSONArray jsonArray = (JSONArray)object;
//                    for (int i = 0; i < jsonArray.size(); i++) {
//                        JSONObject jsonObject = (JSONObject)jsonArray.get(i);
//                        //System.out.println(" * " + jsonObject.get("name"));
//                        String name = jsonObject.get("name").toString();
//                        //adds actors for each movie
//                        if(!allactors.contains(name.toLowerCase(Locale.ROOT).toString())) {
//                            allactors.add(name.toLowerCase(Locale.ROOT).toString());
//                            actors.add(name.toLowerCase(Locale.ROOT).toString());
//                            //TODO: what about duplicates of same actor?
//                            numActors++;
//                        }
//                        else{
//                            //System.out.println("Already exists*****************************************");
//                        }
//
//                    }
//                    movies.add(actors);
//                    //builds graph for each movie at a time since all actors work with each other
//                }
//                ++numMovies;
//            }
//            System.out.println(numActors);
//            System.out.println(allactors.size());
//            //start
//            Movies m = new Movies(movies,numActors);
//
//            //m.print();
//
//            csvParser.close();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            System.out.println("File " + args[0] + " is invalid or is in the wrong format.");
//        }
//
//    }
//}