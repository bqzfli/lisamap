package srs.DataSource.Vector;

import srs.Geometry.IEnvelope;
import srs.Geometry.srsGeometryType;

/** 文件头信息结构 */
public final class ShpHeader{
	public int recordCount;
	public srsGeometryType geoType;
	public IEnvelope envelope;

	public ShpHeader clone(){
		ShpHeader varCopy = new ShpHeader();
		varCopy.recordCount = this.recordCount;
		varCopy.geoType = this.geoType;
		varCopy.envelope = this.envelope;
		return varCopy;
	}
	
	public void dispose() throws Exception {
		envelope.dispose();
	}
}
