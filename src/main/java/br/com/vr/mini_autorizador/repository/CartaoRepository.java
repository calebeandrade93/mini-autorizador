package br.com.vr.mini_autorizador.repository;

import br.com.vr.mini_autorizador.entity.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, String> {

}
