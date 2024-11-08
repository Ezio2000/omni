package org.omni.toolkit.struc.list;

import org.omni.toolkit.ex.ForbiddenException;
import org.omni.toolkit.vir.Virs;

import java.util.*;

/**
 * @author Xieningjun
 * @date 2024/11/8 16:26
 * @description
 */
public class ExpireList<E> implements List<E> {

    private final List<E> delegate;

    private final long expire;

    public ExpireList(List<E> delegate, long expire) {
        this.delegate = delegate;
        this.expire = expire;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean add(E e) {
        Virs.after(() -> delegate.remove(e), expire);
        return delegate.add(e);
    }

    @Override
    public boolean remove(Object o) {
        throw new ForbiddenException("Forbidden to clear.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Virs.after(() -> delegate.removeAll(c), expire);
        return delegate.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Virs.after(() -> delegate.removeAll(c), expire);
        return delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new ForbiddenException("Forbidden to remove all.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void clear() {
        throw new ForbiddenException("Forbidden to clear.");
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public E set(int index, E element) {
        Virs.after(() -> delegate.remove(element), expire);
        return delegate.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        Virs.after(() -> delegate.remove(element), expire);
        delegate.add(index, element);
    }

    @Override
    public E remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

}
