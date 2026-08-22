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

	public boolean isTokenValid(String token) {
		try {
			String[] parts = token.split("\\.");

			if (parts.length != 3) {
				return false;
			}

			String data = parts[0] + "." + parts[1];
			String signature = createSignature(data);

			if (!signature.equals(parts[2])) {
				return false;
			}

			long expiryTime = getExpiryTime(token);
			return expiryTime > Instant.now().toEpochMilli();
		} catch (Exception ex) {
			return false;
		}
	}

	public String getEmailFromToken(String token) {
		String payload = decode(token.split("\\.")[1]);
		return getValue(payload, "sub");
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

	private String decode(String data) {
		byte[] decoded = Base64.getUrlDecoder().decode(data);
		return new String(decoded, StandardCharsets.UTF_8);
	}

	private long getExpiryTime(String token) {
		String payload = decode(token.split("\\.")[1]);
		String expiry = getValue(payload, "exp");
		return Long.parseLong(expiry);
	}

	private String getValue(String json, String key) {
		String search = "\"" + key + "\":";
		int start = json.indexOf(search) + search.length();

		if (json.charAt(start) == '"') {
			int valueStart = start + 1;
			int valueEnd = json.indexOf("\"", valueStart);
			return json.substring(valueStart, valueEnd);
		}

		int valueEnd = json.indexOf(",", start);
		if (valueEnd == -1) {
			valueEnd = json.indexOf("}", start);
		}

		return json.substring(start, valueEnd);
	}
}
