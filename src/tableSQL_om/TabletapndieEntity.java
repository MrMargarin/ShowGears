package tableSQL_om;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Rimskii on 05.09.2015.
 */
@Entity
@Table(name = "tabletapndie", schema = "", catalog = "metgears_db")
public class TabletapndieEntity {
    private int code;
    private String name;
    private String catName;
    private Collection<SubtabletapndieEntity> subtabletapndiesByCode;

    @Id
    @Column(name = "Code", nullable = false, insertable = true, updatable = true)
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Basic
    @Column(name = "Name", nullable = true, insertable = true, updatable = true, length = 245)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "CatName", nullable = true, insertable = true, updatable = true, length = 215)
    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabletapndieEntity that = (TabletapndieEntity) o;

        if (code != that.code) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (catName != null ? !catName.equals(that.catName) : that.catName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (catName != null ? catName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "tabletapndieByCode")
    public Collection<SubtabletapndieEntity> getSubtabletapndiesByCode() {
        return subtabletapndiesByCode;
    }

    public void setSubtabletapndiesByCode(Collection<SubtabletapndieEntity> subtabletapndiesByCode) {
        this.subtabletapndiesByCode = subtabletapndiesByCode;
    }
}
