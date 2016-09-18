package com.prowo.dynamic.webext.common.context;

import com.prowo.ydnamic.cache.CacheManager;
import com.prowo.ydnamic.context.IRefresher;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ydnamic.persist.ComxPagingDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采用继承和覆盖的方式来实现缓存的加载，实用于单个配置系统。
 */
public class DefaultRefresher implements IRefresher {
    // 合作商管理
    public static final String USER_ALL = "SELECT c_id, c_pass FROM c_users WHERE is_delete=0 and c_remark like '%?%'";
    // 配置管理ADMIN
    private static final String CONFIG_QUERY_ADMIN = "SELECT c_key, c_value FROM c_configs WHERE is_delete=0 and partner_id ='admin'";
    // 异常管理
    private static final String EXCEPTIONS_QUERY = "SELECT c_code, c_value FROM c_exceptions WHERE is_delete='0'";
    // 接口管理
    private static final String DICT_ALL = "SELECT c_name, partner_id FROM c_dicts where partner_id is not null and is_delete='0'";
    // 消息 管理
    private static final String MESSAGES_QUERY_ADMIN = "SELECT c_key, c_value FROM c_messages WHERE is_delete='0' and partner_id='admin'";

    protected ComxPagingDAO cadao;
    protected CacheManager cacheManager;

    public void setCadao(ComxPagingDAO cadao) {
        this.cadao = cadao;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 加载配置ADMIN的缓存
     *
     * @throws Exception
     */
    @Override
    public void loadConfigs() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_CONFIG);
        List<Map<String, Object>> configList = cadao.querys(CONFIG_QUERY_ADMIN);
        Map<String, String> configs = new HashMap<String, String>();
        for (Map<String, Object> map : configList) {
            configs.put(map.get("c_key").toString(), map.get("c_value").toString());
        }
        configs.putAll(getSubConfigs());// 将admin里有重复的可以会冲掉，
        cacheManager.setMap(HandlerConstans.CACHE_KEY_CONFIG, configs);
    }

    protected Map<String, String> getSubConfigs() throws Exception {
        return null;

    }

    @Override
    public void loadExceptions() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_EXCEPTION);
        List<Map<String, Object>> messageList = cadao.querys(EXCEPTIONS_QUERY);
        Map<String, String> messages = new HashMap<String, String>();
        for (Map<String, Object> map : messageList) {
            messages.put(map.get("c_code").toString(), map.get("c_value").toString());
        }
        messages.putAll(getSubExceptions());// 将admin里有重复的可以会冲掉，
        cacheManager.setMap(HandlerConstans.CACHE_KEY_EXCEPTION, messages);
    }

    protected Map<String, String> getSubExceptions() throws Exception {
        return null;
    }

    @Override
    public void loadExt() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadIpAdresses() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadMessages() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_MESSAGE);
        List<Map<String, Object>> messageList = cadao.querys(MESSAGES_QUERY_ADMIN);
        Map<String, String> messages = new HashMap<String, String>();
        for (Map<String, Object> map : messageList) {
            messages.put(map.get("c_key").toString(), map.get("c_value").toString());
        }
        messages.putAll(getSubMessages());// 将admin里有重复的可以会冲掉，
        cacheManager.setMap(HandlerConstans.CACHE_KEY_MESSAGE, messages);
    }

    protected Map<String, String> getSubMessages() throws Exception {
        return null;
    }

    @Override
    public void loadUsers() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_USER);
        List<Map<String, Object>> userList = cadao.querys(USER_ALL.replace("?", getSqlParameter()));
        Map<String, String> users = new HashMap<String, String>();
        for (Map<String, Object> map : userList) {
            users.put(map.get("c_id").toString(), map.get("c_pass").toString());
        }
        cacheManager.setMap(HandlerConstans.CACHE_KEY_USER, users);
    }

    protected String getSqlParameter() {
        return "admin";
    }

    @Override
    public void refreshCache() throws Exception {
        try {
            /*
             * loadConfigs(); loadUsers();
             */
        } catch (Exception e) {
            LoggerUtil.log(Level.ERROR, e, "refreshCache error");
            throw new RuntimeException("refreshCache error");
        }
    }

    @Override
    public void loadDicts() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_DICT);
        List<Map<String, Object>> dictList = cadao.querys(DICT_ALL);
        Map<String, String> dicts = new HashMap<String, String>();
        for (Map<String, Object> map : dictList) {
            dicts.put(map.get("partner_id").toString() + "/" + map.get("c_name").toString(), "0");
        }
        cacheManager.setMap(HandlerConstans.CACHE_KEY_DICT, dicts);
    }

}
