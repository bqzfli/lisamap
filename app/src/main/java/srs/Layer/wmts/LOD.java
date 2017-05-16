package srs.Layer.wmts;

import srs.Geometry.IPoint;

/**Level Of Detail
 * @author 李忠义
 *20120313
 */
public class LOD {


	/**每个切片的高度 */
	public int Height = 254;


	/**每个切片的宽度 */
	public int Width = 254;

	/** 级别 */
	public int Level;

	/*** 分辨率 */
	public double Resolution;

	/** * 比例尺 */
	public double ScaleDenominator;

	/*** 地图链接地址 */
	public String Url;

	/**本层切片数据的起始位置 */
	public IPoint Origin = null; 

	public LOD(){}

	/**
	 * @param level 级别
	 * @param resolution 分辨率
	 * @param scale 比例尺
	 * @param url 地图链接地址
	 */
	public LOD(int level,double resolution,double scale,String url){
		this.Level=level;
		this.Resolution=resolution;
		this.ScaleDenominator=scale;
		this.Url=url;
	}

	/**
	 * @param level 级别
	 * @param resolution 分辨率
	 * @param scale 比例尺
	 */
	public LOD(int level,double resolution,double scale){
		this.Level=level;
		this.Resolution=resolution;
		this.ScaleDenominator=scale;
	}

}
