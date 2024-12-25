package org.omni.toolkit.design.custom;

/**
 * @author Xieningjun
 * @date 2024/11/20 14:23
 * @description
 */
public class DisposableTuple<E1, E2> extends Tuple<E1, E2> {

    private E1 first;

    private E2 second;

    public DisposableTuple(E1 first) {
        super(first, null);
    }
    
    public E1 getFirst() {
        return first;
    }

}
