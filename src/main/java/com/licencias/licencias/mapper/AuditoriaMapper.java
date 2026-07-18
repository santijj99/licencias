package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.response.AuditoriaResponse;
import com.licencias.licencias.entity.Auditoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface AuditoriaMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.email", target = "usuarioEmail")
    AuditoriaResponse toResponse(Auditoria auditoria);
}
