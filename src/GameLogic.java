import java.util.ArrayList;

public class GameLogic {
    private final int SPACE;
    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private Player player;

    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;

    public int moveCount = 0;

    private final Runnable isCompletedCallback;

    public GameLogic(int space, ArrayList<Wall> walls, ArrayList<Baggage> baggs, Player player, Runnable isCompletedCallback) {
        this.SPACE = space;
        this.walls = walls;
        this.baggs = baggs;
        this.player = player;
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

    private boolean checkBagCollision(int type) {

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

    public void moveToLeft() {
        if (checkWallCollision(player,
                LEFT_COLLISION)) {
            return;
        }

        if (checkBagCollision(LEFT_COLLISION)) {
            return;
        }

        player.move(-SPACE, 0);
        moveCount += 1;
    }

    public void moveToRight() {

        if (checkWallCollision(player, RIGHT_COLLISION)) {
            return;
        }

        if (checkBagCollision(RIGHT_COLLISION)) {
            return;
        }

        player.move(SPACE, 0);
        moveCount += 1;
    }

    public void moveToTop() {
        if (checkWallCollision(player, TOP_COLLISION)) {
            return;
        }

        if (checkBagCollision(TOP_COLLISION)) {
            return;
        }

        player.move(0, -SPACE);
        moveCount += 1;
    }

    public void moveToBottom() {
        if (checkWallCollision(player, BOTTOM_COLLISION)) {
            return;
        }

        if (checkBagCollision(BOTTOM_COLLISION)) {
            return;
        }

        player.move(0, SPACE);
        moveCount += 1;
    }
}
