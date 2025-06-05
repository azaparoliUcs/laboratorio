package br.com.ucs.laboratorio.gestao.domain.dto;

import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;

    private String password;

    private UserType userType;

    private String name;

    private Long laboratoryId;
}
