package srs.Operation.Event;

import java.util.EventListener;

public interface FeatureChangedListener extends EventListener {
	void doEvent(FeatureChangedEvent event);
}
