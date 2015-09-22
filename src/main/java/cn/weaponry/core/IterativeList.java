/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Wrapping of a list that supports adding while iterating. will cache
 * all the elements in a new list while startIterating(), and add them all
 * back in endIterating();
 * @author WeAthFolD
 */
public class IterativeList<T> implements List<T> {
	
	public static <U> IterativeList<U> of(List<U> original) {
		return new IterativeList(original);
	}
	
	private final List<T> original;
	private List<T> temp;
	private boolean iterating = false;
	
	private IterativeList(List<T> _original) {
		original = _original;
	}
	
	public void startIterating() {
		if(iterating)
			throw new RuntimeException("Already iterating!");
		iterating = true;
	}
	
	public void endIterating() {
		if(!iterating)
			throw new RuntimeException("Not iterating!");
		
		iterating = false;
		if(temp != null) {
			original.addAll(temp);
			temp = null;
		}
	}
	
	private void checkTemp() {
		if(temp != null) temp = new ArrayList();
	}

	@Override
	public boolean add(T e) {
		if(iterating) {
			checkTemp();
			return temp.add(e);
		} else {
			return original.add(e);
		}
	}

	@Override
	public void add(int index, T element) {
		original.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		if(iterating) {
			checkTemp();
			return temp.addAll(c);
		} else return original.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if(iterating)
			throw new UnsupportedOperationException();
		return original.addAll(index, c);
	}

	@Override
	public void clear() {
		if(iterating)
			throw new UnsupportedOperationException();
		original.clear();
	}

	@Override
	public boolean contains(Object o) {
		return original.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return original.containsAll(c);
	}

	@Override
	public T get(int index) {
		return original.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return original.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return original.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return original.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return original.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return original.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return original.remove(o);
	}

	@Override
	public T remove(int index) {
		return original.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return original.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return original.retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		return original.set(index, element);
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return original.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return original.toArray();
	}

	@Override
	public <X> X[] toArray(X[] a) {
		return original.toArray(a);
	}
	
}
