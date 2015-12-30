/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yubico.u2f.data.messages.AuthenticateRequestData;
import com.yubico.u2f.data.messages.RegisterRequestData;

/**
 * @author Rob Winch
 *
 */
public class InMemoryRequestStorage {

	private Map<String,AuthenticateRequestData> idToData = new ConcurrentHashMap<>();
	private Map<String,RegisterRequestData> idToRegisterData = new ConcurrentHashMap<>();

	public void save(RegisterRequestData request) {
		idToRegisterData.put(request.getRequestId(), request);
	}

	public RegisterRequestData deleteRegistration(String id) {
		return idToRegisterData.remove(id);
	}

	public void save(AuthenticateRequestData request) {
		idToData.put(request.getRequestId(), request);
	}

	public AuthenticateRequestData delete(String id) {
		return idToData.remove(id);
	}
}
