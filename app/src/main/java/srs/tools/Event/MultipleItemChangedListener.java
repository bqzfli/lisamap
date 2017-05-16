package srs.tools.Event;

import java.io.IOException;
import java.util.EventListener;
import java.util.List;

/**
* @ClassName: MultipleItemChangedListener
* @Description: TODO(这里用一句话描述这个类的作用)
* @Version: V1.0.0.0
* @author lisa
* @date 2016年12月26日 下午4:34:08
***********************************
* @editor lisa 
* @data 2016年12月26日 下午4:34:08
* @todo TODO
*/
public interface MultipleItemChangedListener extends EventListener {

	public void doEventSettingsChanged(List<Integer> indexs) throws IOException;
}
