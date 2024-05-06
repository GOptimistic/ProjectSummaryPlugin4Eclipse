package buaa.guanz.prosummary.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import buaa.guanz.prosummary.commons.BaseResult;
import buaa.guanz.prosummary.commons.JsonUtils;

public class FileSummaryGenerator {
//	private Map<String, String> fileSummaryMap = new HashMap<>();
	
	public FileSummaryGenerator() {
//		fileSummaryMap.put("org.xtreemfs.babudb.api.exception.BabuDBException", 
//				"The BabuDBException class in org.xtreemfs.babudb.api.exception package is a database exception class that extends the Exception class and provides various error codes for different types of errors that can occur in the BabuDB database.");
//		fileSummaryMap.put("org.xtreemfs.babudb.lsmdb.LSN",
//				"The java class file org.xtreemfs.babudb.lsmdb.LSN represents a Log Sequence Number (LSN) used in XtreemFS, a distributed file system. It stores the view ID and sequence number and provides methods to compare and convert LSNs, and to display them as strings.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.database.ResultSet",
//				"The java class file org.xtreemfs.babudb.api.database.ResultSet is an extension of the java.util.Iterator interface, providing a result set for prefix and range queries. It defines an extra method to explicitly free any buffers bound to the iterator, to keep the memory footprint of BabuDB as low as possible.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.index.ByteRangeComparator",
//				"The java class file org.xtreemfs.babudb.api.index.ByteRangeComparator is a comparator for byte buffers and byte ranges used in the XtreemFS Babudb open-source project. It is designed to be serializable, ensuring the ability to record instances of custom ByteRangeComparators in the database log. The comparator efficiently compares byte ranges and byte buffers, and it also converts prefixes to ranges to facilitate prefix queries.");
//		fileSummaryMap.put("org.xtreemfs.babudb.replication.transmission.dispatcher.Operation",
//				"org.xtreemfs.babudb.replication.transmission.dispatcher.Operation is an abstract class that provides the base functionality for operations triggered by external RPC server requests. It contains methods for starting the request, processing it, and returning an empty request message. The class also contains a method for parsing an RPC message and returning an error response if necessary.");
//		fileSummaryMap.put("org.xtreemfs.babudb.replication.BabuDBInterface",
//				"The java class file org.xtreemfs.babudb.replication.BabuDBInterface provides methods for executing various actions on BabuDB, including appending entries to a local disk logger, getting the latest LSN, waiting for a checkpoint, stopping and restarting BabuDB, and retrieving information about databases.");
//		fileSummaryMap.put("org.xtreemfs.babudb.log.LogEntry",
//				"The LogEntry class in the xtreemfs/babudb project is used to store metadata and payload information in a log-structured file system. It provides methods for serializing and deserializing log entries, as well as methods for assigning a view ID and a log sequence number to the entry. The class also includes methods for setting and getting the payload type, listener, attachment, and checksum of the log entry.");
//		fileSummaryMap.put("org.xtreemfs.babudb.replication.transmission.dispatcher.Request",
//				"The java class file org.xtreemfs.babudb.replication.transmission.dispatcher.Request is an object used to encapsulate a request for a replication operation in the XtreemFS Babudb open-source project. It contains information such as the request ID, the sender's address, the request message, and the operation being performed. The class also includes methods for sending different types of errors and responses to the sender, and for freeing the resources associated with the request. The access to the request is not thread-safe.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.dev.BabuDBInternal",
//				"The java class file org.xtreemfs.babudb.api.dev.BabuDBInternal provides an interface for internal usage of the BabuDB library. It exposes methods for managing checkpointer, response manager, database manager, snapshot manager, and transaction manager. The file also includes methods for getting configuration and initializing BabuDB services.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.database.DatabaseRequestListener",
//				"This Java class file, org.xtreemfs.babudb.api.database.DatabaseRequestListener, is an interface that defines a listener for asynchronously waiting for a BabuDBRequest to be finished. It provides two methods: finished(), which is called when the request is completed successfully, and failed(), which is called when the request fails.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.BabuDB",
//				"The BabuDB API provides a main user application interface for managing databases, snapshots, and runtime state in a distributed, replicated, and asynchronously-consistent file system-based storage system.");
//		fileSummaryMap.put("org.xtreemfs.babudb.replication.transmission.ErrorCode",
//				"The ErrorCode class in the xtreemfs/babudb project defines a set of error codes that may occur during replication transmission. It also provides methods for mapping user errors to transmission errors and vice versa.");
//		fileSummaryMap.put("org.xtreemfs.babudb.index.ByteRange",
//				"The org.xtreemfs.babudb.index.ByteRange class represents a range of bytes in a buffer and provides methods for accessing, manipulating, and converting the data within the range.");
//		fileSummaryMap.put("org.xtreemfs.babudb.snapshots.SnapshotConfig",
//				"The SnapshotConfig interface in the xtreemfs/babudb project is a set of configuration parameters for a new snapshot, including the snapshot name, indices to include, and prefix keys to write. It also allows for checking if a given key is contained in the snapshot.");
//		fileSummaryMap.put("org.xtreemfs.babudb.BabuDBRequestResultImpl",
//				"The org.xtreemfs.babudb.BabuDBRequestResultImpl class is a future-object that encapsulates the results and errors of BabuDB requests. It allows for registering a listener to be notified of the result, and provides methods to retrieve the result once it's available.");
//		fileSummaryMap.put("org.xtreemfs.babudb.api.dev.DatabaseInternal",
//				"The `DatabaseInternal` interface provides internal access to the Babudb database and includes methods for creating and managing insert groups, writing snapshots, and performing direct lookups and prefix lookups on indexes.");
	}
	
	public String getFileSummary(String className, String filePath, String projectName) {
		System.out.println("###### Start " + className + " " + filePath + " " + projectName);
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("fileName", className);
		paramMap.put("repoName", projectName);
		String fileContent = "";
		try {
            fileContent = HttpTools.readFileToString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
		paramMap.put("context", fileContent);

		String res = HttpTools.postformRequest(HttpVars.FILESUMMARYURL, paramMap);
		BaseResult jsonObj = JsonUtils.getJsonResult(res);
		if (jsonObj != null) {
			System.out.println("Parse json data ok.");
			System.out.println("###### Summary " + jsonObj.getCode() + " " + jsonObj.getRes());
			return jsonObj.getRes();
		} else {
			return "Generate file summary failed";
		}
		
	}
}
