package org.renci.hearsay.dao.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.renci.hearsay.dao.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "alignment")
@XmlType(name = "Alignment")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "alignment")
public class Alignment implements Persistable {

    private static final long serialVersionUID = 240726999951481482L;

    @XmlAttribute(name = "id")
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alignment_id_seq")
    @SequenceGenerator(name = "alignment_id_seq", sequenceName = "alignment_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reference_sequence_fid")
    private ReferenceSequence referenceSequence;

    @Column(name = "genomic_start")
    private Integer genomicStart;

    @Column(name = "genomic_stop")
    private Integer genomicStop;

    @Column(name = "strand_type")
    @Enumerated(EnumType.STRING)
    private StrandType strandType;

    @OneToMany(mappedBy = "alignment", fetch = FetchType.EAGER)
    @OrderBy("regionStart ASC")
    private List<Region> regions;

    @Column(name = "protein")
    private String protein;

    @Column(name = "protein_region_start")
    private Integer proteinRegionStart;

    @Column(name = "protein_region_stop")
    private Integer proteinRegionStop;

    public Alignment() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReferenceSequence getReferenceSequence() {
        return referenceSequence;
    }

    public void setReferenceSequence(ReferenceSequence referenceSequence) {
        this.referenceSequence = referenceSequence;
    }

    public Integer getGenomicStart() {
        return genomicStart;
    }

    public void setGenomicStart(Integer genomicStart) {
        this.genomicStart = genomicStart;
    }

    public Integer getGenomicStop() {
        return genomicStop;
    }

    public void setGenomicStop(Integer genomicStop) {
        this.genomicStop = genomicStop;
    }

    public StrandType getStrandType() {
        return strandType;
    }

    public void setStrandType(StrandType strandType) {
        this.strandType = strandType;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public Integer getProteinRegionStart() {
        return proteinRegionStart;
    }

    public void setProteinRegionStart(Integer proteinRegionStart) {
        this.proteinRegionStart = proteinRegionStart;
    }

    public Integer getProteinRegionStop() {
        return proteinRegionStop;
    }

    public void setProteinRegionStop(Integer proteinRegionStop) {
        this.proteinRegionStop = proteinRegionStop;
    }

    @Override
    public String toString() {
        return String
                .format("Alignment [id=%s, genomicStart=%s, genomicStop=%s, strandType=%s, protein=%s, proteinRegionStart=%s, proteinRegionStop=%s]",
                        id, genomicStart, genomicStop, strandType, protein, proteinRegionStart, proteinRegionStop);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genomicStart == null) ? 0 : genomicStart.hashCode());
        result = prime * result + ((genomicStop == null) ? 0 : genomicStop.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((protein == null) ? 0 : protein.hashCode());
        result = prime * result + ((proteinRegionStart == null) ? 0 : proteinRegionStart.hashCode());
        result = prime * result + ((proteinRegionStop == null) ? 0 : proteinRegionStop.hashCode());
        result = prime * result + ((strandType == null) ? 0 : strandType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alignment other = (Alignment) obj;
        if (genomicStart == null) {
            if (other.genomicStart != null)
                return false;
        } else if (!genomicStart.equals(other.genomicStart))
            return false;
        if (genomicStop == null) {
            if (other.genomicStop != null)
                return false;
        } else if (!genomicStop.equals(other.genomicStop))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (protein == null) {
            if (other.protein != null)
                return false;
        } else if (!protein.equals(other.protein))
            return false;
        if (proteinRegionStart == null) {
            if (other.proteinRegionStart != null)
                return false;
        } else if (!proteinRegionStart.equals(other.proteinRegionStart))
            return false;
        if (proteinRegionStop == null) {
            if (other.proteinRegionStop != null)
                return false;
        } else if (!proteinRegionStop.equals(other.proteinRegionStop))
            return false;
        if (strandType != other.strandType)
            return false;
        return true;
    }

}