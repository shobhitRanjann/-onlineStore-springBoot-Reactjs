package com.example.testclient.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.testclient.customUser.CustomUserDetailsService;
import com.example.testclient.entity.Department;
import com.example.testclient.entity.User;
import com.example.testclient.razorpay.AllOrders;
import com.example.testclient.razorpay.OrderRequest;
import com.example.testclient.razorpay.OrderResponse;
import com.example.testclient.razorpay.PaymentResponse;
import com.example.testclient.razorpay.RazorPayClientConfig;
import com.example.testclient.razorpayService.OrderService;
import com.example.testclient.service.DepartmentService;
import com.example.testclient.service.DepartmentSpace;
import com.example.testclient.service.UserService;
import com.example.testclient.util.JwtUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {
	@Autowired
	DepartmentService departmentservice;

	@Autowired
	UserService userservic;

	@Autowired
	AuthenticationManager authenticationmanager;

	@Autowired
	private CustomUserDetailsService userdetailsservice;

	@Autowired
	private DepartmentSpace deptspace;

	@Autowired
	private JwtUtil jwtTokenUtil;

	private RazorpayClient client;

	@Autowired
	private RazorPayClientConfig razorPayClientConfig;

	@Autowired
	private OrderService orderService;

	@Autowired
	public OrderController(RazorPayClientConfig razorpayClientConfig) throws RazorpayException {
		this.razorPayClientConfig = razorpayClientConfig;
		this.client = new RazorpayClient(razorpayClientConfig.getKey(), razorpayClientConfig.getSecret());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/order")
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
		OrderResponse razorPay = null;
		try {

			User uu = userservic.findByUserMail(orderRequest.getEmail());

			String amountInPaise = convertRupeeToPaise(orderRequest.getAmount());
			System.out.println("amount   >>>> " + amountInPaise);
			// Create an order in RazorPay and get the order id
			Order order = createRazorPayOrder(amountInPaise);
			System.out.println("order >>" + order);
			System.out.println((String) order.get("id") + "  <<<<< >>>>>   " + amountInPaise);
			razorPay = getOrderResponse((String) order.get("id"), amountInPaise);
			// Save order in the database
			orderService.saveOrder(razorPay.getRazorpayOrderId(), uu.getId());
		} catch (RazorpayException e) {
			// return new ResponseEntity<>(new ApiResponse(false, "Error while create
			// payment order: " + e.getMessage()), HttpStatus.EXPECTATION_FAILED);
		}
		return ResponseEntity.ok(razorPay);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping("/order")
	public ResponseEntity<?> updateOrder(@RequestBody PaymentResponse paymentResponse) {
		System.out.println("updating order" + paymentResponse + "  secrett  >" + razorPayClientConfig.getSecret());
		String errorMsg = orderService.validateAndUpdateOrder(paymentResponse.getRazorpayOrderId(),
				paymentResponse.getRazorpayPaymentId(), paymentResponse.getRazorpaySignature(),
				razorPayClientConfig.getSecret(), paymentResponse.getProductId());
		if (errorMsg != null) {
			// return new ResponseEntity<>(new ApiResponse(false, errorMsg),
			// HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(paymentResponse.getRazorpayPaymentId());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/myorder", method = RequestMethod.POST ,produces = "application/json")
	@ResponseBody
    public List<Department> myallOrder(@RequestBody AllOrders allorders) throws RazorpayException {
		RazorpayClient razorpay = new RazorpayClient("rzp_test_Rdab4sKKJnl5EP", "kZ2qg16zTQr5dou5mpdAIdWn");
		ArrayList<Order> order = new ArrayList<Order>();
		List<Department> orderdepartment = new ArrayList<Department>();
		
		try {
			  User uu = userservic.findByUserMail(allorders.getEmail());
			  List<com.example.testclient.entity.Order> ordders= orderService.findByUserId(Long.valueOf(uu.getId()));
			  for(int i=0;i<ordders.size();i++) {
				//  System.out.println();
				  if(razorpay.Orders.fetch(ordders.get(i).getRazorpayOrderId()) != null) {
					  Order oo = razorpay.Orders.fetch(ordders.get(i).getRazorpayOrderId());
					  if(oo.get("amount").equals(oo.get("amount_paid"))) {
						  com.example.testclient.entity.Order oder = orderService.findByOrderId(oo.get("id"));
						  Optional<Department> convertdept = departmentservice.findDepartmentById(Long.valueOf(oder.getProductId()));
						  if(convertdept.isPresent()) {
							  Department dd = convertdept.get();
							  orderdepartment.add(dd);
							  System.out.println("dd is here   >> "+dd);
						  }
						  else {
							  return null;
						  }
					  }
				  }
			  }
			} catch (RazorpayException e) {
	         	System.out.println(e.getMessage());
			  return orderdepartment;
			}
      //  return ResponseEntity.ok(order.toString());
		return orderdepartment;
    }

	private OrderResponse getOrderResponse(String orderId, String amountInPaise) {
		OrderResponse razorPay = new OrderResponse();
		razorPay.setApplicationFee(amountInPaise);
		razorPay.setRazorpayOrderId(orderId);
		razorPay.setSecretKey(razorPayClientConfig.getKey());
		return razorPay;
	}

	private String convertRupeeToPaise(String paise) {
		BigDecimal b = new BigDecimal(paise);
		BigDecimal value = b.multiply(new BigDecimal("100"));
		return value.setScale(0, RoundingMode.UP).toString();
	}

	private Order createRazorPayOrder(String amount) throws RazorpayException {
		RazorpayClient razorpay = new RazorpayClient("rzp_test_Rdab4sKKJnl5EP", "kZ2qg16zTQr5dou5mpdAIdWn");
		JSONObject options = new JSONObject();
		options.put("amount", amount);
		options.put("currency", "INR");
		options.put("receipt", "txn_123456");
		return razorpay.Orders.create(options);
	}
}
