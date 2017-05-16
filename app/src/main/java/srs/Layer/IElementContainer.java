package srs.Layer;

import java.io.IOException;
import java.util.List;

import srs.Display.FromMapPointDelegate;
import srs.Element.IElement;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Layer.Event.ElementManager;


//C# TO JAVA CONVERTER TODO TASK: Delegates are not available in Java:
//public delegate void ElementEventHandler();

public interface IElementContainer{
	int getElementCount();
	List<IElement> getElements();
	List<Integer> getSelectedElements();
	void setSelectedElements(List<Integer> value);
	void setSelectedElement(Integer value);
	IEnvelope getExtent() throws IOException;
	ElementManager getElementChanged();
	ElementManager getElementSelectedChanged();

	void AddElement(IElement element) throws IOException;
	void AddElements(IElement[] elements) throws IOException;
	void AddElements(List<IElement> elements) throws IOException;
//	void AddGPS(IElement element) throws IOException;
	void RemoveElementsE(List<IElement> elements) throws IOException;
	void RemoveElementsI(List<Integer> ids) throws IOException;
	void RemoveElement(IElement elements) throws IOException;
	void RemoveElement(Integer id) throws IOException;
//	void RemoveGPS() throws IOException;
	void ClearElement() throws IOException;
	void ClearSelectedElement();
	List<Integer> Select(IGeometry geometry, boolean isMulti) throws IOException;
	List<Integer> SelectPoint(IGeometry geometry, boolean b) throws IOException;

	void Refresh();
	void Refresh(FromMapPointDelegate Delegate);
	
	void dispose() throws Exception;
}