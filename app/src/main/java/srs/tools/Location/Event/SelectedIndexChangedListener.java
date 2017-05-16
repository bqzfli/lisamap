package srs.tools.Location.Event;


import java.util.EventListener;

/**
 * 选择目标下标更换 接口
 * @author yzr
 *
 */
public interface SelectedIndexChangedListener extends EventListener {

	public void doEventSelectedIndexChanged(double[] xy,double[] lotlat) throws Exception;

}