package net.jakubec.view.listener;

import java.io.File;
import java.util.EventListener;

public interface ImageDisplayListener extends EventListener {

	public void imageOpened(File file);

	public void zoomLevelChanged(double newZoomLevel);
}
