package srs.Rendering;

import java.io.IOException;
import java.util.List;

import srs.DataSource.Vector.IFeatureClass;
import srs.Display.Drawing;
import srs.Display.FromMapPointDelegate;
import srs.Display.Symbol.IFillSymbol;
import srs.Display.Symbol.ILineSymbol;
import srs.Display.Symbol.IPointSymbol;
import srs.Display.Symbol.ISymbol;
import srs.Geometry.IEnvelope;
import srs.Geometry.IGeometry;
import srs.Geometry.IPoint;
import srs.Geometry.IPolygon;
import srs.Geometry.IPolyline;
import srs.Geometry.srsGeometryType;
import srs.Utility.sRSException;
import android.graphics.Bitmap;
import android.os.Handler;

public abstract class FeatureRenderer extends Renderer implements IFeatureRenderer {
	protected FeatureRenderer() {
		super();
	}

	/**
	 * 绘制要素
	 * 
	 * @param featureClass
	 *            要素集
	 * @param extent
	 *            画的范围
	 * @param canvas
	 *            画布
	 * @param Delegate
	 *            转换坐标代理
	 * @throws sRSException
	 * @throws IOException
	 */
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent, Bitmap canvas, List<Integer> draws,
			FromMapPointDelegate Delegate, Handler handler) throws sRSException, IOException {
		return false;
	}

	/**
	 * 仅绘制要素集中选中的要素
	 * 
	 * @param featureClass
	 *            要素集
	 * @param extent
	 *            画的范围
	 * @param canvas
	 *            画布
	 * @param sels
	 *            选中的要素序列号
	 * @param Delegate
	 *            转换坐标代理
	 * @return
	 * @throws IOException
	 * @throws sRSException
	 */
	public boolean Draw(IFeatureClass featureClass, IEnvelope extent, Bitmap canvas, List<Integer> sels,
			List<Integer> draws, FromMapPointDelegate Delegate, Handler handler) throws sRSException,
			IOException {
		return false;
	}

	@Override
	public IRenderer clone() {
		throw new RuntimeException("The method or operation is not implemented.");
	}

	@Override
	public void dispose() {
	}

	/**
	 * 画形状
	 * 
	 * @param draw
	 *            画的对象
	 * @param geoType
	 *            矢量类型
	 * @param shape
	 *            Geometry
	 * @param symbol
	 *            渲染风格
	 * @throws sRSException
	 */
	protected final void DrawGeometry(Drawing draw, srsGeometryType geoType, IGeometry shape, ISymbol symbol)
			throws sRSException {
		switch (geoType) {
		case Point: {
			if (symbol != null && symbol instanceof IPointSymbol && shape instanceof IPoint) {
				draw.DrawPoint((IPoint) shape, (IPointSymbol) symbol, null, 0, 0);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		case Polyline: {
			if (symbol != null && symbol instanceof ILineSymbol && shape instanceof IPolyline) {
				draw.DrawPolyline((IPolyline) shape, (ILineSymbol) symbol);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		case Polygon: {
			if (symbol != null && symbol instanceof IFillSymbol && shape instanceof IPolygon) {
				draw.DrawPolygon((IPolygon) shape, (IFillSymbol) symbol);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		default: {
			throw new sRSException("1022");
		}
		}
	}
	
	/**
	 * 
	 * 画形状
	 * 
	 * @param draw
	 *            画的对象
	 * @param geoType
	 *            矢量类型
	 * @param shape
	 *            Geometry
	 * @param symbol
	 *            渲染风格
	 * @param bp
	 * 				图片渲染
	 * @throws sRSException
	 */
	protected final void DrawGeometry(Drawing draw, srsGeometryType geoType, IGeometry shape, ISymbol symbol,Bitmap bp)
			throws sRSException {
		switch (geoType) {
		case Point: {
			float xmove = 0;
			float ymove = 0;
			if(bp!=null){
				xmove = -bp.getWidth()/2;
				ymove = -bp.getHeight();
			}
			if (symbol != null && symbol instanceof IPointSymbol && shape instanceof IPoint) {
				draw.DrawPoint((IPoint) shape, (IPointSymbol) symbol, bp, xmove, ymove);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		case Polyline: {
			if (symbol != null && symbol instanceof ILineSymbol && shape instanceof IPolyline) {
				draw.DrawPolyline((IPolyline) shape, (ILineSymbol) symbol);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		case Polygon: {
			if (symbol != null && symbol instanceof IFillSymbol && shape instanceof IPolygon) {
				draw.DrawPolygon((IPolygon) shape, (IFillSymbol) symbol);
			} else {
				throw new sRSException("1022");
			}
			break;
		}
		default: {
			throw new sRSException("1022");
		}
		}
	}
}