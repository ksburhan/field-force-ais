package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameConstants {

    public static char[] VALID_TARGETS = { '1', '2', '3', '4', '-' };
    public static List<Character> VALID_ELEMENTS = Arrays.asList('0', '1', '2', '3', '4', 'x', '-', 'f');
    public static List<Character> VALID_CONSUMEABLES = new ArrayList<Character>();
    public static int TIME_LIMIT = 3;

    public static int HP = 150;
    public static int SHIELD = 50;

    public static int ATTACK_DAMAGE = 20;
    public static int WALK_IN_PLAYER_DAMAGE = 20;
    public static int PLAYER_WALKED_INTO_DAMAGE = 10;

    public static int FIRE_DURATION_ON_MAP = 20;
    public static int ON_FIRE_EFFECT_DURATION = 3;
    public static int ON_FIRE_DAMAGE = 5;

    public static int WALL_HP = 20;
    public static int WALK_IN_WALL_DAMAGE = 20;
    public static int WALL_TAKE_DAMAGE = 10;
}
