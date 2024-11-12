package org.omni.toolkit.design.mq.event;

import org.omni.toolkit.empty.EmptyObj;

import java.util.Objects;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:41
 * @description
 */
public class NotifyEvent implements Event<EmptyObj> {

    private static final NotifyEvent event = null;

    private NotifyEvent() {}

    public static NotifyEvent of() {
        return Objects.requireNonNullElseGet(event, NotifyEvent::new);
    }

    @Override
    public EmptyObj getData() {
        return EmptyObj.of();
    }

}
