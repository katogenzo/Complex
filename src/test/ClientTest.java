/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package test;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * How to send a request directly using {@link HttpClient}.
 * 
 * @since 4.0
 */
public class ClientTest {
//	static {
//		System.setProperty("java.net.useSystemProxies", "true");
//	}

	public static void main(String[] args) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("192.168.1.158", 8080, "http");
//		httpclient.getCredentialsProvider().setCredentials(new AuthScope("192.168.1.158", 8080),new UsernamePasswordCredentials("", ""));
		try {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			HttpHost target = new HttpHost("www.drugoogle.com", 80, "http");
			HttpGet httpget = new HttpGet("/index/index.htm");

			System.out.println("executing request to " + httpget);

			HttpResponse rsp = httpclient.execute(target,httpget);
			HttpEntity entity = rsp.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(rsp.getStatusLine());
			Header[] headers = rsp.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			if (entity != null) {
				System.out.println(EntityUtils.toString(entity,"UTF-8"));
			}

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

}
