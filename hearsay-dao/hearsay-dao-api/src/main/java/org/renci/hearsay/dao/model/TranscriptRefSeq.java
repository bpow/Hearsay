package org.renci.hearsay.dao.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "transcriptRefSeq")
@XmlType(name = "TranscriptRefSeq")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "transcript_sequence")
@NamedQueries({ @NamedQuery(name = "TranscriptRefSeq.findAll", query = "SELECT a FROM TranscriptRefSeq a order by a.accession") })
public class TranscriptRefSeq extends ReferenceSequence {

    private static final long serialVersionUID = 7343059748383867273L;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gene_fid")
    private Gene gene;

    @OneToMany(mappedBy = "transcriptRefSeq", fetch = FetchType.EAGER)
    private Set<TranscriptAlignment> alignments;

    @OneToMany(mappedBy = "transcriptRefSeq", fetch = FetchType.EAGER)
    @OrderBy("regionStart ASC")
    private Set<Feature> features;

    public TranscriptRefSeq() {
        super();
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public Set<TranscriptAlignment> getAlignments() {
        return alignments;
    }

    public void setAlignments(Set<TranscriptAlignment> alignments) {
        this.alignments = alignments;
    }

    @Override
    public String toString() {
        return String.format("TranscriptRefSeq [id=%s, accession=%s]", id, accession);
    }

}