package com.licencias.licencias.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conexiones_empresa")
@SQLDelete(sql = "UPDATE conexiones_empresa SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted = false")
public class ConexionEmpresa extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false, unique = true)
    private Empresa empresa;

    @Column(nullable = false, length = 255)
    private String host;

    @Column(nullable = false)
    private Integer puerto;

    @Column(name = "database_name", nullable = false, length = 100)
    private String databaseName;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "password_encriptada", nullable = false, length = 512)
    private String passwordEncriptada;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ssl = true;
}
