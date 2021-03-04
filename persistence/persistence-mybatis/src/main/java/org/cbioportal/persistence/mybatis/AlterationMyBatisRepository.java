package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.CNA;
import org.cbioportal.model.CopyNumberCountByGene;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.model.MutationEventType;
import org.cbioportal.model.QueryElement;
import org.cbioportal.model.util.Select;
import org.cbioportal.persistence.AlterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class AlterationMyBatisRepository implements AlterationRepository {

    @Autowired
    private AlterationCountsMapper alterationCountsMapper;

    @Override
    public List<AlterationCountByGene> getSampleAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                 Select<Integer> entrezGeneIds,
                                                                 final Select<MutationEventType> mutationEventTypes,
                                                                 final Select<CNA> cnaEventTypes,
                                                                 QueryElement searchFusions) {

        // TODO add test
        if (mutationEventTypes != null && !mutationEventTypes.hasAll() && searchFusions != QueryElement.PASS)
            throw new IllegalArgumentException("Filtering for mutations vs. fusions and specifying mutation types" +
                "simultaneously is not permitted.");

        if (((mutationEventTypes == null || mutationEventTypes.hasNone()) && (cnaEventTypes == null || cnaEventTypes.hasNone()))
            || (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty())) {
            return Collections.emptyList();
        }

        return alterationCountsMapper.getSampleAlterationCounts(
            molecularProfileCaseIdentifiers,
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions);
    }

    @Override
    public List<AlterationCountByGene> getPatientAlterationCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                                  Select<Integer> entrezGeneIds,
                                                                  Select<MutationEventType> mutationEventTypes,
                                                                  Select<CNA> cnaEventTypes,
                                                                  QueryElement searchFusions) {

        if (mutationEventTypes != null && !mutationEventTypes.hasAll() && searchFusions != QueryElement.PASS)
            throw new IllegalArgumentException("Filtering for mutations vs. fusions and specifying mutation types" +
                "simultaneously is not permitted.");

        if (((mutationEventTypes == null || mutationEventTypes.hasNone()) && (cnaEventTypes == null || cnaEventTypes.hasNone()))
            || (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty())) {
            return Collections.emptyList();
        }

        return alterationCountsMapper.getPatientAlterationCounts(
            molecularProfileCaseIdentifiers,
            entrezGeneIds,
            createMutationTypeList(mutationEventTypes),
            createCnaTypeList(cnaEventTypes),
            searchFusions);
    }

    @Override
    public List<CopyNumberCountByGene> getSampleCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                          Select<Integer> entrezGeneIds,
                                                          Select<CNA> cnaEventTypes) {

        if (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty()
            || cnaEventTypes == null || cnaEventTypes.hasNone()) {
            return Collections.emptyList();
        }
        
        return alterationCountsMapper.getSampleCnaCounts(
            molecularProfileCaseIdentifiers,
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes));
    }

    @Override
    public List<CopyNumberCountByGene> getPatientCnaCounts(List<MolecularProfileCaseIdentifier> molecularProfileCaseIdentifiers,
                                                           Select<Integer> entrezGeneIds,
                                                           Select<CNA> cnaEventTypes) {

        if (molecularProfileCaseIdentifiers == null || molecularProfileCaseIdentifiers.isEmpty()
            || cnaEventTypes == null || cnaEventTypes.hasNone()) {
            return Collections.emptyList();
        }
        
        return alterationCountsMapper.getPatientCnaCounts(
            molecularProfileCaseIdentifiers,
            entrezGeneIds,
            createCnaTypeList(cnaEventTypes));
    }
    
    private Select<Short> createCnaTypeList(final Select<CNA> cnaEventTypes) {
        return cnaEventTypes != null ? cnaEventTypes.map(CNA::getCode) : Select.none();
    }

    private Select<String> createMutationTypeList(final Select<MutationEventType> mutationEventTypes) {
        if (mutationEventTypes == null) {
            return Select.none();
        }
        Select<String> mappedMutationTypes = mutationEventTypes.map(MutationEventType::getMutationType);
        mappedMutationTypes.inverse(mutationEventTypes.inverse());

        return mappedMutationTypes;
    }

}
