package com.base.network.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** Created by miqi on 2017/7/12. */
public class Encryptor {

  private static final String key = "268c31e264550b06c1e18d022f84c48a";
  private static final String iv = "3a64180a57ee21af9ecdd1836d206c41";

  /** 补码方式为nopadding */
  public static final String NoPadding = "AES/CBC/NoPadding";

  /** 补码方式为PKCS5PADDING */
  public static final String PKCS5Padding = "AES/CBC/PKCS5PADDING";

  /** 补码方式为PKCS7PADDING */
  public static final String PKCS7Padding = "AES/CBC/PKCS7PADDING";

  /**
   * hex转为byte参数
   */
  public static byte[] hexStr2Bytes(String hexStr) {
    hexStr = hexStr.toLowerCase();
    char[] hexs = hexStr.toCharArray();
    byte[] bytes = new byte[hexStr.length() / 2];
    int n;
    int j;
    for (int i = 0; i < bytes.length; i++) {
      if (hexs[2 * i] < 0x3a) {
        n = hexs[2 * i] - 0x30; // 3 12312  16
      } else {
        n = hexs[2 * i] - 0x57; // 10 b11 acbd
      }

      if (hexs[2 * i + 1] < 0x3a) {
        j = hexs[2 * i + 1] - 0x30;
      } else {
        j = hexs[2 * i + 1] - 0x57;
      }
      bytes[i] = (byte) (n * 16 + j); // byte
    }
    return bytes;
  }

  /**
   * aes-128-cbc解密
   *
   * @param data 要进行解密得数据
   * @return 解密后得数据
   */
  public static byte[] decrypt(byte[] data) {
    byte[] bkeys = hexStr2Bytes(key);
    byte[] bivs = hexStr2Bytes(iv);
    try {
      IvParameterSpec ivParameterSpec = new IvParameterSpec(bivs);
      SecretKeySpec secretKeySpec = new SecretKeySpec(bkeys, "AES");
      Cipher cipher = Cipher.getInstance(NoPadding);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

      byte[] original = cipher.doFinal(fixDataLength(data));
      byte[] originall = new byte[original.length];
      System.arraycopy(original, 0, originall, 0, original.length );
      return fixDataLength(originall);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * aes-128-cbc加密
   *
   * @param data 要加密得数据
   * @return 加密后得数据
   */
  public static byte[] encrypt(byte[] data) {
    byte[] bkeys = hexStr2Bytes(key);
    byte[] bivs = hexStr2Bytes(iv);
    try {
      IvParameterSpec ivParameterSpec = new IvParameterSpec(bivs);
      SecretKeySpec secretKeySpec = new SecretKeySpec(bkeys, "AES");

      Cipher cipher = Cipher.getInstance(NoPadding);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      return cipher.doFinal(fixDataLength(data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 流数据数据补全，不够128位得倍数要补全128位的倍数
   *
   */
  public static byte[] fixDataLength(byte[] data) {
    int fixLength = data.length % 16;
    if (fixLength == 0) {
      return data;
    }
    byte[] fixData = new byte[data.length + (16 - fixLength)];
    for (int i = fixData.length - 1; i >= data.length; i--) {
      fixData[i] = (byte) 0;
    }
    System.arraycopy(data, 0, fixData, 0, data.length);
    return fixData;
  }

  /**
   * GZIP解压缩
   */
  public static byte[] uncompress(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    if (!isGzip(bytes)) {
      return null;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
    try {
      GZIPInputStream ungzip = new GZIPInputStream(in);
      byte[] buffer = new byte[256];
      int n;
      while ((n = ungzip.read(buffer)) >= 0) {
        out.write(buffer, 0, n);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return out.toByteArray();
  }

  /**
   * 判断byte[]是否是Gzip格式
   */
  private static boolean isGzip(byte[] data) {
    int header = (data[0] << 8) | data[1] & 0xFF;
    return header == 0x1f8b;
  }

}
