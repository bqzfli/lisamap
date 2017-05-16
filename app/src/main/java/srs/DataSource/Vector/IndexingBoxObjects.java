package srs.DataSource.Vector;

import srs.Geometry.IEnvelope;

public final class IndexingBoxObjects{
	public IEnvelope Box;
	public int ID;

	public IndexingBoxObjects clone(){
		IndexingBoxObjects varCopy = new IndexingBoxObjects();
		varCopy.Box = this.Box;
		varCopy.ID = this.ID;
		return varCopy;
	}
}

