import java.awt.Image;
import javax.swing.ImageIcon;

public class BaseArea extends Actor {

    public BaseArea(int x, int y) {
        super(x, y);

        initArea();
    }

    private void initArea() {

        ImageIcon baseAreaIcon = new ImageIcon("src/assets/base-area.png");
        Image image = baseAreaIcon.getImage();
        setImage(image);
    }
    public void move(int x, int y) {

        int dx = x() + x;
        int dy = y() + y;

        setX(dx);
        setY(dy);
    }
}
