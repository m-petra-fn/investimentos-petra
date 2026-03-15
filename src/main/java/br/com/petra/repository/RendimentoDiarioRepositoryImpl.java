package br.com.petra.repository;

import br.com.petra.domain.QRendimentoDiario;
import br.com.petra.domain.RendimentoDiario;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RendimentoDiarioRepositoryImpl implements RendimentoDiarioRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RendimentoDiario> findByInvestimentoIdQueryDsl(UUID investimentoId, Pageable pageable) {
        QRendimentoDiario rendimento = QRendimentoDiario.rendimentoDiario;
        BooleanExpression predicate = rendimento.investimento.id.eq(investimentoId);

        return findPage(predicate, pageable);
    }

    @Override
    public Page<RendimentoDiario> findByInvestimentoIdAndPeriodoQueryDsl(
            UUID investimentoId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        QRendimentoDiario rendimento = QRendimentoDiario.rendimentoDiario;

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.of(23, 59, 59));

        BooleanExpression predicate = rendimento.investimento.id.eq(investimentoId)
                .and(rendimento.dataReferencia.between(fromDateTime.toLocalDate(), toDateTime.toLocalDate()));

        return findPage(predicate, pageable);
    }

    private Page<RendimentoDiario> findPage(BooleanExpression predicate, Pageable pageable) {
        QRendimentoDiario rendimento = QRendimentoDiario.rendimentoDiario;

        List<RendimentoDiario> content = queryFactory
                .selectFrom(rendimento)
                .where(predicate)
                .orderBy(rendimento.dataReferencia.desc(), rendimento.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(rendimento.count())
                .from(rendimento)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}

