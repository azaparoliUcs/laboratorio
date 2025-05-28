package br.com.ucs.laboratorio.gestao.domain.entity;

import br.com.ucs.laboratorio.gestao.domain.converter.UserTypeConverter;
import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TB_USUARIO")
public class UserModel implements Serializable {

    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TP_USUARIO")
    @Convert(converter = UserTypeConverter.class)
    private UserType userType;

    @Column(name = "DS_NOME")
    private String name;

    @Column(name = "DS_EMAIL")
    private String email;

    @Column(name = "DS_SENHA")
    private String password;

    @ManyToOne
    @JoinColumn(name = "ID_LABORATORIO")
    private LaboratoryModel laboratory;
}
