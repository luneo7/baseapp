package br.com.lucas.baseapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "stores")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "cousine"}, allowGetters = true)
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "store_id" )
    private Long storeId;

    private String storeName;

    private String storeAddress;

    @ManyToOne( )
    @JoinColumn(name = "cousine_id", nullable = false)
    private Cousine cousine;

    @JsonIgnore
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "store")
    private List<Product> products;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) { this.storeAddress = storeAddress; }

    public Cousine getCousine() {
        return cousine;
    }

    public void setCousine(Cousine cousine) { this.cousine = cousine; }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) { this.products = products; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Store [storeId=" + storeId + ", storeName=" + storeName + ", storeAddress=" + storeAddress + ", createdAt=" + ", updatedAt=" + updatedAt + "]";
    }
}
