package testPackage;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.events.PaintEvent; 
import org.eclipse.swt.events.PaintListener; 
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas; 
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell; 

public class CaseButton {
private Shell shell;
public int width = 5;
public int height = 5;

public static void main(String[] args) { 
try { 
CaseButton window = new CaseButton(); 
window.open(); 
} catch (Exception e) { 
e.printStackTrace(); 
} 
} 

/** 
* Open the window. 
*/ 

public void open() { 
Display display = Display.getDefault(); 
createContents(); 
shell.open(); 
shell.layout(); 
while (!shell.isDisposed()) { 
if (!display.readAndDispatch()) { 
display.sleep(); 
} 
} 
} 
/** 
* Create contents of the window. 
*/ 
protected void createContents() { 
shell = new Shell(); 
shell.setText("SWT Application"); 
GridLayout gridLayout = new GridLayout();
gridLayout.numColumns = 1; 
gridLayout.marginWidth = 0;
gridLayout.marginHeight = 0;
gridLayout.verticalSpacing = 4;
gridLayout.makeColumnsEqualWidth=false; 
shell.setLayout(gridLayout);

GridData gridData=new GridData(); 
gridData.verticalSpan = 0; 
gridData.horizontalSpan = 3;
gridData.verticalAlignment = GridData.FILL;
gridData.grabExcessVerticalSpace= true;
gridData.horizontalAlignment = GridData.FILL;
gridData.grabExcessHorizontalSpace = true; 

final Button button = new Button(shell, SWT.PUSH);
button.setText("draw Rectangle");

final Canvas canvas = new Canvas(shell, SWT.NONE);
canvas.setLayoutData(gridData);

//Add the PaintListener Here
canvas.addPaintListener(new CaseButtonPaintListener());
//Important 
canvas.addPaintListener(new PaintListener() { 
public void paintControl(PaintEvent e) {	
e.gc.drawRoundRectangle(10, 10, 180, 80, 1, 1); 

} 
});


button.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
System.out.println("buttton_start ");	

canvas.redraw(); // it can' work

System.out.println("buttton_end ");	

}
});	
}

 private class CaseButtonPaintListener implements PaintListener {
	 
	 public void paintControl(PaintEvent e)
	 {
		 width = width + 5;
		 height = height + 5;
		 e.gc.drawRoundRectangle(10, 10, 180, 80, width, height);
		 
	 }
 }

}