import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;


interface LiftState {
    void moveUp();
    void moveDown();
    void stop();
}


interface LiftStrategy {
    void addRequest(int floor);
    int nextFloor();
}

class Elevator implements LiftState, LiftStrategy {
    private int currentFloor = 0;
    private boolean movingUp = true;
    private TreeSet<Integer> upRequests = new TreeSet<>();
    private Queue<Integer> downRequests = new PriorityQueue<>((a, b) -> b - a);

    @Override
    public void moveUp() {
        if (!upRequests.isEmpty()) {
            currentFloor = upRequests.pollFirst();
            System.out.println("Lift moving up to floor: " + currentFloor);
        } else {
            stop();
        }
    }

    @Override
    public void moveDown() {
        if (!downRequests.isEmpty()) {
            currentFloor = downRequests.poll();
            System.out.println("Lift moving down to floor: " + currentFloor);
        } else {
            stop();
        }
    }

    @Override
    public void stop() {
        System.out.println("Lift stopped at floor: " + currentFloor);
        if (!upRequests.isEmpty()) {
            movingUp = true;
        } else if (!downRequests.isEmpty()) {
            movingUp = false;
        }
    }

    @Override
    public void addRequest(int floor) {
        if (floor > currentFloor) {
            upRequests.add(floor);
        } else {
            downRequests.offer(floor);
        }
    }

    @Override
    public int nextFloor() {
        if (movingUp && !upRequests.isEmpty()) {
            return upRequests.first();
        } else if (!movingUp && !downRequests.isEmpty()) {
            return downRequests.peek();
        }
        return currentFloor;
    }

    public void executeRequests() {
        while (!upRequests.isEmpty() || !downRequests.isEmpty()) {
            if (movingUp) {
                moveUp();
            } else {
                moveDown();
            }
        }
        stop();
    }

    public static void main(String[] args) {
        Elevator lift = new Elevator();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Please Enter no of inputs:");
            int reqCapacity=Integer.parseInt(br.readLine());
            
            while((reqCapacity--)!=0){
                String req=br.readLine();
                Integer[] floors = Arrays.stream(req.split(","))
                                         .map(Integer::parseInt)
                                         .toArray(Integer[]::new);
                for(Integer i:floors){
                    lift.addRequest(i);
                }                      
                lift.executeRequests();
            }
        }catch(IOException e){
             e.printStackTrace();            
        }    
    }
}