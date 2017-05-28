package com.lisa.datamanager.wrap;

import org.dom4j.Element;
import srs.Display.Symbol.*;
import srs.Rendering.*;

public class XmlFunction {

	/**灏哫ML鏁版嵁杞崲涓篢askLayer
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public static TaskLayer LoadTaskLayerXML(Element node) throws Exception{
		if (node == null)
			return null;

		Class<?> type=Class.forName("com.lisa.datamanager.wrap.TaskLayer");
		if(type==null){
			throw new Exception("Renderer璁剧疆閿欒锛�");
		}
		String nameSapce=type.getPackage().getName();

		TaskLayer layer = (TaskLayer)type.newInstance();
		layer.LoadXMLData(node);
		return layer;
	}


	/**灏哫ML鏁版嵁杞崲涓篢askLayer
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

		Class<?> type = Class.forName("Rendering.Renderer");
		if(type==null){
			throw new Exception("Renderer璁剧疆閿欒锛�");
		}
		String name=type.getPackage().getName()+ "" +node.attributeValue("Type");
		Class<?> rendererType=Class.forName(name); 
		IRenderer renderer = (IRenderer)rendererType.newInstance();
		renderer.LoadXMLData(node);
		return renderer;
	}

	/**淇濆瓨XML鏁版嵁
	 * @param node
	 * @param renderer
	 */
	public static void SaveRendererXML(Element node, IRenderer renderer){
		if (renderer != null){
			AppendAttribute(node, "Type", renderer.getClass().getName());
			((IRenderer)renderer).SaveXMLData(node);
		}
	}

	/**缁欒妭鐐规坊鍔犲睘鎬�
	 * @param node 闇�瑕佹坊鍔犲睘鎬х殑鑺傜偣
	 * @param name 灞炴�у悕绉�
	 * @param value 灞炴�у��
	 */
	public static void AppendAttribute(Element node, String name, String value){
		node.addAttribute(name, value);
	}

	/**淇濆瓨Symbol鐨刋ML鏁版嵁
	 * @param node
	 * @param symbol
	 */
	public static void SaveSymbolXML(Element node, ISymbol symbol){
		if (symbol != null){
			AppendAttribute(node, "Type", symbol.getClass().getName());
			((Symbol)symbol).SaveXMLData(node);
		}
	}

	/**灏哫ML鏁版嵁杞崲涓篠ymbol
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
			throw new Exception("Symbol璁剧疆閿欒锛�");
		}

		Symbol symbol=(Symbol)symbolType.newInstance();
		symbol.LoadXMLData(node);
		return symbol;		
	}
}

