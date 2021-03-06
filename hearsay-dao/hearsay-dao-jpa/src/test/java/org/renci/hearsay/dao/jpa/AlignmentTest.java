package org.renci.hearsay.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.IntRange;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.hearsay.dao.HearsayDAOException;
import org.renci.hearsay.dao.model.Alignment;
import org.renci.hearsay.dao.model.Identifier;
import org.renci.hearsay.dao.model.Location;
import org.renci.hearsay.dao.model.ReferenceSequence;
import org.renci.hearsay.dao.model.Region;
import org.renci.hearsay.dao.model.RegionType;
import org.renci.hearsay.dao.model.StrandType;

public class AlignmentTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-hearsay", null);
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void nullRegions() {

        ReferenceSequenceDAOImpl referenceSequenceDAO = new ReferenceSequenceDAOImpl();
        referenceSequenceDAO.setEntityManager(em);

        AlignmentDAOImpl alignmentDAO = new AlignmentDAOImpl();
        alignmentDAO.setEntityManager(em);

        List<Long> referenceSequenceIdList = Arrays.asList(93854L);

        try {
            for (Long referenceSequenceId : referenceSequenceIdList) {

                ReferenceSequence referenceSequence = referenceSequenceDAO.findById(referenceSequenceId);

                List<Alignment> alignmentList = alignmentDAO.findByReferenceSequenceId(referenceSequence.getId());

                if (CollectionUtils.isNotEmpty(alignmentList)) {

                    for (Alignment alignment : alignmentList) {
                        List<Region> regionList = alignment.getRegions();
                        if (CollectionUtils.isEmpty(regionList)) {
                            for (Region region : regionList) {
                                assertTrue(region.getRegionLocation() == null);
                            }
                        }

                    }
                }
            }
        } catch (HearsayDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fixRegionsWithNoGenomicLocation() {
        File alignmentsFile = new File("/tmp", "GCF_000001405.28_knownrefseq_alignments.gff3");
        LinkedList<String> alignmentsLines = new LinkedList<String>();
        try (FileInputStream fis = new FileInputStream(alignmentsFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                alignmentsLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ReferenceSequenceDAOImpl referenceSequenceDAO = new ReferenceSequenceDAOImpl();
        referenceSequenceDAO.setEntityManager(em);

        AlignmentDAOImpl alignmentDAO = new AlignmentDAOImpl();
        alignmentDAO.setEntityManager(em);

        GeneDAOImpl geneDAO = new GeneDAOImpl();
        geneDAO.setEntityManager(em);

        LocationDAOImpl locationDAO = new LocationDAOImpl();
        locationDAO.setEntityManager(em);

        RegionDAOImpl regionDAO = new RegionDAOImpl();
        regionDAO.setEntityManager(em);

        try {

            List<Long> referenceSequenceIdList = Arrays.asList(93854L, 94004L, 77351L, 81574L, 77866L, 70161L, 70163L,
                    82238L, 82243L, 70162L, 82240L, 82244L, 82242L, 82245L, 82239L, 82241L, 87211L, 77350L, 91687L,
                    91685L, 91686L, 91684L, 89010L, 64975L, 64974L, 85889L, 64971L, 64972L, 63901L, 88734L, 88736L,
                    61196L, 88735L, 75794L, 86093L, 86095L, 86094L, 86092L, 86091L, 93836L, 86097L, 65884L);

            for (Long referenceSequenceId : referenceSequenceIdList) {

                ReferenceSequence referenceSequence = referenceSequenceDAO.findById(referenceSequenceId);

                StrandType strandType = referenceSequence.getStrandType();

                String refSeqGenomicAccession = null;
                String refSeqVersionedAccession = null;
                if (CollectionUtils.isEmpty(referenceSequence.getIdentifiers())) {
                    System.out.println("refseq has no identifiers");
                    return;
                }
                for (Identifier identifier : referenceSequence.getIdentifiers()) {
                    if (identifier.getSystem().equals("www.ncbi.nlm.nih.gov/genome")) {
                        refSeqGenomicAccession = identifier.getValue();
                    }
                    if (identifier.getSystem().equals("www.ncbi.nlm.nih.gov/nuccore")) {
                        refSeqVersionedAccession = identifier.getValue();
                    }
                }

                List<Alignment> alignmentList = alignmentDAO.findByReferenceSequenceId(referenceSequence.getId());

                if (CollectionUtils.isNotEmpty(alignmentList)) {

                    for (Alignment alignment : alignmentList) {

                        Location proteinLocation = alignment.getProteinLocation();

                        List<Region> regionList = alignment.getRegions();

                        if (CollectionUtils.isNotEmpty(regionList)) {

                            for (Region region : regionList) {
                                Location transcriptLocation = region.getTranscriptLocation();
                                if (transcriptLocation == null) {
                                    continue;
                                }

                                Location regionLocation = region.getRegionLocation();

                                if (regionLocation != null) {
                                    continue;
                                }

                                for (String line : alignmentsLines) {

                                    if (line.startsWith("#")) {
                                        continue;
                                    }

                                    String[] columns = line.split("\t");
                                    String genomicAccession = columns[0];
                                    String genomicStart = columns[3];
                                    String genomicStop = columns[4];
                                    String strand = columns[6];
                                    String attributes = columns[8];

                                    if (!genomicAccession.substring(0, genomicAccession.indexOf(".")).equals(
                                            refSeqGenomicAccession.substring(0, refSeqGenomicAccession.indexOf(".")))) {
                                        continue;
                                    }

                                    if (attributes.contains(refSeqVersionedAccession)) {

                                        String[] attributeSplit = attributes.split(";");
                                        String[] targetSplit = attributeSplit[1].split(" ");
                                        Integer start = Integer.valueOf(targetSplit[1]);
                                        Integer stop = Integer.valueOf(targetSplit[2]);

                                        if (strandType.equals(StrandType.MINUS)) {
                                            System.out.printf("genomic: (%s, %s), transcript: (%s, %s)%n", start
                                                    .toString(), stop.toString(), transcriptLocation.getStop()
                                                    .toString(), transcriptLocation.getStart().toString());
                                            if (transcriptLocation.getStart().equals(stop)
                                                    && transcriptLocation.getStop().equals(start)) {
                                                Location genomicLocation = new Location(Integer.valueOf(genomicStart),
                                                        Integer.valueOf(genomicStop));
                                                em.getTransaction().begin();
                                                genomicLocation.setId(locationDAO.save(genomicLocation));
                                                em.getTransaction().commit();
                                                region.setRegionLocation(genomicLocation);
                                                em.getTransaction().begin();
                                                regionDAO.save(region);
                                                em.getTransaction().commit();
                                            }
                                        } else {
                                            System.out.printf("genomic: (%s, %s), transcript: (%s, %s)%n", start
                                                    .toString(), stop.toString(), transcriptLocation.getStart()
                                                    .toString(), transcriptLocation.getStop().toString());
                                            if (transcriptLocation.getStart().equals(start)
                                                    && transcriptLocation.getStop().equals(stop)) {
                                                Location genomicLocation = new Location(Integer.valueOf(genomicStart),
                                                        Integer.valueOf(genomicStop));
                                                genomicLocation.setId(locationDAO.save(genomicLocation));
                                                em.getTransaction().begin();
                                                genomicLocation.setId(locationDAO.save(genomicLocation));
                                                em.getTransaction().commit();
                                                region.setRegionLocation(genomicLocation);
                                                em.getTransaction().begin();
                                                regionDAO.save(region);
                                                em.getTransaction().commit();
                                            }
                                        }

                                    }

                                }

                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createIntrons() {

        ReferenceSequenceDAOImpl referenceSequenceDAO = new ReferenceSequenceDAOImpl();
        referenceSequenceDAO.setEntityManager(em);

        AlignmentDAOImpl alignmentDAO = new AlignmentDAOImpl();
        alignmentDAO.setEntityManager(em);

        try {

            // ReferenceSequence referenceSequence = referenceSequenceDAO.findById(55965L);
            // List<Alignment> alignmentList = alignmentDAO.findByReferenceSequenceId(55965L);

            ReferenceSequence referenceSequence = referenceSequenceDAO.findById(55830L);
            List<Alignment> alignmentList = alignmentDAO.findByReferenceSequenceId(55830L);

            StrandType strandType = referenceSequence.getStrandType();
            System.out.println(strandType.toString());

            for (Alignment alignment : alignmentList) {
                Location proteinLocation = alignment.getProteinLocation();
                System.out.printf("Protein: %s%n", proteinLocation.toString());

                List<Region> regionList = alignment.getRegions();
                List<Region> utrRegionList = new ArrayList<Region>();

                printRegions(regionList);

                for (Region region : regionList) {
                    Location transcriptLocation = region.getTranscriptLocation();
                    if (transcriptLocation == null) {
                        continue;
                    }

                    Location regionLocation = region.getRegionLocation();
                    int regionStart = regionLocation.getStart();
                    int regionStop = regionLocation.getStop();

                    IntRange transcriptRange = transcriptLocation.toRange();

                    int transcriptStart = transcriptLocation.getStart();
                    int transcriptStop = transcriptLocation.getStop();

                    if (strandType.equals(StrandType.MINUS)) {

                        if (transcriptRange.containsInteger(alignment.getProteinLocation().getStart())) {

                            transcriptLocation.setStart(alignment.getProteinLocation().getStart() - 1);

                            regionLocation.setStart(regionLocation.getStop() - transcriptLocation.diff());

                            Region newRegion = new Region(RegionType.EXON);
                            newRegion.setAlignment(alignment);

                            Location newTranscriptLocation = new Location(transcriptStart, alignment
                                    .getProteinLocation().getStart());
                            newRegion.setTranscriptLocation(newTranscriptLocation);

                            Location newRegionLocation = new Location(regionLocation.getStart() - 1
                                    - newTranscriptLocation.diff(), regionLocation.getStart() - 1);
                            newRegion.setRegionLocation(newRegionLocation);

                            utrRegionList.add(newRegion);
                        }

                        if (transcriptRange.containsInteger(alignment.getProteinLocation().getStop())) {

                            transcriptLocation.setStart(alignment.getProteinLocation().getStop());

                            regionLocation.setStart(regionLocation.getStop() - transcriptLocation.diff());

                            Region newRegion = new Region(RegionType.EXON);
                            newRegion.setAlignment(alignment);

                            Location newTranscriptLocation = new Location(transcriptStart, alignment
                                    .getProteinLocation().getStop() + 1);
                            newRegion.setTranscriptLocation(newTranscriptLocation);

                            // is this off by one?
                            Location newRegionLocation = new Location(regionLocation.getStart()
                                    - newTranscriptLocation.diff() - 1, regionLocation.getStart() - 1);
                            newRegion.setRegionLocation(newRegionLocation);

                            utrRegionList.add(newRegion);
                        }

                    }

                    if (strandType.equals(StrandType.PLUS)) {

                        if (transcriptRange.containsInteger(alignment.getProteinLocation().getStart())) {

                            transcriptLocation.setStop(alignment.getProteinLocation().getStop() - 1);

                            regionLocation.setStop(regionStart + transcriptLocation.diff());

                            Region newRegion = new Region(RegionType.EXON);
                            newRegion.setAlignment(alignment);

                            Location newTranscriptLocation = new Location(alignment.getProteinLocation().getStart(),
                                    transcriptStop);
                            newRegion.setTranscriptLocation(newTranscriptLocation);

                            Location newRegionLocation = new Location(regionLocation.getStop() + 1,
                                    regionLocation.getStop() + 1 + newTranscriptLocation.diff());
                            newRegion.setRegionLocation(newRegionLocation);

                            utrRegionList.add(newRegion);

                        }

                        if (transcriptRange.containsInteger(alignment.getProteinLocation().getStop())) {

                            transcriptLocation.setStop(alignment.getProteinLocation().getStop());

                            regionLocation.setStop(regionStart + transcriptLocation.diff());

                            Region newRegion = new Region(RegionType.UTR3);
                            newRegion.setAlignment(alignment);

                            Location newTranscriptLocation = new Location(alignment.getProteinLocation().getStop() + 1,
                                    transcriptStop);
                            newRegion.setTranscriptLocation(newTranscriptLocation);

                            Location newRegionLocation = new Location(regionStop - newTranscriptLocation.diff(),
                                    regionStop);
                            newRegion.setRegionLocation(newRegionLocation);

                            utrRegionList.add(newRegion);

                        }

                    }

                }

                regionList.addAll(utrRegionList);

                for (Region region : regionList) {

                    if (strandType.equals(StrandType.PLUS)
                            && region.getTranscriptLocation().getStop() < alignment.getProteinLocation().getStart()) {
                        region.setRegionType(RegionType.UTR5);
                    }

                    if (strandType.equals(StrandType.PLUS)
                            && region.getTranscriptLocation().getStop() > alignment.getProteinLocation().getStop()) {
                        region.setRegionType(RegionType.UTR3);
                    }

                    if (strandType.equals(StrandType.MINUS)
                            && region.getTranscriptLocation().getStop() < alignment.getProteinLocation().getStart()) {
                        region.setRegionType(RegionType.UTR5);
                    }

                    if (strandType.equals(StrandType.MINUS)
                            && region.getTranscriptLocation().getStop() > alignment.getProteinLocation().getStop()) {
                        region.setRegionType(RegionType.UTR3);
                    }

                }

                printRegions(regionList);

                List<Region> intronRegionList = new ArrayList<Region>();

                // adding intron regions
                Region previousRegion = null;
                for (Region currentRegion : regionList) {
                    if (previousRegion == null) {
                        previousRegion = currentRegion;
                        continue;
                    }

                    if (previousRegion.getRegionLocation().getStop()
                            .equals(currentRegion.getRegionLocation().getStart() - 1)) {
                        previousRegion = currentRegion;
                        continue;
                    }

                    Region region = new Region(RegionType.INTRON);
                    region.setAlignment(alignment);
                    Location regionLocation = new Location(previousRegion.getRegionLocation().getStop() + 1,
                            currentRegion.getRegionLocation().getStart() - 1);
                    region.setRegionLocation(regionLocation);
                    intronRegionList.add(region);

                    previousRegion = currentRegion;
                }

                regionList.addAll(intronRegionList);

                printRegions(regionList);
            }

        } catch (HearsayDAOException e) {
            e.printStackTrace();
        }

    }

    private void printRegions(List<Region> regionList) {
        Collections.sort(regionList, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return Integer.compare(o1.getRegionLocation().getStart(), o2.getRegionLocation().getStart());
            }
        });
        System.out.println("-----------");
        for (Region region : regionList) {
            Location regionLocation = region.getRegionLocation();
            Location transcriptLocation = region.getTranscriptLocation();
            if (transcriptLocation == null) {
                System.out.printf("%s, %s%n", region.toString(), regionLocation.toString());
                continue;
            }
            System.out.printf("%s, %s, %s%n", region.toString(), regionLocation.toString(),
                    transcriptLocation.toString());
        }
    }

}
