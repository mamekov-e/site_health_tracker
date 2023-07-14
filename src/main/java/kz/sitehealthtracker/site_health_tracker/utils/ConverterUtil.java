package kz.sitehealthtracker.site_health_tracker.utils;

import org.modelmapper.ModelMapper;

import java.util.List;

public class ConverterUtil {
    private static ModelMapper modelMapper;

    public static <T> T convertObject(Object fromClass, Class<T> toClass) {
        modelMapper = new ModelMapper();
        return modelMapper.map(fromClass, toClass);
    }

    public static <ListType, ConversionType> List<ConversionType> convertList(List<ListType> list, Class<ConversionType> toClass) {
        modelMapper = new ModelMapper();
        return list.stream().map(el -> modelMapper.map(el, toClass)).toList();
    }
}
