package com.fundoonotes.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final String secret;
	private final long expiration;

	public JwtService(@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration}") long expiration) {
		this.secret = secret;
		this.expiration = expiration;
	}

	public String generateToken(String email) {
		try {
			String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
			long expiryTime = Instant.now().toEpochMilli() + expiration;
			String payload = "{\"sub\":\"" + email + "\",\"exp\":" + new Date(expiryTime).getTime() + "}";

			String headerPart = encode(header.getBytes(StandardCharsets.UTF_8));
			String payloadPart = encode(payload.getBytes(StandardCharsets.UTF_8));
			String data = headerPart + "." + payloadPart;
			String signature = createSignature(data);

			return data + "." + signature;
		} catch (Exception ex) {
			throw new RuntimeException("Token generation failed");
		}
	}

	private String createSignature(String data) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(keySpec);
		return encode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
	}

	private String encode(byte[] data) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
	}
}
