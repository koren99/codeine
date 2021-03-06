package codeine.db.mysql;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import codeine.executer.Task;
import codeine.jsons.global.GlobalConfigurationJsonStore;
import codeine.jsons.global.MysqlConfigurationJson;
import codeine.utils.network.InetUtils;

public class NearestMysqlHostSelectorPreferLocalhost implements Task, MysqlHostSelector{

	private static final Logger log = Logger.getLogger(NearestMysqlHostSelectorPreferLocalhost.class);
	@Inject
	private GlobalConfigurationJsonStore conf;
	
	private MysqlConfigurationJson mysqlConf; 
	
	@Override
	public MysqlConfigurationJson mysql() {
		return mysql(false);
	}

	private MysqlConfigurationJson mysql(boolean forceNew) {
		if (null != mysqlConf && !forceNew){
			return mysqlConf;
		}
		mysqlConf = getLocalConfOrNull();
		if (null != mysqlConf) {
			return mysqlConf;
		}
		mysqlConf = selectNearestConf();
		return mysqlConf;
	}

	private MysqlConfigurationJson selectNearestConf() {
		log.info("selectNearestConf - starting");
		MysqlConfigurationJson selectedMysql = new NearestHostSelector(conf.get().mysql()).select();
		log.info("selectNearestConf - selected mysql " + selectedMysql);
		return selectedMysql;
	}

	public MysqlConfigurationJson getLocalConfOrNull() {
		return getLocalConfOrNull(conf);
	}

	public static MysqlConfigurationJson getLocalConfOrNull(GlobalConfigurationJsonStore conf2) {
		log.info("getLocalConfOrNull - checking host");
		for (MysqlConfigurationJson mysqlConfigurationJson : conf2.get().mysql()) {
			try {
				if (InetAddress.getByName(mysqlConfigurationJson.host()).equals(InetUtils.getLocalHost())){
					log.info("returning localhost " + mysqlConfigurationJson.host());
					return mysqlConfigurationJson;
				}
			} catch (UnknownHostException e) {
				log.warn("host unknown " + e.getMessage());
			}
		}
		return null;
	}
	public MysqlConfigurationJson getLocalConf() {
		MysqlConfigurationJson localConf = getLocalConfOrNull();
		if (null == localConf) {
			throw new RuntimeException("could not find mysql configuration to start with. host is " + InetUtils.getLocalHost() + " and configuration is " + conf.get().mysql());
		}
		return localConf;
	}

	
	@Override
	public String toString() {
		return "NearestMysqlHostSelector [mysqlConf=" + mysqlConf + "]";
	}

	@Override
	public void run() {
		log.info("Checking for nearest mysql db");
		mysql(true);
	}
}
