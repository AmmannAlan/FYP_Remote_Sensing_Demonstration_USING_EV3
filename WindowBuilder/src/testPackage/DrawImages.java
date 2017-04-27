package testPackage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
 
/**
 * 
 */
public class DrawImages {
  Display display = new Display();
  Shell shell = new Shell(display);
 
  public DrawImages() {
    shell.setLayout(new FillLayout());
    Canvas canvas = new Canvas(shell, SWT.NULL);
    final Image image = new Image(display, "D:\\UoL\\FYP-ELEC340\\FYP_Report\\filter.png");
    canvas.addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        e.gc.drawImage(image, 10, 10);
        e.gc.drawImage(image, 0, 0, 100, 100, 200, 10, 200, 50);
      }
    });
    shell.setSize(430, 200);
    shell.open();
    //textUser.forceFocus();
    captureControl(canvas, "canvas.bmp");
 
    // Set up the event loop.
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        // If no more entries in event queue
        display.sleep();
      }
    }
 
    display.dispose();
  }
  /**
   * Captures the specified control and saves the result into a file in the BMP format.
   * @param control
   * @param fileName
   */
  public static void captureControl(Control control, String file) {
    GC gc = new GC(control);
    Image image = new Image(control.getDisplay(), control.getSize().x, control.getSize().y);
    gc.copyArea(image, 0, 0);
    ImageLoader loader = new ImageLoader();
    loader.data = new ImageData[] { image.getImageData() };
    loader.save(file, SWT.IMAGE_BMP);
    gc.dispose();
  }
 
  public static void main(String[] args) {
    new DrawImages();
  }
}