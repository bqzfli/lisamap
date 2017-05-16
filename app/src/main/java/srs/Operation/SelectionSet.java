package srs.Operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;
import srs.Display.FromMapPointDelegate;
import srs.Display.IScreenDisplay;
import srs.Display.ScreenDisplay;
import srs.Display.Setting;
import srs.Element.FillElement;
import srs.Element.IFillElement;
import srs.Element.ILineElement;
import srs.Element.IPicElement;
import srs.Element.IPointElement;
import srs.Element.LineElement;
import srs.Element.PicElement;
import srs.Element.PointElement;
import srs.Geometry.IGeometry;


/** 选择集 */
public final class SelectionSet implements ISelectionSet{
	private IScreenDisplay _ScreenDisplay;
	private List<SelectedFeatures> _SelectedFeatures;

	/** 
	 构造函数

	 @param screenDisplay 屏幕绘图句柄
	 */
	public SelectionSet(IScreenDisplay screenDisplay){
		_ScreenDisplay = screenDisplay;
		_SelectedFeatures = new ArrayList<SelectedFeatures>();
	}

	/* (non-Javadoc)
	 * @see Operation.ISelectionSet#FeatureCount()
	 */
	public int getFeatureCount(){
		int sum = 0;
		for (SelectedFeatures s : _SelectedFeatures){
			sum += s.FIDs.size();
		}
		return sum;
	}

	/** 
	 选择集

	 */
	public SelectedFeatures[] getSelectedFeatures(){
		return _SelectedFeatures.toArray(new SelectedFeatures[]{});
	}

	/** 
	 向选择集添加记录

	 @param selectedFeatures 选择集
	 */
	public void AddFeatures(SelectedFeatures selectedFeatures){
		_SelectedFeatures.add(selectedFeatures.clone());
	}

	/** 
	 从选择集删除记录

	 @param sf
	 */
	public void RemoveSelectedFeatures(SelectedFeatures sf){
		_SelectedFeatures.remove(sf.clone());
	}

	/** 
	 清除选择集

	 */
	public void ClearSelection(){            
		_SelectedFeatures.clear();
	}

	/** 
	 刷新，绘制选择集中的记录
	 * @throws IOException 

	 */
	public void Refresh() throws IOException{
		//删除by 李忠义 20120306
		//    	 _ScreenDisplay.ResetPartCaches();
		Refresh(new FromMapPointDelegate((ScreenDisplay) _ScreenDisplay));
	}

	/** 
	 刷新，绘制选择集中的记录

	 @param Delegate 坐标转换委托
	 * @throws IOException 
	 */
	public void Refresh(FromMapPointDelegate Delegate) throws IOException{
		int xmove = 0;
		int ymove = 0;
		if(Setting.POINTSELECTED!=null){
			xmove = -Setting.POINTSELECTED.getWidth()/2;
			ymove = -Setting.POINTSELECTED.getHeight();
		}
		for (int i = 0; i < _SelectedFeatures.size(); i++){
			SelectedFeatures s = _SelectedFeatures.get(i).clone();
			for (int j = 0;j < s.FIDs.size(); j++){
				IGeometry geometry = s.FeatureClass.getGeometry(s.FIDs.get(j));
				switch (geometry.GeometryType()){
				case Point:{
					IPointElement element = new PointElement();
					element.setGeometry(geometry);
					element.setSymbol(Setting.SelectPointFeatureStyle);
					//修改 by 李忠义 20120306
					// element.Draw(_ScreenDisplay.GetCache(CanvasCache.SelectionCache), Delegate);
					element.Draw(_ScreenDisplay.getCanvas(), Delegate);
					break;
				}case Polyline:{
					ILineElement element = new LineElement();
					element.setGeometry(geometry);
					element.setSymbol(Setting.SelectLineFeatureStyle);
					//修改 by 李忠义 20120306
					// element.Draw(_ScreenDisplay.GetCache(CanvasCache.SelectionCache), Delegate);
					element.Draw(_ScreenDisplay.getCanvas(), Delegate);
					break;
				}case Polygon:{
					IFillElement element = new FillElement();
					element.setGeometry(geometry);
					element.setSymbol(Setting.SelectPolygonFeatureStyle);
					//修改 by 李忠义 20120306
					// element.Draw(_ScreenDisplay.GetCache(CanvasCache.SelectionCache), Delegate);
					element.Draw(_ScreenDisplay.getCanvas(), Delegate);
					break;
				}default:{
					break;
				}
				}
				if(Setting.POINTSELECTED!=null){
					IPicElement element = new PicElement();
					element.setGeometry(geometry);
					element.setPic(Setting.POINTSELECTED, xmove, ymove);
					element.Draw(_ScreenDisplay.getCanvas(), Delegate);
				}
			}
		}
	}
}