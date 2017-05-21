package srs.Operation.Event;

import java.util.EventListener;


public interface SelectionChangedListener extends EventListener {
	public void doEvent(SelectionChangedEvent event,SelectEventArgs e);
}
