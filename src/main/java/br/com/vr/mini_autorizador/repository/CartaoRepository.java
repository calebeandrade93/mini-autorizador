package br.com.vr.mini_autorizador.repository;

import br.com.vr.mini_autorizador.entity.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String> {

    @Query(value = "SELECT * FROM cartoes WHERE numero_cartao = :numeroCartao FOR UPDATE", nativeQuery = true)
    Optional<Cartao> findByIdLock(@Param("numeroCartao") String numeroCartao);
}