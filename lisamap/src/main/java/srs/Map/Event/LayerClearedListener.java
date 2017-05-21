package srs.Map.Event;

import java.util.EventListener;

public interface LayerClearedListener extends EventListener {
	void doEvent(LayerClearedEvent event);
}
