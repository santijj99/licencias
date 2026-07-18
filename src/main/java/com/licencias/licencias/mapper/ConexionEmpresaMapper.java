package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.ConexionEmpresaRequest;
import com.licencias.licencias.dto.response.ConexionEmpresaResponse;
import com.licencias.licencias.entity.ConexionEmpresa;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ConexionEmpresaMapper {

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nombre", target = "empresaNombre")
    ConexionEmpresaResponse toResponse(ConexionEmpresa conexion);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "passwordEncriptada", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ConexionEmpresa toEntity(ConexionEmpresaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "passwordEncriptada", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(ConexionEmpresaRequest request, @MappingTarget ConexionEmpresa conexion);
}
