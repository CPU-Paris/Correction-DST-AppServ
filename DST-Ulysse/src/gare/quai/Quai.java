package gare.quai;

import java.util.Timer;

public class Quai {

    private final int numeroQuai;
    private Timer timer;
    private int train = -1;

    public Quai(int numeroQuai) {
        this.numeroQuai = numeroQuai;
    }

    public int train() {
        return train;
    }

    public int numeroQuai(){
        return this.numeroQuai;
    }

    public void accueillir(int numTrain) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (this.train == -1) {
                    this.train = numTrain;
                    this.timer = new Timer();
                    this.timer.schedule(new DepartForce(this), 1000 * 60 * 15);
                    break;
                } else {
                    this.wait();
                }
            }
        }

    }

    public void liberer() {
        synchronized (this){
            this.train = -1;
            this.timer.cancel();
            this.notifyAll();
        }
    }

}
