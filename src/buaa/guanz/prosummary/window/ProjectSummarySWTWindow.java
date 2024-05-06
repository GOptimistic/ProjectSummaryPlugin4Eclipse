package buaa.guanz.prosummary.window;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import buaa.guanz.prosummary.utils.DependencyAnalyzer;
import buaa.guanz.prosummary.utils.FileSummaryGenerator;
import buaa.guanz.prosummary.utils.ProjectSummaryGenerator;

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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.FontDescriptor;

public class ProjectSummarySWTWindow {
	private LocalResourceManager localResourceManager;
	private Shell shell;
	private Text text_summary;
	private Text text_status;
	private Table table;
	private TableViewer tableViewer;
	private String projectName;
	private String projectPath;
	private DependencyAnalyzer analyzer = new DependencyAnalyzer();
	private FileSummaryGenerator fileSummaryGenerator = new FileSummaryGenerator();
	private ProjectSummaryGenerator projectSummaryGenerator = new ProjectSummaryGenerator();
	
	private List<SimpleEntry<String, String>> dependencyAnalyzeResults;
	private Map<String, String> fileSummaries = new HashMap<>();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ProjectSummarySWTWindow window = new ProjectSummarySWTWindow("Test", "Path");
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runSWTWindow(String projectName, String projectPath) {
		try {
			ProjectSummarySWTWindow window = new ProjectSummarySWTWindow(projectName, projectPath);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ProjectSummarySWTWindow(String projectName, String projectPath) {
		this.projectName = projectName;
		this.projectPath = projectPath;
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
		gd_composite_status.heightHint = 60;
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
		gd_text_status.heightHint = 45;
		gd_text_status.widthHint = 620;
		text_status.setLayoutData(gd_text_status);
		
		ScrolledComposite scrolledCompositeTable = new ScrolledComposite(composite_global, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledCompositeTable = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledCompositeTable.widthHint = 770;
		gd_scrolledCompositeTable.heightHint = 410;
		scrolledCompositeTable.setLayoutData(gd_scrolledCompositeTable);
		scrolledCompositeTable.setExpandHorizontal(true);
		scrolledCompositeTable.setExpandVertical(true);
		
		tableViewer = new TableViewer(scrolledCompositeTable, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
        table.setHeaderVisible(true);
		
		TableColumn tblclmnFileName = new TableColumn(table, SWT.NONE);
		tblclmnFileName.setWidth(200);
		tblclmnFileName.setText("File Name");
		
		TableColumn tblclmnFileSummary = new TableColumn(table, SWT.NONE);
		tblclmnFileSummary.setWidth(550);
		tblclmnFileSummary.setText("File Summary");
		tableViewer.setContentProvider(new ContentProvider());
        tableViewer.setLabelProvider(new TableLabelProvider());
		scrolledCompositeTable.setContent(table);
		scrolledCompositeTable.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		shell.open();
		shell.layout();
		
		// 依赖分析
		text_status.setText("Dependency Analyzing...");
		
		Job dependencyAnalyzeJob = new Job("Dependency Analyze"){
            protected IStatus run(IProgressMonitor monitor){
            	// 在此添加获取数据的代码
            	File projectDir = new File(projectPath);
            	ArrayList<ArrayList<String>> tableRowList = new ArrayList<>();
        		try {
        			dependencyAnalyzeResults = analyzer.getImportAnalyzeResults(projectDir);
        		} catch (Exception e) {
        			// 依赖分析失败
        			e.printStackTrace();
        			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_WARNING);
        	        dialog.setText("WARNING");
        	        dialog.setMessage("依赖分析失败！");
        	        dialog.open();
        		}
        		for (Entry<String, String> entry : dependencyAnalyzeResults) {
        			String className = entry.getKey();
        			ArrayList<String> tableRow = new ArrayList<>();
        			tableRow.add(className);
        			tableRow.add("");
        			tableRowList.add(tableRow);
        		}
        		
        		
	            Display.getDefault().asyncExec(new Runnable(){
		            public void run(){
		            	// 在此添加更新界面的代码
		            	// 展示得到的结果
		            	tableRowList.forEach(row -> tableViewer.add(row));
		            	text_status.setText("Dependency Analyzie Done!\nGenerating File Summary...");
		            }
	            });
				return Status.OK_STATUS;
        	}
        };
        

		// 获取文件摘要
        Job fileSummaryJob = new Job("File Summary"){
	        protected IStatus run(IProgressMonitor monitor){
	        	// 在此添加获取数据的代码
//	        	System.out.println("Analyze results:");
//	    		dependencyAnalyzeResults.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
	        	ArrayList<ArrayList<String>> tableRowList = new ArrayList<>();
	        	for (Entry<String, String> entry : dependencyAnalyzeResults) {
	    			String className = entry.getKey();
	    			String filePath = entry.getValue();
	    			String fileSummary = fileSummaryGenerator.getFileSummary(className, filePath, projectName);
	    			ArrayList<String> tableRow = new ArrayList<>();
	    			tableRow.add(className);
	    			tableRow.add(fileSummary);
	    			fileSummaries.put(className, fileSummary);
	    			tableRowList.add(tableRow);
	    		}

	            Display.getDefault().asyncExec(new Runnable(){
		            public void run(){
		            	// 在此添加更新界面的代码
		            	// 展示得到的结果
		            	table.removeAll();
		            	tableRowList.forEach(row -> tableViewer.add(row));
		            	text_status.setText("Dependency Analyzie Done!\nGenerate File Summary Done!\nGenerating Project Summary...");
		            }
	            });
				return Status.OK_STATUS;
        	}
        };
        
        // 获取项目摘要
        Job projectSummaryJob = new Job("Project Summary"){
	        protected IStatus run(IProgressMonitor monitor){
	        	// 在此添加获取数据的代码
	        	String projectSummary = projectSummaryGenerator.getProjectSummary(projectName, fileSummaries);
	            Display.getDefault().asyncExec(new Runnable(){
		            public void run(){
		            	// 在此添加更新界面的代码
		            	// 展示得到的结果
		            	text_summary.setText(projectSummary);
		            	text_status.setText("Dependency Analyzie Done!\nGenerate File Summary Done!\nGenerate Project Summary Done!");
		            }
	            });
				return Status.OK_STATUS;
        	}
        };
        
        // 调度规则，相当于信号量
        ISchedulingRule scheduleRULE = new ISchedulingRule() {
            public boolean contains(ISchedulingRule rule) {
              return this.equals(rule);
            }

            public boolean isConflicting(ISchedulingRule rule) {
              return this.equals(rule);
            }
        };
        // 按顺序设置规则，就会按顺序调度
        dependencyAnalyzeJob.setRule(scheduleRULE);
        fileSummaryJob.setRule(scheduleRULE);
        projectSummaryJob.setRule(scheduleRULE);
        
        dependencyAnalyzeJob.schedule();
        fileSummaryJob.schedule();
        projectSummaryJob.schedule();
        
		
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
