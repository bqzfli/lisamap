package srs.Display;

import srs.Display.Symbol.ELABELPOSITION;
import srs.Display.Symbol.ISimpleFillSymbol;
import srs.Display.Symbol.ISimpleLineSymbol;
import srs.Display.Symbol.ISimplePointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Display.Symbol.ITextSymbol;
import srs.Display.Symbol.SimpleFillStyle;
import srs.Display.Symbol.SimpleFillSymbol;
import srs.Display.Symbol.SimpleLineStyle;
import srs.Display.Symbol.SimpleLineSymbol;
import srs.Display.Symbol.SimplePointStyle;
import srs.Display.Symbol.SimplePointSymbol;
import srs.Display.Symbol.TextSymbol;
import srs.Layer.FeatureLayer;
import srs.Layer.IDBLayer;
import srs.Layer.IFeatureLayer;
import srs.Layer.Label;
import srs.Layer.LabelDB;

import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;


/**
 * @author 李忠义  控制各类型矢量数据的默认显示样式
 *
 */
public class Setting {

	/**选中目标点样式
	 * 
	 */
	public static Bitmap POINTSELECTED = null;

	/**文字缩放比率
	 * 
	 */
	public static float TextRate=2;

	/**Zoom
	 * 
	 */
	public static ISimpleFillSymbol ZoomStyle = new SimpleFillSymbol(        
			Color.argb(40,0,0,255),        
			new SimpleLineSymbol(Color.RED, 1, SimpleLineStyle.Solid),
			SimpleFillStyle.Soild);

	/**TouchTrackOfSelect
	 * 
	 */
	public static ISimpleFillSymbol TouchSelectStyle = new SimpleFillSymbol(
			Color.argb(200,255,0,0),        
			new SimpleLineSymbol(Color.argb(200,255,0,0),1, SimpleLineStyle.Solid),
			SimpleFillStyle.Soild);

	
	/**标注样式
	 * 面积、边长、边长节点
	 */
	public static ITextSymbol SymbolTextArea = new TextSymbol(
			Typeface.create("Times New Roman", Typeface.NORMAL),
			0,
			10f,
			false,
			Color.RED,
			ELABELPOSITION.CENTER);
	public static ITextSymbol SymbolTextLength = new TextSymbol(
			Typeface.create("Times New Roman", Typeface.NORMAL),
			0,
			10f,
			false,
			Color.rgb(64, 64, 64),
			ELABELPOSITION.CENTER);
	public static ISimplePointSymbol SymbolPointLength=new SimplePointSymbol(Color.rgb(64, 64, 64), 10, SimplePointStyle.HollowSquare);
	
	
	
	/**操作图层的样式
	 * 
	 */
	public static ISimplePointSymbol ActivePoint=new SimplePointSymbol(Color.YELLOW, 10, SimplePointStyle.HollowSquare);
	public static ISimpleLineSymbol ActivePolyline = new SimpleLineSymbol(Color.YELLOW, 2, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol ActivePolygon = new SimpleFillSymbol(Color.argb(8, 255, 0,0), new SimpleLineSymbol(Color.YELLOW, 3, SimpleLineStyle.Solid), SimpleFillStyle.Soild);

	public static ISimplePointSymbol MovePointStyleRed = new SimplePointSymbol(Color.rgb(255, 0, 0), 10, SimplePointStyle.Circle);
	public static ISimplePointSymbol MovePointStyleBlue = new SimplePointSymbol(Color.rgb(0, 0, 255), 10, SimplePointStyle.Circle);

	/**村样式：
	 * 
	 */
	public static ISymbol SYMBOLCUN = new SimpleFillSymbol(Color.argb(0, 75, 0, 255),
			new SimpleLineSymbol(Color.argb(255, 200,224,255),4,SimpleLineStyle.Solid),
			SimpleFillStyle.Hollow,
			Color.argb(255, 255, 255, 0));
	/**样方样式：
	 * 
	 */
	public static ISymbol SYMBOLYF = new SimpleFillSymbol(Color.argb(0, 75, 0, 255),
			new SimpleLineSymbol(Color.argb(255, 255,0, 0),4,SimpleLineStyle.Solid),
			SimpleFillStyle.Hollow,
			Color.argb(255, 255, 255, 0));
	/**地块样式：
	 * 
	 */
	public static ISymbol SYMBOLZRDK = new SimpleFillSymbol(Color.argb(56, 2, 255, 0),
			new SimpleLineSymbol(Color.argb(255, 255,255, 0),1,SimpleLineStyle.Solid),
			SimpleFillStyle.Soild,
			Color.argb(255, 0, 200, 0));
	/**Track
	 * 
	 */
	public static ISimplePointSymbol TrackPointStyle = new SimplePointSymbol(Color.GREEN, 6, SimplePointStyle.Square);
	public static ISimplePointSymbol TrackNewPointStyle = new SimplePointSymbol(Color.rgb(255, 189, 0), 8, SimplePointStyle.Square);
	public static ISimpleLineSymbol TrackLineStyle = new SimpleLineSymbol(Color.GREEN, 2, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol TrackPolygonStyle = new SimpleFillSymbol(Color.argb(128, 255, 255, 128), new SimpleLineSymbol(Color.GREEN, 3, SimpleLineStyle.Solid), SimpleFillStyle.Soild);

	/**Snap
	 * 
	 */
	public static ISimplePointSymbol SnapStyle = new SimplePointSymbol(Color.argb(0, 255, 255,122), 12, SimplePointStyle.Circle);

	/**Select
	 * 
	 */
	public static ISimplePointSymbol SelectPointFeatureStyle = new SimplePointSymbol(Color.rgb(0, 255, 255), 30, SimplePointStyle.HollowSquare);
	public static ISimpleFillSymbol SelectPolygonFeatureStyle = new SimpleFillSymbol(Color.argb(50,0, 255, 255), 
			new SimpleLineSymbol(Color.rgb(0, 255, 255), 4, SimpleLineStyle.Solid), SimpleFillStyle.Soild);
	public static ISimpleLineSymbol SelectLineFeatureStyle = new SimpleLineSymbol(Color.rgb(0, 255, 255), 2, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol SelectElementStyle = new SimpleFillSymbol(
			Color.argb(128, 0, 255, 0),
			new SimpleLineSymbol(Color.LTGRAY, 1, SimpleLineStyle.Dash),
			SimpleFillStyle.Hollow);

	public static ISimplePointSymbol SelectPointElementStyle = new SimplePointSymbol(Color.GREEN, 6, SimplePointStyle.Square);
	public static ISimplePointSymbol SelectCrossPointElementStyle = new SimplePointSymbol(Color.BLACK, 6, SimplePointStyle.X);

	/**Move
	 * 
	 */
	public static ISimplePointSymbol MovePointStyle = new SimplePointSymbol(Color.rgb(0, 255, 0), 10, SimplePointStyle.Circle);
	public static ISimpleLineSymbol MoveLineStyle = new SimpleLineSymbol(Color.rgb(0, 255, 0), 1, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol MovePolygonStyle = new SimpleFillSymbol(Color.rgb(0, 255, 0), new SimpleLineSymbol(Color.rgb(0, 255, 0), 1, SimpleLineStyle.Solid), SimpleFillStyle.Soild);
	public static ISimpleFillSymbol MoveExtentStyle = new SimpleFillSymbol(
			Color.argb(255,255,255,0),
			new SimpleLineSymbol(Color.argb(20,Color.red(Color.BLUE),Color.green(Color.BLUE),Color.blue(Color.BLUE)), 1, SimpleLineStyle.Solid),
			SimpleFillStyle.Soild);

	/**Element
	 * 
	 */
	public static ISimplePointSymbol ElementPointStyle = new SimplePointSymbol(Color.argb(255, 255, 0, 0), 14, SimplePointStyle.Circle);
	public static ISimpleLineSymbol ElementLineStyle = new SimpleLineSymbol(Color.argb(255, 255, 255, 0), 3, SimpleLineStyle.DashDotDot);
	public static ISimpleFillSymbol ElementFillStyle = new SimpleFillSymbol(Color.argb(0,0,255,255), 
			new SimpleLineSymbol(Color.rgb(0, 128, 255), 4, SimpleLineStyle.DashDotDot),
			SimpleFillStyle.Hollow);
	//    修改by 郭晓惠  添加字体大小
	public static ITextSymbol ElementTextStyle = new TextSymbol(
			Typeface.create("Times New Roman",Typeface.NORMAL), 
			0,
			16f,
			false,
			Color.BLACK,
			ELABELPOSITION.TOP_CENTER);

	public static ITextSymbol SymbelTextCenterBlack = new TextSymbol(
			Typeface.create("Times New Roman", Typeface.NORMAL),
			0,
			10f,
			false,
			Color.WHITE,
			ELABELPOSITION.TOP_CENTER);
	
	public static ITextSymbol LabelSymbol = new TextSymbol(
			Typeface.create("Times New Roman", Typeface.NORMAL),
			0,
			10f,
			false,
			Color.WHITE,
			ELABELPOSITION.TOP_CENTER);

	////MapSurround
	//public static ISimplePointSymbol ElementPointStyle = new SimplePointSymbol(Color.Orange, 12, SimplePointStyle.Circle);
	//public static ISimpleLineSymbol ElementLineStyle = new SimpleLineSymbol(Color.Orange, 1, SimpleLineStyle.Solid);

	/**Flash
	 * 
	 */
	public static ISimplePointSymbol FlashPointStyle = new SimplePointSymbol(Color.RED, 12, SimplePointStyle.Circle);
	public static ISimpleLineSymbol FlashLineStyle = new SimpleLineSymbol(Color.RED, 4, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol FlashFillStyle = new SimpleFillSymbol(Color.RED, FlashLineStyle, SimpleFillStyle.Soild);

	/**ROI
	 * 
	 */
	public static ISimplePointSymbol ROIPointStyle = new SimplePointSymbol(Color.BLACK, 12, SimplePointStyle.Circle);
	public static ISimpleLineSymbol ROILineStyle = new SimpleLineSymbol(Color.BLACK, 1, SimpleLineStyle.Solid);
	public static ISimpleFillSymbol ROIFillStyle = new SimpleFillSymbol(Color.BLACK, ROILineStyle, SimpleFillStyle.Soild);


	/**鹰眼图中框的样式
	 * 
	 */
	public static ISimpleFillSymbol EagleEyeRectStyle = new SimpleFillSymbol(
			Color.argb(255,255,255,0),
			new SimpleLineSymbol(Color.RED, 1, SimpleLineStyle.Solid), 
			SimpleFillStyle.Hollow);


	/**设置图层标注有样式
	 * @param layer 图层名称
	 * @param fieldName 字段名称
	 * @param sizeText 标注文字尺寸
	 * @param colorText 标注文字颜色
	 * @param typeText 标注文字样式，可以为空
	 * @param power 若标注需要对某一字段值进行乘、除法运算，输入倍数
	 * @param decimalFormat 若标注为浮点型，设置十进制样式
	 * @return 是否设置成功
	 */
	public static boolean SetLayerLabel(
			IFeatureLayer layer,
			String fieldName,
			int sizeText,
			int colorText,
			Typeface typeText,
			double power,
			DecimalFormat decimalFormat){
		int feildName = layer.getFeatureClass().getFields().FindField(fieldName);
		if(feildName>-1){
			Label lab = new Label();
			lab.setFieldName(fieldName);
			TextSymbol symbolCUNT = new TextSymbol();
			symbolCUNT.setSize(sizeText);
			symbolCUNT.setColor(colorText);
			if(typeText == null){
				symbolCUNT.setFont(Typeface.create("Times New Roman", 1));
			}else{
				symbolCUNT.setFont(typeText);
			}
			lab.setSymbol(symbolCUNT);
			lab.setPower(power);
			lab.setDecimalFormat(decimalFormat);
			layer.setLabel(lab);
			layer.setDisplayLabel(true);
			return true;
		}
		Log.e("LABEL", "图层:"+layer.getName()+",缺少字段:"+fieldName+",so 无法设置标注");
		return false;
	}


	/**设置图层标注有样式
	 * @param layer 图层名称
	 * @param sizeText 标注文字尺寸
	 * @param colorText 标注文字颜色
	 * @param typeText 标注文字样式，可以为空
	 * @param power 若标注需要对某一字段值进行乘、除法运算，输入倍数
	 * @param decimalFormat 若标注为浮点型，设置十进制样式
	 * @return 是否设置成功
	 */
	public static boolean SetLabelRenderDBLAYER(
			IDBLayer layer,
			int sizeText,
			int colorText,
			Typeface typeText,
			double power,
			DecimalFormat decimalFormat,
			ELABELPOSITION position){
		LabelDB lab = new LabelDB();
		/*lab.setFieldName(fieldName);*/
		TextSymbol symbolC = new TextSymbol();
		symbolC.setSize(sizeText);
		symbolC.setColor(colorText);
		symbolC.setPosition(position);
		if(typeText == null){
			symbolC.setFont(Typeface.create("Times New Roman", 1));
		}else{
			symbolC.setFont(typeText);
		}
		lab.setSymbol(symbolC);
		lab.setPower(power);
		lab.setDecimalFormat(decimalFormat);
		layer.setLabel(lab);
		layer.setDisplayLabel(true);
		return true;
	}

	//public static ISimpleLineSymbol MapSurroundElementLineStyle = new SimpleLineSymbol(Color.Green, 1, SimpleLineStyle.Solid);
	//public static ISimpleFillSymbol MapSurroundElementFillStyle = new SimpleFillSymbol(Color.Green, MapSurroundElementLineStyle, SimpleFillStyle.Hollow);
	//public static ISimpleFillSymbol PageElementFillStyle = new SimpleFillSymbol(Color.White, null, SimpleFillStyle.Hollow);
	//public static ISimpleLineSymbol TrackElementLineStyle = new SimpleLineSymbol(Color.Black, 1, SimpleLineStyle.Solid);
	//public static ISimpleFillSymbol ModifyElementStyle = new SimpleFillSymbol(Color.Black, TrackElementLineStyle, SimpleFillStyle.Hollow);
	//Default Vector Layer
	//public static IRenderer FeatureLayerRenderer = new SimpleRenderer();
	//ModelBuilder
	//public static ISimpleLineSymbol DynamicLinkLineStyle = new SimpleLineSymbol(Color.Black, 2, SimpleLineStyle.Solid);
	//public static ISimpleLineSymbol ModelOutLineStyle = new SimpleLineSymbol(Color.Black, 1, SimpleLineStyle.Solid);
	//public static ISimpleFillSymbol ObjectsFillStyle = new SimpleFillSymbol(Color.Green, ModelOutLineStyle, SimpleFillStyle.Soild);
	//public static ISimpleFillSymbol OperatorFillStyle = new SimpleFillSymbol(Color.Orange, ModelOutLineStyle, SimpleFillStyle.Soild);

}
