package fr.ubx.poo.model.go.character;


import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Direction;
import java.util.*;

public class Bomb extends GameObject {

    Timer t=new Timer();
    private int range;

    public Bomb(Game game,Position position){
        super(game,position);
        this.range=game.getPlayer().getRange();
        t.schedule(kaboom,4000);

    }

    TimerTask kaboom = new TimerTask() {
        @Override
        public void run() {
            for (int i = 1; i <= range; i++) {
                for (Direction direction : Direction.values()) {
                    Position nextPos = direction.nextPosition(getPosition());
                    Decor nextdec = game.getWorld().get(nextPos);

                    if (nextdec instanceof Heart) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setAffichage(true);
                    }
                    if (nextdec instanceof Box) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setAffichage(true);
                    }
                    if (nextdec instanceof Bombnumberdec) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setAffichage(true);
                    }
                    if (nextdec instanceof Bombnumberinc) {
                        game.getWorld().clear(nextPos);
                        game.getWorld().setAffichage(true);
                    }
                    if (nextPos.equals(game.getPlayer().getPosition())) {
                        game.getPlayer().getHurt(game.getPlayer().getPosition());
                    }
                    List<Monster> monsters = game.getMonsters();
                    
                    for (Monster monster : monsters) {
                    	if (monster.getPosition().equals(nextPos)) {
                    		monster.getHurt(monster.getPosition());
                    	}
                    }
                    
                }
            }
            game.getPlayer().incBomb();
            game.getWorld().clear(getPosition());
            game.getWorld().setAffichage(true);
        }
    };



}
