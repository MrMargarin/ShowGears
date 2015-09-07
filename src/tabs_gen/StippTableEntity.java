package tabs_gen;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dmitrey on 07.09.2015.
 */
@Entity
@Table(name = "stipp_table", schema = "", catalog = "main_db")
public class StippTableEntity implements Serializable{
    private Integer id;                 //1
    private Integer stusId;             //2
    private String prodNameVen;         //3
    private Integer venName;            //4
    private Double priceVen;            //5
    private Integer quantityVen;        //6
    private StusTableEntity stusProd;   //7

    public StippTableEntity(){}

   // @Id //1
  //  @Column(name = "id", nullable = true, insertable = true, updatable = true)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @Basic  //2
    @Column(name = "stus_id")
    public Integer getStusId() { return stusId; }
    public void setStusId(Integer stusId) {
        this.stusId = stusId;
    }

    @Basic  //3
    @Column(name = "prodName_ven", nullable = true, insertable = true, updatable = true, length = 50)
    public String getProdNameVen() {
        return prodNameVen;
    }
    public void setProdNameVen(String prodNameVen) { this.prodNameVen = prodNameVen; }

    @Basic  //4
    @Column(name = "venName", nullable = true, insertable = true, updatable = true)
    public Integer getVenName() {
        return venName;
    }
    public void setVenName(Integer venName) {
        this.venName = venName;
    }

    @Basic  //5
    @Column(name = "price_ven", nullable = true, insertable = true, updatable = true, precision = 0)
    public Double getPriceVen() {
        return priceVen;
    }
    public void setPriceVen(Double priceVen) {
        this.priceVen = priceVen;
    }

    @Basic  //6
    @Column(name = "quantity_ven", nullable = true, insertable = true, updatable = true)
    public Integer getQuantityVen() {
        return quantityVen;
    }
    public void setQuantityVen(Integer quantityVen) {
        this.quantityVen = quantityVen;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)  //7
    @JoinColumn(name = "id")
    public StusTableEntity getStusProd() { return stusProd; }
    public void setStusProd(StusTableEntity stusProd) { this.stusProd = stusProd; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StippTableEntity that = (StippTableEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (stusId != null ? !stusId.equals(that.stusId) : that.stusId != null) return false;
        if (prodNameVen != null ? !prodNameVen.equals(that.prodNameVen) : that.prodNameVen != null) return false;
        if (venName != null ? !venName.equals(that.venName) : that.venName != null) return false;
        if (priceVen != null ? !priceVen.equals(that.priceVen) : that.priceVen != null) return false;
        if (quantityVen != null ? !quantityVen.equals(that.quantityVen) : that.quantityVen != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stusId != null ? stusId.hashCode() : 0);
        result = 31 * result + (prodNameVen != null ? prodNameVen.hashCode() : 0);
        result = 31 * result + (venName != null ? venName.hashCode() : 0);
        result = 31 * result + (priceVen != null ? priceVen.hashCode() : 0);
        result = 31 * result + (quantityVen != null ? quantityVen.hashCode() : 0);
        return result;
    }
}
