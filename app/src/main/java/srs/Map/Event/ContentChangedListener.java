package srs.Map.Event;

import java.util.EventListener;
import java.util.EventObject;

public interface ContentChangedListener extends EventListener {
	void doEvent(EventObject event);
}
