package com.lisa.datamanager.wrap;

import org.dom4j.Element;
import srs.Display.Symbol.*;
import srs.Rendering.*;

public class XmlFunction {

	/**����TASKLAYER�ڵ�
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public static TaskLayer LoadTaskLayerXML(Element node) throws Exception{
		if (node == null)
			return null;

		Class<?> type=Class.forName("com.lisa.datamanager.wrap.TaskLayer");
		if(type==null){
			throw new Exception("�����ļ�TaskLayer��������!");
		}
		String nameSapce=type.getPackage().getName();

		TaskLayer layer = (TaskLayer)type.newInstance();
		layer.LoadXMLData(node);
		return layer;
	}


	/**��XML����ת��ΪTaskLayer
	 * @param node
	 * @param layer
	 */
	public static void SaveTaskLayerXML(Element node, TaskLayer layer){
		if (layer != null){
			layer.SaveXMLData(node);
		}
	}

	public static IRenderer LoadRendererXML(Element node) throws Exception{
		if (node.attributeValue("Type") == null)
			return null;

		Class<?> type = Class.forName("srs.Rendering.Renderer");
		if(type==null){
			throw new Exception("Renderer????????");
		}
		String name=type.getPackage().getName()+ "" +node.attributeValue("Type");
		Class<?> rendererType=Class.forName(name); 
		IRenderer renderer = (IRenderer)rendererType.newInstance();
		renderer.LoadXMLData(node);
		return renderer;
	}

	/**����XML����
	 * @param node
	 * @param renderer
	 */
	public static void SaveRendererXML(Element node, IRenderer renderer){
		if (renderer != null){
			AppendAttribute(node, "Type", renderer.getClass().getName());
			((IRenderer)renderer).SaveXMLData(node);
		}
	}

	/**���ڵ������??
	 * @param node ??Ҫ������ԵĽڵ�
	 * @param name ��???��??
	 * @param value ��??????
	 */
	public static void AppendAttribute(Element node, String name, String value){
		node.addAttribute(name, value);
	}

	/**����Symbol��XML����
	 * @param node
	 * @param symbol
	 */
	public static void SaveSymbolXML(Element node, ISymbol symbol){
		if (symbol != null){
			AppendAttribute(node, "Type", symbol.getClass().getName());
			((Symbol)symbol).SaveXMLData(node);
		}
	}

	/**��XML����ת��ΪSymbol
	 * @param node
	 * @return
	 * @throws Exception 
	 */
	public static ISymbol LoadSymbolXML(Element node) throws Exception{
		if (node.attributeValue("Type") == null){
			return null;
		}

		Class<?> type=Class.forName("Display.Symbol.Symbol");
		String name=type.getPackage().getName()+ "" +node.attributeValue("Type");
		Class<?>  symbolType=Class.forName(name);
		if(symbolType==null){
			throw new Exception("Symbol���ô���??");
		}

		Symbol symbol=(Symbol)symbolType.newInstance();
		symbol.LoadXMLData(node);
		return symbol;		
	}
}

