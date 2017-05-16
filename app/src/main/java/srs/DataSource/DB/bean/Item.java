package srs.DataSource.DB.bean;
/**
* @ClassName: Item
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午2:46:12
***********************************
* @editor lisa
* @data 2016年12月25日 下午2:46:12
* @todo TODO
*/
public class Item {
	private String feild;
	private String label;
	private String fieldset;
	
	//add 2016-01-12
	private String pfeild;
	
	public String getFeild() {
		return feild;
	}
	public void setFeild(String feild) {
		this.feild = feild;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFieldset() {
		return fieldset;
	}
	public void setFieldset(String fieldset) {
		this.fieldset = fieldset;
	}
    public String getPfeild() {
        return pfeild;
    }
    public void setPfeild(String pfeild) {
        this.pfeild = pfeild;
    }
}
