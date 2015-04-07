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
package com.gdestiny.cache.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Provides I/O operations
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class IoUtils {

	/** {@value} */
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
	/** {@value} */
	public static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024; // 500 Kb
	/** {@value} */
	public static final int CONTINUE_LOADING_PERCENTAGE = 75;

	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	private IoUtils() {
	}

	public static String readFully(Reader reader) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			int count;
			while ((count = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, count);
			}
			return writer.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * Deletes the contents of {@code dir}. Throws an IOException if any file
	 * could not be deleted, or if {@code dir} is not a readable directory.
	 */
	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new IOException("not a readable directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (!file.delete()) {
				throw new IOException("failed to delete file: " + file);
			}
		}
	}

	/**
	 * Copies stream, fires progress events by listener, can be interrupted by
	 * listener. Uses buffer size = {@value #DEFAULT_BUFFER_SIZE} bytes.
	 * 
	 * @param is
	 *            Input stream
	 * @param os
	 *            Output stream
	 * @param listener
	 *            null-ok; Listener of copying progress and controller of
	 *            copying interrupting
	 * @return <b>true</b> - if stream copied successfully; <b>false</b> - if
	 *         copying was interrupted by listener
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os,
			CopyListener listener) throws IOException {
		return copyStream(is, os, listener, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Copies stream, fires progress events by listener, can be interrupted by
	 * listener.
	 * 
	 * @param is
	 *            Input stream
	 * @param os
	 *            Output stream
	 * @param listener
	 *            null-ok; Listener of copying progress and controller of
	 *            copying interrupting
	 * @param bufferSize
	 *            Buffer size for copying, also represents a step for firing
	 *            progress listener callback, i.e. progress event will be fired
	 *            after every copied <b>bufferSize</b> bytes
	 * @return <b>true</b> - if stream copied successfully; <b>false</b> - if
	 *         copying was interrupted by listener
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os,
			CopyListener listener, int bufferSize) throws IOException {
		int current = 0;
		int total = is.available();
		if (total <= 0) {
			total = DEFAULT_IMAGE_TOTAL_SIZE;
		}

		final byte[] bytes = new byte[bufferSize];
		int count;
		if (shouldStopLoading(listener, current, total))
			return false;
		while ((count = is.read(bytes, 0, bufferSize)) != -1) {
			os.write(bytes, 0, count);
			current += count;
			if (shouldStopLoading(listener, current, total))
				return false;
		}
		os.flush();
		return true;
	}

	private static boolean shouldStopLoading(CopyListener listener,
			int current, int total) {
		if (listener != null) {
			boolean shouldContinue = listener.onBytesCopied(current, total);
			if (!shouldContinue) {
				if (100 * current / total < CONTINUE_LOADING_PERCENTAGE) {
					return true; // if loaded more than 75% then continue
									// loading anyway
				}
			}
		}
		return false;
	}

	/**
	 * Reads all data from stream and close it silently
	 * 
	 * @param is
	 *            Input stream
	 */
	public static void readAndCloseStream(InputStream is) {
		final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1)
				;
		} catch (IOException ignored) {
		} finally {
			closeSilently(is);
		}
	}

	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ignored) {
			}
		}
	}

	/** Listener and controller for copy process */
	public static interface CopyListener {
		/**
		 * @param current
		 *            Loaded bytes
		 * @param total
		 *            Total bytes for loading
		 * @return <b>true</b> - if copying should be continued; <b>false</b> -
		 *         if copying should be interrupted
		 */
		boolean onBytesCopied(int current, int total);
	}

}
