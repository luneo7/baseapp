package br.com.lucas.baseapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "product_id" )
    private Long productId;

    private String productName;

    private String description;

    @JsonIgnore
    @ManyToOne ( )
    @JoinColumn (name = "store_id")
    private Store store;

    private Double price;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Store getStore() { return store; }

    public void setStore(Store store) { this.store = store; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", productName=" + productName + ", createdAt=" + ", updatedAt=" + updatedAt + "]";
    }
}
