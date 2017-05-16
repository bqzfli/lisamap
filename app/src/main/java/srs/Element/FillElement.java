package srs.Element;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

import srs.Core.XmlFunction;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Setting;
import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.ITextSymbol;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IPart;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.MATHOD;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 面要素
 * @author Administrator
 *
 */
public class FillElement extends GraphicElement implements IFillElement{
	/** 绘制该要素采用的Symbol  */
	private IFillSymbol mSymbol=null;
	/**
	 * 面积标注样式
	 */
	private ITextSymbol mSymbolTextArea = null;
	/**
	 * 边长标注样式
	 */
	private ITextSymbol mSymbolTextLength = null;
	
	/** 是否绘制面积*/
	private boolean mIsDrawArea = false;

	/**需要绘制边长的序号
	 * 
	 */
	private Integer[] mIndexLength = null;

	/**是否要绘制全部边长
	 * 
	 */
	private boolean mIsDrawAllLength = false;
	
	/**面积换算比率
	 * 倍数
	 * 
	 */
	private double mPowerArea = 1;
	
	/**距离换算比率
	 * 倍数
	 */
	private double mPowerDistance = 1;


	/**文本格式面积
	 * 
	 */
	private DecimalFormat mDecimalFormatArea = null;

	/**文本格式边长
	 * 
	 */
	private DecimalFormat mDecimalFormatLength = null;

	/**
	 * 构造函数
	 */
	public FillElement(){
		super();
		mSymbol = new SimpleFillSymbol();
		mDecimalFormatLength = new DecimalFormat("#.#");
		mDecimalFormatArea = new DecimalFormat("#.#");
		mSymbolTextArea = Setting.SymbolTextArea;
		mSymbolTextLength = Setting.SymbolTextLength;
		mPowerArea = 1;
		mPowerDistance =1;
	}
	
	/**是否绘制面积
	 * @param value
	 */
	public void setIsDrawArea(boolean value){
		mIsDrawArea = value;
	}
	
	/**需要绘制边长的边的序号
	 * @param indexs
	 */
	public void setDrawLength(Integer[] indexs){
		mIndexLength = indexs;
		if(mIndexLength!=null&&mIndexLength.length>0){
			mIsDrawAllLength = false;
		}
	}
	
	/**是否绘制面积
	 * @param value
	 */
	public void setDrawLengthAll(boolean value){
		mIsDrawAllLength = value;
		if(mIsDrawAllLength){
			mIndexLength = null;
		}
	}
	
	
	/**面积文本的样式
	 * @param value
	 */
	public void setDecimalFormatArea(DecimalFormat value){
		mDecimalFormatArea = value;
	}


	/* (non-Javadoc)
	 * @see Element.IFillElement#Symbol()
	 */
	public final IFillSymbol getSymbol(){
		return mSymbol;
	}

	/* (non-Javadoc)
	 * @see Element.IFillElement#Symbol(Show.Symbol.IFillSymbol)
	 */
	public void setSymbol(IFillSymbol value){
		if (mSymbol != value){
			mSymbol = value;
		}
	}

	public ITextSymbol getSymbolTextArea(){
		return mSymbolTextArea;
	}

	public void setSymbolTextArea(ITextSymbol value){
		if (mSymbolTextArea != value){
			mSymbolTextArea = value;
		}
	}
	

	public ITextSymbol getSymbolTextLength(){
		return mSymbolTextLength;
	}

	public void setSymbolTextLength(ITextSymbol value){
		if (mSymbolTextLength != value){
			mSymbolTextLength = value;
		}
	}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

	@Override
	public void Draw(Bitmap canvas, FromMapPointDelegate Delegate) {
		try {
			if (this.getGeometry() == null) {
				throw new sRSException("1020");
			}
			if (mSymbol == null) {
				throw new sRSException("1021");
			}
			if (!(this.getGeometry() instanceof IEnvelope)
					&& !(this.getGeometry() instanceof IPolygon)) {
				throw new sRSException("1022");
			}

			Drawing draw = new Drawing(new Canvas(canvas),
					Delegate);
			if (this.getGeometry() instanceof IEnvelope) {
				draw.DrawRectangle((IEnvelope) this.getGeometry(), mSymbol);
			} else {
				draw.DrawPolygon((IPolygon) this.getGeometry(), mSymbol);
				if (mIsDrawArea) {
					double areaValue = ((IPolygon) this.getGeometry()).Area()*mPowerArea;
					String strArea = mDecimalFormatArea.format(areaValue);
					IPoint pointC = this.getGeometry().CenterPoint();
					draw.DrawText(strArea +"(亩)", pointC,  mSymbolTextArea, 2);
					/*draw.DrawPoint(pointC, Setting.SymbolPointLength, null, 0, 0);*/
				}
				if(mIsDrawAllLength){
					IPart[] parts = ((IPolygon)this.getGeometry()).Parts();
					double distance = 0;
					String strDistance = "";
					IPoint midPoint = null;
					for(IPart part:parts){
						IPoint[] points = part.Points();
						for(int i=0;i<points.length-1;i++){
							midPoint = MATHOD.CenterPoint(points[i],points[i+1]);
							distance = MATHOD.Distance(points[i],points[i+1])*mPowerDistance;
							strDistance = mDecimalFormatLength.format(distance);
							draw.DrawText(strDistance, midPoint, mSymbolTextLength,2);
							draw.DrawPoint(points[i], Setting.SymbolPointLength, null, 0, 0);
							draw.DrawPoint(points[i+1], Setting.SymbolPointLength, null, 0, 0);
						}
					}
				}else if(mIndexLength!=null&&mIndexLength.length>0){
					IPart[] parts = ((IPolygon)this.getGeometry()).Parts();
					double distance = 0;
					String strDistance = "";
					IPoint midPoint = null;
					for(IPart part:parts){
						IPoint[] points = part.Points();
						for(Integer index:mIndexLength){
							if(index>-1&&index < points.length-1){
								midPoint = MATHOD.CenterPoint(points[index],points[index+1]);
								distance = MATHOD.Distance(points[index],points[index+1])*mPowerDistance;
								strDistance = mDecimalFormatLength.format(distance);
								draw.DrawText(strDistance, midPoint, mSymbolTextLength,2);
								draw.DrawPoint(points[index], Setting.SymbolPointLength, null, 0, 0);
								draw.DrawPoint(points[index+1], Setting.SymbolPointLength, null, 0, 0);
							}
						}
					}
				}
			}
		} catch (sRSException e) {
			e.printStackTrace();
		}
	}


	@Override
	public IElement Clone(){
		IFillElement element = new FillElement();
		element.setName(this.getName());
		if (this.getGeometry() !=null){
			element.setGeometry(this.getGeometry().Clone());
		}

		if (element.getSymbol() instanceof IFillSymbol){
			ISymbol tempVar = mSymbol.Clone();
			element.setSymbol((IFillSymbol)((tempVar instanceof IFillSymbol) ? tempVar : null));
		}

		return element;
	}

	/** 
	 加载XML数据
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

	@Override
	public void LoadXMLData(org.dom4j.Element node) throws SecurityException,
	IllegalArgumentException, ClassNotFoundException, 
	sRSException, NoSuchMethodException, 
	InstantiationException, IllegalAccessException, InvocationTargetException{
		if (node == null){
			return;
		}

		super.LoadXMLData(node);

		org.dom4j.Element symbolNode=node.element("Symbol");
		if (symbolNode != null){
			ISymbol symbol = XmlFunction.LoadSymbolXML(symbolNode);
			if (symbol instanceof IFillSymbol){
				mSymbol = (IFillSymbol)symbol;
			}
		}
	}

	/** 
	 保存XML数据
	 @param node
	 */
	@Override
	public void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		super.SaveXMLData(node);

		org.dom4j.Element symbolNode =node.getDocument().addElement("Symbol");
		XmlFunction.SaveSymbolXML(symbolNode, mSymbol);
		node.add(symbolNode);
	}

	@Override
	public void setDecimalFormatLength(DecimalFormat value) {
		mDecimalFormatLength = value;
	}

	@Override
	public void setPowerArea(double power) {
		// TODO Auto-generated method stub
		mPowerArea =power;
	}

	@Override
	public void setPowerLength(double power) {
		// TODO Auto-generated method stub
		mPowerDistance = power;
	}


}