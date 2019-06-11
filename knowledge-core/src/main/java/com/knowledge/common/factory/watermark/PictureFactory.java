package com.knowledge.common.factory.watermark;

public interface PictureFactory<T extends WaterMark> {

    T produce(Class<? extends T> clazz);

}
