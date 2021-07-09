package com.corp.utils;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtil{
    private static final String signature_algorithm = "SHA256withRSA"; // 签名算法
    private static final String encryptAlgorithm = "RSA"; // 加密算法
    private static final String decryptAlgorithm = "RSA"; // 解密算法
    private static final String charset = "UTF-8";
    private static final int max_encrypt_block = 234; //2048位rsa单次最大加密长度
    private static final int max_decrypt_block = 256; //2048位rsa单次最大解密长度

    // 对方base64后公钥字符串,用于验签
    private static final String sign_pub_key_str = "";
    // 己方base64后私钥字符串,用于签名
    private static final String sign_pri_key_str = "";
    // 对方base64后公钥字符串,用于加密
    private static final String crypt_pub_key_str = "";
    // 己方base64后私钥字符串,用于解密
    private static final String crypt_pri_key_str = "";

    public static String sign(String param, String signPrivateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey sign_private_key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(signPrivateKey))); // 用于签名
            Signature signature = Signature.getInstance(signature_algorithm);
            signature.initSign(sign_private_key);
            signature.update(param.getBytes(charset));
            return Base64Utils.encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("签名异常", e);
        }
    }

    public static String encrypt(String param, String cryptPublicKey) {

        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException("待加密数据为空");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey crypt_public_key = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Utils.decodeFromString(cryptPublicKey))); // 用于加密
            Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, crypt_public_key);
            byte[] data = param.getBytes(charset);
            int inputLen = data.length;

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen > max_encrypt_block + offSet) {
                    cache = cipher.doFinal(data, offSet, max_encrypt_block);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * max_encrypt_block;
            }
            return Base64Utils.encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("加密异常", e);
        }
    }

    public static boolean verifySign(String param, String sign, String signPublicKey) {
        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey sign_public_key = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Utils.decodeFromString(signPublicKey))); // 用于验签
            Signature signature = Signature.getInstance(signature_algorithm);
            signature.initVerify(sign_public_key);
            signature.update(param.getBytes(charset));
            return signature.verify(Base64Utils.decodeFromString(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签失败", e);
        }
    }

    public static String decrypt(String param, String cryptPrivateKey) {

        if (StringUtils.isEmpty(param)) {
            throw new IllegalArgumentException("待解密数据为空");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey crypt_private_key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(cryptPrivateKey))); // 用于解密

            Cipher cipher = Cipher.getInstance(decryptAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, crypt_private_key);
            byte[] data = Base64Utils.decodeFromString(param);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen > max_decrypt_block + offSet) {
                    cache = cipher.doFinal(data, offSet, max_decrypt_block);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                i++;
                out.write(cache, 0, cache.length);
                offSet = i * max_decrypt_block;
            }
            return new String(out.toByteArray(), charset);
        } catch (Exception e) {
            throw new RuntimeException("解密处理异常", e);
        }
    }

    public static void main(String[] args){
//        generateKey();
//        requestDemo();
          //serverDemo();
    }


}
