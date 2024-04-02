package buaa.guanz.prosummary.window;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import buaa.guanz.prosummary.commons.BaseResult.Predict;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;

import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;

public class ProjectSummarySWTWindow {
	private LocalResourceManager localResourceManager;
	private Shell shell;
	private Text text_summary;
	private Text text_status;
	private Table table;
	private String projectName;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ProjectSummarySWTWindow window = new ProjectSummarySWTWindow("Test");
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runSWTWindow(String projecrName) {
		try {
			ProjectSummarySWTWindow window = new ProjectSummarySWTWindow(projecrName);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProjectSummarySWTWindow(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shell = new Shell();
		createResourceManager();
		shell.setMinimumSize(new Point(800, 600));
		shell.setMaximumSize(new Point(800, 600));
		shell.setSize(800, 600);
		shell.setText("Project Summary");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite_global = new Composite(shell, SWT.NONE);
		composite_global.setLayout(new GridLayout(1, false));
		
		Composite composite_name = new Composite(composite_global, SWT.NONE);
		composite_name.setLayout(new GridLayout(2, false));
		GridData gd_composite_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_name.heightHint = 30;
		gd_composite_name.widthHint = 800;
		composite_name.setLayoutData(gd_composite_name);
		
		Label lblProjectName = new Label(composite_name, SWT.NONE);
		lblProjectName.setFont(localResourceManager.create(FontDescriptor.createFrom(".AppleSystemUIFont", 13, SWT.BOLD)));
		GridData gd_lblProjectName = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblProjectName.heightHint = 30;
		gd_lblProjectName.widthHint = 150;
		lblProjectName.setLayoutData(gd_lblProjectName);
		lblProjectName.setText("Project Name:");
		
		Label lblProject = new Label(composite_name, SWT.NONE);
		lblProject.setFont(localResourceManager.create(FontDescriptor.createFrom(".AppleSystemUIFont", 13, SWT.NORMAL)));
		GridData gd_lblProject = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblProject.widthHint = 620;
		lblProject.setLayoutData(gd_lblProject);
		lblProject.setText(this.projectName);
		
		Composite composite_summary = new Composite(composite_global, SWT.NONE);
		composite_summary.setLayout(new GridLayout(2, false));
		GridData gd_composite_summary = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_summary.heightHint = 30;
		gd_composite_summary.widthHint = 800;
		composite_summary.setLayoutData(gd_composite_summary);
		
		Label lblProjectSummary = new Label(composite_summary, SWT.NONE);
		lblProjectSummary.setFont(localResourceManager.create(FontDescriptor.createFrom(".AppleSystemUIFont", 13, SWT.BOLD)));
		GridData gd_lblProjectSummary = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblProjectSummary.heightHint = 30;
		gd_lblProjectSummary.widthHint = 150;
		lblProjectSummary.setLayoutData(gd_lblProjectSummary);
		lblProjectSummary.setText("Project Summary:");
		
		text_summary = new Text(composite_summary, SWT.BORDER);
		text_summary.setFont(localResourceManager.create(FontDescriptor.createFrom(".AppleSystemUIFont", 13, SWT.NORMAL)));
		GridData gd_text_summary = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_text_summary.heightHint = 18;
		gd_text_summary.widthHint = 620;
		text_summary.setLayoutData(gd_text_summary);
		
		Composite composite_status = new Composite(composite_global, SWT.NONE);
		composite_status.setLayout(new GridLayout(2, false));
		GridData gd_composite_status = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_status.heightHint = 30;
		gd_composite_status.widthHint = 800;
		composite_status.setLayoutData(gd_composite_status);
		
		Label lblCurrentStatus = new Label(composite_status, SWT.NONE);
		GridData gd_lblCurrentStatus = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_lblCurrentStatus.heightHint = 30;
		gd_lblCurrentStatus.widthHint = 150;
		lblCurrentStatus.setLayoutData(gd_lblCurrentStatus);
		lblCurrentStatus.setFont(localResourceManager.create(FontDescriptor.createFrom(".AppleSystemUIFont", 13, SWT.BOLD)));
		lblCurrentStatus.setText("Current Status:");
		
		text_status = new Text(composite_status, SWT.BORDER);
		GridData gd_text_status = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_text_status.heightHint = 18;
		gd_text_status.widthHint = 620;
		text_status.setLayoutData(gd_text_status);
		
		ScrolledComposite scrolledCompositeTable = new ScrolledComposite(composite_global, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledCompositeTable = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledCompositeTable.widthHint = 770;
		gd_scrolledCompositeTable.heightHint = 435;
		scrolledCompositeTable.setLayoutData(gd_scrolledCompositeTable);
		scrolledCompositeTable.setExpandHorizontal(true);
		scrolledCompositeTable.setExpandVertical(true);
		
		TableViewer tableViewer = new TableViewer(scrolledCompositeTable, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
        table.setHeaderVisible(true);
		
		TableColumn tblclmnFileName = new TableColumn(table, SWT.NONE);
		tblclmnFileName.setWidth(200);
		tblclmnFileName.setText("File Name");
		
		TableColumn tblclmnFileSummary = new TableColumn(table, SWT.NONE);
		tblclmnFileSummary.setWidth(600);
		tblclmnFileSummary.setText("File Summary");
		tableViewer.setContentProvider(new ContentProvider());
        tableViewer.setLabelProvider(new TableLabelProvider());
		scrolledCompositeTable.setContent(table);
		scrolledCompositeTable.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
	}
}
