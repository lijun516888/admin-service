package com.knowledge.common.factory.watermark;

public class GenericPictureFactory<T extends WaterMark> implements PictureFactory<T> {

    @Override
    public T produce(Class<? extends T> clazz) {
        if(clazz == null) {
            return null;
        }
        try {
            return (T) Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
