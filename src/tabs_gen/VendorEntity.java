package tabs_gen;

import javax.persistence.*;

/**
 * Created by Dmitrey on 07.09.2015.
 */
@Entity
@Table(name = "vendor", schema = "", catalog = "main_db")
public class VendorEntity {
    private int id;
    private String saleManagerName;
    private String purchManagerName;

    public VendorEntity(){}

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "saleManagerName", nullable = true, insertable = true, updatable = true, length = 50)
    public String getSaleManagerName() {
        return saleManagerName;
    }

    public void setSaleManagerName(String saleManagerName) {
        this.saleManagerName = saleManagerName;
    }

    @Basic
    @Column(name = "purchManagerName", nullable = true, insertable = true, updatable = true, length = 50)
    public String getPurchManagerName() {
        return purchManagerName;
    }

    public void setPurchManagerName(String purchManagerName) {
        this.purchManagerName = purchManagerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VendorEntity that = (VendorEntity) o;

        if (id != that.id) return false;
        if (saleManagerName != null ? !saleManagerName.equals(that.saleManagerName) : that.saleManagerName != null)
            return false;
        if (purchManagerName != null ? !purchManagerName.equals(that.purchManagerName) : that.purchManagerName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (saleManagerName != null ? saleManagerName.hashCode() : 0);
        result = 31 * result + (purchManagerName != null ? purchManagerName.hashCode() : 0);
        return result;
    }
}
