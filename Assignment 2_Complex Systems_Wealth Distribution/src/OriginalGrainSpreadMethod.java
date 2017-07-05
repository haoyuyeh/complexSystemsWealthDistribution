/**
 * this class defines how the initial grains of lands are generated
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

/**
 * @author haoyu
 *
 */
public class OriginalGrainSpreadMethod implements GrainSpreadMethod {

	/**
	 * depending on the variable percent_best_land, those lands defined as best
	 * land will contain max grain and spreading some grains to their neighbors
	 * at beginning
	 * 
	 * @param all_Location
	 * @param col
	 * @param row
	 */
	public void spreadGrains(Land[][] all_Location, int col, int row) {

		// defining "best land" in a random way
		for (int i = 0; i < col; i++) {
			for (int j = 0; j < row; j++) {
				if (randomInt(0, 100) <= Simulation.percent_best_land) {
					all_Location[i][j].max_grain_here = Simulation.MAX_GRAIN;
					all_Location[i][j].grains_here = Simulation.MAX_GRAIN;
				}
			}
		}

		// From those "best land", taking some grains (DIFFUSE_PERCENT) and
		// spreading them to the 8 neighbors
		for (int t = 0; t < 5; t++) {
			for (int i = 0; i < col; i++) {
				for (int j = 0; j < row; j++) {
					if (all_Location[i][j].max_grain_here != 0) {
						all_Location[i][j].grains_here =
								all_Location[i][j].max_grain_here;
						diffuseGrains(all_Location, i, j,
								Simulation.DIFFUSE_PERCENT);
					}
				}
			}
		}

		// each land will diffuse some more the grains to spread them over the
		// grid
		for (int t = 0; t < 10; t++) {
			for (int i = 0; i < col; i++) {
				for (int j = 0; j < row; j++) {
					diffuseGrains(all_Location, i, j,
							Simulation.DIFFUSE_PERCENT);
				}
			}
		}

		// after diffusion, set max_grain_here = grains_here
		for (int i = 0; i < col; i++) {
			for (int j = 0; j < row; j++) {
				all_Location[i][j].max_grain_here =
						(int) all_Location[i][j].grains_here;
				all_Location[i][j].grains_here =
						(int) all_Location[i][j].max_grain_here;
			}
		}
	}

	/**
	 * Add the value of grains to the 8 neighbors
	 * 
	 * @param all_Location
	 * @param col
	 * @param row
	 * @param grains
	 */
	public void addToNeighbors(Land[][] all_Location, int col, int row,
			double grains) {
		// Right,
		all_Location[MovingMethod.moveColRight(col, 1)][row].grains_here +=
				grains;
		// Left
		all_Location[MovingMethod.moveColLeft(col, 1)][row].grains_here +=
				grains;
		// Up
		all_Location[col][MovingMethod.moveRowUp(row, 1)].grains_here += grains;
		// Down
		all_Location[col][MovingMethod.moveRowDown(row, 1)].grains_here +=
				grains;
		// Up-Right
		all_Location[MovingMethod.moveColRight(col, 1)][MovingMethod
				.moveRowUp(row, 1)].grains_here += grains;
		// Up-Left
		all_Location[MovingMethod.moveColLeft(col, 1)][MovingMethod
				.moveRowUp(row, 1)].grains_here += grains;
		// Down_Right
		all_Location[MovingMethod.moveColRight(col, 1)][MovingMethod
				.moveRowDown(row, 1)].grains_here += grains;
		// Down-Left
		all_Location[MovingMethod.moveColLeft(col, 1)][MovingMethod
				.moveRowDown(row, 1)].grains_here += grains;
	}

	/**
	 * Calculate the diffused value depending on the percent value, remove those
	 * grains from the owner and report this value to the 8 neighbors
	 * 
	 * @param all_Location
	 * @param col
	 * @param row
	 * @param percent
	 */
	public void diffuseGrains(Land[][] all_Location, int col, int row,
			double percent) {
		double grains = (all_Location[col][row].grains_here) * (percent / 100);
		all_Location[col][row].grains_here -= grains;
		addToNeighbors(all_Location, col, row, ((1.0 * grains) / 8));
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
