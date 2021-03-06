/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_serveur;

import java.net.URLDecoder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Byte.decode;
import static java.lang.Integer.decode;
import javax.crypto.*;
import java.security.*;
import java.util.Base64;


import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author Nathan Vaty
 */
public class ChiffrementAES {
    
    public ChiffrementAES() {
        
    }
    
    /**
     * Permet de crypter notre message à l'aide de la clef
     * @param aCrypter
     * @param cle
     * @return 
     */
    public String cryptage(String aCrypter, String cle) {
        try {
            byte[] data;
            byte[] result;
            Cipher cipher = Cipher.getInstance("AES");
            byte[] decodedKey = Base64.getDecoder().decode(cle.getBytes("UTF-8"));
            Key key = new SecretKeySpec(decodedKey, 0,
             decodedKey.length, "AES");
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data = aCrypter.getBytes("UTF-8");
            byte[] toEncode = Base64.getDecoder().decode(data);
            result = cipher.doFinal(toEncode);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Permet la generation de la cle
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public String generateCle() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
            Key key = kg.generateKey();
            String textFromKey = Base64.getEncoder().encodeToString(key.getEncoded());
            return textFromKey;
    }
    
    /**
     * permet d'exporter notre cle dans un fichier
     * @param cle
     * @param nomFichier
     * @throws FileNotFoundException 
     */
    public void exportFichier(String cle,String nomFichier) throws FileNotFoundException {
        try (PrintWriter fichier = new PrintWriter("fichiers/" + nomFichier+ ".txt")) {
            fichier.print(cle);
        }
    }
    
    /**
     * permet de recuperer la cle à partir d'un fichier
     * @param nomFichier
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
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
    
    /**
     * Permet de decrypter un message
     * @param aDecrypter
     * @param cle
     * @return 
     */
    public String decrypter(String aDecrypter, String cle) {
        try {
            byte[] data;
            byte[] result;
            byte[] original;
            Cipher cipher = Cipher.getInstance("AES");
            byte[] decodedKey = Base64.getDecoder().decode(cle.getBytes("UTF-8"));
            Key key = new SecretKeySpec(decodedKey, 0,
             decodedKey.length, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            data = aDecrypter.getBytes("UTF-8");
            result = cipher.doFinal(Base64.getDecoder().decode(data));
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
}


