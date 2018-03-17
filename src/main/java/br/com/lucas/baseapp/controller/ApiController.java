package br.com.lucas.baseapp.controller;

import br.com.lucas.baseapp.model.*;
import br.com.lucas.baseapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/v1")
public class ApiController {

    @Autowired
    CousineService cousineService;

    @Autowired
    StoreService storeService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @RequestMapping(value = "/Cousine", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Cousine> getAllCosines() {
        return cousineService.getAll();
    }


    @RequestMapping(value = "/Cousine/{cousineId}/stores", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Store> getCosineStores(@PathVariable(value = "cousineId") Long cousineId) {
        Cousine cousine = new Cousine();
        cousine.setCousineId(cousineId);
        return storeService.findByCousine(cousine);
    }

    @RequestMapping(value = "/Cousine/search/{searchText}", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Cousine> searchCousines(@PathVariable(value = "searchText") String searchText) {
        return cousineService.findByName(searchText);
    }

    @RequestMapping(value = "/Store", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Store> getAllStores() {
        return storeService.getAll();
    }


    @RequestMapping(value = "/Store/search/{searchText}", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Store> searchStoreByAddress(@PathVariable(value = "searchText") String searchText) {
        return storeService.findByAddress(searchText);
    }

    @RequestMapping(value = "/Store/{storeId}", method = RequestMethod.GET)
    @ResponseBody
    public Store getStoreById(@PathVariable(value = "storeId") Long id) {
        return storeService.getById(id);

    }

    @RequestMapping(value = "/Store/{storeId}/products", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getStoreProducts(@PathVariable(value = "storeId") Long id) {
        Store store = new Store();
        store.setStoreId(id);
        return productService.findByStore(store);
    }

    @RequestMapping(value = "/Product", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Product> getAllProducts() {
        return productService.getAll();
    }

    @RequestMapping(value = "/Product/search/{searchText}", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Product> searchProductByName(@PathVariable(value = "searchText") String searchText) {
        return productService.findByName(searchText);
    }

    @RequestMapping(value = "/Product/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProductById(@PathVariable(value = "productId") Long id) {
        return productService.getById(id);

    }

    @RequestMapping(value = "/Order/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Order getOrderById(@PathVariable(value = "orderId") Long id) {
        return orderService.getById(id);

    }

    @RequestMapping(value = "/Order", method = RequestMethod.POST)
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        orderService.put(order);
        order.getOrderItems().forEach( orderItem -> { orderItem.setOrder(order); orderItemService.put(orderItem); });
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/Order/{orderId}/status/{status}", method = RequestMethod.GET)
    @ResponseBody
    public String setOrderStatus(@PathVariable(value = "orderId") Long id, @PathVariable(value = "status") String status) {
        Order order = orderService.getById(id);
        order.setStatus(status);
        orderService.put(order);
        return "";
    }

    @RequestMapping(value = "/Order/{orderId}/items", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderItem> getOrderItems(@PathVariable(value = "orderId") Long id) {
        Order order = new Order();
        order.setOrderId(id);
        return orderItemService.findByOrder(order);
    }

    @RequestMapping(value = "/Order/customer/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getOrderByCustomerId(@PathVariable(value = "customerId") Long id) {
        Customer customer = new Customer();
        customer.setCustomerId(id);
        return orderService.findByCustomer(customer);

    }

}
