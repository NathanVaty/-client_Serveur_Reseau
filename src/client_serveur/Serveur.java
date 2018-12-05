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
        
                BufferedReader fluxEntree = new BufferedReader(new InputStreamReader(
                                            clientSocket.getInputStream()));
                PrintWriter fluxSortie = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(
                                           System.in));
            
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println();
            System.exit(-1);
        }
        
        try {
            while(true){
                clientSocket = serverSocket.accept();
                String hostName = clientSocket.getInetAddress().getHostName();
                System.out.println(hostName);
                System.out.println("from: " + hostName + ": " + fluxEntree.readLine());
                String servInput;
                while ((servInput = stdOut.readLine()) != null) {
                    fluxSortie.println(servInput);
                    
                }
            }
            
        } catch(IOException e) {
            System.out.println("Accept failed on port 4444");
            System.exit(-1);
        }
    }
}



















