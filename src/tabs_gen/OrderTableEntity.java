package tabs_gen;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Dmitrey on 07.09.2015.
 */
@Entity
@Table(name = "order_table", schema = "", catalog = "main_db")
public class OrderTableEntity {
    private int id;
    private String managerName;
    private String comment;
    private List<OrderlistTableEntity> ordProds;

    public OrderTableEntity(){}

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "managerName", nullable = true, insertable = true, updatable = true, length = 50)
    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    @Basic
    @Column(name = "comment", nullable = true, insertable = true, updatable = true, length = 50)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    public List<OrderlistTableEntity> getOrdProds() {return this.ordProds;}
    public void setOrdProds(List<OrderlistTableEntity> ordProds) { this.ordProds = ordProds; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTableEntity that = (OrderTableEntity) o;

        if (id != that.id) return false;
        if (managerName != null ? !managerName.equals(that.managerName) : that.managerName != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (managerName != null ? managerName.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
