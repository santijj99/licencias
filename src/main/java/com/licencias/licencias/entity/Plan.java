package com.licencias.licencias.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planes")
@SQLDelete(sql = "UPDATE planes SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Plan extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal precio;

    @Column(name = "cantidad_usuarios", nullable = false)
    private Integer cantidadUsuarios;

    @Column(name = "cantidad_sucursales", nullable = false)
    private Integer cantidadSucursales;

    @Column(name = "cantidad_dispositivos", nullable = false)
    private Integer cantidadDispositivos;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
