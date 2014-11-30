package nachos.threads;

import java.util.Iterator;
import java.util.LinkedList;

import nachos.machine.Lib;
import nachos.machine.Machine;

/**
 * A round-robin scheduler tracks waiting threads in FIFO queues, implemented
 * with linked lists. When a thread begins waiting for access, it is appended to
 * the end of a list. The next thread to receive access is always the first
 * thread in the list. This causes access to be given on a first-come
 * first-serve basis.
 */
public class RoundRobinScheduler extends Scheduler {

  /**
   * Allocate a new round-robin scheduler.
   */
  public RoundRobinScheduler() {
      interrupt_interval_counter = 0;
      queue_num = 0;
  }
    private int interrupt_interval_counter;
    private int queue_num;

  /**
   * Allocate a new FIFO thread queue.
   * 
   * @param transferPriority
   *          ignored. Round robin schedulers have no priority.
   * @return a new FIFO thread queue.
   */
  @Override
  public ThreadQueue newThreadQueue(boolean transferPriority) {
    return new FifoQueue();
  }

  /**
   * Always yield at the end of the interrupt interval.
   * 
   * @return true if the current thread should yield
   */
    

  @Override
  public boolean timerInterrupt() {
      ++interrupt_interval_counter;
      if(interrupt_interval_counter == 8){ // Change "queues" every 8 interrupts
          queue_num = (queue_num + 1) % 3;
          interrupt_interval_counter = 0;
      }
      
      if(queue_num == 0){
          System.out.println("\nqueue #: " + queue_num);
          System.out.println("\nyielding at " + Machine.timer().getTime());
          return true; // Yield every 500 ticks for first queue
      }
      else if(queue_num == 1){
          if(interrupt_interval_counter % 2 == 0){
              System.out.println("\nqueue #: " + queue_num);
              System.out.println("\nyielding at " + Machine.timer().getTime());
              return true; // Yield every 1000 ticks for second queue
          }
          else {
              return false;
          }
      }
      else {
          System.out.println("\nqueue #: " + queue_num);
          return false; // Never yield for last queue; non-preemptive FCFS
      }
  }

  private class FifoQueue extends ThreadQueue {

    private LinkedList<KThread> waitQueue = new LinkedList<KThread>();

    /**
     * Add a thread to the end of the wait queue.
     * 
     * @param thread
     *          the thread to append to the queue.
     */
    @Override
    public void waitForAccess(KThread thread) {
      Lib.assertTrue(Machine.interrupt().disabled());

      waitQueue.add(thread);
    }

    /**
     * Remove a thread from the beginning of the queue.
     * 
     * @return the first thread on the queue, or <tt>null</tt> if the queue is
     *         empty.
     */
    @Override
    public KThread nextThread() {
      Lib.assertTrue(Machine.interrupt().disabled());

      if (waitQueue.isEmpty())
        return null;

      KThread next = waitQueue.removeFirst();
      // System.out.println("switched to " + next.getName());
      return next;
    }

    /**
     * The specified thread has received exclusive access, without using
     * <tt>waitForAccess()</tt> or <tt>nextThread()</tt>. Assert that no threads
     * are waiting for access.
     */
    @Override
    public void acquire(KThread thread) {
      Lib.assertTrue(Machine.interrupt().disabled());

      Lib.assertTrue(waitQueue.isEmpty());
    }

    /**
     * Print out the contents of the queue.
     */
    @Override
    public void print() {
      Lib.assertTrue(Machine.interrupt().disabled());

      for (Iterator<KThread> i = waitQueue.iterator(); i.hasNext();)
        System.out.print(i.next() + " ");
    }

  }

  public static void selfTest() {
    RoundRobinSchedulerTest.runTest();
  }

}
