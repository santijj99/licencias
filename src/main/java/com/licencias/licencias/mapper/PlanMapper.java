package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.PlanRequest;
import com.licencias.licencias.dto.response.PlanResponse;
import com.licencias.licencias.entity.Plan;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface PlanMapper {

    PlanResponse toResponse(Plan plan);

    Plan toEntity(PlanRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PlanRequest request, @MappingTarget Plan plan);
}
