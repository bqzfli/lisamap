package srs.Map.Event;

import java.util.EventListener;

public interface MapExtentChangedListener extends EventListener {
	void doEvent(MapExtentChangedEvent event);
}
