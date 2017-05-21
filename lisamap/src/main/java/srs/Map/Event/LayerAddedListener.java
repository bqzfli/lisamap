package srs.Map.Event;

import java.util.EventListener;

public interface LayerAddedListener extends EventListener {
	void doEvent(LayerAddedEvent event);
}
