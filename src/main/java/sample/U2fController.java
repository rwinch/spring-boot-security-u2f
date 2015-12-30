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

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yubico.u2f.U2F;
import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.AuthenticateRequestData;
import com.yubico.u2f.data.messages.AuthenticateResponse;
import com.yubico.u2f.data.messages.RegisterRequestData;
import com.yubico.u2f.data.messages.RegisterResponse;
import com.yubico.u2f.exceptions.DeviceCompromisedException;
import com.yubico.u2f.exceptions.NoEligableDevicesException;

/**
 * @author Rob Winch
 *
 */
@Controller
public class U2fController {
	@Autowired
	InMemoryRequestStorage requestStorage;

	@Autowired
	InMemoryDeviceRegistration devices;

	@Autowired
	U2F u2f;

	String SERVER_ADDRESS = "https://localhost:8443";

	@RequestMapping("/u2f/register")
	public String registerForm(Principal principal, Map<String, Object> model) {
		String username = principal.getName();
		RegisterRequestData registerRequestData = u2f.startRegistration(SERVER_ADDRESS, getRegistrations(username));
		requestStorage.save(registerRequestData);
		model.put("data", registerRequestData.toJson());
		return "u2f/register";
	}

	@RequestMapping(value = "/u2f/register", method = RequestMethod.POST)
	public String register(Principal principal, @RequestParam("tokenResponse") String response) {
		String username = principal.getName();
		RegisterResponse registerResponse = RegisterResponse.fromJson(response);
		RegisterRequestData registerRequestData = requestStorage.deleteRegistration(registerResponse.getRequestId());
		DeviceRegistration registration = u2f.finishRegistration(registerRequestData, registerResponse);
		devices.saveRegistrationForUsername(registration, username);
		return "redirect:/u2f/authenticate";
	}

	@RequestMapping("/u2f/authenticate")
	public String authenticateForm(Principal principal, Map<String, Object> model) throws NoEligableDevicesException {
		String username = principal.getName();
		// Generate a challenge for each U2F device that this user has
		// registered
		AuthenticateRequestData requestData = u2f.startAuthentication(SERVER_ADDRESS, getRegistrations(username));

		// Store the challenges for future reference
		requestStorage.save(requestData);

		// Return an HTML page containing the challenges
		model.put("data", requestData.toJson());
		return "u2f/authenticate";
	}

	@RequestMapping(value = "/u2f/authenticate", method = RequestMethod.POST)
	public String authenticate(@RequestParam String tokenResponse, Principal principal)
			throws DeviceCompromisedException {
		AuthenticateResponse response = AuthenticateResponse.fromJson(tokenResponse);
		String username = principal.getName();

		// Get the challenges that we stored when starting the authentication
		AuthenticateRequestData authenticateRequest = requestStorage.delete(response.getRequestId());

		// Verify the that the given response is valid for one of the registered
		// devices
		u2f.finishAuthentication(authenticateRequest, response, getRegistrations(username));

		return "u2f/success";
	}

	private List<DeviceRegistration> getRegistrations(String username) {
		List<DeviceRegistration> findRegistrationsByUsername = devices.findRegistrationsByUsername(username);
		return findRegistrationsByUsername;
	}

}
