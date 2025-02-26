package dev.freaks.BADProject02.repository;

import dev.freaks.BADProject02.model.DeptEmp;
import dev.freaks.BADProject02.model.composite.DeptEmpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {
    @Modifying
    @Query("UPDATE DeptEmp d SET d.toDate = :endDate WHERE d.empNo = :empNo")
    void updateEndDate(@Param("empNo") Integer empNo, @Param("endDate") LocalDate endDate);

    Optional<DeptEmp> findByEmpNoAndToDateIsNull(Integer empNo);

    Optional<DeptEmp> findByEmpNo(Integer empNo);


}
