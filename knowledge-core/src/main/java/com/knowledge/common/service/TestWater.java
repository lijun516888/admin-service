package com.knowledge.common.service;

import com.knowledge.common.service.impl.FileWaterServiceImpl;

public class TestWater {

    public static void main(String[] args) {
        MarkService service = new FileWaterServiceImpl();
        FileWaterMark mark = new FileWaterMark();
        service.making(null, mark);
    }

}
