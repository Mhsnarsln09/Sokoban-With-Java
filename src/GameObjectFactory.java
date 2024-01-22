
public class GameObjectFactory {

    public static Wall createWall(int x, int y) {
        return new Wall(x, y);
    }

    public static Baggage createBaggage(int x, int y) {
        return new Baggage(x, y);
    }

    public static BaseArea createBaseArea(int x, int y) {
        return new BaseArea(x, y);
    }
    public static SkateBoardPower skateBoardPower(int x, int y) {
        return new SkateBoardPower(x, y);
    }
    public static GoalWizard goalWizard(int x, int y) {
        return new GoalWizard(x, y);
    }

    public  static  Player createPlayer(int x, int y) {
        return new Player(x, y);
    }
}
