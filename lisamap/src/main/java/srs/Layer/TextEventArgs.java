package srs.Layer;

import java.util.EventObject;

@SuppressWarnings("serial")
public class TextEventArgs extends EventObject {

	/**事件参数中的文本信息
	 * 
	 */
	private String _text; 

    /**事件参数中的文本信息
     * @return
     */
    public String Text(){
        return _text;
    }

    /**无参数构造函数，文本内容为
     * 
     */
    public TextEventArgs(){
    	super(new Object());
        _text = "";
    }

    /** 有参数构造函数
     * @param text 文本内容
     */
    public TextEventArgs(String text){
    	super(new Object());
        _text = text;
    }
	
}
