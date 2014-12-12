package nachos.threads;

import java.util.*;
import nachos.machine.Machine;

public class MultilevelQueueSchedulerTest {

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

/*
*
*  Determines a random amount of time before job is interrupted  
*
*/    
    @Override
    public void run() {

      while(!terminated){
        Random rand = new Random();
        int range = 2701; //max value is 2701 to avoid super lengthy waits
        
        int interrupt_value = rand.nextInt(range - 300) + 300; //minimum value is 300
        System.out.println("Job will interrupt after: " + interrupt_value + " ticks");
        System.out.println();        

        Machine.interrupt().tick(interrupt_value);
        terminate();
				System.out.println("Job #" + id + "has finished, WOOHOOOOOO!!!");
      }

   }
  }

/*
*
* Makes and initializes workers and threads, starts jobs, ends when jobs are finished.
*
*/

  public static void runProject() {

  System.out.println("___________________________________");
  System.out.println("|                                 |");
  System.out.println("| MULTILEVELQUEUESCHEDULER START  |");
  System.out.println("|                                 |");
  System.out.println("|_________________________________|");
  System.out.println();

  //Array of workers
  ThreadTask[] workers;
  workers = new ThreadTask[15];

  //Array of threads
  KThread[] threads;
  threads = new KThread[15];

  //Initializes workers and threads
  for(int i=0; i < 15; ++i){
    workers[i] = new ThreadTask(i);
    threads[i] = new KThread(workers[i]);
    threads[i].setName(Integer.toString(i));
  }

  //Fork threads to the queue
  for(int i=0; i < 15; ++i){
    workers[i].forkTime = Machine.timer().getTime();
    threads[i].fork();
  }

  //Alarm
  ThreadedKernel.alarm.waitUntil(5000); //Each thread now limited to 5000 ticks

  //Joins threads
  for(int i=0; i < 15; ++i){
    threads[i].join();
  }

  System.out.println();
  System.out.println("___________________________________");
  System.out.println("|                                 |");
  System.out.println("| MULTILEVELQUEUESCHEDULER FINISH |");
  System.out.println("|                                 |");
  System.out.println("|_________________________________|");



 }
}
