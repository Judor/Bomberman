package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Decor processEntity(WorldEntity entity) {
        switch (entity) {
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case Box:
                return new Box();
            case Princess:
                return new Bomberwoman();
            case BombNumberDec:
                return new Bombnumberdec();
            case BombNumberInc:
                return new Bombnumberinc();
            case BombRangeDec:
                return new Bombrangedec();
            case BombRangeInc:
                return new Bombrangeinc();
            case DoorNextOpened:
                return new Doornextopened();
            case DoorNextClosed:
                return new Doornextclosed();
            case DoorPrevOpened:
                return new Doorprevopened();
            case Key:
                return new Key();
            case Heart:
                return new Heart();
        }
            return null;
    }
}

