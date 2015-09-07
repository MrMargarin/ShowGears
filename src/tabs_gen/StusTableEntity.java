package tabs_gen;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitrey on 07.09.2015.
 */
@Entity
@Table(name = "stus_table", schema = "", catalog = "main_db")
public class StusTableEntity {
    private int id;
    private String prodName;
    private String catName;
    private List<StippTableEntity> stipps;

    public StusTableEntity(){}

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "prodName", nullable = true, insertable = true, updatable = true, length = 50)
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    @Basic
    @Column(name = "catName", nullable = true, insertable = true, updatable = true, length = 50)
    public String getCatName() {
        return catName;
    }
    public void setCatName(String catName) {
        this.catName = catName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stusProd")
    public List<StippTableEntity> getStipps() {return this.stipps;}
    public void setStipps(List<StippTableEntity> stipps) { this.stipps = stipps; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StusTableEntity that = (StusTableEntity) o;

        if (id != that.id) return false;
        if (prodName != null ? !prodName.equals(that.prodName) : that.prodName != null) return false;
        if (catName != null ? !catName.equals(that.catName) : that.catName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (prodName != null ? prodName.hashCode() : 0);
        result = 31 * result + (catName != null ? catName.hashCode() : 0);
        return result;
    }
}
