package fr.ubx.poo.model.go.character;


import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.view.sprite.SpriteBomb;

import java.util.*;

public class Bomb extends GameObject {
    private List<Position> listExplosion=new ArrayList<>();
    Timer t=new Timer();
    private int range;
    private boolean boomed=false;

    public Bomb(Game game,Position position){
        super(game,position);
        this.range=game.getPlayer().getRange();
        t.schedule(kaboom,4000);
    }

    TimerTask kaboom = new TimerTask() {
        @Override
        public void run() {
            World world = game.getWorld();
            Player player=game.getPlayer();
            List<Monster> monsters = game.getMonsters();
            for (int i = 1; i <= range; i++) {
                for (Direction direction : Direction.values()) {
                    Position nextPos = direction.nextPosition(getPosition());
                    Decor nextDec = world.get(nextPos);

                    if(world.isEmpty(nextPos)){
                        listExplosion.add(nextPos);
                    }
                    if (nextDec instanceof Heart) {
                        listExplosion.add(nextPos);
                        world.clear(nextPos);
                        world.setAffichage(true);
                    }
                    if (nextDec instanceof Box) {
                        listExplosion.add(nextPos);
                        world.clear(nextPos);
                        world.setAffichage(true);
                    }
                    if (nextDec instanceof Bombnumberdec) {
                        listExplosion.add(nextPos);
                        world.clear(nextPos);
                        world.setAffichage(true);
                    }
                    if (nextDec instanceof Bombnumberinc) {
                        listExplosion.add(nextPos);
                        world.clear(nextPos);
                        world.setAffichage(true);
                    }
                    if (nextPos.equals(player.getPosition())) {
                        listExplosion.add(nextPos);
                        player.getHurt(player.getPosition());
                    }
                    for (Monster monster : monsters) {
                    	if (monster.getPosition().equals(nextPos)) {
                            listExplosion.add(nextPos);
                    		monster.getHurt(monster.getPosition());
                    	}
                    }
                    
                }
            }
            player.incBomb();
            world.clear(getPosition());
            world.setAffichage(true);
            boomed = true;
        }
    };

    public boolean getBoomed(){
        return boomed;
    }

    public List<Position> getListExplosion() {
        return listExplosion;
    }
}
