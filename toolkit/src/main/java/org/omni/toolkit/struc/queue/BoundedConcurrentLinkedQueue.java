package org.omni.toolkit.struc.queue;

import org.omni.toolkit.sug.Sugars;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xieningjun
 */
public class BoundedConcurrentLinkedQueue<T> extends ConcurrentLinkedQueue<T> {

    private final int maxSize;

    // 构造函数，初始化队列和最大容量
    public BoundedConcurrentLinkedQueue(int maxSize) {
        Sugars.$if$throw(maxSize <= 0, new IllegalArgumentException("MaxSize must be greater than 0"));
        this.maxSize = maxSize;
    }

    // 重写 offer() 方法，确保队列不会超过最大大小
    @Override
    public boolean offer(T element) {
        boolean added = super.offer(element);  // 调用父类的 offer 方法添加元素
        // 如果队列大小超过了最大容量，移除队列首部的元素
        while (size() > maxSize) {
            poll();  // 删除队列的头部元素
        }
        return added;
    }
}
