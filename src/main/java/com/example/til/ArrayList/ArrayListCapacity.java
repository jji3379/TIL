package com.example.til.ArrayList;

import java.lang.reflect.Field;

public class ArrayListCapacity {
    // capacity 구하는 메서드
    static int getCapacity(java.util.ArrayList<Integer> list) throws Exception {
        Field field = list.getClass().getDeclaredField("elementData");
        field.setAccessible(true);

        return ((Object[]) field.get(list)).length;
    }

    // capacity와 size를 출력하는 메서드
    static void print(java.util.ArrayList<Integer> list) throws Exception {
        System.out.printf("size: %d, capacity: %d\n", list.size(), getCapacity(list));
    }

    public static void main(String[] args) throws Exception {
        java.util.ArrayList<Integer> list = new java.util.ArrayList<>();

        print(list);

        for (int i = 0; i < 30; i++) {
            list.add(i);
            print(list);
        }
    }
}
