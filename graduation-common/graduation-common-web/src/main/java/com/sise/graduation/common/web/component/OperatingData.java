package com.sise.graduation.common.web.component;

import java.util.concurrent.atomic.AtomicInteger;

public class OperatingData {
    private static AtomicInteger workNum = new AtomicInteger(0);

    public static void addWorkNum() {
        workNum.incrementAndGet();
    }

    public static void decWorkNum() {
        workNum.decrementAndGet();
    }

    public static int getWorkNum(){
        return workNum.intValue();
    }
}
