package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.DispositivoRequest;
import com.licencias.licencias.dto.response.DispositivoResponse;
import com.licencias.licencias.entity.Dispositivo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface DispositivoMapper {

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nombre", target = "empresaNombre")
    DispositivoResponse toResponse(Dispositivo dispositivo);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "ultimoAcceso", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Dispositivo toEntity(DispositivoRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "ultimoAcceso", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(DispositivoRequest request, @MappingTarget Dispositivo dispositivo);
}
