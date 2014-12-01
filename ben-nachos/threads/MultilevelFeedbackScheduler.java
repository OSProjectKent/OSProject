package nachos.threads;

import nachos.machine.Lib;
import nachos.machine.Machine;

/**
 * Implements MLFQ CPU scheduling for Nachos threads.
 */
public class MultilevelFeedbackScheduler extends Scheduler {

  /**
   * Initialize a new thread queue for managing access to the CPU using MLFQ.
   */
  @Override
  public ThreadQueue newThreadQueue(boolean transferPriority) {
    // you can ignore the transferPriority parameter for MLFQ
    return new MultilevelFeedbackQueue();
  }

  /**
   * Called every scheduling time slice. Decides whether the current thread
   * needs to yield (and performs any other required processing).
   * 
   * @return True if the current thread should yield
   */
  @Override
  public boolean timerInterrupt() {
    Lib.assertTrue(Machine.interrupt().disabled());

    // TODO

    return true;
  }

  /**
   * The ThreadQueue class is a general-purpose class for a thread of queues
   * waiting on some resource. In our case, this queue will manage access to the
   * CPU under MLFQ.
   */
  private static class MultilevelFeedbackQueue extends ThreadQueue {

    /**
     * Initialize the queue.
     */
    public MultilevelFeedbackQueue() {

      // TODO

    }

    /**
     * Called when a thread needs to be put into the waiting queue to acquire
     * the resource (in this case, the resource is the CPU).
     * 
     * @param thread
     *          The thread to insert into the queue.
     */
    @Override
    public void waitForAccess(KThread thread) {
      Lib.assertTrue(Machine.interrupt().disabled());

      // TODO

    }

    /**
     * Called when we need to select the next thread to acquire the resource
     * (which in this case, means getting to run on the CPU).
     * 
     * @return The next thread to schedule.
     */
    @Override
    public KThread nextThread() {
      Lib.assertTrue(Machine.interrupt().disabled());

      // TODO

      return null;
    }

    /**
     * Called when a thread can immediately take possession of the resource
     * (that is, without having to go through waitForAccess() first). In our
     * case, this means the thread will immediately be able to run on the CPU.
     * Depending on your implementation, you may or may not actually need to do
     * anything here.
     * 
     * @param thread
     *          The thread to run on the CPU.
     */
    @Override
    public void acquire(KThread thread) {
      Lib.assertTrue(Machine.interrupt().disabled());

      // TODO

    }

    /**
     * Print out the threads waiting on the CPU. Helpful in debugging.
     */
    @Override
    public void print() {
      Lib.assertTrue(Machine.interrupt().disabled());

      // TODO

    }

  }

  /**
   * Execute tests of the MLFQ scheduler. Don't modify this method; instead, put
   * your test code inside the MultilevelFeedbackSchedulerTest class.
   */
  public static void selfTest() {
    MultilevelFeedbackSchedulerTest.runTest();
  }

}
