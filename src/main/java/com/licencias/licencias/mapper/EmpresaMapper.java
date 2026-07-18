package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.EmpresaRequest;
import com.licencias.licencias.dto.response.EmpresaResponse;
import com.licencias.licencias.entity.Empresa;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EmpresaMapper {

    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.nombre", target = "planNombre")
    EmpresaResponse toResponse(Empresa empresa);

    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Empresa toEntity(EmpresaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "plan", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(EmpresaRequest request, @MappingTarget Empresa empresa);
}
