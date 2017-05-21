package srs.DataSource.Vector;

/** 
 * Heuristics used for tree generation
 */
public final class Heuristic{
	/** 
	 Maximum tree depth
	 */
	public int maxdepth;
	/** 
	 Minimum object count at node
	 */
	public int mintricnt;
	/** 
	 Target object count at node
	 */
	public int tartricnt;
	/** 
	 Minimum Error metric ?the volume of a box + a unit cube.
	 The unit cube in the metric prevents big boxes that happen to be flat having a zero result and muddling things up.
	 */
	public int minerror;

	public Heuristic clone(){
		Heuristic varCopy = new Heuristic();

		varCopy.maxdepth = this.maxdepth;
		varCopy.mintricnt = this.mintricnt;
		varCopy.tartricnt = this.tartricnt;
		varCopy.minerror = this.minerror;

		return varCopy;
	}	
}