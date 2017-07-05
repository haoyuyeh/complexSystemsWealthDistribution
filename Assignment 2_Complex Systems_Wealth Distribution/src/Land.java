
/**
 * this class defines what a land needs and what a land does in the wealth
 * distribution model
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

import java.util.ArrayList;

public class Land {

	public static int grain_grow_interval = 2;// (1-10)
	public static int num_grain_grown = 10;// (1-10)
	// Amount of grains in this patch.
	public double grains_here = 0;
	// Max amount of grains this patch can hold.
	public int max_grain_here = 0;
	// List of the persons in the current location.
	public ArrayList<Person> list_persons;

	/**
	 * initializing variables
	 */
	public Land() {
		list_persons = new ArrayList<Person>();
	}

	/**
	 * initializing variables
	 * 
	 * @param max_grains
	 */
	public Land(int max_grains) {
		max_grain_here = max_grains;
		list_persons = new ArrayList<Person>();
	}

	/**
	 * for growing grains of the land if the land does not reach it's maximum
	 * amount of grain, adding num_grain_grown to its grain amount but no more
	 * then max_grain_here
	 */
	public void grow_grain() {
		//
		if (grains_here < max_grain_here) {
			grains_here = grains_here + num_grain_grown;
			if (grains_here > max_grain_here) {
				grains_here = max_grain_here;
			}
		}
	}

	/**
	 * distributing the grains_here in the land equally by the amount of people
	 * currently on it, if there is no people, the grains are not changed. after
	 * distributing, all people will be removed from list_persons
	 */
	public void harvest() {
		if (list_persons.size() > 0) {
			int wealthHarvest = (int) (grains_here / list_persons.size());
			for (Person p : list_persons) {
				p.add_wealth(wealthHarvest);
			}
			grains_here = 0;
			removeAllPersons();
		}
	}

	/**
	 * when person stops on the land, add him into list_persons
	 * 
	 * @param toAdd
	 */
	public void addPerson(Person toAdd) {
		// Add a person to this location
		list_persons.add(toAdd);
	}

	/**
	 * Remove all the people from this land
	 */
	public void removeAllPersons() {
		list_persons.clear();
	}
}
