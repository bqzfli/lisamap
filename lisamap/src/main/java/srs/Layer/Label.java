package srs.Layer;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;

import srs.Core.XmlFunction;
import srs.DataSource.DataTable.DataTable;
import srs.DataSource.Table.ITable;
import srs.DataSource.Vector.IFeature;
import srs.DataSource.Vector.IFeatureClass;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.ITextSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Element.ITextElement;
import srs.Element.TextElement;
import srs.Geometry.IEnvelope;
import srs.Utility.sRSException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
/**
 * 标记信息
 * @author Administrator
 *
 */
public class Label /*implements IXMLPersist*/{
	private String mFieldName;
	private double mMaximumScale;
	private double mMinimumScale;
	protected ITextSymbol mLabelSymbol;
	/**倍数
	 * 
	 */
	protected double mPower = 1;
	protected DecimalFormat mDecimalFormat = null;

	public void dispose() {
		mFieldName = null;
		mLabelSymbol.dispose(); 
		mLabelSymbol = null;
		mDecimalFormat = null;
	}

	public Label(){
		mMaximumScale = Double.MAX_VALUE;
		mMinimumScale = Double.MIN_VALUE;
		mLabelSymbol = new TextSymbol();
		mFieldName = "";
		mDecimalFormat = new DecimalFormat("#.##");
	}

	/**标注字段 */
	public final String getFieldName(){
		return mFieldName;
	}

	public final void setFieldName(String value){
		mFieldName = value;
	}



	/** 
	 最大比例尺

	 */
	public final double MaximumScale(){
		return mMaximumScale;
	}

	public final void MaximumScale(double value){
		mMaximumScale = value;
	}

	/** 
	 最小比例尺

	 */
	public final double MinimumScale(){
		return mMinimumScale;
	}

	public final void MinimumScale(double value){
		mMinimumScale = value;
	}

	/** 
	 标注样式

	 */
	public final ITextSymbol getSymbol(){
		return mLabelSymbol;
	}

	public final void setSymbol(ITextSymbol value){
		mLabelSymbol = value;
	}

	public void DrawLabel(IFeatureClass featureClass, 
			Bitmap canvas, 
			IEnvelope extent,
			List<Integer> sels,
			FromMapPointDelegate Delegate) throws sRSException{
		try{
			if(sels==null||sels.size()==0){
				return;
			}

			DataTable table = ((ITable)((featureClass instanceof ITable) ? featureClass : null)).getAttributeTable();
			Class<?> fieldType = featureClass.getFields().getField(featureClass.getFields().FindField(mFieldName)).getType();

			drawElements(canvas, sels, fieldType, table, featureClass, Delegate);			
			Log.i("MAP_LABLE", featureClass.getName() + ":在绘制Label个数"+ String.valueOf(sels.size()));
		}catch (java.lang.Exception e){
			Log.e("MAP_LABLE", featureClass.getName() +":在绘制Label过程中发生不可预知的错误.\r\t"+e.getMessage());
			throw new sRSException("在绘制Label过程中发生不可预知的错误.");
		}
	}

	/**若标注需要进行乘、除法运算，请设置倍数
	 * @param power
	 */
	public void setPower(double power){
		if(power!=0){
			this.mPower = power;
		}
	}

	/**若标注为浮点型，设置十进制样式
	 * @param decimalFormat
	 */
	public void setDecimalFormat(DecimalFormat decimalFormat){
		if(decimalFormat!=null){
			mDecimalFormat = decimalFormat;
		}
	}


	/**绘制Element
	 * @param canvas 画布
	 * @param sels 要绘制的要素序号
	 * @param fieldType 标注的字段类型
	 * @param table 数据表
	 * @param featureClass 矢量数据集
	 * @param Delegate 坐标点转换的代理
	 * @throws Exception
	 */
	private void drawElements(Bitmap canvas, 
			List<Integer> sels,
			Class<?> fieldType,
			DataTable table,
			IFeatureClass featureClass,
			FromMapPointDelegate Delegate) throws Exception{
		for (int i = 0; i < sels.size(); i++){
			IFeature feature = featureClass.getFeature(sels.get(i));
			ITextElement element = new TextElement();
			element.setScaleText(false);
 			element.setGeometry(feature.getGeometry().CenterPoint());
			element.setSymbol(mLabelSymbol);
			String labelValue ="";
			if(fieldType == String.class){
				labelValue = table.getEntityRows().get(sels.get(i)).getStringCHS(mFieldName);
			}else if(fieldType == Integer.class){
				int value = table.getEntityRows().get(sels.get(i)).getInt(mFieldName);
				labelValue = String.valueOf(value);
			}else if(fieldType == Double.class){		
				double value = table.getEntityRows().get(sels.get(i)).getDouble(mFieldName);	
				value *= mPower;
				labelValue = mDecimalFormat.format(value); 
			}
			element.setText(labelValue);
			element.Draw(canvas, Delegate);
		}
	}



	public final void LoadXMLData(org.dom4j.Element node) throws SecurityException, 
	IllegalArgumentException, sRSException, NoSuchMethodException,
	InstantiationException, IllegalAccessException,
	InvocationTargetException, ClassNotFoundException{
		if (node == null){
			return;
		}

		mFieldName = node.attributeValue("FieldNAME");
		try{
			mMaximumScale = Double.parseDouble(node.attributeValue("MaximumScale"));
		}catch(NumberFormatException e){
			mMaximumScale = Double.MAX_VALUE;
		}

		try{
			mMinimumScale = Double.parseDouble(node.attributeValue("MinimumScale"));
		}catch(NumberFormatException e){
			mMinimumScale = Double.MIN_VALUE;
		}

		org.dom4j.Element symbolNode = node.element("TextSymbol");
		if (symbolNode != null){
			ISymbol symbol = XmlFunction.LoadSymbolXML(symbolNode);
			if (symbol instanceof ITextSymbol){
				mLabelSymbol = (ITextSymbol)symbol;
			}
		}
	}

	@SuppressLint("UseValueOf")
	public final void SaveXMLData(org.dom4j.Element node){
		if (node == null){
			return;
		}

		XmlFunction.AppendAttribute(node, "FieldNAME", (new Integer(mFieldName)).toString());
		XmlFunction.AppendAttribute(node, "MaximumScale", (new Double(mMaximumScale)).toString());
		XmlFunction.AppendAttribute(node, "MinimumScale", (new Double(mMinimumScale)).toString());

		org.dom4j.Element symbolNode = node.getParent().addElement("TextSymbol");
		XmlFunction.SaveSymbolXML(symbolNode, mLabelSymbol);
		node.add(symbolNode);
	}


}
