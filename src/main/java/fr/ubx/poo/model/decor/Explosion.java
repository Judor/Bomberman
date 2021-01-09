package fr.ubx.poo.model.decor;

import java.util.Timer;
import java.util.TimerTask;

public class Explosion extends Decor {
    private boolean explosionSaw=false;
    Timer t=new Timer();

    @Override
    public String toString() {
        return "Explosion";
    }

    public void explode() {
        t.schedule(setExplosionSaw, 1000);
    }



    private TimerTask setExplosionSaw = new TimerTask() {
        @Override
        public void run() {
            explosionSaw=true;
        }
    };


}