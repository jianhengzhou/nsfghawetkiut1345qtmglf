package com.gdestiny.github.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.egit.github.core.client.PageIterator;

public class IteratorUtils {

	private IteratorUtils() {
		throw new AssertionError();
	}

	/**
	 * ���磬���첽
	 * 
	 * @param iterators
	 * @return
	 */
	public static <T> List<T> iteratorToList(PageIterator<T> iterators) {
		List<T> list = new ArrayList<T>();
		while (iterators.hasNext()) {
			Collection<T> collection = iterators.next();
			for (T t : collection) {
				list.add(t);
				GLog.sysout("add list");
			}
		}
		return list;
	}

	/**
	 * ���磬���첽
	 * 
	 * @param iterators
	 * @return
	 */
	public static <T> List<T> iteratorNextPage(PageIterator<T> iterators) {
		List<T> list = new ArrayList<T>();
		if (iterators.hasNext()) {
			GLog.sysout("hasNext");
			Collection<T> collection = iterators.next();
			for (T t : collection) {
				list.add(t);
			}
		} else
			GLog.sysout("noMore");
		return list;
	}
}
