package com.uex.api.domain.contato;

import com.uex.api.domain.endereco.Endereco;
import com.uex.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "contatos")
@Entity(name = "Contato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nome;
    private String cpf;
    private String telefone;

    @Embedded
    private Endereco endereco;

}
