package testPackage;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainClass {
  public static void main(String[] args) {

  Display display = new Display();
    final Shell shell = new Shell(display);

    final Image image = new Image(display, new MainClass().getClass().getResourceAsStream("swt.png"));
    System.out.println(image.getImageData().scanlinePad);
    image.getImageData().scanlinePad = 40;
    System.out.println(image.getImageData().scanlinePad);

    shell.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent event) {
        event.gc.drawImage(image, 0, 0);
        Rectangle rect = shell.getClientArea();
        ImageData data = image.getImageData();

        int srcX = data.width / 4;
        int srcY = data.height / 4;
        int srcWidth = data.width / 2;
        int srcHeight = data.height / 2;
        int destWidth = 2 * srcWidth;
        int destHeight = 2 * srcHeight;

        event.gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, rect.width
            - destWidth, rect.height - destHeight, destWidth, destHeight);
      }
    });
    shell.setText("Draw Images");
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    image.dispose();
    display.dispose();
  }
}