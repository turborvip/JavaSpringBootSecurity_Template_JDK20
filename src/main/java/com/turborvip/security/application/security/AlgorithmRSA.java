package com.turborvip.security.application.security;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
public class AlgorithmRSA {
    private PrivateKey privateKey;
    private PublicKey publicKey;


    public AlgorithmRSA(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
    public AlgorithmRSA(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

//    public void initFromStrings(){
//        try{
//            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
//            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            publicKey = keyFactory.generatePublic(keySpecPublic);
//            privateKey = keyFactory.generatePrivate(keySpecPrivate);
//        }catch (Exception ignored){}
//    }

    public void printKeys(){
        System.err.println("Public key\n"+ encode(publicKey.getEncoded()));
        System.err.println("Private key\n"+ encode(privateKey.getEncoded()));
    }

    public String encrypt(String message) throws Exception {
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, "UTF8");
    }

    public static void main(String[] args) {
//        AlgorithmRSA rsa = new AlgorithmRSA("aaaa","bbbb");
//        rsa.initFromStrings();
//
//        try{
//            String encryptedMessage = rsa.encrypt("Hello World");
//            String decryptedMessage = rsa.decrypt(encryptedMessage);
//
//            System.err.println("Encrypted:\n"+encryptedMessage);
//            System.err.println("Decrypted:\n"+decryptedMessage);
//
//        }catch (Exception ingored){}
    }
}
