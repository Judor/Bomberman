package fr.ubx.poo.view.sprite;


import fr.ubx.poo.model.go.character.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

import java.util.Timer;
import java.util.TimerTask;


public class SpriteBomb extends SpriteGameObject {
    Timer t=new Timer();
    private int state=0;

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();

        TimerTask state1 = new TimerTask() {
            @Override
            public void run() {
                state = 1;
            }
        };

        t.schedule(state1, 1000);
        TimerTask state2 = new TimerTask() {
            @Override
            public void run() {
                state = 2;
            }
        };

        t.schedule(state2, 2000);
        TimerTask state3 = new TimerTask() {
            @Override
            public void run() {
                state = 3;
            }
        };

        t.schedule(state3, 3000);
        TimerTask boom = new TimerTask() {
            @Override
            public void run() {
                state = 4;
            }
        };

        t.schedule(boom, 4000);
    }


    public void updateImage() {
        if (state < 4) {
            setImage(ImageFactory.getInstance().getBomb(state));
        } else {
            remove();
        }
    }


    public Bomb getBomb() {
        return (Bomb) this.go;
    }


}
