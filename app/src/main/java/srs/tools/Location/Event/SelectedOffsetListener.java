package srs.tools.Location.Event;


import java.util.EventListener;

/**
 * 选择目标下标更换 接口
 * @author yzr
 *
 */
public interface SelectedOffsetListener extends EventListener {

	public void doEventSelectedIndexChanged(double[] offsets) throws Exception;

}