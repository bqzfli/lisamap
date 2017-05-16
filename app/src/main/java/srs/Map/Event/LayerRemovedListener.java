package srs.Map.Event;

import java.util.EventListener;

public interface LayerRemovedListener extends EventListener {
	void doEvent(LayerRemovedEvent event);
}
