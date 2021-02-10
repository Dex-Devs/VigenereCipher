package com.example.myapp.java;

import java.util.*;
import edu.duke.*;
import java.io.File;

public class VigenereBreaker {
    
    //  divide encrypted message
    public String sliceString(String message, int whichSlice, int totalSlices) {
        
        StringBuilder sb = new StringBuilder();
        
        for(int i = whichSlice ; i < message.length() ; i+= totalSlices) { 
            sb.append(message.charAt(i));
            
        }
        return sb.toString();
    }
    
    //  get keyLenghts
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        
        CaesarCracker cCracker = new CaesarCracker();
        
        for(int i = 0 ; i < key.length ; i++) {
            String slice = sliceString(encrypted, i, key.length);
            key[i] = cCracker.getKey(slice);
        }
        return key;
    }

    //  build HashSet of words -- will serve as DICTIONARY
    public HashSet<String> readDictionary(FileResource fr) { 
        
        HashSet<String> wordsFromDictionary = new HashSet<>();
        
        // ASSUMING THAT THE FILE READ HAS ONE WORD ON EACH LINE AND EVERY WORD IS UNIQUE FROM OTHERS
        for(String word : fr.lines()) {
            wordsFromDictionary.add(word.toLowerCase());
        }
        
        return wordsFromDictionary;
    }
    
    //  get Character that appears the most in the dictionary
    private char mostCommonCharIn(HashSet<String> wordsInDictionary) { 
        
        String alphabetLower = "abcdefghijklmnopqrstuvwxyz";
        
        int [] alphabetCount = new int[26];
        
        for(String word : wordsInDictionary) {
            String lowerCaseWord = word.toLowerCase(); // convert
            for(int i = 0 ; i < lowerCaseWord.length() ; i++) { 
                int index = alphabetLower.indexOf(word.charAt(i));
                
                if(index != -1) {
                    alphabetCount[index] +=1;
                }
            }
        }
        
        // ASSUMING THAT COUNTS HAS BEEN MAD
        int max = 0;
        int indexOfMax = 0;
        for(int i = 0 ; i < alphabetCount.length ; i++) { 
            if(alphabetCount[i] > max) { 
                max = alphabetCount[i];
                indexOfMax = i;
            }
        }
        
        return alphabetLower.charAt(indexOfMax);
    }
    
    //  get real/valid word counts
    public int countWords(String message, HashSet<String> dictionary) {

        int count = 0;
        for(String word : message.split("\\W")) {
            if(dictionary.contains(word.toLowerCase())){
                count+=1;
            }
        }
        
        return count;
    }
    
    //  get decrypted String with most number of valid words using a particular dictionary
    public String breakForLanguage(String encrypted, HashSet<String> dictionary) throws InterruptedException { 
        
        Thread.sleep(1500);
        int keyLengthMostValidWords = 0;
        int validWordCounts = 0;
        int [] keysGot = null;

//        try every keylength from 1-100 to obtain keys
        for(int i = 1; i < 100 ; i++) { 
//            use keys obtained to decrypt message
            char mostCommonCharInLanguage = mostCommonCharIn(dictionary);
            int[] keysFromCurrKLength = tryKeyLength(encrypted, i, mostCommonCharInLanguage);
                    
            VigenereCipher vCipher = new VigenereCipher(keysFromCurrKLength);
//            decrypt message using keyLength got
            String decrypted = vCipher.decrypt(encrypted);
//            count number of valid words
            int initValidWordsCount = countWords(decrypted, dictionary);
            
//            compare count to others
            if(initValidWordsCount > validWordCounts) { 
                keyLengthMostValidWords = i; // set keylenght of decryption with most number of valid words
                validWordCounts = initValidWordsCount; //   set max valid word counts
                keysGot = keysFromCurrKLength; //   get keys used on decryption
            }
        }
        
        // ASSUMING KEYLENGTH WITH MAX VALID WORDs HAS BEEN FOUND
        int [] validKeys = tryKeyLength(encrypted, keyLengthMostValidWords, 'e');
        VigenereCipher vCipher = new VigenereCipher(validKeys);
        
        System.out.println("Keylenght used to enrypt the file: " + keyLengthMostValidWords );
        System.out.println("The decrypted String contains " + validWordCounts + " valid words.\nKeys used are");
        for(Integer key : keysGot) {
            System.out.println(key);
        }
        
        return vCipher.decrypt(encrypted);  
    }
    
    public void breakForAllLang(String encrypted, HashMap<String, HashSet<String>> languageWordMap) throws InterruptedException { 
        
        String languageUsed = null;
        String validDecryptedTexts = null;
        int mostValidWordsCount = 0;
        
        for(String language : languageWordMap.keySet()) { 
            char mostCommon = mostCommonCharIn(languageWordMap.get(language));
            HashSet<String> dictionary = languageWordMap.get(language);
            System.out.println("DECRYPTING USING THE LANGUAGE " + language + " AS DICTIONARY.........");
            
            String decryptedTexts = breakForLanguage(encrypted, dictionary);
            
            int decryptedWordCount = countWords(decryptedTexts, dictionary);
            
            if(decryptedWordCount > mostValidWordsCount) { 
                mostValidWordsCount = decryptedWordCount;
                languageUsed = language;
                validDecryptedTexts = decryptedTexts;
                
            }
        }
        
        System.out.println("Language used for effective decryprion: " + languageUsed + "\nNumber of valid words from dictionary: " + mostValidWordsCount
                            + "\nDecrypted Text: \n" + validDecryptedTexts);
    } 
    
    // test
    public void breakVigenere () throws InterruptedException {
        
        FileResource fr = new FileResource();
        
        String fileContents = fr.asString(); // return contents as String
        System.out.println("Encrypted file contents: \n" +  fileContents);
        
        // ------------------------------------------------------------------ //
//        int [] keysGot = tryKeyLength(fileContents, 4, 'e');
//        
//        System.out.println("KEYS FOUND");
//        for(Integer key : keysGot) { 
//            System.out.println("Key " + key);
//        }
//        VigenereCipher vCipher = new VigenereCipher(keysGot);
//        System.out.println(vCipher.decrypt(fileContents));
        
        // ------------------------------------------------------------------ //

        // USING ENGLISH WORDS AS DICTIONARY 
        
//        FileResource dictionaryFile = new FileResource("com/example/myapp/dictionaries/English");
//        // dictionary
//        HashSet<String> englishDictionary = readDictionary(dictionaryFile); // read from dictionary
//        
//        String read = breakForLanguage(fileContents, englishDictionary);
//        
//        System.out.println("Most valid decryption returned: \n" + read);
        
        // ------------------------------------------------------------------ //
        
        // FINDING THE DICTIONARY THAT IS USED FOR DECRYPTION
        
        HashMap<String, HashSet<String>> languageWordMap = new HashMap<>();
        
        DirectoryResource languageFile = new DirectoryResource(); // select all files
        
        // iterate through all filenames
        for(File file : languageFile.selectedFiles()) { 
            String language = file.getName();
            
            FileResource resourcFromFileName = new FileResource("com/example/myapp/dictionaries/" + language); // create instance of file resource using file name
            
            //  all dictionaries must be unique -- replace dictionary if there are repeatitions
            HashSet<String> dictionary = readDictionary(resourcFromFileName);
            languageWordMap.put(language, dictionary);
            
        }
        
        // printing DECRYPTION DETAILS
        
        breakForAllLang(fileContents, languageWordMap);
        // ------------------------------------------------------------------ //
        
       
        
     

    }
}
