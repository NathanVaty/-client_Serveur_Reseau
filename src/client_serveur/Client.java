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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Client {
    // main => methode
    public static void main(String[] args) throws IOException{    
        // variable locale
        Socket echoSocket = null;
        PrintWriter fluxSortant = null; // Flux de sortie
        BufferedReader fluxEntrant = null; //Flux d'entrée
        Scanner entree = new Scanner(System.in);
        // Une saisie  ip interactive
        String ipServer = ""; // TODO Utiliser une méthode du serveur pour récuperer les noms
        int numPortServer; // valeur saisie par l'utilisateur => port du serveur
        String serverName = ""; // Nom du serveur
        
        // L'utilisateur saisie l'ip et le port du serveur
        System.out.println("Bonjour ! Veuilez saisir l'ip de votre serveur");
        System.out.print("ip du serveur : ");
        ipServer = entree.next();
        System.out.println("Veuillez saisir le port d'écoute du serveur"
                         + " (port défaut : 4444)");
        System.out.print("Port du serveur : ");
        numPortServer = entree.nextInt();
        // On vide la mémoire tampon de la saisie
        entree.nextLine();
        
        // Demande de connexion lors de la création d'une socket et création
        // des flux d'entrés/sortie
        try {
            echoSocket = new Socket(ipServer, numPortServer);
            fluxSortant = new PrintWriter(echoSocket.getOutputStream(), true);
            fluxEntrant =  new BufferedReader(new InputStreamReader(
                                                  echoSocket.getInputStream()));
            serverName = echoSocket.getInetAddress().getHostName();
            System.out.println("Connexion au serveur : " + serverName);
             
        } catch(UnknownHostException e){
            System.out.println("Destination unknow" + ipServer);
            System.exit(-1);
        } catch(IOException e){
            System.out.println("Now to investigate this I/O issue to" 
                                                          + ipServer);
            System.exit(-1);
        }
       
        
        String userInput = "";
        boolean running = true;
        String messServ = "";
        // On effectue la saisie sur le flux d'entré 
        try {     
            while(running){
                userInput = entree.nextLine();
                fluxSortant.println(userInput);
                messServ = fluxEntrant.readLine();
                // On affiche le message du serveur
                System.out.println("from " + serverName + " : " + messServ);
                // Si le serveur renvoie bye on arrete la connexion
                if (messServ.equals("bye")){
                    running = false;
                } else {
                    // Si l'utilisateur saisie le message bye on ferme la connexion
                    if (userInput.equals("bye")){
                        running = false;
                    }                        
                }
            }
        } catch (IOException e){
                System.out.println("Now to investigate this I/O issue to" 
                                                          + ipServer);
            System.exit(-1);
        } catch (Exception ex){
            System.out.println("Fermeture de la connexion au serveur");
        }   
        // On ferme la connexion et les différents flux ouvert
        fluxEntrant.close();
        fluxSortant.close();
        echoSocket.close();
    }
}



