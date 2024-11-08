package org.omni.redis.command;

/**
 * @author Xieningjun
 * @date 2024/11/7 19:27
 * @description
 */
public interface ServerCommand {

    String info();

    String ping();

}
