import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Graph<T> {

    private LinkedList<Integer>[] adjacencyList;

    ArrayList<String> actors;

    Queue q = new Queue();


    public Graph(int vertices, ArrayList<String> actors){
        this.actors = actors;
        adjacencyList = new LinkedList[vertices];
        for(int i = 0; i < vertices; i++){
            adjacencyList[i] = new LinkedList<Integer>();
        }
    }

    //add relation to actor1 and actor2
    public void add(int a1, int a2){
        if(a1 != a2 && !adjacencyList[a1].contains(a2) && !adjacencyList[a2].contains(a1)) {
            adjacencyList[a1].add(a2);
            adjacencyList[a2].add(a1);
        }
    }

    //help gathered from https://www.youtube.com/watch?v=oDqjPvD54Ss
    public ArrayList<Integer> bfs(int start, int end){
        int[] prev = findPath(start);

        return reconstructPath(start,end,prev);
    }

    //implementation of BFS algorithm to find shortest path to target
    //creates empty boolean array and int array to keep track of path and visited actors
    //returns previous parents to reconstruct path in following function
    public int[] findPath(int start){
        Queue q = new Queue();
        q.enqueue(start);
        //init boolean array of visited vertices (set all to false)
        boolean[] visited = new boolean[adjacencyList.length];
        for(int i = 0; i < visited.length; i ++){
            visited[i] = false;
        }
        visited[start] = true;
        //prev[i] is from whom we got to i (parent)
        int[] prev = new int[adjacencyList.length];
        for(int i = 0; i < prev.length; i ++){
            prev[i] = -1;
        }
        //prev[start] = start;
        int current;
        //gets all neighbors of current "node"
        LinkedList<Integer> neighbors;
        while(!q.isEmpty()){
            current = q.dequeue();
            neighbors = adjacencyList[current];
            //goes through all neighbors of current and adds them to queue to be looked at if not already visited
            for(int next : neighbors){
                if(!visited[next]){
                    prev[next] = current;
                    q.enqueue(next);
                    visited[next] = true;

                }
            }
        }
        return prev;
    }


    //back tracks through path array to find path from start to target
    public ArrayList<Integer> reconstructPath(int start, int target, int[] prev){
        ArrayList<Integer> path = new ArrayList<>();
        for(int i = target; i != -1; i=prev[i]){
            if(prev[i] != -1){
                path.add(i);
            }
        }
        //since we loop backwards, we reverse the path back in order
        Collections.reverse(path);

        if(path.size() > 0 && path.get(path.size()-1) == target){
            return path;
        }
        return null;

    }

}
