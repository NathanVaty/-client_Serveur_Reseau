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
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author Nathan Vaty
 */
public class Client {
    
    Client(){
        
    }
    
    // méthode de connexion avec chat simple
    public void connexChatSimple(String ipServer, int numPortServer, 
                                           String choixChat) throws IOException{
         // Saisie du clavier
        Scanner entree = new Scanner(System.in);
        // variable locale
        Socket echoSocket = null;
        PrintWriter fluxSortant = null; // Flux de sortie
        BufferedReader fluxEntrant = null; //Flux d'entrée
        String serverName = ""; // Nom du serveur

        // Demande de connexion lors de la création d'une socket et création
        // des flux d'entrés/sortie
        try {
            echoSocket = new Socket(ipServer, numPortServer);
            fluxSortant = new PrintWriter(echoSocket.getOutputStream(), true);
            fluxEntrant =  new BufferedReader(new InputStreamReader(
                                                  echoSocket.getInputStream()));
            serverName = echoSocket.getInetAddress().getHostName();
            System.out.println("Connexion au serveur : " + serverName);
            // On envoie le choix du chat au serveur
            fluxSortant.println(choixChat);
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
    
    // méthode de connexion avec chat crypté
    public void connexChatCrypte(String ipServer, int numPortServer, 
                                           String choixChat) throws IOException{
         // Saisie du clavier
        Scanner entree = new Scanner(System.in);
        // variable locale
        Socket echoSocket = null;
        PrintWriter fluxSortant = null; // Flux de sortie
        BufferedReader fluxEntrant = null; //Flux d'entrée
        String serverName = ""; // Nom du serveur
        ChiffrementAES crypte = new ChiffrementAES(); // Message crypté
        String cleCrypte = "";

        // On vérifie la clé de chiffrement
        try {
            cleCrypte = crypte.importFromFichier("cleC");
        } catch (FileNotFoundException e){
            System.out.println("Pas de clé existante");
            crypte.exportFichier(ipServer, choixChat);
            System.exit(-1);
        }
        // Demande de connexion lors de la création d'une socket et création
        // des flux d'entrés/sortie
        try {
            echoSocket = new Socket(ipServer, numPortServer);
            fluxSortant = new PrintWriter(echoSocket.getOutputStream(), true);
            fluxEntrant =  new BufferedReader(new InputStreamReader(
                                                  echoSocket.getInputStream()));
            serverName = echoSocket.getInetAddress().getHostName();
            System.out.println("Connexion au serveur : " + serverName);
            // On envoie le choix du chat au serveur
            fluxSortant.println(choixChat);
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
                // On envoie un message crypté au serveur a travers le fluxSortant
                fluxSortant.println(crypte.cryptage(userInput));
                messServ = fluxEntrant.readLine();
                // On affiche le message du serveur en version crypté
                System.out.println("Message crypte "
                                    + "from " + serverName + " : " + messServ);
                // On affiche le message du serveur en version decrypté
                System.out.println("Message decrypte from " + serverName + " : " 
                                  + crypte.decrypter(messServ, cleCrypte));
                
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
    
    public static void main(String[] args) throws IOException{    
        // Saisie du clavier
        Scanner entree = new Scanner(System.in);
        Client client = new Client();
        // Variable utilisé pour la connxion de l'utilisateur
        String ipServer = ""; // Adressse IP du serveur
        int numPortServer; // valeur saisie par l'utilisateur => port du serveur
        String choixChat =""; // Choix du chat par le client
        boolean erreurSaisie = false;
        
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
        
        do {
            //On demande a l'utilisateur si il souhaite une version simple ou crypté
            System.out.println("Voulez vous utilisez le chat simple (s) ou le chat"
                    + " crypte (c)");
            choixChat = entree.next();
            //On met le choix de l'utilisateur en minuscule
            choixChat = choixChat.toLowerCase(); 
       
            // L'utilisateur veux utiliser le chat simple
            switch (choixChat) {
                case "s":
                    System.out.println("Connexion a un chat simple");
                    // On vide la mémoire tampon de la saisie
                    entree.nextLine();
                    client.connexChatSimple(ipServer,numPortServer,choixChat);
                    break;
                case "c":
                    System.out.println("Connexion a un chat crypte");
                    // On vide la mémoire tampon de la saisie
                    entree.nextLine();
                    client.connexChatSimple(ipServer,numPortServer,choixChat);
                    break;
                default:
                    System.out.println("Saisie incorrecte veuillez recommencer");
                    erreurSaisie = true;
                    // On vide la mémoire tampon de la saisie
                    entree.nextLine();
                    break;
            }
        } while (erreurSaisie);
    }
}


