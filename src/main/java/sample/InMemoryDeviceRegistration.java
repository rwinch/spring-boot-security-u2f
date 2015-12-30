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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yubico.u2f.data.DeviceRegistration;

/**
 * @author Rob Winch
 *
 */
public class InMemoryDeviceRegistration {
	private Map<String,List<DeviceRegistration>> usernameToRegistration = new ConcurrentHashMap<>();

	public void saveRegistrationForUsername(DeviceRegistration registration, String username) {
		List<DeviceRegistration> registrations = findRegistrationsByUsername(username);
		registrations.add(registration);
		usernameToRegistration.put(username, registrations);
	}

	public List<DeviceRegistration> findRegistrationsByUsername(String username) {
		List<DeviceRegistration> result = usernameToRegistration.get(username);
		if(result == null) {
			result = new ArrayList<>();
		}
		return result;
	}
}
