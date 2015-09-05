package tableSQL_om;

import javax.persistence.*;

/**
 * Created by Rimskii on 05.09.2015.
 */
@Entity
@Table(name = "subtabledrill", schema = "", catalog = "metgears_db")
public class SubtabledrillEntity {
    private int id;
    private Integer code;
    private String name;
    private String ������������������;
    private String ������������;
    private String �������������������;
    private TabledrillEntity tabledrillByCode;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Code", nullable = true, insertable = true, updatable = true)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Basic
    @Column(name = "Name", nullable = true, insertable = true, updatable = true, length = 276)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "�������� ����������", nullable = true, insertable = true, updatable = true, length = 34)
    public String get������������������() {
        return ������������������;
    }

    public void set������������������(String ������������������) {
        this.������������������ = ������������������;
    }

    @Basic
    @Column(name = "���� (�� ������)", nullable = true, insertable = true, updatable = true, length = 7)
    public String get������������() {
        return ������������;
    }

    public void set������������(String ������������) {
        this.������������ = ������������;
    }

    @Basic
    @Column(name = "������� (�� �����-�����)", nullable = true, insertable = true, updatable = true, length = 68)
    public String get�������������������() {
        return �������������������;
    }

    public void set�������������������(String �������������������) {
        this.������������������� = �������������������;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubtabledrillEntity that = (SubtabledrillEntity) o;

        if (id != that.id) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (������������������ != null ? !������������������.equals(that.������������������) : that.������������������ != null)
            return false;
        if (������������ != null ? !������������.equals(that.������������) : that.������������ != null) return false;
        if (������������������� != null ? !�������������������.equals(that.�������������������) : that.������������������� != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (������������������ != null ? ������������������.hashCode() : 0);
        result = 31 * result + (������������ != null ? ������������.hashCode() : 0);
        result = 31 * result + (������������������� != null ? �������������������.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "Code", referencedColumnName = "Code")
    public TabledrillEntity getTabledrillByCode() {
        return tabledrillByCode;
    }

    public void setTabledrillByCode(TabledrillEntity tabledrillByCode) {
        this.tabledrillByCode = tabledrillByCode;
    }
}
