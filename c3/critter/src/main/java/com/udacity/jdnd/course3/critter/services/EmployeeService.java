package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.getOne(id);
    }

    public List<Employee> getEmployeesForService(Set<EmployeeSkill> requiredSkill, LocalDate requiredDate) {
        return employeeRepository
                .findAll()
                .stream()
                .filter(employee -> employee.getSkills().containsAll(requiredSkill))
                .filter(employee -> employee.getDaysAvailable().contains(requiredDate.getDayOfWeek()))
                .collect(Collectors.toList());
    }
}
