package com.lisa.datamanager.wrap;

import org.dom4j.Element;

public class TableStyleInfo {
	
	/**琛ㄥ紡鏂囦欢璺緞
	 * 
	 */
	public String TablePath = "";
	
	/**琛ㄥ紡鏂囦欢A琛ㄨ矾寰�
	 * 
	 */
	public String TableAPath = "";
	
	/**鍏抽敭瀛�
	 * 
	 */
	public String Keys = "";
	
	
	
	public void LoadXMLNode(Element node){
		
		if(node.getName().trim().equalsIgnoreCase("TableStyle")){
			TablePath=node.attributeValue("PathName");
			TableAPath=node.attributeValue("PathA");
			Keys=node.attributeValue("CodeID");
		}
	}
}
