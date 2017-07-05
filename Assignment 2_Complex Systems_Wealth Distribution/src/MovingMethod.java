/**
 * this class defines some functions helping people traveling over the grids
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

/**
 * @author haoyu
 *
 */
public class MovingMethod {
	// Size of the grid
	public static int NUM_COLUMNS;
	public static int NUM_ROWS;
	
	/**
	 * initializing variables
	 */
	public MovingMethod() {
		MovingMethod.NUM_COLUMNS = 0;
		MovingMethod.NUM_ROWS = 0;
	}

	/**
	 * the following functions have the same behavior which are used to help
	 * looking for the next location and diffusing the grains on each location
	 * 
	 * when moving, it will not go outside the boundary for example, when
	 * reaching the extreme right will move to the starting left. same behavior
	 * for going left, up and down
	 * 
	 * @param col
	 * @param row
	 *            represent the current location
	 * @param steps
	 *            represent how many distances it tries to move
	 * 
	 */
	public static int moveColRight(int col, int steps) {
		if ((col + steps) >= NUM_COLUMNS) {
			return moveColRight(0, ((col + steps) - NUM_COLUMNS));
		}
		return (col + steps);
	}

	public static int moveColLeft(int col, int steps) {
		if ((col - steps) < 0) {
			return moveColLeft(NUM_COLUMNS, (steps - col));
		}
		return (col - steps);
	}

	public static int moveRowDown(int row, int steps) {
		if ((row + steps) >= NUM_ROWS) {
			return moveRowDown(0, ((row + steps) - NUM_ROWS));
		}
		return (row + steps);
	}

	public static int moveRowUp(int row, int steps) {
		if ((row - steps) < 0) {
			return moveRowUp(NUM_ROWS, (steps - row));
		}
		return (row - steps);
	}
}
