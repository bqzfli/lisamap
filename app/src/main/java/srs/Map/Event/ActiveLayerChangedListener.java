package srs.Map.Event;

import java.util.EventListener;

public interface ActiveLayerChangedListener extends EventListener{
	void doEvent(ActiveLayerChangedEvent event);
}
