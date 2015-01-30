package com.gdestiny.github.utils;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.event.Event;

public class EventUtils {

	private EventUtils() {
		throw new AssertionError();
	}

	public static User getEventUser(Event event) {
		User author = event.getActor();
		if (author != null)
			return author;

		User org = event.getOrg();

		return org != null ? org : null;
	}

	public static String getAuthor(Event event) {
		User user = getEventUser(event);
		return user != null ? user.getLogin() : null;
	}

	public static String getAuthorAvatarUrl(Event event) {
		User user = getEventUser(event);
		return user != null ? user.getAvatarUrl() : null;
	}

	public static String getEventType(Event event) {
		if(event.getType().equals(Event.TYPE_CREATE)){
			return "create";
		}
		return null;
	}
}
