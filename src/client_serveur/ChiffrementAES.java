/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_serveur;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.crypto.*;
import java.security.*;

/**
 *
 * @author Nathan Vaty
 */
public class ChiffrementAES {
    
    public ChiffrementAES() {
        
    }
    
    public String cryptage(String aCrypter) {
        try {
            byte[] data;
            byte[] result;
            
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            Key key = kg.generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data = aCrypter.getBytes();
            result = cipher.doFinal(data);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public String generateCle() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
            Key key = kg.generateKey();
            return key.toString();
    }
    public void exportFichier(String cle,String nomFichier) throws FileNotFoundException {
        try (PrintWriter fichier = new PrintWriter("fichiers/" + nomFichier+ ".txt")) {
            fichier.print(cle);
        }
    }
    
    public String importFromFichier(String nomFichier) throws FileNotFoundException, IOException {
        FileReader fichier = new FileReader("fichiers/" + nomFichier+ ".txt");
        StringBuilder cle = new StringBuilder();
        int carac = fichier.read();
        while(carac != -1) {  
            cle.append((char) carac);
            carac = fichier.read();
        }
        fichier.close();
        return cle.toString();
    }
    public String decrypter(String aDecrypter, String cle) {
        try {
            byte[] data;
            byte[] result;
            byte[] original;
            KeyGenerator kg = KeyGenerator.getInstance(cle);
            Key key = kg.generateKey();
            Cipher cipher = Cipher.getInstance(cle);
            
            data = aDecrypter.getBytes();
            result = cipher.doFinal(data);
            cipher.init(Cipher.DECRYPT_MODE, key);
            original = cipher.doFinal(result);
            return new String(original);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void main(String[] args) {
//        // TODO Auto genrated method stub
//        byte[] data;
//        byte[] result;
//        byte[] original;
//        
//        try {
//            ChiffrementAES chiffre = new ChiffrementAES();
//            chiffre.exportFichier("Salut toi", "test");
//            KeyGenerator kg = KeyGenerator.getInstance("AES");
//            Key key = kg.generateKey();
//            Cipher cipher = Cipher.getInstance("AES");
//            
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            data = "Hello World!".getBytes();
//            result = cipher.doFinal(data);
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            original = cipher.doFinal(result);
//            System.out.println(chiffre.importFromFichier("test"));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }
}

























