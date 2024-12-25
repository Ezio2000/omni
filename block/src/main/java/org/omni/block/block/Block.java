package org.omni.block.block;

import lombok.Data;
import org.omni.toolkit.design.event.Event;

import java.time.Instant;

/**
 * @author Xieningjun
 * @date 2024/12/24 16:34
 * @description
 */
@Data
public class Block<T> implements Event<T> {

    private final T data;

    private final String prevHash;

    private final long timestamp = Instant.now().toEpochMilli();

    private String hash;

    private int status;

    private int difficulty;

    public Block(T data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
    }

    public String mine() {
        return "";
    }

}
