import java.util.Collections;

public class FCFS {

    public static void main(String[] args) {
        //instantiate processes
        Process P1 = new Process("P1", new int[]{4, 5, 6, 7, 6, 4, 5, 4}, new int[]{15, 31, 26, 24, 41, 51, 16}, 0);
        Process P2 = new Process("P2", new int[]{9, 11, 15, 12, 8, 11, 9, 10, 7}, new int[]{28, 22, 15, 12, 8, 11, 9, 10}, 0);
        Process P3 = new Process("P3", new int[]{24, 12, 6, 17, 11, 22, 18}, new int[]{28, 21, 27, 21, 54, 31}, 0);
        Process P4 = new Process("P4", new int[]{15, 14, 16, 18, 14, 13, 16, 15}, new int[]{35, 41, 45, 51, 61, 54, 61}, 0);
        Process P5 = new Process("P5", new int[]{6, 5, 15, 4, 7, 4, 6, 10, 3 }, new int[]{22, 21, 31, 26, 31, 18, 21, 33}, 0);
        Process P6 = new Process("P6", new int[]{22, 27, 25, 11, 19, 18, 6, 6}, new int[]{38, 41, 29, 26, 32, 22, 26}, 0);
        Process P7 = new Process("P7", new int[]{4, 7, 6, 5, 4, 7, 6, 5, 6, 9}, new int[]{36, 31, 32, 41, 42, 39, 33, 34, 21}, 0);
        Process P8 = new Process("P8", new int[]{5, 4, 6, 4, 6, 5, 4, 6, 6}, new int[]{14, 33, 31, 31, 37, 21, 19, 11}, 0);

        ReadyQueue<Process> readyQueue = new ReadyQueue<Process>();

        //add processes to ready queue
        readyQueue.enqueue(P1);
        readyQueue.enqueue(P2);
        readyQueue.enqueue(P3);
        readyQueue.enqueue(P4);
        readyQueue.enqueue(P5);
        readyQueue.enqueue(P6);
        readyQueue.enqueue(P7);
        readyQueue.enqueue(P8);

        //initialize timer
        int curTime = 0;

        //get first process in queue and set its state to EXECUTING
        Process curProc = readyQueue.dequeue();
        curProc.setState("EXECUTING");

        System.out.println("Currently executing: " + curProc.getId());

        //main loop.  Each iteration represents 1 time unit.
        while(!readyQueue.isEmpty()) {

            //if state is EXECUTING
            if(curProc.getState() == "EXECUTING") {
                //if current burst > 0
                if (curProc.getCurrentBurst() > 0) {
                    //decrement current burst
                    curProc.decrementCurrentBurst();
                }

                //if  current burst is 0
                if (curProc.getCurrentBurst() <= 0) {
                    //increment process's cpu burst index
                    curProc.incrementCurrentBurst();
                    //set state to BLOCKING
                    curProc.setState("BLOCKING");
                }
            }

            //if state is BLOCKING
            if(curProc.getState() == "BLOCKING") {
                //current process's new arrival time = current time + current io time
                curProc.setArrivalTime(curTime + curProc.getCurrentIO());
                //increment process's current io burst index
                curProc.incrementCurrentIOIndex();
                //set process's state to READY
                curProc.setState("READY");
            }

            //if state is READY
            if(curProc.getState() == "READY") {
                //add process to end of ready queue
                readyQueue.enqueue(curProc);
                //rearrange ready queue based on arrival times
                Collections.sort(readyQueue);
                //get next process
            }
        }
    }





}
