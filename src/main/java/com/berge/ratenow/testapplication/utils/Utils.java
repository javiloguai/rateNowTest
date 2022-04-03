package com.berge.ratenow.testapplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class Utils {


    public static <S, T> List<T> transformList(List<S> list, Function<S, T> func) {
        if (list == null) {
            return new ArrayList<>();
        }

        return list.stream().map(func).collect(Collectors.toList());
    }

    public static <S, T> Page<T> transformPage(Page<S> items, Function<S, T> func) {
        List<T> list = items.getContent().stream().map(func).collect(Collectors.toList());
        return (Page<T>) new PageImpl(list, PageRequest.of(items.getNumber(), items.getSize()), items.getTotalElements());
    }

 

}

