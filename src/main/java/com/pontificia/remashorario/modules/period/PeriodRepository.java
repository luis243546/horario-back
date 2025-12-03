package com.pontificia.remashorario.modules.period;

import com.pontificia.remashorario.utils.abstractBase.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PeriodRepository extends BaseRepository<PeriodEntity> {
    boolean existsByName(String name);
    @Query("SELECT p FROM PeriodEntity p " +
            "WHERE :date BETWEEN p.startDate AND p.endDate")
    Optional<PeriodEntity> findByDate(@Param("date") LocalDate date);
}
