/**
 * this class extends class Person and defines some features that can be
 * inherited by person
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

/**
 * @author haoyu
 *
 */
public class InheritablePerson extends Person {
	// defining how much percent of parents' feature will be inherited by child
	public static int inheritedRate = 50;
	// the feature a person inherits from their parents
	public Feature inheritedFeature;
	// using to equally distribute the amount of people of each feature
	private static int counter = 0;

	// features that can be inherited
	public enum Feature {
		METABOLISM, WEALTH, VISION
	};

	/**
	 * initializing variables
	 */
	public InheritablePerson() {
		// for not using superclass's constructor
		super("");
		inti();
	}

	/**
	 * initializing variables and deciding which feature the person inherits
	 */
	private void inti() {
		life_expectancy = randomInt(life_expectancy_min, life_expectancy_max);
		age = randomInt(0, life_expectancy);
		metabolism = randomInt(1, metabolism_max);
		wealth = randomInt(metabolism, 50);
		vision = randomInt(1, vision_max);
		switch (counter % Feature.values().length) {
		case 0:
			inheritedFeature = Feature.METABOLISM;
			break;
		case 1:
			inheritedFeature = Feature.WEALTH;
			break;
		case 2:
			inheritedFeature = Feature.VISION;
			break;
		default:
		}
		incrementCounter();
	}

	/**
	 * reset variables and based on inheritedRate to calculate the value of
	 * child's feature
	 */
	public void set_variables() {
		age = 0;
		life_expectancy = randomInt(life_expectancy_min, life_expectancy_max);
		double rate = (double) inheritedRate / 100.0;
		switch (inheritedFeature) {
		case METABOLISM:
			metabolism =
					(int) (metabolism * rate) + randomInt(1, metabolism_max);
			if (metabolism > metabolism_max) {
				metabolism = metabolism_max;
			}
			break;
		case WEALTH:
			wealth = (int) (wealth * rate) + randomInt(metabolism, 50);
			break;
		case VISION:
			vision = (int) (vision * rate) + randomInt(1, vision_max);
			if (vision > vision_max) {
				vision = vision_max;
			}
			break;
		default:
		}
	}

	/**
	 * increasing counter by 1, if counter is greater than 10000, set it to 0
	 */
	private void incrementCounter() {
		if (counter > 10000) {
			counter = 0;
		} else {
			counter++;
		}
	}
}
