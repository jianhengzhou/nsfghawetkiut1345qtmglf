/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdestiny.github.app;

import java.net.HttpURLConnection;

import org.eclipse.egit.github.core.client.GitHubClient;

import com.gdestiny.github.utils.Constants;

public class DefaultClient extends GitHubClient {

	public DefaultClient() {
		super();

		setSerializeNulls(false);
	}

	@Override
	protected HttpURLConnection configureRequest(HttpURLConnection request) {
		super.configureRequest(request);
		// full html
		request.setRequestProperty(HEADER_ACCEPT,
				"application/vnd.github.beta.full+json");
		request.setConnectTimeout(Constants.CONNECT_TIMEOUT);
		request.setReadTimeout(Constants.READ_TIMEOUT);
		return request;
	}
}
