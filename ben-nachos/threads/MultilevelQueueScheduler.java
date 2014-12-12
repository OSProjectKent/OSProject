package nachos.threads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import nachos.machine.Lib;
import nachos.machine.Machine;

/*
 *      Provides a multilevel queue scheduler that tracks waiting threads in a wait queue
 * partitioned into three FIFO queues, implemented using a vector containing three 
 * linked lists. Threads in the first and second list are scheduled using 
 * a round robin algorithm with quanta lasting ~ 500 and 1000 ticks (one and two interrupt
 * intervals) respectively. The third queue is scheduled using FCFS.
 *      Time slicing among queues is implemented as follows: the first queue is given sixteen
 * interrupt intervals, the second eight, and the third four. Thus, as the total time elapsed
 * approaches infinity, ~ 57.14, 28.57, and 14.28% of total CPU time are allotted to the first,
 * second, and third queues respectively.
 *      When a thread starts waiting for the CPU, its priority is incremented (demoted) and it
 * is appended to the list at the index of its priority in the wait queue. Newly created
 * threads are added to the first ready queue. If a thread fails to terminate within the
 * time quantum allotted in the first ready queue, it is appended to the second ready queue.
 * Likewise, if it fails to terminate in the second ready queue's quantum, it is appended to the
 * third where it remains until exiting.
 */
public class MultilevelQueueScheduler extends Scheduler {
    
    /*
     * Initialize a new multilevel queue scheduler object.
     */
    public MultilevelQueueScheduler() {
        interrupt_interval_counter = 0;
        current_queue = 0;
    }
    
    /*
     * Allocate a new multilevel thread queue.
     *
     * transferPriority
     *          ignored; no priority donation implemented.
     * Returns a new multilevel thread queue.
     */
    @Override
    public ThreadQueue newThreadQueue(boolean transferPriority) {
        return new MultilevelQueue();
    }
    
    /*
     * Called at the end of every interrupt interval. Determines whether or not the current
     * thread should yield.
     *
     * @return true if the current thread should yield
     */
    @Override
    public boolean timerInterrupt() {
        ++interrupt_interval_counter;
//				System.out.println("### timerInterrupt()");        
        /*
				 * Change current queue if necessary 
				 * This controls the current queue to pull threads from.
				 * If the current queue has used more than it's time amount,
				 * change the queue to pull from and reset that queues counter
				 * else continue pulling threads from the same queue
				 */
        if(current_queue == 0){
            // First queue has a sixteen interrupt interval time slice.
            if(interrupt_interval_counter >= 6){
                current_queue = 1;
                interrupt_interval_counter = 0;
            }
        }
        else if(current_queue == 1){
            // Second queue has an eight interrupt interval time slice.
            if(interrupt_interval_counter >= 3){
                current_queue = 2;
                interrupt_interval_counter = 0;
            }
        }
        else {
            // Third queue has a four interrupt interval time slice.
            if(interrupt_interval_counter >= 1){
                current_queue = 0;
                interrupt_interval_counter = 0;
            }
        }
       
       /* 
			  * Implement quanta within each queue 
        * (CHANGED 12-1) Change queue_num to current_queue
				*/
        if(current_queue == 0){
//            System.out.println("\nThe current queue running is: " + current_queue);
            System.out.println("Computation yielding at: " + Machine.timer().getTime() + " Ticks");
 						//separate running instances by time quantum
						System.out.println("\n\n##########\n");


           // Yield every 500 ticks for first queue.
						//(i.e. every time timerInterrupt() is invoked)
            return true;
        }
        else if(current_queue == 1){
 	    
            
            if(interrupt_interval_counter % 2 == 0){
//		System.out.println("\nThe current queue running is: " + current_queue);
		System.out.println("Computation yielding at: " + Machine.timer().getTime() + " Ticks");
 						//separate running instances by time quantum
						System.out.println("\n\n##########\n");


               // Yield every 1000 ticks for second queue. 
								//(i.e. every two invocations of timerInterrupt())
                return true;
            }
            else{

                return false;
            }
        }
        else {
//            System.out.println("\nThe current queue running is: " + current_queue);
						//separate running instances by time quantum
						System.out.println("\n\n##########\n");
        
				    // Never yield for last queue; FCFS until third queue's slice expires.
            return false;
        }

    } //end timerInterrupt()
    
		/* getter for current_queue */
		public int getCurrentQueue() {
				return current_queue;
		}

    public static void selfTest() {
        MultilevelQueueSchedulerTest.runProject();
    }

		public void printStatus() {
			
		}

    public int interrupt_interval_counter;
    
    public int current_queue;
    
    public class MultilevelQueue extends ThreadQueue {
        
        /*
         * Initialize MultilevelQueue object; allocate three empty linked lists for the wait queue.
         */
        public MultilevelQueue(){
	    //Creates 'spaces' for linked lists
            wait_queue = new Vector<LinkedList<KThread>>(3);
            //Adds linked lists to the wait_queue in the for loop
	    for(int i = 0; i < 3; ++i){
                wait_queue.add(new LinkedList<KThread>());
            }
        }
        
        
        /*
         * When a job is interrupted or newly created its added to the wait queue and it adds it to that queue
         */
        @Override
        public void waitForAccess(KThread thread) {
            Lib.assertTrue(Machine.interrupt().disabled());
            
            int priority = thread.getPriority();
            
            // Demote (increment) priority if necessary.
            if(priority < 2){
                ++priority;
                thread.setPriority(priority);
            }
            wait_queue.get(priority).add(thread);
        }
        
        /*
         * Checks each queue to see if empty in order to save time from processing empty queues
	 * Uses a KThread type in order to keep track of this as well as the current_queue value
	 * Mod 3 is used in in case it is ont on wait_queue 0
         * Returns the first thread on the current queue, or null if all queues are
         *         empty.
         */
        @Override
        public KThread nextThread(){
            Lib.assertTrue(Machine.interrupt().disabled()); //Logs if interupt is disabled
	    
	    //Thread that will be returned in order to process the next job
	    KThread nextJob;
            //KThread 
			System.out.println("Trying to pull job from Queue #" + current_queue);	    
	    if(wait_queue.get(current_queue).isEmpty() == false)
		{
				System.out.println("Able to pull job from Queue #" + current_queue);
		    nextJob = wait_queue.get(current_queue).removeFirst();
		    System.out.println("Job switching to:: " + nextJob.getName());
		    return nextJob;
		}
	    else if(wait_queue.get((current_queue + 1) % 3).isEmpty() == false)
		{
				System.out.println("Queue #" + current_queue + " is empty, pulling from queue #" + ((current_queue + 1) % 3) + " instead");
		    nextJob = wait_queue.get((current_queue + 1) % 3).removeFirst();
		    System.out.println("Job switching to: " + nextJob.getName());
		    return nextJob;
		}
	    else if(wait_queue.get((current_queue + 2) % 3).isEmpty() == false)
		{
				System.out.println("Queue #" + current_queue + " is empty, pulling from queue #" + ((current_queue + 2) % 3) + " instead");
		    nextJob = wait_queue.get((current_queue + 2) % 3).removeFirst();
		    System.out.println("Job switching to: " + nextJob.getName());
		    return nextJob;
		}
            else
		{
		    System.out.println("\nALL QUEUES ARE EMPTY");
		    return null;
		}
        }
        
        /* 
         * The specified thread has received exclusive access, without using
         * waitForAccess() or nextThread(). Assert that no threads
         * are waiting for access. 
         */
        @Override
        public void acquire(KThread thread) {
            Lib.assertTrue(Machine.interrupt().disabled());
            
            
            // Assert that the wait queue is empty
            Lib.assertTrue(   wait_queue.get(0).isEmpty()
                           && wait_queue.get(1).isEmpty()
                           && wait_queue.get(2).isEmpty()
                           );
        }
        
        /*
         * Print the contents of the wait queue usually not used
         */
        @Override
        public void print() {
            Lib.assertTrue(Machine.interrupt().disabled());
            for(int i = 0; i < wait_queue.size(); ++i){
                for(int j = 0; j < wait_queue.get(i).size(); ++j){
                    System.out.println(wait_queue.get(i).get(j).getName());
                }
            }
        }
        
	//Wait Queue initialized
        public Vector<LinkedList<KThread>> wait_queue;
    }
}
