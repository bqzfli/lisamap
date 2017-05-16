/**
 * 
 */
package srs.CoordinateSystem;

/**本初子午线
 * @author bqzf
 * @version 20150606
 */
public class PrimeMeridian {

	private String _Name;
	private double _Value;

	public PrimeMeridian(){
		this("未知",0);
	}

	public PrimeMeridian(String name, double value){
		_Name = name;
		_Value = value;
	}


	/**名称
	 * @return 名称
	 */
	public String getName(){
		return _Name; 
	}

	/** 名称
	 * @param value
	 */
	public void setName(String value){
		_Name = value; 
	}


	/** 中央子午线
	 * @return 中央子午线
	 */
	public double getValue(){
		return _Value;
	}

	/** 中央子午线
	 * @param value 中央子午线
	 */
	public void setValue(double value){
		_Value = value; 
	}


}
