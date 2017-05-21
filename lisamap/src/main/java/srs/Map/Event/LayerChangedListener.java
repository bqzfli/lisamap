package srs.Map.Event;

import java.util.EventListener;

public interface LayerChangedListener extends EventListener {
	void doEvent(LayerChangedEvent event);
}
