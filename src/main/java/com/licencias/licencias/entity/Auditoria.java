package com.licencias.licencias.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auditorias")
@EntityListeners(AuditingEntityListener.class)
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioGlobal usuario;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant fecha;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(length = 50)
    private String ip;

    @Column(length = 1000)
    private String detalle;

    @Column(name = "recurso", length = 100)
    private String recurso;

    @Column(name = "recurso_id")
    private Long recursoId;
}
