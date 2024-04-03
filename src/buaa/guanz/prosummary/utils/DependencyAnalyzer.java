package buaa.guanz.prosummary.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class DependencyAnalyzer {

	private Map<String, String> class2FilePath = new HashMap<>();
	private Map<String, String> filePath2Class = new HashMap<>();
    private Map<String, Set<String>> class2Imports = new HashMap<>();
    private String[] keywords = {"util", "test", "doc", "tmp", "temp", "backup", "old", "demo", "example", "archive"};
    private JavaParser javaParser = new JavaParser();
    private static int MAX_FILE_NUM = 16;
    
    public DependencyAnalyzer(){
    	
    }

    
    public void clearDependencies() {
    	this.class2FilePath.clear();
    	this.filePath2Class.clear();
    	this.class2Imports.clear();
    }
    
    public boolean containsKeywords(String str) {
    	String lowerCaseStr = str.toLowerCase();
        for (String keyword : keywords) {
            if (lowerCaseStr.contains(keyword)) {
                return true; // 找到一个关键字，返回true
            }
        }
        return false; // 没有找到任何关键字，返回false
    }

    public void analyzeProject(File file) throws Exception {
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
            	analyzeProject(child);
            }
        } else if (file.getName().endsWith(".java") && !containsKeywords(file.getName())) {
            FileInputStream in = new FileInputStream(file);
            
            CompilationUnit cu = null;
            try {
                // Parse the file
            	ParseResult<CompilationUnit> parseResult = javaParser.parse(in);
            	// 检查解析是否成功
                if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                	cu = parseResult.getResult().get();
                } else {
                    System.out.println("###### Parsing failed.");
                    throw new Exception(String.format("###### Parsing %s failed.", file.getName()));
                }  
            } catch (Exception e) {
            	System.out.printf("###### Parsing failed. %s", e.getMessage());
            	return;
            } finally {
                in.close();
            }
            if (cu != null) {
            	String fileName = file.getName().replace(".java", "");
            	// 获取包名
                Optional<String> packageName = cu.getPackageDeclaration()
                                                  .map(pd -> pd.getName().asString());
                String fullClassName = packageName.map(pn -> pn + "." + fileName).orElse(fileName);
                Set<String> dependencies = extractDependencies(cu);
                class2FilePath.put(fullClassName, file.getAbsolutePath());
                filePath2Class.put(file.getAbsolutePath(), fullClassName);
                class2Imports.put(fullClassName, dependencies);
            }
            
        }
    }
    
    private static Set<String> extractDependencies(CompilationUnit cu) {
        Set<String> dependencies = new HashSet<>();
        // 遍历所有导入声明
        for (ImportDeclaration importDecl : cu.getImports()) {
            // 添加导入的类或包名到依赖列表
            dependencies.add(importDecl.getNameAsString());
        }
        return dependencies;
    }


    public List<Entry<String, Integer>> processDependencies() {
        // 首先预处理依赖关系，将不是本项目的依赖去除
    	for (Map.Entry<String, Set<String>> entry : class2Imports.entrySet()) {
    		// 使用迭代器遍历当前条目的值（即Set<String>）以便在遍历时修改集合
            Iterator<String> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                String importClassName = iterator.next();
                // 如果class2FilePath的键中不包含当前导入的类名，就从集合中移除它
                if (!class2FilePath.containsKey(importClassName)) {
                    iterator.remove();
                }
            }
    	}
    	// 计算入度
    	Map<String, Integer> inDegrees = new HashMap<>();
    	// 初始化
//    	for (String className : class2Imports.keySet()) {
//    		inDegrees.put(className, 0);
//    	}
        for (Set<String> dependents : class2Imports.values()) {
            for (String dependent : dependents) {
                inDegrees.put(dependent, inDegrees.getOrDefault(dependent, 0) + 1);
            }
        }
        
        List<Map.Entry<String, Integer>> topNodes = inDegrees.entrySet().stream()
	        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
	        .limit(MAX_FILE_NUM)
	        .collect(Collectors.toList());
        return topNodes;
    }
    
    public List<SimpleEntry<String, String>> getImportAnalyzeResults(File project) throws Exception {
    	this.clearDependencies();
    	this.analyzeProject(project);
        List<Entry<String, Integer>> topNodes = this.processDependencies();
        List<SimpleEntry<String, String>> results = new ArrayList<>();
        topNodes.forEach(entry -> results.add(new SimpleEntry<>(entry.getKey(), class2FilePath.get(entry.getKey()))));
        try {
            Thread.sleep(5000); // 睡眠 1000 毫秒，即 1 秒
        } catch (InterruptedException e) {
            // 处理中断异常
        	e.printStackTrace();
        }
        return results;
    }
    
    public static void main(String[] args) throws Exception {
        File projectDir = new File("/Users/guanzheng/cls_work/python_test/github_repo_data/java/xtreemfs_babudb");
        DependencyAnalyzer analyzer = new DependencyAnalyzer();
        analyzer.clearDependencies();
        analyzer.analyzeProject(projectDir);
        List<Entry<String, Integer>> topNodes = analyzer.processDependencies();
        
        System.out.println("Top nodes by in-degree:");
        topNodes.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        
        List<SimpleEntry<String, String>> results = analyzer.getImportAnalyzeResults(projectDir);
        System.out.println("----------------");
        System.out.println("Analyze results:");
        results.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}


