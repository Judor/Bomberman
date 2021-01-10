package fr.ubx.poo.view.image;

public enum ImageResource {
    BANNER_BOMB ("banner_bomb.png"),
    BANNER_RANGE ("banner_range.png"),
    HEART("heart.png"),
    KEY("key.png"),
    DIGIT_0 ("banner_0.jpg"),
    DIGIT_1 ("banner_1.jpg"),
    DIGIT_2 ("banner_2.jpg"),
    DIGIT_3 ("banner_3.jpg"),
    DIGIT_4 ("banner_4.jpg"),
    DIGIT_5 ("banner_5.jpg"),
    DIGIT_6 ("banner_6.jpg"),
    DIGIT_7 ("banner_7.jpg"),
    DIGIT_8 ("banner_8.jpg"),
    DIGIT_9 ("banner_9.jpg"),
    PLAYER_DOWN("player_down.png"),
    PLAYER_LEFT("player_left.png"),
    PLAYER_RIGHT("player_right.png"),
    PLAYER_UP("player_up.png"),
    PLAYER_DOWN_INDESTRUCTIBLE("player_down_indestructible.png"),
    PLAYER_LEFT_INDESTRUCTIBLE("player_left_indestructible.png"),
    PLAYER_RIGHT_INDESTRUCTIBLE("player_right_indestructible.png"),
    PLAYER_UP_INDESTRUCTIBLE("player_up_indestructible.png"),
    STONE("stone.png"),
    TREE("tree.png"),
    BOX("box.png"),
    BOMBERWOMAN("bomberwoman.png"),
    BOMBNUMBERDEC("bonus_bomb_nb_dec.png"),
    BOMBNUMBERINC("bonus_bomb_nb_inc.png"),
    BOMBRANGEDEC("bonus_bomb_range_dec.png"),
    BOMBRANGEINC("bonus_bomb_range_inc.png"),
    DOORNEXTOPENED("door_opened.png"),
    DOORNEXTCLOSED("door_closed.png"),
    DOORPREVOPENED("door_opened.png"),
    MONSTER_UP("monster_up.png"),
    MONSTER_DOWN("monster_down.png"),
    MONSTER_RIGHT("monster_right.png"),
    MONSTER_LEFT("monster_left.png"),
    MONSTER_UP_3("monster_up_3.png"),
    MONSTER_DOWN_3("monster_down_3.png"),
    MONSTER_RIGHT_3("monster_right_3.png"),
    MONSTER_LEFT_3("monster_left_3.png"),
    MONSTER_DOWN_2("monster_down_2.png"),
    MONSTER_RIGHT_2("monster_right_2.png"),
    MONSTER_LEFT_2("monster_left_2.png"),
    BOMB_4("bomb4.png"),
    BOMB_3("bomb3.png"),
    BOMB_2("bomb2.png"),
    BOMB_1("bomb1.png"),
    EXPLOSION("explosion.png"),

    ;

    private final String FileName;

    ImageResource(String fileName) {
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }
}
