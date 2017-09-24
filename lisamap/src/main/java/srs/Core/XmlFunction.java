package srs.Core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.Symbol;
import srs.Element.IElement;
import srs.Geometry.IGeometry;
import srs.Layer.ILayer;
import srs.Map.IMap;
import srs.Rendering.IRenderer;
import srs.Rendering.Renderer;
import srs.Utility.IXMLPersist;
import srs.Utility.sRSException;


/** xml相关信息读、写、解析
 * @author bqzf
 * @version 20150606
 *
 */
public class XmlFunction{
	/** 
	 保存Symbol的XML数据
	 @param node
	 @param symbol
	 */
	public static void SaveSymbolXML(org.dom4j.Element node, ISymbol symbol){
		if (symbol != null){
			AppendAttribute(node, "Type", symbol.getClass().getName());
			((Symbol)((symbol instanceof Symbol) ? symbol : null)).SaveXMLData(node);
		}
	}

	/** 
	 将XML数据转换为Symbol

	 @param node
	 @return 
	 * @throws sRSException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws ClassNotFoundException 
	 */
	public static ISymbol LoadSymbolXML(org.dom4j.Element node)
			throws sRSException,
			SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, 
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException{
		if (node.attributeValue("Type") == null){
			return null;
		}

		Class<?> type = Symbol.class;
		String nameSpace = type.getPackage().getName();

		Class<?> symbolType = Class.forName(nameSpace + "." + node.attributeValue("Type"));
		if (symbolType == null){
			throw new sRSException("Symbol设置错误！");
		}

		Constructor<?> cons=symbolType.getConstructor(null);
		Symbol symbol = (Symbol)cons.newInstance(null);
		symbol.LoadXMLData(node);
		return symbol;
	}

	/** 
	 保存XML数据

	 @param node
	 @param renderer
	 */
	public static void SaveRendererXML(org.dom4j.Element node, 
			IRenderer renderer){
		if (renderer != null){
			AppendAttribute(node, "Type", renderer.getClass().getName());
			((Renderer)((renderer instanceof Renderer) ? renderer : null)).SaveXMLData(node);
		}
	}

	/** 
	 将XML数据转换为Renderer

	 @param node
	 @return 
	 * @throws ClassNotFoundException 
	 * @throws sRSException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public static IRenderer LoadRendererXML(org.dom4j.Element node) 
			throws ClassNotFoundException, 
			sRSException, SecurityException, 
			NoSuchMethodException, 
			IllegalArgumentException,
			InstantiationException,
			IllegalAccessException,
			InvocationTargetException{
		if (node.attributeValue("Type") == null){
			return null;
		}

		Class<?> type = Renderer.class;
		String nameSpace = type.getPackage().getName();
		Class<?> rendererType = Class.forName(nameSpace + "." + node.attributeValue("Type"));
		if (rendererType == null){
			throw new sRSException("Renderer设置错误！");
		}

		Constructor<?> cons=rendererType.getConstructor(null);
		Renderer renderer = (Renderer)cons.newInstance(null);
		renderer.LoadXMLData(node);
		return renderer;
	}

	/** 
	 保存Layer的XML数据

	 @param node
	 @param layer
	 */
	public static void SaveLayerXML(org.dom4j.Element node, 
			ILayer layer){
		if (layer != null){
			AppendAttribute(node, "Type", layer.getClass().getName());
			((IXMLPersist)((layer instanceof IXMLPersist) ? layer : null)).SaveXMLData(node);
		}
	}

	/** 
	 将XML数据转换为Layer

	 @param node
	 @return 
	 * @throws ClassNotFoundException 
	 * @throws sRSException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public static ILayer LoadLayerXML(org.dom4j.Element node) 
			throws ClassNotFoundException,
			sRSException, SecurityException, 
			NoSuchMethodException, 
			IllegalArgumentException, 
			InstantiationException,
			IllegalAccessException, 
			InvocationTargetException{
		if (node.attributeValue("Type") == null){
			return null;
		}

		Class<?> type = ILayer.class;
		String nameSpace = type.getPackage().getName();
		Class<?> layerType = Class.forName(nameSpace + "" + node.attributeValue("Type"));
		if (layerType == null){
			throw new sRSException("Symbol设置错误！");
		}

		Constructor<?> cons = layerType.getConstructor(null);
		ILayer layer = (ILayer)cons.newInstance(null);
		((IXMLPersist)((layer instanceof IXMLPersist) ? layer : null)).LoadXMLData(node);
		return layer;
	}

	/*	*//** 
	 保存ColorRamp的XML数据

	 @param node
	 @param colorRamp
	 *//*
	public static void SaveColorRampXML(org.dom4j.Element node, IColorRamp colorRamp){
		if (colorRamp != null){
			AppendAttribute(node, "Type", colorRamp.getClass().getName());
			(ColorRamp)((colorRamp instanceof ColorRamp) ? colorRamp : null).SaveXMLData(node);
		}
	}*/

	/** 
	 将XML数据转换为ColorRamp

	 @param node
	 *//*
	public static IColorRamp LoadColorRampXML(org.dom4j.Element node){
		if (node.Attributes["Type"] == null){
			return null;
		}

		java.lang.Class type = ColorRamp.class;
		Assembly assemb = type.Assembly;
		String nameSpace = type.Namespace;
		java.lang.Class rampType = assemb.GetType(nameSpace + "." + node.Attributes["Type"].getValue());
		if (rampType == null){
			throw new SRSException("颜色表设置错误！");
		}

		ColorRamp colorRamp = (ColorRamp)Activator.CreateInstance(rampType);
		colorRamp.LoadXMLData(node);
		return colorRamp;
	}*/

	/** 
	 保存MapSurround的XML数据
	 */
	public static void SaveElementXML(org.dom4j.Element node, 
			IElement element){
		if (element != null){
			AppendAttribute(node, "Type", element.getClass().getName());
			((srs.Element.Element)((element instanceof srs.Element.Element) ? element : null)).SaveXMLData(node);
		}
	}

	/** 
	 将XML数据转换为Element

	 @param node
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws sRSException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static IElement LoadElementXML(org.dom4j.Element node)
			throws SecurityException,
			IllegalArgumentException,
			ClassNotFoundException, 
			sRSException, NoSuchMethodException, 
			InstantiationException,
			IllegalAccessException, 
			InvocationTargetException{
		if (node.attributeValue("Type") == null){
			return null;
		}

		Class<?> type = srs.Element.Element.class;
		String nameSpace = type.getPackage().getName();
		Class<?> surType = Class.forName(nameSpace + "" + node.attributeValue("Type"));
		if (surType == null){
			throw new sRSException("Element错误！");
		}

		Constructor<?> cons=surType.getConstructor(null);
		srs.Element.Element element = (srs.Element.Element)cons.newInstance(null);
		element.LoadXMLData(node);
		return element;
	}

	/*	*//** 
	 保存MapSurround的XML数据

	 *//*
	public static void SaveMapSurroundXML(org.dom4j.Element node, IMapSurround mapSurround){
		if (mapSurround != null){
			AppendAttribute(node, "Type", mapSurround.getClass().getName());
			(MapSurround)((mapSurround instanceof MapSurround) ? mapSurround : null).SaveXMLData(node);
		}
	}
	  */
	
	/** 
	 将XML数据转换为MapSurround

	 @param node
	 *//*
	public static IMapSurround LoadMapSurroundXML(org.dom4j.Element node, IMap map){
		if (node.Attributes["Type"] == null){
			return null;
		}
		java.lang.Class type = MapSurround.class;
		Assembly assemb = type.Assembly;
		String nameSpace = type.Namespace;
		java.lang.Class surType = assemb.GetType(nameSpace + "." + node.Attributes["Type"].getValue());
		if (surType == null){
			throw new SRSException("MapSurround错误！");
		}
		MapSurround surround = (MapSurround)Activator.CreateInstance(surType);
		surround.setMap(map);
		surround.LoadXMLData(node);
		return surround;
	}*/

	/** 
	 给节点添加属性
	 @param node 需要添加属性的节点
	 @param name 属性名称
	 @param value 属性值
	 */
	public static void AppendAttribute(org.dom4j.Element node, 
			String name,
			String value){
		node.addAttribute(name, value);
	}
}
