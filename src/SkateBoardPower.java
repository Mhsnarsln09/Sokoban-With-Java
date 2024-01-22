import java.awt.Image;
import javax.swing.ImageIcon;
public class SkateBoardPower extends Actor {
    public SkateBoardPower(int x, int y) {
        super(x, y);

        initArea();
    }

    private void initArea() {

        ImageIcon skateBoardIcon = new ImageIcon("src/assets/skateboard.png");
        Image image = skateBoardIcon.getImage();
        setImage(image);
    }
}
