package srs.DataSource.DB.bean;

import java.util.List;


/**
* @ClassName: BasicProperties
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月25日 下午2:46:12
***********************************
* @editor lisa
* @data 2016年12月25日 下午2:46:12
* @todo TODO
*/
public class BasicProperties {
	private static final long serialVersionUID = 1L;
	private String title;
	private String mark;// 姘村嵃鏍囧織
	private String str_NamePhoto;// 鍥剧墖鏂囦欢閲嶅懡鍚�

	private String type;

	private List<BasicProperties> action;// 鍦板浘鐨勬搷浣�
	private List<BasicProperties> table;// 鍦板浘鐨勬搷浣�
	private String name;
	private String prefix;// 鍓嶄竴涓〉闈㈡ā鍧�
	private boolean show;
	private String label;
	private String style;
	private boolean editable;
	private String input;
	private String inherit;// 0锛氫笉缁ф壙 1锛氱洿鎺ョ户鎵垮綋鍓嶅�� 2锛氬綋鍓嶅�煎姞1
	private String pfeild;// 缁ф壙瀛楁
	private String buttonlabel;
	private String fieldset;// 鍒嗙被鏄剧ず

	private List<Item> item;// 鍚湁涓嬫媺妗嗙殑鏁版嵁

	// 鐓х墖缁勪欢
	private boolean istake;// 鏄惁鍚媿鐓�
	private String maxsize;
	private List<Item> labels;// 鐓х墖鏍囬鏄剧ず鐨勫唴瀹�
	private String dir;// 璺緞
	private String filename;// 浠ュ摢涓睘鎬ф潵鍛藉悕
	private List<Item> watermark;// 姘村嵃
	private String feild;// 鑺傜偣涓哄浘鐗囧唴瀹圭殑鍞竴鍊肩殑瀹氫箟锛屽搴旇〃涓璓HOTO涓殑鍝釜瀛楁

	// 澶栧姞瀛楁 2015-08-05
	private String dklabelname;
	
	//add 2016-01-06
	private String dictable; //鍒楄〃鏁版嵁瀵瑰簲鐨勮〃
	private String filter; //鍒楄〃鏁版嵁鑾峰彇鏉′欢
	//add 2016-01-13
	private String repeat;//涓昏〃涓庡浠借〃鏄惁閮藉寘鍚瀛楁 0--娌℃湁  1--鏈�
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDklabelname() {
		return dklabelname;
	}

	public void setDklabelname(String dklabelname) {
		this.dklabelname = dklabelname;
	}

	public List<BasicProperties> getTable() {
		return table;
	}

	public void setTable(List<BasicProperties> table) {
		this.table = table;
	}

	public List<BasicProperties> getAction() {
		return action;
	}

	public void setAction(List<BasicProperties> action) {
		this.action = action;
	}

	public String getPfeild() {
		return pfeild;
	}

	public void setPfeild(String pfeild) {
		this.pfeild = pfeild;
	}

	public String getFeild() {
		return feild;
	}

	public void setFeild(String feild) {
		this.feild = feild;
	}

	public boolean isIstake() {
		return istake;
	}

	public void setIstake(boolean istake) {
		this.istake = istake;
	}

	public String getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(String maxsize) {
		this.maxsize = maxsize;
	}

	public List<Item> getLabels() {
		return labels;
	}

	public void setLabels(List<Item> labels) {
		this.labels = labels;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Item> getWatermark() {
		return watermark;
	}

	public void setWatermark(List<Item> watermark) {
		this.watermark = watermark;
	}

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getInherit() {
		return inherit;
	}

	public void setInherit(String inherit) {
		this.inherit = inherit;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getButtonlabel() {
		return buttonlabel;
	}

	public void setButtonlabel(String buttonlabel) {
		this.buttonlabel = buttonlabel;
	}

	public String getFieldset() {
		return fieldset;
	}

	public void setFieldset(String fieldset) {
		this.fieldset = fieldset;
	}

	public String getStr_NamePhoto() {
		return str_NamePhoto;
	}

	public void setStr_NamePhoto(String str_NamePhoto) {
		this.str_NamePhoto = str_NamePhoto;
	}

    public String getDictable() {
        return dictable;
    }

    public void setDictable(String dictable) {
        this.dictable = dictable;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
