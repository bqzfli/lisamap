package srs.Utility;

public interface IXMLPersist {

	/** 
	 从XML节点中读取属性创建对象
	 
	 @param node 保存对象属性的XML节点
	*/
	void LoadXMLData(org.dom4j.Element node);

	/** 
	 将对象的属性保存到XML节点中
	 
	 @param node 保存对象属性的XML节点
	*/
	void SaveXMLData(org.dom4j.Element node);

}
