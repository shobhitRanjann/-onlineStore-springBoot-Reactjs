package com.example.testclient.razorpayService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.testclient.entity.Order;
import com.example.testclient.razorpay.Signature;
import com.example.testclient.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;
 
/**
 * 
 * @author Chinna
 *
 */
@Slf4j
@Service
public class OrderService {
 
    @Autowired
    private OrderRepository orderRepository;
 
    @Transactional
    public Order saveOrder(final String razorpayOrderId, final Long userId) {
        Order order = new Order();
        System.out.println("from service ");
        order.setRazorpayOrderId(razorpayOrderId);
        order.setUserId(userId);
        return orderRepository.save(order);
    }
 
    @Transactional
    public String validateAndUpdateOrder(final String razorpayOrderId, final String razorpayPaymentId, final String razorpaySignature, final String secret, final String productId) {
        String errorMsg = null;
        try {
            Order order = orderRepository.findByRazorpayOrderId(razorpayOrderId);
            // Verify if the razorpay signature matches the generated one to
            // confirm the authenticity of the details returned
            String generatedSignature = Signature.calculateRFC2104HMAC(order.getRazorpayOrderId() + "|" + razorpayPaymentId, secret);
            System.out.println("generatedSignature       >>>>>>>>>> "+generatedSignature);
            if (!generatedSignature.equals(razorpaySignature)) {
                order.setRazorpayOrderId(razorpayOrderId);
                order.setRazorpayPaymentId(razorpayPaymentId);
                order.setRazorpaySignature(razorpaySignature);
                order.setProductId(productId);
                orderRepository.save(order);
            } else {
                errorMsg = "Payment validation failed: Signature doesn't match";
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        return errorMsg;
    }

	public List<Order> findByUserId(Long id) {
		// TODO Auto-generated method stub
		List<Order> orde= orderRepository.findByUserId(id);
		return orde;
	}

	public Order findByOrderId(String id) {
		// TODO Auto-generated method stub
		return orderRepository.findByRazorpayOrderId(id);
	}
}