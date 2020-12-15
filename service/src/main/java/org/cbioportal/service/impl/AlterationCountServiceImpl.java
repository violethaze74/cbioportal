package org.cbioportal.service.impl;

import org.cbioportal.model.*;
import org.cbioportal.model.util.Select;
import org.cbioportal.persistence.AlterationRepository;
import org.cbioportal.service.AlterationCountService;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AlterationCountServiceImpl implements AlterationCountService {

    @Autowired
    private AlterationRepository alterationRepository;
    @Autowired
    private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;
    @Autowired
    private AlterationEnrichmentUtil<CopyNumberCountByGene> alterationEnrichmentUtilCna;

    @Override
	public List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                 List<Integer> entrezGeneIds,
                                                                 boolean includeFrequency,
                                                                 boolean includeMissingAlterationsFromGenePanel,
                                                                 Select<MutationEventType> mutationEventTypes,
                                                                 Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                                 boolean searchFusions) {
        
        List<AlterationCountByGene> alterationCountByGenes;
        if (molecularProfileCaseIdentifiers.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getSampleAlterationCounts(molecularProfileCaseIdentifiers,
                entrezGeneIds,
                mutationEventTypes,
                cnaEventTypes,
                searchFusions);
            if (includeFrequency) {
                alterationEnrichmentUtil.includeFrequencyForSamples(molecularProfileCaseIdentifiers,
                    alterationCountByGenes,
                    includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }
    
    @Override
    public List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                  List<Integer> entrezGeneIds,
                                                                  boolean includeFrequency,
                                                                  boolean includeMissingAlterationsFromGenePanel,
                                                                  Select<MutationEventType> mutationEventTypes,
                                                                  Select<CopyNumberAlterationEventType> cnaEventTypes,
                                                                  boolean searchFusions) {
        
        List<AlterationCountByGene> alterationCountByGenes;
        if (molecularProfileCaseIdentifiers.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getPatientAlterationCounts(molecularProfileCaseIdentifiers,
                    entrezGeneIds, mutationEventTypes, cnaEventTypes, searchFusions);
            if (includeFrequency) {
                alterationEnrichmentUtil.includeFrequencyForPatients(molecularProfileCaseIdentifiers, alterationCountByGenes, includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }

    @Override
    public List<AlterationCountByGene> getSampleMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                               List<Integer> entrezGeneIds,
                                                               boolean includeFrequency,
                                                               boolean includeMissingAlterationsFromGenePanel,
                                                               Select<MutationEventType> mutationEventTypes) {
        return getSampleAlterationCounts(molecularProfileCaseIdentifiers,
            entrezGeneIds,
            includeFrequency,
            includeMissingAlterationsFromGenePanel,
            mutationEventTypes,
            Select.none(),
            false);
    }

    @Override
    public List<AlterationCountByGene> getPatientMutationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                List<Integer> entrezGeneIds,
                                                                boolean includeFrequency,
                                                                boolean includeMissingAlterationsFromGenePanel,
                                                                Select<MutationEventType> mutationEventTypes) {
        return getPatientAlterationCounts(molecularProfileCaseIdentifiers,
            entrezGeneIds,
            includeFrequency,
            includeMissingAlterationsFromGenePanel,
            mutationEventTypes,
            Select.none(),
            false);
    }

    @Override
    public List<AlterationCountByGene> getSampleFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                             List<Integer> entrezGeneIds,
                                                             boolean includeFrequency,
                                                             boolean includeMissingAlterationsFromGenePanel,
                                                             Select<MutationEventType> mutationEventTypes) {
        return getSampleAlterationCounts(molecularProfileCaseIdentifiers,
            entrezGeneIds,
            includeFrequency,
            includeMissingAlterationsFromGenePanel,
            mutationEventTypes,
            Select.none(),
            true);
    }

    @Override
    public List<AlterationCountByGene> getPatientFusionCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                              List<Integer> entrezGeneIds,
                                                              boolean includeFrequency,
                                                              boolean includeMissingAlterationsFromGenePanel,
                                                              Select<MutationEventType> mutationEventTypes) {
        return getPatientAlterationCounts(molecularProfileCaseIdentifiers,
            entrezGeneIds,
            includeFrequency,
            includeMissingAlterationsFromGenePanel,
            mutationEventTypes,
            Select.none(),
            true);    }
            
// -- Should be reinstated when the legacy CNA count endpoint retires            
//    @Override
//    public List<AlterationCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                          List<Integer> entrezGeneIds,
//                                                          boolean includeFrequency,
//                                                          boolean includeMissingAlterationsFromGenePanel,
//                                                          List<CopyNumberAlterationEventType> cnaEventTypes) {
//        return getSampleAlterationCounts(molecularProfileCaseIdentifiers,
//            entrezGeneIds,
//            includeFrequency,
//            includeMissingAlterationsFromGenePanel,
//            new ArrayList<>(),
//            cnaEventTypes,
//            false);
//    }
//
//    @Override
//    public List<AlterationCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
//                                                           List<Integer> entrezGeneIds,
//                                                           boolean includeFrequency,
//                                                           boolean includeMissingAlterationsFromGenePanel,
//                                                           List<CopyNumberAlterationEventType> cnaEventTypes) {
//        return getPatientAlterationCounts(molecularProfileCaseIdentifiers,
//            entrezGeneIds,
//            includeFrequency,
//            includeMissingAlterationsFromGenePanel,
//            new ArrayList<>(),
//            cnaEventTypes,
//            false);
//    }
    
    @Override
    public List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          List<Integer> entrezGeneIds,
                                                          boolean includeFrequency,
                                                          boolean includeMissingAlterationsFromGenePanel,
                                                          Select<CopyNumberAlterationEventType> cnaEventTypes) {
        List<CopyNumberCountByGene> alterationCountByGenes;
        if (molecularProfileCaseIdentifiers.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getSampleCnaCounts(
                molecularProfileCaseIdentifiers,
                entrezGeneIds,
                cnaEventTypes);
            if (includeFrequency) {
                alterationEnrichmentUtilCna.includeFrequencyForSamples(molecularProfileCaseIdentifiers, alterationCountByGenes, includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }

    @Override
    public List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           List<Integer> entrezGeneIds,
                                                           boolean includeFrequency,
                                                           boolean includeMissingAlterationsFromGenePanel,
                                                           Select<CopyNumberAlterationEventType> cnaEventTypes) {
        List<CopyNumberCountByGene> alterationCountByGenes;
        if (molecularProfileCaseIdentifiers.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getPatientCnaCounts(
                molecularProfileCaseIdentifiers,
                entrezGeneIds,
                cnaEventTypes);
            if (includeFrequency) {
                alterationEnrichmentUtilCna.includeFrequencyForPatients(molecularProfileCaseIdentifiers, alterationCountByGenes, includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }

}
