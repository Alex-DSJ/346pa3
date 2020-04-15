/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		/*
		 * TODO:
		 * Should be settable from the command line
		 * or the default if no arguments supplied.
		 */
		int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
		try
		{
			if(argv.length == 0) {
				
				System.out.println("Default number of philosophers ( = 4 ) will be used.");
			}
			else {
				if(argv.length > 1) {
					System.out.println("Too many arguments, only the first one will be taken");
				}
				try {
					final int NUMBER_OF_PHILOSOPHERS = Integer.parseInt(argv[0]);
					if(NUMBER_OF_PHILOSOPHERS > 0) {
						iPhilosophers = NUMBER_OF_PHILOSOPHERS;
						System.out.println("Valid argument, the number of philosopher will be set by " + NUMBER_OF_PHILOSOPHERS + ".");
					}
					else {
						System.out.println("%java DiningPhilosophers " + argv[0]);
						System.out.println("\"" + argv[0] + "\" is not a positive decimal integer\n" );
						System.out.println("Usage: java Dining Philosopher[" + DEFAULT_NUMBER_OF_PHILOSOPHERS + "]\n%");
					}
				} catch (Exception e) {
					System.out.println("Invalid argument, the number of philosopher will be set to 4 by default.");
				}
			}

			

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
