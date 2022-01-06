
package gare;

import java.util.*;

/**
 * Classe représentant un quai. S'assurer d'appeler les méthodes dans un bloc
 * synchronized de l'objet.
 * 
 * @file /src/gare/Quai.java
 */
public class Quai {
    int noQuai;

    public Quai(int noQuai) {
        this.noQuai = noQuai;
    }

    public int getNoQuais() {
        return noQuai;
    }

    private int train_a_quais = 0;

    public int train() {
        return this.train_a_quais;
    }

    private Timer timer = new Timer();

    public void liberer() {
        this.train_a_quais = 0;
        this.timer.cancel();
        this.timer.purge();
        this.notifyAll();
    }

    public void accueillir(int noTrain) {
        // tant qu'il y a un train sur le quai, on attend qu'il parte
        // (à condition que l'ID train soit un naturel positif non nul)
        while (this.train() > 0) { 
            this.wait();
        }
        this.train_a_quais = noTrain;
        this.timer.schedule(new DepartForcé(this, noTrain), 1000 * 60 * 15);
    }

    private class DepartForcé extends TimerTask {
        private Quai quai = null;
        private int noTrain = 0;

        public DepartForcé(Quai quai, int noTrain) {
            this.quai = quai;
            this.noTrain = noTrain;
        }

        @Override
        public void run() {
            synchronized (this.quai) {
                if (this.quai.train() == noTrain) {
                    this.quai.liberer();
                }
            }
        }
    }
}
