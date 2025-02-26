package dev.freaks.BADProject02.repository;

import dev.freaks.BADProject02.model.Title;
import dev.freaks.BADProject02.model.composite.TitleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, TitleId> {
    List<Title> findByEmpNoAndToDateAfter(Integer empNo, LocalDate toDate);
}
