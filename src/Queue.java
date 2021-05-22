import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Queue<item> {
    private LinkedList<Integer> arr;

    public Queue(){
        arr = new LinkedList<Integer>();
    }

    public void enqueue(int item){
        arr.add(item);
    }

    public int dequeue(){
        int item = arr.getFirst();
        arr.removeFirst();
        return item;
    }

    public boolean isEmpty(){
        return arr.isEmpty();
    }

    public int size(){
        return arr.size();
    }

    public void print(){
        System.out.println(Arrays.toString(arr.toArray()));
    }

}
