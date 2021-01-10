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

    private Timer t=new Timer();
    private int range;
    private List<Position> listExplosion=new ArrayList<>();
    private boolean boomed=false;

    public Bomb(Game game,Position position){
        super(game,position);
        this.range=game.getPlayer().getRange();
        t.schedule(kaboom,4000);
    }

    TimerTask kaboom = new TimerTask() {
        public void run() {
            boomed=true;
        }
    };

    public void theBomb() {
        World world = game.getWorld();
        Player player = game.getPlayer();
        List<Monster> monsters = game.getMonsters();
        for (Direction direction : Direction.values()) {
            Position nextPos = getPosition();
            for (int i = 1; i <= range; i++) {
                nextPos = direction.nextPosition(nextPos);
                Decor nextDec = world.get(nextPos);

                if((world.isEmpty(nextPos))&&(nextPos.inside(world.getDimension()))){
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
                    break;
                }
                if (nextDec instanceof Stone || nextDec instanceof Tree || nextDec instanceof Bomberwoman || nextDec instanceof Doorprevopened || nextDec instanceof Doornextopened || nextDec instanceof Doornextclosed) {
                    break;
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
                    player.getHurt(player.getPosition());
                }
                for (Monster monster : monsters) {
                    if (monster.getPosition().equals(nextPos)) {
                        monster.getHurt(monster.getPosition());
                    }
                }
            }
        }
        listExplosion.add(getPosition());
        listExplosion.forEach(e -> world.set(e, new Explosion()));      //Create the explosion Sprite for each impacted position

        TimerTask explosion = new TimerTask() {
            public void run() {
                listExplosion.forEach(e -> world.clear(e));
                world.setAffichage(true);
            }
        };

        Timer t1 = new Timer();
        t1.schedule(explosion, 1000);   //New timer to make last the explosion sprite for 1sec and then clear it
        player.incBomb();
        boomed = true;
    }

    public void update() {                              //We check if boomed is true.If it's the case, it have been 4 secs since the creation of the bomb
        Decor d = game.getWorld().get(getPosition());   //We also check if the bomb is in the explosion range of another one. If it's the case, it have to bomb right away
        if (boomed || d instanceof Explosion) {
            boomed=true;
            theBomb();
        }
    }

    public boolean getBoomed(){
        return boomed;
    }

}