/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	//
	final int NUMBER_OF_CHOPSTICKS;
	//if someone is talking, isTalking == true
	public boolean isTalking;
	
	//inner class for chopstick
	private class Chopstick{
		
		private boolean isAvailable;
		
		public Chopstick() {
			isAvailable = true;
		}
	}
	
	private Chopstick chopsticks[];
	final public String DEBUG = "\\tDEBUG -- ";


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		//the number of chopsticks and philosophers are indentical
		NUMBER_OF_CHOPSTICKS = piNumberOfPhilosophers;
		//initialize an array of chopsticks with the number of philosophers
		chopsticks = new Chopstick[NUMBER_OF_CHOPSTICKS];
		for (int i = 0; i < NUMBER_OF_CHOPSTICKS; i++) {
			chopsticks[i] = new Chopstick();
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		//continuously check if the two chopsticks are available
		while(true) {
//			System.out.println(DEBUG + "Philosopher " + piTID + " is trying to pick up two chopsticks");
			//if any of the chopstick is unavailable, wait...
			if(!chopsticks[piTID-1].isAvailable || !chopsticks[piTID%NUMBER_OF_CHOPSTICKS].isAvailable) {
				try {
//					System.out.println(DEBUG + "chopstick " + (piTID-1) + ": " + chopsticks[piTID-1].isAvailable);
//					System.out.println(DEBUG + "chopstick " + (piTID%NUMBER_OF_CHOPSTICKS) + ": " + chopsticks[piTID%NUMBER_OF_CHOPSTICKS].isAvailable);
//					System.out.println(DEBUG + "Philosopher " + piTID + " has to wait...");
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//else, set the status of these two chopsticks to unavailable, and break the while loop to eat
			else {
//				System.out.println();
//				System.out.println(DEBUG + "The philosopher has obtain the chopsticks successfully, will make the two unavailable");
				chopsticks[(piTID-1)%NUMBER_OF_CHOPSTICKS].isAvailable = false;
				chopsticks[piTID%NUMBER_OF_CHOPSTICKS].isAvailable = false;
//				System.out.println(DEBUG + "chopstick " + (piTID-1) + ": " + chopsticks[piTID-1].isAvailable);
//				System.out.println(DEBUG + "chopstick " + (piTID%NUMBER_OF_CHOPSTICKS) + ": " + chopsticks[piTID%NUMBER_OF_CHOPSTICKS].isAvailable);
				break;
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		//set the two chopsticks to be available
		chopsticks[piTID-1].isAvailable = true;
		chopsticks[piTID%NUMBER_OF_CHOPSTICKS].isAvailable = true;
		//notify other philosophers
		//TODO check if it is possible to notify the two beside only
		notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		//while someone is talking, wait and check again...
		while(isTalking) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//when a philosopher finally finish, or nobody is talking
		//mark isTalking to be true to prevent shut up others
		isTalking = true;
		
		//no need to check if he is eating since a philosopher is a thread
		//a thread cannot be eating while talking
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		//notify all other philosophers
		isTalking = false;
		notifyAll();
	}
}

// EOF
