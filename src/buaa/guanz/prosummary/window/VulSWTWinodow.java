package buaa.guanz.prosummary.window;

import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import buaa.guanz.prosummary.commons.BaseResult.Predict;

public class VulSWTWinodow {
	private static Table table;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void runSWTWindow(List<Predict> pList, String projectName) {
		try {
			VulSWTWinodow window = new VulSWTWinodow();
			window.open(pList, projectName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Open the window.
	 */
	public void open(List<Predict> pList, String projectName) {
		Display display = Display.getDefault();
		Shell shlVulnerabilityDetection = new Shell();
		shlVulnerabilityDetection.setSize(816, 591);
		shlVulnerabilityDetection.setText("Vulnerability Detection");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlVulnerabilityDetection, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBounds(29, 23, 740, 498);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		table = new Table(scrolledComposite, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnSourceFile = new TableColumn(table, SWT.CENTER);
		tblclmnSourceFile.setWidth(400);
		tblclmnSourceFile.setText("源码文件名称");
		
		TableColumn tblclmnFunction = new TableColumn(table, SWT.CENTER);
		tblclmnFunction.setWidth(200);
		tblclmnFunction.setText("函数名称");
		
		TableColumn tblclmnPredict = new TableColumn(table, SWT.CENTER);
		tblclmnPredict.setWidth(134);
		tblclmnPredict.setText("是否存在漏洞");
		scrolledComposite.setContent(table);
		scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
//		for(int i = 0 ; i < 20; ++i) {
//			new TableItem(table, SWT.CENTER).setText(new String[] { "a", "b", "c" });
//		}
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(projectName);
		sb.append("/");
		for (Predict predict : pList) {
			String locationString = predict.getLocation();
			int start = locationString.indexOf(sb.toString());
			locationString = locationString.substring(start + sb.length());
			new TableItem(table, SWT.CENTER).setText(new String[] { locationString, predict.getFunction(), String.valueOf(predict.getPred()) });
		}
		
		shlVulnerabilityDetection.open();
		shlVulnerabilityDetection.layout();
		while (!shlVulnerabilityDetection.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}

