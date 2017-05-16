package srs.DataSource.DB.bean;
/**
* @ClassName: KeyValueBean
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午2:48:05
***********************************
* @editor lisa 
* @data 2016年12月25日 下午2:48:05
* @todo TODO
*/
public class KeyValueBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private String foreignKey;// 澶栭敭key锛岀敤浜庤仈鍔�
	private int type;// 閿�肩被鍨�
	private String fieldset;// 閿�肩被鍨�

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getKey() {
		return key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public KeyValueBean(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public KeyValueBean(String key, String value,String fieldset) {
	    this.key = key;
	    this.value = value;
	    this.fieldset = fieldset;
	}

	public KeyValueBean(String key, String value, String foreignKey, int type) {
		this.key = key;
		this.value = value;
		this.foreignKey = foreignKey;
		this.type = type;
	}

	public String getFieldset() {
		return fieldset;
	}

	public void setFieldset(String fieldset) {
		this.fieldset = fieldset;
	}

	public KeyValueBean() {

	}
}
