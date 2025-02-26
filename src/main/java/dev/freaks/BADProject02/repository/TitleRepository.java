package dev.freaks.BADProject02.repository;

import dev.freaks.BADProject02.model.Title;
import dev.freaks.BADProject02.model.composite.TitleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<Title, TitleId> {
    @Modifying
    @Query("UPDATE Title t SET t.toDate = :endDate WHERE t.empNo = :empNo AND t.toDate IS NULL")
    void updateEndDate(@Param("empNo") Integer empNo, @Param("endDate") LocalDate endDate);

    Optional<Title> findByEmpNoAndToDateIsNull(Integer empNo);

}
