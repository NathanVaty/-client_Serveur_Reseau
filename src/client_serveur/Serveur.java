/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package client_serveur;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nathan Vaty
 */
public class Serveur {
    
    public void chatSimple() {
        
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        
        BufferedReader fluxEntree;
        PrintWriter fluxSortie;
        BufferedReader stdOut = new BufferedReader(new InputStreamReader(
                System.in));
        
        // Valeur par défaut de l'écoute du port
        int portListen=4444;
        
        try {
            serverSocket = new ServerSocket(portListen);
        } catch (IOException e) {
            System.out.println();
            System.exit(-1);
        }
        
        try {
            boolean running = true;
            clientSocket = serverSocket.accept();
            String hostName = clientSocket.getInetAddress().getHostName();
            fluxSortie = new PrintWriter(clientSocket.getOutputStream(),true);
            fluxEntree = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            System.out.println("Connexion de: " + hostName);
            String servInput;
            String messageEntrant;
            String modeConnexion;
            modeConnexion = fluxEntree.readLine();
            if (modeConnexion.equals("s")) {
                while(running){
                    messageEntrant = fluxEntree.readLine();
                    System.out.println("from: " + hostName + ": " + messageEntrant);
                    if(messageEntrant.equals("bye")) {
                        running = false;
                    } else {
                        servInput = stdOut.readLine();
                        fluxSortie.println(servInput);
                        if (servInput.equals("bye")) {
                            running = false;
                        }
                    }
                }
            } else {
                ChiffrementAES aes = new ChiffrementAES();
                try {
                    String clefPartagee = aes.importFromFichier("cleS");
                    while(running){
                    messageEntrant = fluxEntree.readLine();
                    System.out.println("Message chiffré");
                    System.out.println("from: " + hostName + ": " + messageEntrant);
                    System.out.println("Message déchiffré");
                    System.out.println("from: " + hostName + ": " + aes.decrypter(messageEntrant, clefPartagee));
                    if(aes.decrypter(messageEntrant, clefPartagee).equals("bye")) {
                        running = false;
                    } else {
                        servInput = stdOut.readLine();
                        fluxSortie.println(aes.cryptage(servInput,clefPartagee));
                        if (servInput.equals("bye")) {
                            running = false;
                        }
                    }
                }    
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Pas de clef partagée");
                }
            }
            
            
            clientSocket.close();
            serverSocket.close();
        } catch(IOException e) {
            System.out.println("Accept failed on port 4444");
            System.exit(-1);
        }
    }
}








