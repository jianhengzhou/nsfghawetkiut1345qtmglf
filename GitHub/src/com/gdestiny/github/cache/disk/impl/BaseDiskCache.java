/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gdestiny.github.cache.disk.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.gdestiny.github.cache.disk.DiskCache;
import com.gdestiny.github.utils.AndroidUtils;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.IoUtils.CopyListener;

import android.util.Log;

public class BaseDiskCache implements DiskCache {

	private static boolean debug = false;
	private static String tag;

	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
	protected static final String TEMP_POSTFIX = ".tmp";

	private File cacheDir;
	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	private FileNameGenerator nameGenerator;

	private String generateName(String name) {
		return nameGenerator.generate(name);
	}

	public boolean contain(String subDir, String name) {
		File file = get(subDir, name);
		return file.exists() && file.isFile();
	}

	public boolean contain(String name) {
		File file = get(name);
		return file.exists() && file.isFile();
	}

	public BaseDiskCache(File cacheDir, FileNameGenerator nameGenerator) {
		tag = getClass().getSimpleName();
		this.cacheDir = cacheDir;
		if (!this.cacheDir.exists())
			this.cacheDir.mkdirs();
		this.nameGenerator = nameGenerator;
	}

	public BaseDiskCache(String cacheDir, FileNameGenerator nameGenerator) {
		tag = getClass().getSimpleName();
		this.cacheDir = new File(cacheDir);
		if (!this.cacheDir.exists())
			this.cacheDir.mkdirs();
		this.nameGenerator = nameGenerator;
	}

	@Override
	public void addSubDirectory(String subDir) {
		if (debug) {
			Log.v(tag, "addSubDirectory:" + subDir);
		}
		File file = new File(cacheDir, subDir);
		if (!file.exists()) {
			if (debug)
				Log.v(tag, "addSubDirectory mkdirs :" + subDir);
			file.mkdirs();
		} else {
			if (debug)
				Log.v(tag, "addSubDirectory exist :" + subDir);
		}
	}

	@Override
	public File getSubDirectory(String subDir) {
		return new File(cacheDir, subDir);
	}

	@Override
	public File getDirectory() {
		return cacheDir;
	}

	@Override
	public File get(String name) {
		return new File(cacheDir, generateName(name));
	}

	@Override
	public boolean remove(String name) {
		if (debug)
			Log.v(tag, "remove:" + name);
		return get(generateName(name)).delete();
	}

	@Override
	public File get(String subDir, String name) {
		return new File(getSubDirectory(subDir), generateName(name));
	}

	protected boolean save(File file, InputStream inStream,
			CopyListener listener) throws IOException {
		File tmpFile = new File(file.getAbsolutePath() + TEMP_POSTFIX);
		File parent = tmpFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		boolean loaded = false;
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					tmpFile), bufferSize);
			try {
				loaded = IoUtils.copyStream(inStream, os, listener, bufferSize);
			} finally {
				IoUtils.closeSilently(os);
			}
		} finally {
			if (loaded && !tmpFile.renameTo(file)) {
				loaded = false;
			}
			if (!loaded) {
				tmpFile.delete();
			}
		}
		return loaded;
	}

	protected boolean save(File file, Serializable object) throws IOException {
		File tmpFile = new File(file + TEMP_POSTFIX);
		File parent = tmpFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
				tmpFile));
		boolean savedSuccessfully = false;
		try {
			os.writeObject(object);
			savedSuccessfully = true;
		} finally {
			IoUtils.closeSilently(os);
			if (savedSuccessfully && !tmpFile.renameTo(file)) {
				savedSuccessfully = false;
			}
			if (!savedSuccessfully) {
				tmpFile.delete();
			}
		}
		return savedSuccessfully;
	}

	protected boolean save(File file, String content) throws IOException {
		File tmpFile = new File(file + TEMP_POSTFIX);
		File parent = tmpFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		BufferedOutputStream os = new BufferedOutputStream(
				new FileOutputStream(tmpFile), bufferSize);
		boolean savedSuccessfully = false;
		try {
			os.write(content.getBytes());
			savedSuccessfully = true;
		} finally {
			IoUtils.closeSilently(os);
			if (savedSuccessfully && !tmpFile.renameTo(file)) {
				savedSuccessfully = false;
			}
			if (!savedSuccessfully) {
				tmpFile.delete();
			}
		}
		return savedSuccessfully;
	}

	@Override
	public boolean save(String subDir, String name, InputStream inStream,
			CopyListener listener) throws IOException {
		addSubDirectory(subDir);
		File file = get(subDir, name);
		return save(file, inStream, listener);
	}

	@Override
	public boolean save(String subDir, String name, String content)
			throws IOException {
		addSubDirectory(subDir);
		File file = get(subDir, name);
		return save(file, content);
	}

	@Override
	public boolean save(String subDir, String name, Serializable object)
			throws IOException {
		addSubDirectory(subDir);
		File file = get(subDir, name);
		return save(file, object);
	}

	@Override
	public boolean save(String name, Serializable object) throws IOException {
		File file = get(generateName(name));
		return save(file, object);
	}

	@Override
	public boolean save(String name, InputStream inStream, CopyListener listener)
			throws IOException {
		File file = get(generateName(name));
		return save(file, inStream, listener);
	}

	@Override
	public boolean save(String name, String content) throws IOException {
		File file = get(generateName(name));
		return save(file, content);
	}

	@Override
	public boolean remove(String subDir, String name) {
		return get(subDir, generateName(name)).delete();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		try {
			AndroidUtils.FileManager.deleteContents(cacheDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getContent(File file) {
		String result = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result = result + "\n" + s;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoUtils.closeSilently(br);
		}
		return result;
	}

	private Serializable getObject(File file) {
		ObjectInputStream is = null;
		Serializable result = null;
		try {
			is = new ObjectInputStream(new FileInputStream(file));
			result = (Serializable) is.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IoUtils.closeSilently(is);
		}
		return result;
	}

	@Override
	public String getContent(String name) {
		File file = get(generateName(name));
		return getContent(file);
	}

	@Override
	public Serializable getObject(String name) {
		File file = get(generateName(name));
		return getObject(file);
	}

	@Override
	public String getContent(String subDir, String name) {
		File file = get(subDir, generateName(name));
		return getContent(file);
	}

	@Override
	public Serializable getObject(String subDir, String name) {
		File file = get(subDir, generateName(name));
		return getObject(file);
	}

}