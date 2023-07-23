package kz.sitehealthtrackerbackend.site_health_tracker_backend.utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class ConverterUtil {
    private static ModelMapper modelMapper;

    public static <T, S> T convertObject(Object fromClass, Class<T> toClass) {
        modelMapper = new ModelMapper();
        return modelMapper.map(fromClass, toClass);
    }

    public static <ListType, ConversionType> List<ConversionType> convertList(List<ListType> list, Class<ConversionType> toClass) {
        modelMapper = new ModelMapper();
        return list.stream().map(el -> modelMapper.map(el, toClass)).toList();
    }

    public static <PageType, ConversionType> Page<ConversionType> convertPage(Page<PageType> page, Class<ConversionType> toClass) {
        modelMapper = new ModelMapper();
        List<ConversionType> convertedContentList = page.getContent().stream()
                .map(el -> modelMapper.map(el, toClass))
                .toList();
        return new PageImpl<>(convertedContentList, page.getPageable(), page.getTotalElements());
    }
}
