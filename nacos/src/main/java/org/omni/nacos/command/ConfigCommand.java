package org.omni.nacos.command;

import com.alibaba.nacos.api.exception.NacosException;

/**
 * @author Xieningjun
 * @date 2024/11/13 13:50
 * @description
 */
public interface ConfigCommand {

    void subscribe(String dataId) throws NacosException;

}
