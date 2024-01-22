import java.util.ArrayList;

public class GameLogic {
    private final int SPACE;
    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private ArrayList<BaseArea> areas;
    private ArrayList<SkateBoardPower> skateBoardPower;
    private ArrayList<GoalWizard> goalWizard;
    private Player player;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    public int moveCount = 0;
    public int firstMoveCountWithPowerShow = 0;
    public int firstStepWithPower = 0;

    private final Runnable isCompletedCallback;
    public boolean isPowerShow = false;
    public boolean isGoalWizard = false;
    public boolean isSkateBoard = false;
    public GameLogic(int space, ArrayList<Wall> walls, ArrayList<Baggage> baggs, ArrayList<BaseArea> areas, Player player, ArrayList<SkateBoardPower> skateBoardPower, ArrayList<GoalWizard> goalWizard, Runnable isCompletedCallback) {
        this.SPACE = space;
        this.walls = walls;
        this.baggs = baggs;
        this.player = player;
        this.areas = areas;
        this.skateBoardPower = skateBoardPower;
        this.goalWizard = goalWizard;
        this.isCompletedCallback = isCompletedCallback;
    }

    private boolean checkWallCollision(Actor actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isLeftCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case TOP_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isTopCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < walls.size(); i++) {

                    Wall wall = walls.get(i);

                    if (actor.isBottomCollision(wall)) {

                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }

    public boolean checkBagCollision(int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isLeftCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {
                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, LEFT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(-SPACE, 0);
                        isCompletedCallback.run();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isRightCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(SPACE, 0);
                        isCompletedCallback.run();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isTopCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {

                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        bag.move(0, -SPACE);
                        isCompletedCallback.run();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (player.isBottomCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {
                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(bag, BOTTOM_COLLISION)) {

                                return true;

                            }
                        }

                        bag.move(0, SPACE);
                        isCompletedCallback.run();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }
    public boolean checkGoalCollision(int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < areas.size(); i++) {

                    BaseArea area = areas.get(i);

                    if (player.isLeftCollision(area)) {

                        for (int j = 0; j < areas.size(); j++) {

                            BaseArea item = areas.get(j);

                            if (!area.equals(item)) {
                                if (area.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(area, LEFT_COLLISION)) {
                                return true;
                            }
                        }

                        area.move(-SPACE, 0);
                        isCompletedCallback.run();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < areas.size(); i++) {

                    BaseArea area = areas.get(i);

                    if (player.isRightCollision(area)) {

                        for (int j = 0; j < areas.size(); j++) {

                            BaseArea item = areas.get(j);

                            if (!area.equals(item)) {
                                if (area.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(area, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        area.move(SPACE, 0);
                        isCompletedCallback.run();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < areas.size(); i++) {

                    BaseArea area = areas.get(i);

                    if (player.isTopCollision(area)) {

                        for (int j = 0; j < areas.size(); j++) {

                            BaseArea item = areas.get(j);

                            if (!area.equals(item)) {
                                if (area.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(area, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        area.move(0, -SPACE);
                        isCompletedCallback.run();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < areas.size(); i++) {

                    BaseArea area = areas.get(i);

                    if (player.isBottomCollision(area)) {

                        for (int j = 0; j < areas.size(); j++) {

                            BaseArea item = areas.get(j);

                            if (!area.equals(item)) {
                                if (area.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(area, BOTTOM_COLLISION)) {
                                return true;
                            }
                        }

                        area.move(0, SPACE);
                        isCompletedCallback.run();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }

    private boolean checkSkateBoardCollision(Actor actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < skateBoardPower.size(); i++) {

                    SkateBoardPower skateBoard = skateBoardPower.get(i);

                    if (actor.isLeftCollision(skateBoard)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < skateBoardPower.size(); i++) {

                    SkateBoardPower skateBoard = skateBoardPower.get(i);

                    if (actor.isRightCollision(skateBoard)) {
                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }
    private boolean checkGoalWizardCollision(Actor actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < goalWizard.size(); i++) {

                    GoalWizard gWizard = goalWizard.get(i);

                    if (actor.isLeftCollision(gWizard)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < goalWizard.size(); i++) {

                    GoalWizard gWizard = goalWizard.get(i);

                    if (actor.isRightCollision(gWizard)) {
                        return true;
                    }
                }

                return false;
            default:
                break;
        }

        return false;
    }

    public void moveToLeft() {
        if (checkWallCollision(player, LEFT_COLLISION)) {
            return;
        }

        if (checkBagCollision(LEFT_COLLISION)) {
            return;
        }
        if (isPowerShow && checkSkateBoardCollision(player, LEFT_COLLISION)){
            isGoalWizard =false;
            isSkateBoard= true;
            isPowerShow =false;
            firstStepWithPower = moveCount + 1;
        }
        if (isPowerShow && checkGoalWizardCollision(player, LEFT_COLLISION)){
            isSkateBoard=false;
            isGoalWizard =true;
            isPowerShow =false;
            firstStepWithPower = moveCount + 1;
        }

        if (isGoalWizard){
            checkGoalCollision(LEFT_COLLISION);
        }
        moveStrategy(isSkateBoard,-SPACE,0,LEFT_COLLISION);
        updateMoveCount();

    }

    public void moveToRight() {

        if (checkWallCollision(player, RIGHT_COLLISION)) {
            return;
        }
        if (checkBagCollision(RIGHT_COLLISION)) {
            return;
        }
        if (isPowerShow && checkSkateBoardCollision(player, RIGHT_COLLISION)){
            isGoalWizard =false;
            isSkateBoard= true;
            isPowerShow =false;
            firstStepWithPower = moveCount + 1;
        }
        if (isPowerShow && checkGoalWizardCollision(player, RIGHT_COLLISION)){
            isSkateBoard=false;
            isGoalWizard =true;
            isPowerShow =false;
            firstStepWithPower = moveCount + 1;
        }
        if (isGoalWizard){
            checkGoalCollision(RIGHT_COLLISION);
        }
        moveStrategy(isSkateBoard,SPACE,0,RIGHT_COLLISION);
        updateMoveCount();
    }

    public void moveToTop() {
        if (checkWallCollision(player, TOP_COLLISION)) {
            return;
        }

        if (checkBagCollision(TOP_COLLISION)) {
            return;
        }
        if (isGoalWizard){
            checkGoalCollision(TOP_COLLISION);
        }
        moveStrategy(isSkateBoard,0,-SPACE,TOP_COLLISION);
        updateMoveCount();
    }

    public void moveToBottom() {
        if (checkWallCollision(player, BOTTOM_COLLISION)) {
            return;
        }

        if (checkBagCollision(BOTTOM_COLLISION)) {
            return;
        }
        if (isGoalWizard){
            checkGoalCollision(BOTTOM_COLLISION);
        }
        moveStrategy(isSkateBoard,0,SPACE,BOTTOM_COLLISION);
        updateMoveCount();
    }

    private void updateMoveCount() {
        moveCount++;
        checkFor5StepsAfterPowerUp();
        checkFor10StepsWithPower();
    }


    private void checkFor5StepsAfterPowerUp(){
        int result = moveCount - firstMoveCountWithPowerShow;
        if (result == 5) isPowerShow = false;
    }

    private void checkFor10StepsWithPower(){
        int result = moveCount - firstStepWithPower;
        if (result == 10) {
            isSkateBoard=false;
            isGoalWizard =false;
        }
    }

    private void moveStrategy(boolean isPowerful,int x, int y, int collision){
        if(isPowerful){
            MoveSkateBoardStrategy playerMove = new MoveWithSkateBoardPowerStrategy();
            playerMove.move(player, x, y, collision);
        } else {
            MoveSkateBoardStrategy moveStrategy = new MoveWithoutSkateBoardPowerStrategy();
            moveStrategy.move(player, x, y, collision);
        }
    }
    public interface MoveSkateBoardStrategy {
        void move(Player player, int x, int y, int collision);
    }

    public class MoveWithSkateBoardPowerStrategy implements MoveSkateBoardStrategy {
        @Override
        public void move(Player player, int x, int y, int collision) {
            while (!checkWallCollision(player,collision) && !checkBagCollision(collision)) {
                player.move(x, y);
            }
        }
    }

    public static class MoveWithoutSkateBoardPowerStrategy implements MoveSkateBoardStrategy {
        @Override
        public void move(Player player, int x, int y, int collision) {
            player.move(x, y);
        }
    }

}
