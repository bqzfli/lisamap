package srs.tools.Location.Event;


import java.util.EventListener;

/**
 * 选中的位置 接口
 * @author lisa
 *
 */
public interface SelectedLocationListener extends EventListener {

	public void doSelectedLocationChanged(double[] offsets) throws Exception;

}