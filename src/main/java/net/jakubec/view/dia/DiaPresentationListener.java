package net.jakubec.view.dia;


import java.util.EventListener;

public interface DiaPresentationListener extends EventListener {
	public void presentationChanged(DiaEvent event);
}
