package codeine;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import codeine.api.CommandStatusJson;
import codeine.api.NodeWithMonitorsInfo;
import codeine.api.ScehudleCommandExecutionInfo;
import codeine.api.VersionItemInfo;
import codeine.jsons.project.ProjectJson;
import codeine.model.Constants;
import codeine.model.Constants.UrlParameters;
import codeine.utils.network.HttpUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CodeineApiClient {

	private String host;
	private int port;
	private Gson gson = new Gson();

	public CodeineApiClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	

	public List<ProjectJson> projects() {
		return apiCall(Constants.PROJECTS_LIST_CONTEXT,"", new TypeToken<List<ProjectJson>>(){}.getType());
	}



	private <T> T apiCall(String path, String params, Type type) {
		String json = HttpUtils.doGET(getServerPath(path) + params);
		return gson.fromJson(json, type);
	}



	private String getServerPath(String contextPath) {
		return "http://"+host+":"+port + Constants.apiContext(contextPath);
	}



	public Map<String, VersionItemInfo> projectStatus(String projectName) {
		return apiCall(
				Constants.PROJECT_STATUS_CONTEXT,"?" + projectNameParam(projectName) ,
				new TypeToken<Map<String, VersionItemInfo>>(){}.getType());
	}



	private String projectNameParam(String projectName) {
		return Constants.UrlParameters.PROJECT_NAME + "=" + HttpUtils.encode(projectName);
	}



	public List<NodeWithMonitorsInfo> projectNodes(String projectName, String version) {
		return apiCall(
				Constants.PROJECT_NODES_CONTEXT,"?" + projectNameParam(projectName)  + "&" + Constants.UrlParameters.VERSION_NAME + "=" + HttpUtils.encode(version),
				new TypeToken<List<NodeWithMonitorsInfo>>(){}.getType());
	}



	public ProjectJson project(String name) {
		for (ProjectJson p : projects()) {
			if (p.name().equals(name)){
				return p;
			}
		}
		throw new IllegalArgumentException("no project " + name);
	}



	public String runCommand(ScehudleCommandExecutionInfo data) {
		String url = getServerPath(Constants.COMMAND_NODES_CONTEXT);
		String postData = UrlParameters.DATA_NAME + "=" + HttpUtils.encode(gson.toJson(data));
		return HttpUtils.doPOST(url, postData);
	}



	public List<CommandStatusJson> commandHistory(String projectName) {
		return apiCall(Constants.COMMANDS_LOG_CONTEXT, "?" + projectNameParam(projectName) , new TypeToken<List<CommandStatusJson>>(){}.getType()); 
	}
}