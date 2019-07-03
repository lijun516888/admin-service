package com.knowledge.example.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Category {

    private String category;
    private String color;
    private String scale;
    private int number;

    Category(String category, String color, String scale, int number) {
        this.category = category;
        this.color = color;
        this.scale = scale;
        this.number = number;
    }

    public static List<Category> getCategory1() {
        List<Category> list = Lists.newArrayList();
        list.add(new Category("冬装", "黄色", "XX", 10));
        list.add(new Category("秋装", "红色", "XXX", 2));
        list.add(new Category("春装", "黄色", "XL", 5));
        list.add(new Category("冬装", "青色", "XXXX", 8));
        return list;
    }

}
