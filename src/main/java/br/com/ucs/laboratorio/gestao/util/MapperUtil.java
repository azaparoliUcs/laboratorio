package br.com.ucs.laboratorio.gestao.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T mapObject(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
