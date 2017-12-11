package com.alcor.ril.security;

import com.alcor.ril.persistence.entity.SysPermissionEntity;
import com.alcor.ril.service.PermissionService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/8
 * Time: 21:34
 */
@Slf4j
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PermissionService permissionService;
    
//    private Map<String, Collection<ConfigAttribute>> requestMap = new HashMap<>();

    private Table<String, String, String> requestMap = HashBasedTable.create();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    Collection<ConfigAttribute> cas = new ArrayList() {{
        add(new SecurityConfig("null"));
    }};
    
    // 判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。
    // 如果不在权限表中则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        return cas;
        if(requestMap.isEmpty()) {
            loadResourceDefine();
        }

        HttpServletRequest request = ((FilterInvocation)object).getRequest();
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        
        log.debug("access [{}:{}]", requestMethod, requestPath);

        Collection<ConfigAttribute> array = new ArrayList<>();

//        for(Map.Entry<String, Collection<ConfigAttribute>> entry: requestMap.entrySet()) {
//            if(antPathMatcher.match(entry.getKey(), requestPath)){
//                log.debug("[{}] match [{}]", requestPath, entry.getKey());
//                array.addAll(entry.getValue());
//            }
//        }
        for(String permission : requestMap.rowKeySet()) {
            Map<String, String> atts = requestMap.row(permission);
            if(antPathMatcher.match(atts.get("url"), requestPath)) {
                //当权限表权限的method为ALL时表示拥有此路径的所有请求方式权利。
                if(atts.get("method").equals(requestMethod) || atts.get("method").equals("ALL")) {
                    log.debug("[{}:{}] match [{}:{}]", requestMethod, requestPath, atts.get("method"), atts.get("url"));
                    array.add(new SecurityConfig(permission));
                }
            }
        }
        
        if(array.isEmpty()) {
            return null;
        } else {
            return array;
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 加载权限表中所有权限
     */
    private void loadResourceDefine(){
        log.debug("loading all permissions...");
//        Collection<ConfigAttribute> array;
        List<SysPermissionEntity> permissions = permissionService.findAll();
        for(SysPermissionEntity permission : permissions) {
            if(permission.getAvailable() == true) {
//                array = new ArrayList<>();
//                ConfigAttribute cfg = new SecurityConfig(permission.getPermission().toUpperCase());
                //此处只添加了权限的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。
                // 此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
//                array.add(cfg);
                //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
//                requestMap.put(permission.getUrl(), array);
                
                requestMap.put(permission.getPermission().toUpperCase(), "url", permission.getUrl());
                requestMap.put(permission.getPermission().toUpperCase(), "method", permission.getMethod());
            }
        }
    }
}
