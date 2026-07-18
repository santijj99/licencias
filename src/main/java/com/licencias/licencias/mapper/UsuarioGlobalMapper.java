package com.licencias.licencias.mapper;

import com.licencias.licencias.dto.request.UsuarioGlobalRequest;
import com.licencias.licencias.dto.response.UsuarioGlobalResponse;
import com.licencias.licencias.entity.UsuarioGlobal;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface UsuarioGlobalMapper {

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nombre", target = "empresaNombre")
    UsuarioGlobalResponse toResponse(UsuarioGlobal usuario);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    UsuarioGlobal toEntity(UsuarioGlobalRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(UsuarioGlobalRequest request, @MappingTarget UsuarioGlobal usuario);
}
