package com.uex.api.domain.contato;

import com.uex.api.domain.contato.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    boolean existsByCpfAndUsuarioId(String cpf, Long id);

    @Query("""
            select c
            from Contato c
            inner join c.usuario u
            """)
    Page<Contato> findAllWithUsuario(Pageable pageable);
}
