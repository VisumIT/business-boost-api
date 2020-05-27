package com.visumIT.Business.boost.repository;

import com.visumIT.Business.boost.models.Empresa;
import com.visumIT.Business.boost.models.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarcaRepository  extends JpaRepository<Marca, Long> {
    List<Marca> findByEmpresa(Empresa empresa);
}
