package org.renci.hearsay.dao.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "referenceSequence")
@XmlType(name = "ReferenceSequence")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_sequence")
@NamedQueries({ @NamedQuery(name = "ReferenceSequence.findAll", query = "SELECT a FROM ReferenceSequence a") })
public class ReferenceSequence extends BaseEntity {

    private static final long serialVersionUID = 1327461104847251913L;

    @Column(name = "accession")
    private String accession;

    @ManyToMany(mappedBy = "referenceSequences", cascade = CascadeType.PERSIST)
    private Set<ReferenceGenome> referenceGenomes;

    public ReferenceSequence() {
        super();
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Set<ReferenceGenome> getReferenceGenomes() {
        return referenceGenomes;
    }

    public void setReferenceGenomes(Set<ReferenceGenome> referenceGenomes) {
        this.referenceGenomes = referenceGenomes;
    }

    @Override
    public String toString() {
        return String.format("ReferenceSequence [accession=%s, id=%s]", accession, id);
    }

}