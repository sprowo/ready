package com.prowo.dynamic.webext.common.context;

import com.prowo.ydnamic.cache.CacheManager;
import com.prowo.ydnamic.context.IDomainList;
import com.prowo.ydnamic.context.IRefresher;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;
import com.prowo.ydnamic.persist.ComxPagingDAO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采用作用域（类似于邮箱）的key结构来隔离数据，适合那种对接包含相互无关却内容相似的多个子系统的运行环境
 */
public class DomainRefresher implements IRefresher, IDomainList {
    // 合作商管理
    public static final String USER_ALL = "SELECT c_id, c_pass FROM c_users WHERE is_delete=0 and (c_remark like '%#path_name#%' or c_id='admin')";
    // 配置管理ADMIN
    private static final String CONFIG_QUERY_ADMIN = "SELECT c_key, c_value FROM c_configs WHERE is_delete=0 and partner_id ='admin'";
    // 异常管理
    private static final String EXCEPTIONS_QUERY_ADMIN = "SELECT c_code, c_value FROM c_exceptions WHERE is_delete='0' and partner_id='admin'";
    // 接口管理
    private static final String DICT_ALL_ADMIN = "SELECT c_name,c_value, partner_id FROM c_dicts where is_delete='0' and partner_id='admin'";
    // 消息 管理
    private static final String MESSAGES_QUERY_ADMIN = "SELECT c_key, c_value FROM c_messages WHERE is_delete='0' and partner_id='admin'";

    // 配置管理
    private static final String CONFIG_QUERY = "SELECT c_key, c_value, partner_id FROM c_configs WHERE is_delete='0' and partner_id in(#partner_list#)";
    // 异常管理
    private static final String EXCEPTIONS_QUERY = "SELECT c_code, c_value, partner_id FROM c_exceptions WHERE is_delete='0' and partner_id in(#partner_list#)";
    // 消息管理
    private static final String MESSAGES_QUERY = "SELECT c_key, c_value, partner_id FROM c_messages WHERE is_delete='0' and partner_id in(#partner_list#)";
    // dict管理
    private static final String DICTS_QUERY = "SELECT c_name,c_value, partner_id FROM c_dicts where is_delete='0' and partner_id in(#partner_list#)";


    protected ComxPagingDAO cadao;
    protected CacheManager cacheManager;
    private String path_name = "admin";
    private String partner_list = "('admin')";
    private List<String> domainList = Arrays.asList("admin");

    public void setCadao(ComxPagingDAO cadao) {
        this.cadao = cadao;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setPathName(String pathName) {
        this.path_name = pathName;
    }

    public void setPartnerList(String partnerList) {
        List<String> partner_list = Arrays.asList(partnerList.split("\\s*,\\s*"));

        StringBuffer sb = new StringBuffer("");
        for (String appId : partner_list) {
            sb.append("'").append(appId).append("',");
        }
        sb.deleteCharAt(sb.length() - 1);
        this.partner_list = sb.toString();
        domainList = partner_list;
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
        List<Map<String, Object>> configList = cadao.querys(CONFIG_QUERY.replace("#partner_list#", partner_list));
        Map<String, String> configs = new HashMap<String, String>();
        for (Map<String, Object> map : configList) {
            configs.put(map.get("c_key").toString() + "@" + map.get("partner_id"), map.get("c_value").toString());
        }
        return configs;
    }

    @Override
    public void loadExceptions() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_EXCEPTION);
        List<Map<String, Object>> messageList = cadao.querys(EXCEPTIONS_QUERY_ADMIN);
        Map<String, String> messages = new HashMap<String, String>();
        for (Map<String, Object> map : messageList) {
            messages.put(map.get("c_code").toString(), map.get("c_value").toString());
        }
        messages.putAll(getSubExceptions());// 将admin里有重复的可以会冲掉，
        cacheManager.setMap(HandlerConstans.CACHE_KEY_EXCEPTION, messages);
    }

    protected Map<String, String> getSubExceptions() throws Exception {
        List<Map<String, Object>> exceptionsList = cadao.querys(EXCEPTIONS_QUERY.replace("#partner_list#", partner_list));
        Map<String, String> exceptions = new HashMap<String, String>();
        for (Map<String, Object> map : exceptionsList) {
            exceptions.put(map.get("c_code").toString() + "@" + map.get("partner_id"), map.get("c_value").toString());
        }
        return exceptions;
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
        List<Map<String, Object>> messageList = cadao.querys(MESSAGES_QUERY.replace("#partner_list#", partner_list));
        Map<String, String> messages = new HashMap<String, String>();
        for (Map<String, Object> map : messageList) {
            messages.put(map.get("c_key").toString() + "@" + map.get("partner_id"), map.get("c_value").toString());
        }
        return messages;
    }

    @Override
    public void loadUsers() throws Exception {
        cacheManager.clearCache(HandlerConstans.CACHE_KEY_USER);
        List<Map<String, Object>> userList = cadao.querys(USER_ALL.replace("#path_name#", path_name));
        Map<String, String> users = new HashMap<String, String>();
        for (Map<String, Object> map : userList) {
            users.put(map.get("c_id").toString(), map.get("c_pass").toString());
        }
        cacheManager.setMap(HandlerConstans.CACHE_KEY_USER, users);
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
        List<Map<String, Object>> dictList = cadao.querys(DICT_ALL_ADMIN);
        Map<String, String> dicts = new HashMap<String, String>();
        for (Map<String, Object> map : dictList) {
            dicts.put(map.get("c_name").toString(), map.get("c_value").toString());
        }
        dicts.putAll(getSubDicts());// 将admin里有重复的可以会冲掉，

        cacheManager.setMap(HandlerConstans.CACHE_KEY_DICT, dicts);
    }

    protected Map<String, String> getSubDicts() throws Exception {
        List<Map<String, Object>> configList = cadao.querys(DICTS_QUERY.replace("#partner_list#", partner_list));
        Map<String, String> configs = new HashMap<String, String>();
        for (Map<String, Object> map : configList) {
            configs.put(map.get("c_name").toString() + "@" + map.get("partner_id"), map.get("c_value").toString());
        }
        return configs;
    }

    @Override
    public List<String> getDomainList() {
        return domainList;
    }

}
