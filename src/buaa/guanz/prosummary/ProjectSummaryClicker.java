package buaa.guanz.prosummary;

import java.util.*;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import buaa.guanz.prosummary.commons.BaseResult;
import buaa.guanz.prosummary.commons.JsonUtils;
import buaa.guanz.prosummary.commons.BaseResult.Predict;
import buaa.guanz.prosummary.utils.HttpTools;
import buaa.guanz.prosummary.utils.HttpVars;
import buaa.guanz.prosummary.window.ProjectSummarySWTWindow;
import buaa.guanz.prosummary.window.VulSWTWinodow;

public class ProjectSummaryClicker implements IHandler{
	
	private static String TARGETPROJECTNAME = "";
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	// 该函数用于点击generate按钮时执行
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Start plugin.");
		// TODO Auto-generated method stub
		String projectPath = this.getClikeAbsPath();
		if ("".equals(projectPath)) {
			System.out.println("get project path error");
			return null;
		}
		System.out.println(projectPath);
		showProjectSummaryWindow(TARGETPROJECTNAME);
//		Map<String, String> paramMap = new HashMap<>();
//		paramMap.put("path", projectPath);
//		String res = HttpTools.postformRequest(HttpVars.PREDICTURL, paramMap);
////		System.out.println(res);
//		BaseResult jsonObj = JsonUtils.getJsonResult(res);
//		if (jsonObj != null) {
//			System.out.println("Parse json data ok.");
//			showProjectSummaryWindow(jsonObj.getRes(), TARGETPROJECTNAME);
//		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
		
	}
	
	private String getClikeAbsPath() {
		String projectPath = "";
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object element = structuredSelection.getFirstElement();
			//System.out.println(object.getClass());
 			IProject project = null; 
			if (element instanceof IResource) {
				project= ((IResource)element).getProject();
			}else if (element instanceof IJavaProject) {
                IJavaProject jProject =  ((IJavaProject)element).getJavaProject();    
                project = jProject.getProject();    
            } else if (element instanceof IJavaElement) {    
                IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
                project = jProject.getProject();    
            }
			projectPath = project.getLocationURI().getPath();
			TARGETPROJECTNAME = project.getName();
//			System.out.println(project.getName());
		}
		return projectPath;
	}
	
	
	/**
	 * Launch the application.
	 */
	private static void showProjectSummaryWindow(String projecrName) {
		ProjectSummarySWTWindow.runSWTWindow(projecrName);
	}
}
