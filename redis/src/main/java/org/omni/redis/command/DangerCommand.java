package org.omni.redis.command;

import java.io.Closeable;

/**
 * @author Xieningjun
 * @date 2024/11/7 19:23
 * @description
 */
public interface DangerCommand {

    MonitorProcess monitor();

    interface MonitorProcess extends Closeable {}

}
