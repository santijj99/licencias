package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.LicenciaRequest;
import com.licencias.licencias.dto.response.LicenciaResponse;
import com.licencias.licencias.entity.Licencia;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface LicenciaMapper {

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nombre", target = "empresaNombre")
    LicenciaResponse toResponse(Licencia licencia);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Licencia toEntity(LicenciaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(LicenciaRequest request, @MappingTarget Licencia licencia);
}
