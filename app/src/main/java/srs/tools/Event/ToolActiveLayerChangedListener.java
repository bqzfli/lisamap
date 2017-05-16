package srs.tools.Event;

import java.io.IOException;

public interface ToolActiveLayerChangedListener {
	void doEvent(boolean value) throws IOException;
}
