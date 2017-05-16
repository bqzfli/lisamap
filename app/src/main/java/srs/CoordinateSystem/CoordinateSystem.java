/**
 * 
 */
package srs.CoordinateSystem;

import org.gdal.osr.SpatialReference;

import srs.Utility.unitType;

/**坐标系
 * @author bqzf
 * @version 20150606
 * 
 */
public abstract class CoordinateSystem implements ICoordinateSystem {

	/** 空间参照系*/
	public SpatialReference mSpatialReference;
	/** 名称*/
	protected String mName;
	/** 单位*/
	protected String mUnit;

	/**
	 * 构造函数
	 * @param wkt
	 */
	public CoordinateSystem(String wkt) {
		mSpatialReference = new SpatialReference(wkt);
		InterpretSpatialReference();
	}

	public CoordinateSystem() {
		this("");
	}

	/*
	 * (non-Javadoc)名称
	 * @see CoordinateSystem.ICoordinateSystem#Name() 名称
	 */
	@Override
	public String getName() {
		return mName;
	}

	/*
	 * (non-Javadoc)名称
	 * @see CoordinateSystem.ICoordinateSystem#Name(java.lang.String) 名称
	 */
	@Override
	public void setName(String value) {
		mName = value;
	}

	/*
	 * (non-Javadoc) 单位
	 * @see CoordinateSystem.ICoordinateSystem#Unit() 单位
	 */
	@Override
	public unitType Unit() {
		if (mUnit.equals("Meter")) {
			return unitType.Meter;
		} else if (mUnit.equals("Degree")) {
			return unitType.Degree;
		} else {
			return unitType.Unknown;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * CoordinateSystem.ICoordinateSystem#isSame(CoordinateSystem.ICoordinateSystem
	 * )
	 */
	@Override
	public boolean isSame(ICoordinateSystem other) {
		SpatialReference spatialReference = new SpatialReference(
				other.ExportToWKT());
		return mSpatialReference.IsSame(spatialReference) == 1 ? true : false;
	}

	/*
	 * (non-Javadoc) 导出到WKT
	 * @see CoordinateSystem.ICoordinateSystem#ExportToWKT() 导出到WKT
	 */
	@Override
	public String ExportToWKT() {
		String argout = mSpatialReference.ExportToPrettyWkt(0);
		return argout;
	}

	protected abstract void InterpretSpatialReference();

}
