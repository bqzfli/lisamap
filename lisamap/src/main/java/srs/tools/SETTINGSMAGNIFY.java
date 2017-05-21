package srs.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**地图上的放大镜设置
 * @author lzy
 * @version 20150606
 */
public class SETTINGSMAGNIFY {

	/**放大镜的启动距离
	 * 
	 */
	public static long DISMAGNIFY = 50;
	/**点选的启动距离
	 * 
	 */
	public static long DISSELECT = 30;
	/**放大镜的启动时间,毫秒;迟于此时间不被触发
	 * 
	 */
	public static long TIMEMAGNIFYMIN = 500;

	/**放大镜的启动时间,毫秒;迟于此时间不被触发
	 * 
	 */
	public static long TIMEMAGNIFYMAX = 800;
	/**放大倍数
	 * 
	 */
	public static int FACTOR = 3;
	/**放大镜的半径
	 * 
	 */
	public static int mRadius = 80;
	/**放大镜图片
	 * 
	 */
	private static Bitmap mBitmapMagnifyDAISHIZI = null;

	/**设置放大镜图pain
	 * @param bitmap
	 */
	public static void setFDJDSZ(Bitmap bitmap){
		if(bitmap!=null){
			mBitmapMagnifyDAISHIZI = bitmap;
			mRadius = mBitmapMagnifyDAISHIZI.getWidth()/2;
		}
	}
	
	/**获取带十字花的放大镜
	 * @return 
	 */
	public static Bitmap getFDJDSZ(){
		return mBitmapMagnifyDAISHIZI;
	}
	
	/**获取放大镜尺寸
	 * 
	 */
	public static int getRADIUS(){
		return mRadius;
	}

	/**放大镜位置X
	 * 
	 */
	public static int X = 0;
	/**放大镜位置Y
	 * 
	 */
	public static int Y = 0;

	/**放大形状
	 * 
	 */
	public static Matrix MATRIXSHAPE = new Matrix();
}
