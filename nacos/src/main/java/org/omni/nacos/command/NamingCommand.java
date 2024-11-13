package org.omni.nacos.command;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

/**
 * @author Xieningjun
 * @date 2024/11/13 13:48
 * @description
 */
public interface NamingCommand {

    void register() throws NacosException;

    Instance discover(String serviceName) throws NacosException;

}
