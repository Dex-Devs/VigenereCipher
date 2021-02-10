package com.example.myapp.java;

import edu.duke.FileResource;

public class Tester {
    
    // read File contents -- return String
    private String readTextsFromFile() { 
//        FileResource file = new FileResource(this.dataDirectory);
        FileResource file = new FileResource();
        
        return file.asString(); // return entire contents of a file as String
    }
    
    public void testCaesarCipher() { 
        CaesarCipher cCipherTester = new CaesarCipher(5);
        
        String textsEncrypted = cCipherTester.encrypt(readTextsFromFile());
        System.out.printf("%s%n%n%s%n", "Returned enrypted using CAESAR CIPHER: ",textsEncrypted );
        
    }
    
    public void testCaesarCracker() { 
        CaesarCracker cBreakerTester = new CaesarCracker();
        
        String textsDecrypted = cBreakerTester.decrypt(readTextsFromFile());
        System.out.printf("%s%n%n%s%n", "Returned decrypted using CAESAR CIPHER: ",textsDecrypted );
    }
    
    public void testVigenereCipher() { 
        int [] keys = {17, 14, 12, 4};
        
        VigenereCipher vCipherTester = new VigenereCipher(keys);
        String encrypted = vCipherTester.encrypt(readTextsFromFile());
        System.out.printf("%s%n%n%s", "Returned enrypted using VIGENERE CIPHER: ",encrypted);
        
    }
    
    public void testVigenereBreaker() throws InterruptedException {
        VigenereBreaker vBreaker = new VigenereBreaker();
        vBreaker.breakVigenere();
    }
    public static void main(String [] args) throws InterruptedException { 
        Tester test = new Tester();
//        test.testCaesarCipher();
//        test.testCaesarCracker();
//        test.testVigenereCipher();
        test.testVigenereBreaker();

       
        
    }
}
