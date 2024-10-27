package com.zyh.market.constants;

import java.util.concurrent.atomic.AtomicInteger;

public class ClickCount {
    private static AtomicInteger clickCount = new AtomicInteger(0);

    public static int getClickCount() {
        return clickCount.get();
    }

    public static void incrementClickCount() {
        clickCount.incrementAndGet();
    }
}
