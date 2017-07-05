/**
 * this interface defines method used to spread grains
 * 
 * group members: 782692 Hao Yu Yeh coding 
 * 				  741805 Zheng Chai 
 * 				  734507 Jiaying Li report
 */

/**
 * @author haoyu
 *
 */
public interface GrainSpreadMethod {
	/**
	 * a function used to define how to spread grains over the whole grids
	 * 
	 * @param all_Location
	 *            target lands
	 * @param col
	 * @param row
	 */
	public void spreadGrains(Land[][] all_Location, int col, int row);
}
