package srs.Layer.wmts;

import srs.Geometry.IPoint;

/**切片配置信息
 * @author 李忠义
 * 20150610
 */
public class TileInfo {

    /**切片数据的DPI */
    public int Dpi = 0;
    /**切片格式 */
    public String Format ="";
    /**各级切片定义参数 */
    public LOD[] LODs = null;

  /*  *//**每个切片的高度
     * 
     *//*
    public int Height = 254;
    

    *//**每个切片的宽度
     * 
     *//*
    public int Width = 254;*/


   
    /**切片数据的起始位置
     * 
     *//*
    public IPoint Origin = null; */
    
}
