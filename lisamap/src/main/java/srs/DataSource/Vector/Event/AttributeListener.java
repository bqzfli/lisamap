package srs.DataSource.Vector.Event;

import java.util.EventListener;

public interface AttributeListener extends EventListener{
	void doEvent(AttributeEvent event);
}
