package srs.tools;

import srs.Layer.IDBLayer;

/**
* @ClassName: MapBaseTool
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月26日 下午4:39:25
***********************************
* @editor lisa 
* @data 2016年12月26日 下午4:39:25
* @todo TODO
*/
public class MapBaseTool {

	/**
     * 获得地图点选工具 DBLayer
     * @param mapControl
     * @param LayerId
     * @param isOnlyOneTime
     * @param isOnlyOneSelect
     * @param distanceBuffer        捕捉缓冲区距离
     * @return TouchLongToolMultiple
     */
    public static TouchLongToolMultipleDB getTouchLongToolMultipleDB(
    		MapControl mapControl,
    		IDBLayer layer, 
    		boolean isOnlyOneTime, 
    		boolean isOnlyOneSelect,
            float distanceBuffer){
        mapControl.ClearDrawTool();
        TouchLongToolMultipleDB.DIS_DEFAULT = distanceBuffer;
        TouchLongToolMultipleDB toolMultiple = new TouchLongToolMultipleDB(layer);
        toolMultiple.setBuddyControl(mapControl);// mapControl为操作的地图控件
        toolMultiple.onClick(mapControl);
        toolMultiple.setEnable(true);
        // 设置不累计选择项目
        toolMultiple.IsOnlyOneTime = isOnlyOneTime;
        // 设置是不为单选
        toolMultiple.IsOnlyOneSelect = isOnlyOneSelect;
        return toolMultiple;
    }
}
