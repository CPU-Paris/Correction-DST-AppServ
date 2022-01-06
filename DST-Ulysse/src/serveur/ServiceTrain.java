package serveur;
import java.util.*;
import java.io.*;
import java.net.Socket;
import gare.Quai;
/**
 * Classe appelée à chaque fois qu'un train se connecte à notre serveur
 * @file /src/serveur/ServiceTrain.java
 */
public class ServiceTrain implements Runnable {
    private static List<Quai> lesQuais;
    private static Map<Integer, Quai> planning;
    public static void init(List<Quai> lesQuais, Map<Integer, Quai> lePlanning) {
        ServiceTrain.lesQuais = lesQuais;
        ServiceTrain.planning = lePlanning;
    }

    // chaque thread instancié possède son propre SocketServeur
    private Socket socketServeur;
    public ServiceTrain(Socket socketServeur) {
        this.socketServeur = socketServeur;
        // ??
        new Thread(this).start();
    }
    public void run() {
        BufferedReader socketIn = new BufferedReader (new InputStreamReader(this.socketServeur.getInputStream() ));
        PrintWriter socketOut = new PrintWriter (this.socketServeur.getOutputStream ( ), true);
        int IdTrain = Integer.parseInt(socketIn.readLine());
        Quai quaiDuTrain = planning.get(IdTrain);
        if (quaiDuTrain == null) {
            socketOut.println("Erreur : Le train n'est pas dans le planning");
            return;
        }

        String response;
        synchronized (quaiDuTrain) {
            if (quaiDuTrain.train() == IdTrain) {
                quaiDuTrain.liberer();
                response = "Bon voyage !";
            } else {
                quaiDuTrain.accueilir();
                response = "" + quaiDuTrain.getNoQuais;
            }
        }
        socketOut.println (response);
    }

    @Override
    public void finalize() {
        try {
            socketServeur.close();
        } catch (IOException e) {}
    }
}
