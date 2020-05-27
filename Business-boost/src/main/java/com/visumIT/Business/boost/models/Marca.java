/*Autor: kaique
* */

package com.visumIT.Business.boost.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="tbl_marca")
@Data
public class Marca {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min=2, max=30)
    @Column(name="nome", columnDefinition = "VARCHAR(30)")
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Empresa empresa;

    @Column(columnDefinition = "VARCHAR(50)")
    private String logo;

    /*aqui ficara a propriedade produtos quando houver*/
}
