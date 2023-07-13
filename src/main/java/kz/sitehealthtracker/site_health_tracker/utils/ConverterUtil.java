package kz.sitehealthtracker.site_health_tracker.utils;

import org.modelmapper.ModelMapper;

public class ConverterUtil {
    private static ModelMapper modelMapper;

    public static <T> T convert(Object fromClass, Class<T> toClass) {
        modelMapper = new ModelMapper();
        return modelMapper.map(fromClass, toClass);
    }
}
