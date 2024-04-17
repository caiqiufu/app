package com.slipper.unieap.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.pojo.UnieapDsConfig;
import com.slipper.unieap.repository.UnieapDsConfigRepository;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.json.JSONUtil;

public final class DBManager {
	protected static Logger logger = LoggerFactory.getLogger(DBManager.class);

	private static Map<String, JdbcTemplate> jt = new HashMap<String, JdbcTemplate>();

	public static JdbcTemplate getJT() {
		return (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
	}

	@SuppressWarnings("unchecked")
	public static JdbcTemplate getBizJT(String dsName) {
		if (StringUtils.isEmpty(dsName)) {
			dsName = "unieap";
		}
		if (jt.containsKey(dsName)) {
			return jt.get(dsName);
		} else {
			JdbcTemplate jdbc;
			if (StringUtils.equals(dsName, "unieap")) {
				jdbc = (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
			} else {
				UnieapDsConfigRepository unieapDsConfigRepository = (UnieapDsConfigRepository) ApplicationContextProvider
						.getBean("unieapDsConfigRepository");
				UnieapDsConfig unieapDsConfig = (UnieapDsConfig) unieapDsConfigRepository.findByDsCode(dsName);
				if (unieapDsConfig != null) {
					Map<String, Object> properties = JSONUtil.toBean(unieapDsConfig.getConfig(), Map.class);
					DataSource ds = new HikariDataSource();
					bind(ds, properties);
					jdbc = new JdbcTemplate(ds);
				} else {
					jdbc = (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
				}
			}
			testJT(jdbc);
			jt.put(dsName, jdbc);
			return jdbc;
		}
	}

	private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
	/**
	 * 由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
	 */
	static {
		aliases.addAliases("url", new String[] { "jdbc-url" });
		aliases.addAliases("username", new String[] { "user" });
	}

	private static void bind(DataSource result, Map<String, Object> properties) {
		ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
		Binder binder = new Binder(new ConfigurationPropertySource[] { source.withAliases(aliases) });
		// 将参数绑定到对象
		binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
	}

	private static void testJT(JdbcTemplate jt) {
		String sql = "select sysdate() as dt from dual";
		Map<String, Object> data = jt.queryForMap(sql);
		String dt = data.get("dt").toString();
		logger.info("JdbcTemplate Test, datetime= " + dt);
	}
}
