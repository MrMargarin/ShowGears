package tabs_gen;

import javax.persistence.*;

/**
 * Created by Dmitrey on 07.09.2015.
 */
@Entity
@Table(name = "orderlist_table", schema = "", catalog = "main_db")
public class OrderlistTableEntity {
    private int id;
    private Integer orderId;
    private Integer stusId;
    private Integer quantityReq;
    private OrderTableEntity order;

    public OrderlistTableEntity(){};

    //@Id
    //@Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order_id", nullable = true, insertable = true, updatable = true)
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "stus_id", nullable = true, insertable = true, updatable = true)
    public Integer getStusId() {
        return stusId;
    }

    public void setStusId(Integer stusId) {
        this.stusId = stusId;
    }

    @Basic
    @Column(name = "quantity_req", nullable = true, insertable = true, updatable = true)
    public Integer getQuantityReq() {
        return quantityReq;
    }

    public void setQuantityReq(Integer quantityReq) {
        this.quantityReq = quantityReq;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)  //7
    @JoinColumn(name = "id")
    public OrderTableEntity getOrder() { return order; }
    public void setOrder(OrderTableEntity order) { this.order = order; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderlistTableEntity that = (OrderlistTableEntity) o;

        if (id != that.id) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (stusId != null ? !stusId.equals(that.stusId) : that.stusId != null) return false;
        if (quantityReq != null ? !quantityReq.equals(that.quantityReq) : that.quantityReq != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (stusId != null ? stusId.hashCode() : 0);
        result = 31 * result + (quantityReq != null ? quantityReq.hashCode() : 0);
        return result;
    }

}
