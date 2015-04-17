package org.renci.hearsay.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.jdbc.Index;
import org.renci.hearsay.dao.Persistable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "alleleName")
@XmlType(name = "AlleleName")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "allele_name")
public class AlleleName implements Persistable {

    private static final long serialVersionUID = 3507789252165014279L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allele_name_id_seq")
    @SequenceGenerator(name = "allele_name_id_seq", sequenceName = "allele_name_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Index
    @Column(name = "name")
    private String name;

    @Column(name = "legacy")
    private Boolean legacy;

    public AlleleName() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLegacy() {
        return legacy;
    }

    public void setLegacy(Boolean legacy) {
        this.legacy = legacy;
    }

    @Override
    public String toString() {
        return String.format("AlleleName [id=%s, type=%s, name=%s, legacy=%s]", id, type, name, legacy);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((legacy == null) ? 0 : legacy.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlleleName other = (AlleleName) obj;
        if (legacy == null) {
            if (other.legacy != null)
                return false;
        } else if (!legacy.equals(other.legacy))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
