package fr.ubx.poo.view.sprite;


import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;

import javafx.scene.layout.Pane;
import java.util.TimerTask;
import java.util.Timer;


public class SpriteBomb extends SpriteGameObject {
    Timer t=new Timer();
    private int state=0;

    public SpriteBomb(Pane layer, Bomb bomb){
        super(layer, null, bomb);
        updateImage();
        t.schedule(state1,1000);
        t.schedule(state2,2000);
        t.schedule(state3,3000);
        t.schedule(boom,4000);
    }


    public void updateImage() {
        if (state < 4) {
            setImage(ImageFactory.getInstance().getBomb(state));
        }
        else {
            remove();
        }
    }

    public void setIndestructible(boolean indestructible) {
    }

    private TimerTask state1 = new TimerTask() {
        @Override
        public void run() {
            state=1;
        }
    };
    private TimerTask state2 = new TimerTask() {
        @Override
        public void run() {
            state=2;
        }
    };
    private TimerTask state3 = new TimerTask() {
        @Override
        public void run() {
            state=3;
        }
    };
    private TimerTask boom = new TimerTask() {
        @Override
        public void run() {
            state=4;
        }
    };


    public Bomb getBomb() {
    	return (Bomb) this.go;
    }


}
