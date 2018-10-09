package net.jakubec.view.dia;

public class DiaEvent {

	public static enum DiaEventType{
		FINISHED, NEXTIMAGE
	}

	public final DiaEventType type;

	public DiaEvent(DiaEventType eventType) {
		type = eventType;
	}
}
