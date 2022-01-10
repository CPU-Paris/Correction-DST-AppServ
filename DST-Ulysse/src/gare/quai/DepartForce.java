package gare.quai;

import java.util.TimerTask;

public class DepartForce extends TimerTask {

    private final Quai quai;

    public DepartForce(Quai quai) {
        this.quai = quai;
    }

    @Override
    public void run() {
        this.quai.liberer();
    }
}
