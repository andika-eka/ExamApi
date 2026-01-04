package com.technicaltale.examapi.util;

public class Hashing {

    private String secret;
    public Hashing(String secret){
        this.secret = secret;
    }

    public int getSecretIndex(String key, int modulus){
        return Hashing.getIndex(key+secret, modulus);
    }

    public static int getIndex(String key, int modulus) {
        // 0x7FFFFFFF is the bitmask for positive integers
        return (key.hashCode() & 0x7FFFFFFF) % modulus;
    }



}
