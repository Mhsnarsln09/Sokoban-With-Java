import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;

    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private ArrayList<BaseArea> areas;
    private ArrayList<SkateBoardPower> skateBoardPower;
    private ArrayList<GoalWizard> goalWizard;
    private GameLogic gameLogic;

    private Player player;
    private int w = 0;
    private int h = 0;

    private boolean isCompleted = false;
    private Timer timer;
    private int timeElapsed = 0;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    private void buildWorld(Graphics g) {

        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.addAll(skateBoardPower);
        world.addAll(goalWizard);
        world.add(player);

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);
//
            if ((item instanceof SkateBoardPower || item instanceof GoalWizard) && gameLogic.isPowerShow && !gameLogic.isSkateBoard && !gameLogic.isGoalWizard) {

                g.drawImage(item.getImage(), item.x(), item.y(), this);

            }else if (item instanceof Player || item instanceof Baggage) {

                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2 , this);

            }else if ( item instanceof Wall || item instanceof BaseArea) {

                g.drawImage(item.getImage(), item.x(), item.y() , this);

            }


            g.setColor(new Color(255, 255, 244));
            String formattedTime = formatTime(timeElapsed);
            g.drawString("Moves:" + "    " + gameLogic.moveCount, 375, 20);
            g.drawString("Time:" + "    " + formattedTime, 375, 40);
            g.drawString("Up : W,  ↑", 100, 300);
            g.drawString("Left : A,  ←", 25, 330);
            g.drawString("Down : S,  ↓", 100, 330);
            g.drawString("Right : D,  →", 175, 330);
            if (isCompleted) {
                g.drawString("Finish", 25, 20);
                g.drawString("New Game [ N ]", 375, 300);
                g.drawString("CONGRATULATION", 175, 375);
            } else if (gameLogic.moveCount > 0) {
                g.drawString("Restart [ R ]", 375, 300);
            } else {
                g.drawString("Start [ W,  ↑ ]", 375, 300);
            }

            if(gameLogic.isGoalWizard) {
                g.drawString("Hedef Sihirbazı Etkin", 375, 330);
                g.drawString("Dikkat Et Her Güç Aynı Zamanda Bir Zehirdir.", 125, 400);
            }else if(gameLogic.isSkateBoard) {
                g.drawString("Kaykay Etkin", 375, 330);
                g.drawString("Dikkat Et Her Güç Aynı Zamanda Bir Zehirdir.", 125, 400);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
    }

    private void initWorld() {
        walls = new ArrayList<>();
        baggs = new ArrayList<>();
        areas = new ArrayList<>();
        skateBoardPower = new ArrayList<>();
        goalWizard = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        String levelDesign = GameConfig.getInstance().getProperty("levelDesign");
        for (int i = 0; i < levelDesign.length(); i++) {
            char item = levelDesign.charAt(i);

            switch (item) {
                case '\n':
                    y += SPACE;
                    if (this.w < x) {
                        this.w = x;
                    }
                    x = OFFSET;
                    break;
                case '#':
                    walls.add(GameObjectFactory.createWall(x, y));
                    x += SPACE;
                    break;
                case '$':
                    baggs.add(GameObjectFactory.createBaggage(x, y));
                    x += SPACE;
                    break;
                case '.':
                    areas.add(GameObjectFactory.createBaseArea(x, y));
                    x += SPACE;
                    break;
                case '!':
                    skateBoardPower.add(GameObjectFactory.skateBoardPower(x, y));
                    x += SPACE;
                    break;
                case '%':
                    goalWizard.add(GameObjectFactory.goalWizard(x, y));
                    x += SPACE;
                    break;
                case '@':
                    player = GameObjectFactory.createPlayer(x, y);
                    x += SPACE;
                    break;

                case ' ':
                    x += SPACE;
                    break;

                default:
                    break;
            }
            h = y;
        }
        gameLogic = new GameLogic(SPACE, walls, baggs, areas, player, skateBoardPower, goalWizard, this::isCompleted);
    }
    private enum Direction {
        LEFT, RIGHT, UP, DOWN, RESTART
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Direction direction = mapKeyToDirection(e.getKeyCode());

            if (gameLogic.moveCount == 0 && direction == Direction.UP) {
                startTimer();
            }
            if (direction != null) {
                switch (direction) {
                    case LEFT:
                        gameLogic.moveToLeft();
                        break;
                    case RIGHT:
                        gameLogic.moveToRight();
                        break;
                    case UP:
                        gameLogic.moveToTop();
                        break;
                    case DOWN:
                        gameLogic.moveToBottom();
                        break;
                    case RESTART:
                        restartLevel();
                        break;
                }
            }
            if (isCompleted) {
                return;
            }
            repaint();
        }
    }
    private Direction mapKeyToDirection(int keyCode) {
        int leftKey = Integer.parseInt(GameConfig.getInstance().getProperty("leftKey"));
        int leftAKey = Integer.parseInt(GameConfig.getInstance().getProperty("leftAKey"));
        int rightKey = Integer.parseInt(GameConfig.getInstance().getProperty("rightKey"));
        int rightDKey = Integer.parseInt(GameConfig.getInstance().getProperty("rightDKey"));
        int upKey = Integer.parseInt(GameConfig.getInstance().getProperty("upKey"));
        int upWKey = Integer.parseInt(GameConfig.getInstance().getProperty("upWKey"));
        int downKey = Integer.parseInt(GameConfig.getInstance().getProperty("downKey"));
        int downSKey = Integer.parseInt(GameConfig.getInstance().getProperty("downSKey"));
        int restartKey = Integer.parseInt(GameConfig.getInstance().getProperty("restartKey"));
        int newGameKey = Integer.parseInt(GameConfig.getInstance().getProperty("newGameKey"));

        if (keyCode == leftKey || keyCode == leftAKey){
            return  Direction.LEFT;
        } else  if (keyCode == rightKey || keyCode == rightDKey){
            return  Direction.RIGHT;
        } else  if (keyCode == upKey || keyCode == upWKey){
            return  Direction.UP;
        } else  if (keyCode == downKey || keyCode == downSKey){
            return  Direction.DOWN;
        } else  if ((keyCode == restartKey && !isCompleted) || (keyCode == newGameKey && isCompleted)){
            return  Direction.RESTART;
        }
        return null;
    }


    public void isCompleted() {

        int nOfBags = baggs.size();
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {

            Baggage bag = baggs.get(i);

            for (int j = 0; j < nOfBags; j++) {

                BaseArea area = areas.get(j);

                if (bag.x() == area.x() && bag.y() == area.y()) {

                    finishedBags += 1;
                }
            }
        }

        if (finishedBags == nOfBags) {

            isCompleted = true;
            repaint();
        }
    }

    private void restartLevel() {

        areas.clear();
        baggs.clear();
        walls.clear();

        gameLogic.moveCount = 0;
        timer.cancel();
        timeElapsed = 0;
        gameLogic.isPowerShow = false;
        gameLogic.firstMoveCountWithPowerShow = 0;
        gameLogic.firstStepWithPower = 0;

        initWorld();
        if (isCompleted) {
            isCompleted = false;
        }

    }


    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isCompleted) {
                    timeElapsed++;
                    if (!gameLogic.isPowerShow && (timeElapsed % 7 == 0)){
                        gameLogic.isPowerShow = true;
                        gameLogic.firstMoveCountWithPowerShow = gameLogic.moveCount;
                    }

                    repaint();
                }
            }
        }, 0, 1000);

    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
