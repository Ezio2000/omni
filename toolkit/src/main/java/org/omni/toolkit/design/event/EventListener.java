package org.omni.toolkit.design.event;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:13
 * @description
 */
public interface EventListener<T> {

    void listen(Event<T> event);

}
