package com.paydaytrade.mapper;

import com.paydaytrade.data.dto.request.RegisterRequestDto;
import com.paydaytrade.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoToEntity {
    DtoToEntity INSTANCE = Mappers.getMapper(DtoToEntity.class);
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterRequestDto dto);
}
