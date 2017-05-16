package srs.DataSource.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import srs.Geometry.IEnvelope;
import srs.Geometry.IRelationalOperator;
import srs.Geometry.ISpatialOperator;

public final class Indexing{
	private List<IndexingBoxObjects> mObjList;
	private IEnvelope mBox;
	private Indexing mChild0;
	private Indexing mChild1;

	/**释放资源
	 */
	public void dispose(){
		mObjList = null;
		mChild0.dispose();
		mChild0 = null;
		mChild1.dispose();
		mChild1 = null;
	}
	
	/** 
	 Determines whether the node is a leaf (if data is stored at the node, we assume the node is a leaf)
	 */
	public final boolean IsLeaf(){
		return (mObjList != null);
	}

	public static Indexing CreateSpatialIndex(IEnvelope[] env)
			throws IOException{
		if (env.length == 0)
			return null;
		List<IndexingBoxObjects> objList = new ArrayList<IndexingBoxObjects>();

		for (int i = 0; i < env.length; i++){
			if(env==null){
				//FIXME 因为env存在空值
				continue;
			}
			IndexingBoxObjects g = new IndexingBoxObjects();
			g.Box = env[i];
			g.ID = (int)i;
			objList.add(g.clone());
		}

		Heuristic heur = new Heuristic();
		heur.maxdepth = (int)Math.ceil(log(env.length, 2));
		heur.minerror = 10;
		heur.tartricnt = 5;
		heur.mintricnt = 2;

		return new Indexing(objList, 0, heur);
	}
	
	private static double log(double value, double base){
		return Math.log(value)/Math.log(base);
	}

	private Indexing(List<IndexingBoxObjects> objList, 
			int depth,
			Heuristic heurdata) throws IOException{
		mBox = objList.get(0).Box;
		for (int i = 0; i < objList.size(); i++){
			Object tempVar = ((ISpatialOperator)((mBox instanceof ISpatialOperator) ? mBox : null)).Union(objList.get(i).Box);
			mBox = (IEnvelope)((tempVar instanceof IEnvelope) ? tempVar : null);
		}

		// test our build heuristic - if passes, make children
		if (depth < heurdata.maxdepth && objList.size() > heurdata.mintricnt &&
				(objList.size() > heurdata.tartricnt
						|| ErrorMetric(mBox) > heurdata.minerror)){
			ArrayList<ArrayList<IndexingBoxObjects>> objBuckets = new ArrayList<ArrayList<IndexingBoxObjects>>(); // buckets of geometries
			objBuckets.add(new ArrayList<IndexingBoxObjects>());
			objBuckets.add(new ArrayList<IndexingBoxObjects>());

			boolean longaxis = ((mBox.UpperRight().X() - mBox.LowerLeft().X()) >= (mBox.UpperRight().Y() - mBox.LowerLeft().Y())) ? true : false;
			double geoavg = 0; // geometric average - midpoint of ALL the objects

			if (longaxis){
				// go through all bbox and calculate the average of the midpoints
				double frac = 1.0f / objList.size();
				for (int i = 0; i < objList.size(); i++)
					geoavg += objList.get(i).Box.CenterPoint().X() * frac;

				// bucket bbox based on their midpoint's side of the geo average in the longest axis
				for (int i = 0; i < objList.size(); i++)
					objBuckets.get(geoavg > objList.get(i).Box.CenterPoint().X() ? 1 : 0).add(objList.get(i));
			}else{
				double frac = 1.0f / objList.size();
				for (int i = 0; i < objList.size(); i++)
					geoavg += objList.get(i).Box.CenterPoint().Y() * frac;

				// bucket bbox based on their midpoint's side of the geo average in the longest axis
				for (int i = 0; i < objList.size(); i++)
					objBuckets.get(geoavg > objList.get(i).Box.CenterPoint().Y() ? 1 : 0).add(objList.get(i));
			}
			//If objects couldn't be splitted, just store them at the leaf
			//TODO: Try splitting on another axis
			if (objBuckets.get(0).size() == 0 
					|| objBuckets.get(1).size() == 0){
				mChild0 = null;
				mChild1 = null;
				// copy object list
				mObjList = objList;
			}else{
				// create new children using the buckets
				mChild0 = new Indexing(objBuckets.get(0), depth + 1, heurdata);
				mChild1 = new Indexing(objBuckets.get(1), depth + 1, heurdata);
			}
		}else{
			// otherwise the build heuristic failed, this is 
			// set the first child to null (identifies a leaf)
			mChild0 = null;
			mChild1 = null;
			// copy object list
			mObjList = objList;
		}
	}

	/** 
	 Calculate the floating point error metric 
	 
	 @return 
	*/
	private double ErrorMetric(IEnvelope box)
	{
		return (box.UpperRight().X() - box.LowerLeft().X() + 1) * (box.UpperRight().Y() - box.LowerLeft().Y() + 1);
	}

	/** 
	 Disposes the node
	 
	*/
	public final void Dispose()
	{
		//this.mBox = null;
		this.mChild0 = null;
		this.mChild1 = null;
		this.mObjList = null;
	}

	/** 
	 Searches the tree and looks for intersections with the boundingbox 'bbox'
	 
	 @param box Boundingbox to intersect with
	 * @throws IOException 
	*/
	public final  Collection<Integer> Search(IEnvelope box) 
			throws IOException{
		Collection<Integer> objectlist = new ArrayList<Integer>();
		IntersectTreeRecursive(box, this, objectlist);
		return objectlist;
	}

	/** 
	 Recursive function that traverses the tree and looks for intersections with a boundingbox
	 
	 @param box Boundingbox to intersect with
	 @param node Node to search from
	 @param list List of found intersections
	 * @throws IOException 
	*/
	private void IntersectTreeRecursive(
			IEnvelope box, 
			Indexing node, 
			Collection<Integer> list) throws IOException{
		if (node.IsLeaf()){
			for (int i = 0; i < node.mObjList.size();i++ )
				list.add(node.mObjList.get(i).ID);
		}else{
			if(((IRelationalOperator)((node.mBox instanceof IRelationalOperator) ? node.mBox : null)).
					Intersects(box)){
				IntersectTreeRecursive(box, node.mChild0, list);
				IntersectTreeRecursive(box, node.mChild1, list);
			}
		}
	}
}