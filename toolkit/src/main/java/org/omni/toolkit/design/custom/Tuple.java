package org.omni.toolkit.design.custom;

import lombok.Data;

/**
 * @author Xieningjun
 * @date 2024/11/8 14:59
 * @description
 */
@Data
public class Tuple<E1, E2> {

    private E1 first;

    private E2 second;

    public Tuple(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

}
