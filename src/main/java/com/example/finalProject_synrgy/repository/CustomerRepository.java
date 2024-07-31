package com.example.finalProject_synrgy.repository;

import com.example.finalProject_synrgy.entity.Customer;
import com.example.finalProject_synrgy.data.jpa.repository.JpaRepository;
import com.example.finalProject_synrgy.tereotype.Repository;

import java.util.optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepositoryCustomers, Long>{
Optional<Customers> findByUsername(String username);
}
