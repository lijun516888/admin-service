package com.knowledge.example;

import com.knowledge.example.entity.Category;
import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Java8GroupCount {

    @Test
    public void groupBy() {
        Map<String, List<Category>> collect = Category.getCategory1().stream().collect(Collectors.groupingBy
                (Category::getCategory));
        System.out.println(collect);
    }

    @Test
    public void groupByCounting() {
        Map<String, Long> collect = Category.getCategory1().stream().collect(Collectors.groupingBy
                (Category::getCategory, Collectors.counting()));
        System.out.println(collect);
    }

    @Test
    public void groupBySummarizing() {
        Map<String, IntSummaryStatistics> collect = Category.getCategory1().stream().collect(Collectors.groupingBy
                (Category::getCategory, Collectors.summarizingInt(Category::getNumber)));
        System.out.println(collect);
    }

}
