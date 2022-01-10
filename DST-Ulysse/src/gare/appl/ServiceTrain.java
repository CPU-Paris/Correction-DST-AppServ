package gare.appl;

import gare.quai.Quai;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServiceTrain implements Runnable {

    private static Map<Integer, Quai> planning;
    private static List<Quai> quais;

    public static void init(Map<Integer, Quai> planning,List<Quai> quais){
        ServiceTrain.planning = planning;
        ServiceTrain.quais = quais;
    }

    private final Socket clientSocket;

    public ServiceTrain(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            String lineFromClient = in.readLine();
            Integer trainNumber = Integer.parseInt(lineFromClient);

            Quai quaiForThisTrain = ServiceTrain.planning.get(trainNumber);

            if(quaiForThisTrain == null){
                out.println("Votre train n'est pas attendu à la gare aujourd'hui !");
                this.clientSocket.close();
                return;
            }

            if(quaiForThisTrain.train() == trainNumber){
                quaiForThisTrain.liberer();
                out.println("Bon voyage !");
            } else {
                quaiForThisTrain.accueillir(trainNumber);
                out.format("Le numéro de votre quai est le %d", quaiForThisTrain.numeroQuai());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
