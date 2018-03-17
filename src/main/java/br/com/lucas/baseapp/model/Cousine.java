package br.com.lucas.baseapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "cousines")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "stores"}, allowGetters = true)
public class Cousine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "cousine_id" )
    private Long cousineId;

    private String cousineName;

    @JsonIgnore
    //@JsonManagedReference
    @OneToMany ( cascade = CascadeType.ALL, mappedBy = "cousine" )
    private List<Store> stores;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public Long getCousineId() {
        return cousineId;
    }

    public void setCousineId(Long productId) { this.cousineId = productId; }

    public String getCousineName() {
        return cousineName;
    }

    public void setCousineName(String cousineName) { this.cousineName = cousineName; }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) { this.stores = stores; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Cousine [cousineId=" + cousineId + ", cousineName=" + cousineName + ", createdAt=" + ", updatedAt=" + updatedAt + "]";
    }
}
