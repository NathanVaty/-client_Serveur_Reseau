/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Nathan Vaty
 */
public class Serveur {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        
                BufferedReader fluxEntree;
                PrintWriter fluxSortie; 
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(
                                           System.in));
            
        try {
            serverSocket = new ServerSocket(4444);
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
             clientSocket.close();
             serverSocket.close();
        } catch(IOException e) {
            System.out.println("Accept failed on port 4444");
            System.exit(-1);
        }
    }
}









































