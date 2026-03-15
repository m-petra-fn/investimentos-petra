package br.com.petra.repository;

import br.com.petra.domain.Investimento;
import br.com.petra.domain.QInvestimento;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class InvestimentoRepositoryImpl implements InvestimentoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Investimento> findByIdQueryDsl(UUID id) {
        QInvestimento investimento = QInvestimento.investimento;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(investimento)
                        .where(investimento.id.eq(id))
                        .fetchOne()
        );
    }
}

