package dev.freaks.BADProject02.repository;

import dev.freaks.BADProject02.model.Salary;
import dev.freaks.BADProject02.model.composite.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
    @Modifying
    @Query("UPDATE Salary s SET s.toDate = :endDate WHERE s.empNo = :empNo")
    void updateEndDate(@Param("empNo") Integer empNo, @Param("endDate") LocalDate endDate);


    Optional<Salary> findByEmpNoAndToDateIsNull(Integer empNo);

}
