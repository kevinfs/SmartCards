package com.iris.controller;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iris.model.User;
import com.iris.repository.UserRepository;
import com.iris.service.Tools;

@RestController
public class LoginController {

	@Autowired
	private Tools tools;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/graine")
	public String graine() {
		return String.valueOf(tools.alea(1000));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public String step2(@RequestParam(value = "login") String login) {

		User user = userRepository.findByLogin(login);
		user.updateNumberToSign();
		userRepository.save(user);
		return user != null ? user.getSel() + ";" + user.getNumberToSign()
				: String.valueOf(tools.alea(1000)) + ";" + String.valueOf(tools.alea(1000));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/password")
	public String step3(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
			@RequestParam(value = "graine") String graine, @RequestParam(value = "card") String card) {

		User user = userRepository.findByLogin(login);
		if (user == null) {
			return "KO1";
		}

		String v1 = user.getPassword();
		String v2 = tools.md5(v1 + graine);

		if (password.equals(v2)) {

			// Check card
			byte[] signature = Base64.getMimeDecoder().decode(card);

			// Get public Key
			String key = user.getPublicKey();

			try {

				byte[] decodedKey = Base64.getMimeDecoder().decode(key);
				KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
				X509EncodedKeySpec ecpks = new X509EncodedKeySpec(decodedKey);
				PublicKey publicKey = keyFactory.generatePublic(ecpks);

				boolean b = Tools.verifECDSA(user.getNumberToSign().getBytes(), publicKey, signature);
				System.out.println("Valid : " + b + "\n");
				if (b)
					return "OK";
				else
					return "KOO";

			} catch (Exception e) {

			}

		}

		return "KO";
	}
}
