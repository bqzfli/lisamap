/*package com.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import srs.Display.Setting;
import srs.Element.Element;
import srs.Element.IElement;
import srs.Element.LineElement;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolyline;
import srs.Geometry.Part;
import srs.Geometry.Polyline;
import srs.Layer.ElementContainer;
import srs.tools.MapControl;

public class drawElementTest {

	public static MapControl MapC;
	public static List<IElement> mElements=null;
	
	public static void PartialRefresh(){
		if(MapC!=null){
			MapC.PartialRefresh();
		}
	}
	
	public static void removeAllElement(){
		mElements=new ArrayList<IElement>();
		if(MapC!=null){
			try {
				MapC.getElementContainer().ClearElement();
			} catch (IOException e) {
				e.printStackTrace();
			}
			MapC.PartialRefresh();
		}
	}
	
	public static void AddLine(IPoint[] points){
		if(points.length==2&&MapC!=null){
			IPolyline line=new Polyline(points);
			IElement element=new LineElement();
			((LineElement)element).setSymbol(Setting.ElementLineStyle);
			element.setGeometry(line);
			mElements.add(element);
			try {
				MapC.getElementContainer().AddElement(element);
			} catch (IOException e) {
				e.printStackTrace();
			}
			MapC.PartialRefresh();
		}
	}
	
}
*/