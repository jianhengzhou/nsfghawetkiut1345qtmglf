/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
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
package com.gdestiny.github.cache.disk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.nostra13.universalimageloader.utils.IoUtils;


public interface DiskCache {
	public File getDirectory();

	// sub
	/**
	 * hash
	 * 
	 * @param name
	 * @return
	 */
	public void addSubDirectory(String name);

	public File getSubDirectory(String name);

	public File get(String subDir, String name);

	public boolean save(String subDir, String name, InputStream inStream,
			IoUtils.CopyListener listener) throws IOException;

	public boolean save(String subDir, String name, String content)
			throws IOException;

	public boolean save(String name, InputStream inStream,
			IoUtils.CopyListener listener) throws IOException;

	public boolean save(String name, String content) throws IOException;

	public boolean save(String name, Serializable object) throws IOException;

	public boolean save(String subDir, String name, Serializable object)
			throws IOException;

	public boolean remove(String subDir, String name);

	public File get(String name);

	public String getContent(String name);

	public Serializable getObject(String name);

	public String getContent(String subDir, String name);

	public Serializable getObject(String subDir, String name);

	public boolean remove(String name);

	public void close();

	public void clear();
}
