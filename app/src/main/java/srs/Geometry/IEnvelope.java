
package srs.Geometry;

/**
 * 
 * @author Administrator
 *
 */
public interface IEnvelope extends IGeometry{

	        /**宽度
	         * @return
	         */
	        double Width();

	        /**高度
	         * @return
	         */
	        double Height();

	        /**X最小值
	         * @return
	         */
	        double XMin();

	        /**Y最小值
	         * @return
	         */
	        double YMin();
	
	        /**X最大值
	         * @return
	         */
	        double XMax();

	        /**Y最大值
	         * @return
	         */
	        double YMax();

	        /**左下角点
	         * @return
	         */
	        IPoint LowerLeft();

	        /** 右下角点
	         * @return
	         */
	        IPoint LowerRight();

	        /**左上角点
	         * @return
	         */
	        IPoint UpperLeft();
	
	        /**右上角点
	         * @return
	         */
	        IPoint UpperRight();
	
	        /**
	         * 缩放
	         * @param dx X方向变化量
	         * @param dy Y方向变化量
	         * @param asRatio 是否按比率缩放，asRatio == true时，作乘法缩放，asRatio == false时，作加法缩放
	         */
	        void Expand(double dx, double dy, Boolean asRatio);
	
	        /**设置范围的边框值
	         * @param xMin X最小值
	         * @param yMin Y最小值
	         * @param xMax X最大值
	         * @param yMax Y最大值
	         */
	        void PutCoords(double xMin, double yMin, double xMax, double yMax);
		       
	        /**将矩形转换为面对象
	         * @return 转换成的面对象
	         */
	        IPolygon ConvertToPolygon();
}
