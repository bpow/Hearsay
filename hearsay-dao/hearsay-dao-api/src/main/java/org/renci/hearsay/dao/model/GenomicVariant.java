package org.renci.hearsay.dao.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.openjpa.persistence.jdbc.Index;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "genomicVariant")
@XmlType(name = "GenomicVariant")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "genomic_variant")
@NamedQueries({ @NamedQuery(name = "GenomicVariant.findByGeneName", query = "SELECT a FROM GenomicVariant a where a.gene.name = :name") })
public class GenomicVariant extends VariantRepresentation {

    private static final long serialVersionUID = 8070379578229091997L;

    @Lob
    @Column(name = "ref_allele")
    private String referenceAllele;

    @Lob
    @Column(name = "var_allele")
    private String variantAllele;

    @Index
    @Column(name = "chromosome")
    private String chromosome;

    @OneToMany(mappedBy = "genomicVariant", fetch = FetchType.LAZY)
    private Set<VariantAssertion> assertions;

    public GenomicVariant() {
        super();
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public String getVariantAllele() {
        return variantAllele;
    }

    public void setVariantAllele(String variantAllele) {
        this.variantAllele = variantAllele;
    }

    public Set<VariantAssertion> getAssertions() {
        return assertions;
    }

    public void setAssertions(Set<VariantAssertion> assertions) {
        this.assertions = assertions;
    }

    @Override
    public String toString() {
        return String.format("GenomicVariant [id=%s, referenceAllele=%s, variantAllele=%s, chromosome=%s]", id,
                referenceAllele, variantAllele, chromosome);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
        result = prime * result + ((referenceAllele == null) ? 0 : referenceAllele.hashCode());
        result = prime * result + ((variantAllele == null) ? 0 : variantAllele.hashCode());
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
        GenomicVariant other = (GenomicVariant) obj;
        if (chromosome == null) {
            if (other.chromosome != null)
                return false;
        } else if (!chromosome.equals(other.chromosome))
            return false;
        if (referenceAllele == null) {
            if (other.referenceAllele != null)
                return false;
        } else if (!referenceAllele.equals(other.referenceAllele))
            return false;
        if (variantAllele == null) {
            if (other.variantAllele != null)
                return false;
        } else if (!variantAllele.equals(other.variantAllele))
            return false;
        return true;
    }

}
