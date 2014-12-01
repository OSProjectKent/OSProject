package nachos.threads;

import nachos.machine.Machine;

public class MultilevelQueueSchedulerTest {

  public static void runProject() {
    System.out.println("#############################################");
    System.out.println("## MultilevelQueueScheduler project begins ##");
    System.out.println("#############################################");

    //run the project
    run_Project();

    System.out.println("###########################################");
    System.out.println("## MultilevelQueueScheduler project ends ##");
    System.out.println("###########################################");
  }
      
  private static class ThreadTask implements Runnable {
    
    private final int id;
    private boolean terminated;
//  private boolean io;

    public ThreadTask(int id) {
      this.id = id;
      this.terminated = false;
    } 

    public void terminate() {
      this.terminated = true;
    }
    
    @Override
    public void run() {
      System.out.println("MultilevelQueueSchedulerTest.run()");
      // Do stuff here

      //print thread name 
      System.out.println("thread " + KThread.currentThread().getName() + " started");

      // Do 800 'ticks' of computation
      while(!terminated) {
        Machine.interrupt().tick(800);
      }

      
    }
  }

  private static void run_Project() {
    System.out.println("\n### Running MLQS project ###");

    // create workers
    ThreadTask worker1 = new ThreadTask(1);
    ThreadTask worker2 = new ThreadTask(2);
    ThreadTask worker3 = new ThreadTask(3);
    ThreadTask worker4 = new ThreadTask(4);
    ThreadTask worker5 = new ThreadTask(5);
    ThreadTask worker6 = new ThreadTask(6);
    ThreadTask worker7 = new ThreadTask(7);
    ThreadTask worker8 = new ThreadTask(8);
    ThreadTask worker9 = new ThreadTask(9);

    // initialize new threads to run workers
    KThread thread1 = new KThread(worker1);
    KThread thread2 = new KThread(worker2);
    KThread thread3 = new KThread(worker3);
    KThread thread4 = new KThread(worker4);
    KThread thread5 = new KThread(worker5);
    KThread thread6 = new KThread(worker6);
    KThread thread7 = new KThread(worker7);
    KThread thread8 = new KThread(worker8);
    KThread thread9 = new KThread(worker9);

    // name the threads
    thread1.setName("KThread - 1");
    thread2.setName("KThread - 2");
    thread3.setName("KThread - 3");
    thread4.setName("KThread - 4");
    thread5.setName("KThread - 5");
    thread6.setName("KThread - 6");
    thread7.setName("KThread - 7");
    thread8.setName("KThread - 8");
    thread9.setName("KThread - 9");

    // start running the threads
    thread1.fork();
    thread2.fork();
    thread3.fork();
    thread4.fork();
    thread5.fork();
    thread6.fork();
    thread7.fork();
    thread8.fork();
    thread9.fork();

    // let the threads run for x ticks
    ThreadedKernel.alarm.waitUntil(5000);

    //kill threads
    worker1.terminate();
    worker2.terminate();
    worker3.terminate();
    worker4.terminate();
    worker5.terminate();
    worker6.terminate();
    worker7.terminate();
    worker8.terminate();
    worker9.terminate();

    //wait until all threads are done
    thread1.join();
    thread2.join();
    thread3.join();
    thread4.join();
    thread5.join();
    thread6.join();
    thread7.join();
    thread8.join();
    thread9.join();

   //fine
    System.out.println("\n### Finished MLQS project ###");
  }
}