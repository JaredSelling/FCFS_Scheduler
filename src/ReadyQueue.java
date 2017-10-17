import java.util.LinkedList;

public class ReadyQueue<T extends Comparable<? super T>> {

    private LinkedList<T> readyQueue = new LinkedList<T>();

    public void enqueue(T item) {
        readyQueue.addLast(item);
    }

    public T dequeue() {
        return readyQueue.poll();
    }

    public boolean hasProcesses() {
        return !readyQueue.isEmpty();
    }

    public int size() {
        return readyQueue.size();
    }

    public boolean isEmpty() { return readyQueue.isEmpty(); }


    public T getItem(int index) { return readyQueue.get(index);}


}
