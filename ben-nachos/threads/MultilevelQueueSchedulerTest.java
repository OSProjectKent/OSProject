package nachos.threads;

import java.util.*;
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

	public LinkedList<String> arrival;
	public LinkedList<String> terminate;
      
  public static class ThreadTask implements Runnable {
    
    public final int id;
    public boolean terminated;
		public long constructTime;
		public long forkTime;
		public long terminatedTime;
		
		public Random	r = new Random();
//  public boolean io;

    public ThreadTask(int id) {
      this.id = id;
      this.terminated = false;
			this.constructTime = Machine.timer().getTime();
			System.out.println("Job #" + id + " has been created");
    } 

    public void terminate() {
			this.terminatedTime = Machine.timer().getTime();
      this.terminated = true;
    }
    
    @Override
    public void run() {
//			System.out.println("### run()");
      // Do stuff here

      //print thread name 
//      System.out.print("thread " + KThread.currentThread().getName() + " started");
//			System.out.println(" | " + Machine.timer().getTime());

      // Do 800 'ticks' of computation
      while(!terminated) {
			  int randomNum = r.nextInt((2400) + 1) + 300;
				System.out.println("Job #" + id + " has " + randomNum + " computation ticks");
        Machine.interrupt().tick(randomNum);
				terminate();
      }

      
    }
  }

  public static void run_Project() {
    System.out.println("\n### Running MLQS project ###");
		System.out.println();

		System.out.println("### create jobs ###");
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
    thread1.setName("1");
    thread2.setName("2");
    thread3.setName("3");
    thread4.setName("4");
    thread5.setName("5");
    thread6.setName("6");
    thread7.setName("7");
    thread8.setName("8");
    thread9.setName("9");

    // start running the threads

		System.out.println("\n");
		System.out.println("### move jobs to queue ###");
		worker1.forkTime = Machine.timer().getTime();
    thread1.fork();
		worker2.forkTime = Machine.timer().getTime();
    thread2.fork();
		worker3.forkTime = Machine.timer().getTime();
	  thread3.fork();
 		worker4.forkTime = Machine.timer().getTime();
   	thread4.fork();
 		worker5.forkTime = Machine.timer().getTime();
   	thread5.fork();
 		worker6.forkTime = Machine.timer().getTime();
   	thread6.fork();
 		worker7.forkTime = Machine.timer().getTime();
   	thread7.fork();
 		worker8.forkTime = Machine.timer().getTime();
   	thread8.fork();
 		worker9.forkTime = Machine.timer().getTime();
   	thread9.fork();
		System.out.println("\n\n");


    // let the threads run for x ticks
    ThreadedKernel.alarm.waitUntil(5000);

    //kill threads
/*    worker1.terminate();
    worker2.terminate();
    worker3.terminate();
    worker4.terminate();
    worker5.terminate();
    worker6.terminate();
    worker7.terminate();
    worker8.terminate();
    worker9.terminate(); */

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
