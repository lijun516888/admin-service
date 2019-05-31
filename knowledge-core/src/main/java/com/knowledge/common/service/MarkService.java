package com.knowledge.common.service;

public interface MarkService<T extends WaterMark> {

    String making(byte[] source, T waterMark);

}
