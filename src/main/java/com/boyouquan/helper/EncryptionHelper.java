package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public class EncryptionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionHelper.class);

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    public String encrypt(String plainText) {
        try {
            byte[] keyBytes = ensureKeyLength(boYouQuanConfig.getEncryptionKey()).getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encryptedBytes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] keyBytes = ensureKeyLength(boYouQuanConfig.getEncryptionKey()).getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] encryptedBytes = HexFormat.of().parseHex(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private String ensureKeyLength(String key) {
        int requiredLength = KEY_SIZE / 8;
        if (key.length() < requiredLength) {
            return String.format("%-" + requiredLength + "s", key).substring(0, requiredLength);
        } else if (key.length() > requiredLength) {
            return key.substring(0, requiredLength);
        }
        return key;
    }

}
