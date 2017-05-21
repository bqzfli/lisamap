package srs.Display.Symbol;

import srs.Utility.sRSException;
import android.graphics.Typeface;
import android.graphics.Color;


public class TextSymbol extends Symbol implements ITextSymbol {

	/** 使用默认字体  目前android不支持其他字体 */
	private Typeface mFont=Typeface.create("Times New Roman", Typeface.NORMAL);

	/** 标注文字中心点的位置
	 * 
	 */
	private ELABELPOSITION mElabelposition = ELABELPOSITION.CENTER;

	private float mAngle;
	private boolean mVertical;
	private float mSize;

	@Override
	public void dispose(){
		mFont = null;
	} 

	public TextSymbol(){
		this(
				Typeface.create("Times New Roman", Typeface.NORMAL), 
				0, 
				10f,
				false,
				Color.BLACK,
				ELABELPOSITION.CENTER
				);
	}

	public TextSymbol(
			Typeface font, 
			float angle, 
			float msize,
			boolean vertical,
			int color,
			ELABELPOSITION labelposition) {
		super();
		mFont = font;
		mAngle = angle;
		mVertical = vertical;
		mSize=msize;
		mElabelposition = labelposition;
		super.setColor(color);
	}


	@Override
	public Typeface getFont() {
		return mFont;
	}

	@Override
	public void setFont(Typeface value) {
		mFont = value;
	}

	@Override
	public float getAngle() {
		return mAngle;
	}

	@Override
	public void setAngle(float value) throws sRSException {
		if (value >= 0 && value <= 360){
			mAngle = value;
		}else{
			throw new sRSException("字体旋转角度应在0－360度之间");
		}
	}

	@Override
	public boolean getVertical() {
		return mVertical;
	}

	@Override
	public void setVertical(boolean value) {
		mVertical = value;
	}

	@Override
	public ISymbol Clone() {
		return new TextSymbol(mFont, mAngle, mSize ,mVertical, this.getColor(),mElabelposition);
	}


	@Override
	public float getSize() {
		return mSize;
	}

	@Override
	public void setSize(float value) {
		this.mSize=value;
	}

	//	   /// <summary>
	//    /// 保存为XML节点
	//    /// </summary>
	//    /// <param name="node"></param>
	//    public  void SaveXMLData(org.dom4j.Element node)
	//    {
	//        if (node == null)
	//            return;
	//
	//        super.SaveXMLData(node);
	//
	//        FontConverter converter = new FontConverter();
	//        //字体
	//        XmlFunction.AppendAttribute(node, "Font", converter.ConvertToString(mFont));
	//
	//        //文字角度
	//        XmlFunction.AppendAttribute(node, "Angle",String.valueOf(mAngle) );
	//
	//        //是否为竖排格式
	//        XmlFunction.AppendAttribute(node, "Vertical", String.valueOf(mVertical));
	//    }

	/// <summary>
	/// 从XML节点中读取
	/// </summary>
	/// <param name="node"></param>
	public  void LoadXMLData(org.dom4j.Element node){
		if (node == null)
			return;
		try {
			super.LoadXMLData(node);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (node.attribute("Font") != null
				&&node.attribute("Typeface") != null){
			String font = "Times New Roman";
			String typeface = "0";
			font = node.attributeValue("Font");
			typeface = node.attributeValue("Typeface");
			mFont = Typeface.create(font, Integer.valueOf(typeface));
		}
		if (node.attribute("Size") != null){
			String size = "10";
			size = node.attributeValue("Size");
			mSize = Float.valueOf(size);			
		}

		if (node.attribute("Angle") != null){           
			String strangle=node.attributeValue("Angle").trim();
			mAngle=Float.valueOf(strangle);
		}

		if (node.attribute("Vertical") != null) {
			String strvertical;          
			strvertical=node.attributeValue("Vertical");
			mVertical = Boolean.valueOf(strvertical);
		}
	}

	@Override
	public ELABELPOSITION getPosition() {
		return mElabelposition;
	}

	@Override
	public void setPosition(ELABELPOSITION position) {
		mElabelposition = position;
	}
}