package srs.Geometry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import srs.Utility.IXMLPersist;
import srs.Utility.sRSException;



public class XmlFunction
{
	/** 
	 保存Geometry的XML数据
	 
	*/
	public static void SaveGeometryXML(org.dom4j.Element node, IGeometry geo)
	{
		if (geo != null)
		{
			AppendAttribute(node, "Type", geo.getClass().getName());
			((Geometry)((geo instanceof Geometry) ? geo : null)).SaveXMLData(node);
		}
	}

	/** 
	 将XML数据转换为Geometry
	 
	 @param node
	 * @throws ClassNotFoundException 
	 * @throws sRSException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	*/
	public static IGeometry LoadGeometryXML(org.dom4j.Element node) throws ClassNotFoundException, sRSException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		if(node.attributeValue("Type")==null)
		{
			return null;
		}

		Class<IGeometry> type = IGeometry.class;
		String nameSpace = type.getPackage().getName();
		
		Class<?> geoType = Class.forName(nameSpace + "" + node.attributeValue("Type"));
		if (geoType == null)
		{
			throw new sRSException("00300001");
		}

		Constructor<?> cons=geoType.getConstructor(null);
		IGeometry geo = (IGeometry)cons.newInstance(null);
		((IXMLPersist)geo).LoadXMLData(node);
//		((IXMLPersist)((geo instanceof IXMLPersist) ? geo : null)).LoadXMLData(node);
		return geo;
		
		
		
		
		
		
		
	}


	/** 
	 给节点添加属性
	 
	 @param node 需要添加属性的节点
	 @param name 属性名称
	 @param value 属性值
	*/
	public static void AppendAttribute(org.dom4j.Element node, String name, String value)
	{
		node.addAttribute(name, value);
	}
}