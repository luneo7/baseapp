package br.com.lucas.baseapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "orderitems")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "orderitem_id" )
    private Long orderItemId;

    //@JsonBackReference
    @JsonIgnore
    @ManyToOne (  )
    @JoinColumn ( name = "order_id" )
    private Order order;

    @JsonBackReference
    @ManyToOne (  )
    @JoinColumn ( name = "product_id" )
    private Product product;

    private Integer quantity;

    private Double price;

    private Double total;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public Long getOrderItemId() { return orderItemId; }

    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotal() { return total; }

    public void setTotal(Double total) { this.total = total; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", price=" + price + ", total=" + total + ", createdAt=" + ", updatedAt=" + updatedAt + "]";
    }
}
