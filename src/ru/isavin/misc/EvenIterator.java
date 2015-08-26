package ru.isavin.misc;

import java.util.*;

/**
 * @author ilasavin
 * @since 26.08.15
 */
public class EvenIterator implements Iterator<Integer> {
    private List<Integer> collection;
    private int currentIndex = -1;
    private int last = -1;

    public EvenIterator(Collection<Integer> collection) {
        this.collection = new ArrayList<>(collection);
        for (int i = 0; i < collection.size(); i++) {
            if (this.collection.get(i) % 2 == 0) {
                currentIndex = i;
                last = currentIndex;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return (currentIndex > -1);
    }

    @Override
    public Integer next() {
        int tmp = currentIndex;
        currentIndex = -1;
        for (int i = tmp + 1; i < collection.size(); i++) {
            if (this.collection.get(i) % 2 == 0) {
                currentIndex = i;
                break;
            }
        }
        last = tmp;
        return collection.get(tmp);
    }

    @Override
    public void remove() {
        if (last < 0) {
            throw new IllegalStateException();
        }
        collection.remove(last);
        for (int i = last; i < collection.size(); i++) {
            if (this.collection.get(i) % 2 == 0) {
                currentIndex = i;
                break;
            }
        }
        last = -1;
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        List<Integer> list = Arrays.asList(2);
        EvenIterator evenIterator = new EvenIterator(list);
        while (evenIterator.hasNext()) {
            System.out.println(evenIterator.next());
            evenIterator.remove();
        }
        System.out.println(list);
    }
}
