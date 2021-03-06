package codeine.servlets.api_servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import codeine.api.NodeWithMonitorsInfo;
import codeine.db.IStatusDatabaseConnector;
import codeine.db.mysql.connectors.StatusDatabaseConnectorListProvider;
import codeine.jsons.peer_status.PeerStatusJsonV2;
import codeine.jsons.peer_status.ProjectStatus;
import codeine.servlet.AbstractApiServlet;

import com.google.inject.Inject;

public class ReporterServlet extends AbstractApiServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ReporterServlet.class);

	private IStatusDatabaseConnector databaseConnector;

	@Inject
	public ReporterServlet(StatusDatabaseConnectorListProvider databaseConnectorProvider) {
		super();
		this.databaseConnector = databaseConnectorProvider.get().get(0);
	}

	@Override
	protected void myPost(HttpServletRequest req, HttpServletResponse resp) {
		NodeWithMonitorsInfo nodeWithMonitorsInfo = readBodyJson(req, NodeWithMonitorsInfo.class);
		log.info("Recieved status " + nodeWithMonitorsInfo);
		PeerStatusJsonV2 json = new PeerStatusJsonV2(nodeWithMonitorsInfo.projectName() + "_"
				+ nodeWithMonitorsInfo.name(), createProjectStatus(nodeWithMonitorsInfo));
		log.info("Pushing peer report " + json);
		databaseConnector.putReplaceStatus(json);
	}

	private ProjectStatus createProjectStatus(NodeWithMonitorsInfo nodeWithMonitorsInfo) {
		ProjectStatus projectStatus = new ProjectStatus(nodeWithMonitorsInfo.projectName(), nodeWithMonitorsInfo);
		return projectStatus;
	}

	@Override
	protected boolean checkPermissions(HttpServletRequest request) {
		return true;
	}
}
