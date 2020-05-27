package com.asiainfo.xwbo.xwbo.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESXnwTools {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public String sKey = "ScsfywlLahmyyc12";
    private String ivParameter = "abcdefghabcdefgh";
    private static AESXnwTools instance = null;

    public AESXnwTools() {

    }

    public static AESXnwTools getInstance() {
        if (instance == null)
            instance = new AESXnwTools();
        return instance;
    }

    public static String Encrypt(String encData, String secretKey, String vector)
            throws Exception {

        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec( raw , "AES" );
        IvParameterSpec iv = new IvParameterSpec( vector.getBytes() );// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv );
        byte[] encrypted = cipher.doFinal( encData.getBytes( "utf-8" ) );
        return new BASE64Encoder().encode( encrypted );// 此处使用BASE64做转码。
    }

    // 加密
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec( raw , "AES" );
        IvParameterSpec iv = new IvParameterSpec( ivParameter.getBytes() );// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv );
        byte[] encrypted = cipher.doFinal( sSrc.getBytes( "utf-8" ) );
        return new BASE64Encoder().encode( encrypted );// 此处使用BASE64做转码。
    }

    // 解密
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes( "ASCII" );
            SecretKeySpec skeySpec = new SecretKeySpec( raw , "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            IvParameterSpec iv = new IvParameterSpec( ivParameter.getBytes() );
            cipher.init( Cipher.DECRYPT_MODE, skeySpec, iv );
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer( sSrc );// 先用base64解密
            byte[] original = cipher.doFinal( encrypted1 );
            String originalString = new String( original , "utf-8" );
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public String decrypt(String sSrc, String key, String ivs) throws Exception {
        try {
            byte[] raw = key.getBytes( "ASCII" );
            SecretKeySpec skeySpec = new SecretKeySpec( raw , "AES" );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
            IvParameterSpec iv = new IvParameterSpec( ivs.getBytes() );
            cipher.init( Cipher.DECRYPT_MODE, skeySpec, iv );
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer( sSrc );// 先用base64解密
            byte[] original = cipher.doFinal( encrypted1 );
            String originalString = new String( original , "utf-8" );
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append( (char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')) );
            strBuf.append( (char) (((bytes[i]) & 0xF) + ((int) 'a')) );
        }

        return strBuf.toString();
    }

    public static void main(String[] args) throws Exception {
        /*
         * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
         * 此处使用AES-128-CBC加密模式，key需要为16位。
         */
//        String host = "https://xxx自己服务器地址/lsApp/xnw/getVpmn_qqw_aes.jsp?";
//        String time =String.valueOf(System.currentTimeMillis()).substring(0,8);
//        String imei = "123";
//        String thisBill = "13857088805";
//        String billList = "18767800051,18358888801,13857088805,19858362399";
//        String mac_v =imei +thisBill+billList+time;
//        String mac =    MD5Util.getMD5(mac_v+ MD5Util.appkey);
//        String url =host+"id="+imei+"&thisBill="+thisBill+"&billList="+billList+"&mac="+mac;
//
//        System.out.println("url="+url);
        // 需要加密的字串
        String cSrc = "121261";
       
        String enString =  AESXnwTools.getInstance().encrypt( cSrc );
        // 加密
        System.out.println( "加密后的字串是：" + enString );
        // 解密
        String DeString = AESXnwTools.getInstance().decrypt( "FWI1oIwWyOwx8woV+2CSkQ==" );
        System.out.println( "解密后的字串是：" + DeString );
    }

}
