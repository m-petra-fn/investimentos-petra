package br.com.petra.domain;

import br.com.petra.domain.converter.StringTrimConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_tipo_indexacao")
public class TipoIndexacaoDominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @Convert(converter = StringTrimConverter.class)
    private String codigo;

    @Column(nullable = false, length = 120)
    @Convert(converter = StringTrimConverter.class)
    private String descricao;
}

