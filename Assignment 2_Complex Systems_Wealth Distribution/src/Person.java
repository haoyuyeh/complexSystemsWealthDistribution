/**
 * this class defines how and what a person should do in wealth distribution
 * model
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

public class Person {

	/**
	 * variables far all persons
	 */
	public static int life_expectancy_max = 100; // (1-100)
	public static int life_expectancy_min = 1; // (1-100)
	public static int metabolism_max = 10; // (1-25)
	public static int vision_max = 5; // (1-15)
	// current location
	public int col;
	public int row;

	/**
	 * Variables for individual persons
	 */
	// Current age of people.
	public int age;
	// Current amount of grains owned.
	public int wealth;
	// Until which age this person will live.
	public int life_expectancy;
	// How much grains this person needs to eat.
	public int metabolism;
	// How far this person can look for grains.
	public int vision;
	// p = poor, r = rich, m = middle
	public char myClass;

	/**
	 * initializing variables
	 */
	public Person() {
		set_variables();
		age = randomInt(0, life_expectancy);
	}

	/**
	 * using this when subclass don't need superclass's constructor
	 */
	public Person(String s) {

	}

	/**
	 * consuming wealth and reset if the person is dead
	 */
	public void Update() {
		wealth = wealth - metabolism;
		age = age + 1;
		// Checking if is Dead
		if (age >= life_expectancy || wealth < 0) {
			// Reset parameters
			set_variables();
		}
	}

	/**
	 * determining the best next location for a person depending on their vision
	 * 
	 * @param all_Location
	 */
	public void updateLocation(Land[][] all_Location) {
		int gRight = 0;
		int gLeft = 0;
		int gUp = 0;
		int gDown = 0;

		// calculating the total grains of four directions respectively based on
		// person's vision
		for (int i = 1; i <= vision; i++) {
			gRight += all_Location[MovingMethod.moveColRight(col,
					i)][row].grains_here;
			gLeft += all_Location[MovingMethod.moveColLeft(col,
					i)][row].grains_here;
			gUp += all_Location[col][MovingMethod.moveRowUp(row,
					i)].grains_here;
			gDown += all_Location[col][MovingMethod.moveRowDown(row,
					i)].grains_here;
		}

		// determining the next location
		if (gRight >= gLeft && gRight >= gUp && gRight >= gDown) {
			all_Location[MovingMethod.moveColRight(col, 1)][row]
					.addPerson(this);
			col = MovingMethod.moveColRight(col, 1);
			return;
		}
		if (gLeft >= gUp && gLeft >= gDown) {
			all_Location[MovingMethod.moveColLeft(col, 1)][row].addPerson(this);
			col = MovingMethod.moveColLeft(col, 1);
			return;
		}
		if (gUp >= gDown) {
			all_Location[col][MovingMethod.moveRowUp(row, 1)].addPerson(this);
			row = MovingMethod.moveRowUp(row, 1);
		} else {
			all_Location[col][MovingMethod.moveRowDown(row, 1)].addPerson(this);
			row = MovingMethod.moveRowDown(row, 1);
		}
	}

	/**
	 * reset parameters
	 */
	public void set_variables() {
		age = 0;
		life_expectancy = randomInt(life_expectancy_min, life_expectancy_max);
		metabolism = randomInt(1, metabolism_max);
		wealth = randomInt(metabolism, 50);
		vision = randomInt(1, vision_max);
	}

	/**
	 * deciding the class of each person, depending on his current wealth
	 * 
	 * @param all_People
	 */
	public static void updateClass(Person[] all_People) {

		// getting the wealthiest person
		int max_wealth_now = 0;
		for (Person p : all_People) {
			if (max_wealth_now < p.wealth) {
				max_wealth_now = p.wealth;
			}
		}

		/**
		 * if the person owns less then 1/3 of the wealthiest one then is poor
		 * if the person owns between 1/3 and 2/3 of the wealthiest is middle if
		 * the person owns more than 2/3 of the wealthiest is rich
		 */
		for (Person p : all_People) {
			if (p.wealth <= max_wealth_now / 3) {
				p.myClass = 'p';
			} else {
				if (p.wealth <= max_wealth_now * 2 / 3) {
					p.myClass = 'm';
				} else {
					p.myClass = 'r';
				}
			}
		}
	}

	/**
	 * updating the wealth of a person
	 * 
	 * @param wealth_optained
	 */
	public void add_wealth(int wealth_optained) {
		wealth += wealth_optained;
	}

	/**
	 * Return a random int between min and max including both
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public int randomInt(int min, int max) {
		int range = max - min;
		return (int) (Math.random() * (range + 1) + min);
	}
}
