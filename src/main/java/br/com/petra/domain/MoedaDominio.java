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
@Table(name = "tb_moeda")
public class MoedaDominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    @Convert(converter = StringTrimConverter.class)
    private String codigo;

    @Column(nullable = false, length = 80)
    @Convert(converter = StringTrimConverter.class)
    private String descricao;
}

