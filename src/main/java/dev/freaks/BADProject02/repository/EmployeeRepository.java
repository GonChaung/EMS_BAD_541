package dev.freaks.BADProject02.repository;

import dev.freaks.BADProject02.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
        SELECT e.emp_no, e.first_name, e.last_name, d.dept_name, MAX(s.salary) AS max_salary
        FROM employees e
        JOIN dept_emp de ON e.emp_no = de.emp_no
        JOIN departments d ON de.dept_no = d.dept_no
        JOIN (SELECT emp_no, salary FROM salaries WHERE to_date = '9999-01-01') s
        ON e.emp_no = s.emp_no
        WHERE s.salary > (SELECT AVG(salary) FROM salaries)
        GROUP BY e.emp_no, e.first_name, e.last_name, d.dept_name
        ORDER BY max_salary DESC LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTop10HighestPaidEmployeesWithDepartment();

    @Query(value = "SELECT MAX(emp_no) FROM employees", nativeQuery = true)
    Long findMaxEmpNoWithLock();

}
