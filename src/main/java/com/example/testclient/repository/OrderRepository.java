package com.example.testclient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testclient.entity.Order;
 
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
 

	List<Order> findByUserId(Long id);

	Order findByRazorpayOrderId(String id);
}