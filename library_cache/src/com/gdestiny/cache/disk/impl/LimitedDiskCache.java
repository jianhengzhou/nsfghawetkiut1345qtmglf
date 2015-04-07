package com.gdestiny.cache.disk.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.gdestiny.cache.utils.FileNameGenerator;
import com.gdestiny.cache.utils.IoUtils.CopyListener;

public class LimitedDiskCache extends BaseDiskCache {

	private final List<String> list;
	/** Size of this cache in units. Not necessarily the number of elements. */
	private int size; // 已经存储的大小
	private int maxSize; // 规定的最大存储空间

	private int putCount;// put的次数
	private int createCount;// create的次数
	private int evictionCount;// 回收的次数
	private int hitCount;// 命中的次数
	private int missCount;// 丢失的次数

	public LimitedDiskCache(File cacheDir, int maxSize,
			FileNameGenerator nameGenerator) {
		super(cacheDir, nameGenerator);
		// TODO Auto-generated constructor stub
		setMaxSize(maxSize);
		this.list = new ArrayList<String>();
	}

	public LimitedDiskCache(String cacheDir, int maxSize,
			FileNameGenerator nameGenerator) {
		super(cacheDir, nameGenerator);
		// TODO Auto-generated constructor stub
		setMaxSize(maxSize);
		this.list = new ArrayList<String>();
	}

	private void setMaxSize(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
	}

	private void put(File file) {
		synchronized (this) {
			putCount++;
			size += file.length();
			String path = file.getPath();
			if (list.contains(path)) {
				list.remove(path);
			}
			list.add(0, path);

			trimToSize(maxSize);
		}
	}

	private void trimToSize(int maxSize) {
		synchronized (this) {
			while (true) {
				if (size <= maxSize)
					break;

				String path = list.get(list.size() - 1);
				File file = new File(path);
				size -= file.length();
				list.remove(list.size() - 1);
			}
		}
	}

	@Override
	protected boolean save(File file, String content) throws IOException {
		// TODO Auto-generated method stub
		boolean result = super.save(file, content);
		if (result) {
			put(file);
		}
		return result;
	}

	@Override
	protected boolean save(File file, InputStream inStream,
			CopyListener listener) throws IOException {
		// TODO Auto-generated method stub
		boolean result = super.save(file, inStream, listener);
		if (result) {
			put(file);
		}
		return result;
	}

	@Override
	protected boolean save(File file, Serializable object) throws IOException {
		// TODO Auto-generated method stub
		boolean result = super.save(file, object);
		if (result) {
			put(file);
		}
		return result;
	}

}
