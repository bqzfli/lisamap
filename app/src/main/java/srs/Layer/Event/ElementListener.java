package srs.Layer.Event;

import java.io.IOException;
import java.util.EventListener;

public interface ElementListener extends EventListener{
	void doEvent() throws IOException;
}
