package br.com.lucas.baseapp;

import br.com.lucas.baseapp.model.*;
import br.com.lucas.baseapp.service.*;
import br.com.lucas.baseapp.solr.SolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@EnableAutoConfiguration
@ComponentScan(basePackages = "br.com.lucas")
@SpringBootApplication
public class BaseApplication implements CommandLineRunner {

	@Autowired
	private CousineService cousineService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;


    public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		Cousine cousine = new Cousine();
		cousine.setCousineName("Pizza");

        cousine = cousineService.put(cousine);

		Store store = new Store();
		store.setCousine(cousine);
		store.setStoreAddress("234 Wellington Street Ottawa");

        store = storeService.put(store);

		Product product = new Product();
		product.setStore(store);
		product.setProductName("Italian");
        product.setDescription("Tomatoes and cheese");
        product.setPrice(1.0);

        product = productService.put(product);

        Customer customer = new Customer();
        customer.setAddress("134 Plaza Drive");
        customer.setEmail("email@email.com");
        customer.setPassword("123");

        customer = customerService.put(customer);

        Order order = new Order();
        order.setContact("555555");
        order.setStatus("new");
        order.setCustomer(customer);
        order.setDeliveryAddress("12 New Road");
        order.setStore(store);
        order.setTotal(1.0);

        order = orderService.put(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setPrice(1.0);
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setTotal(1.0);

        orderItemService.put(orderItem);

	}
}
