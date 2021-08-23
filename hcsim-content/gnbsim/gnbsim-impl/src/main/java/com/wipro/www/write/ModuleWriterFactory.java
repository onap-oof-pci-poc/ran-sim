/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.www.write;

import static com.wipro.www.ModuleConfiguration.*;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.wipro.www.CrudService;

import io.fd.honeycomb.translate.impl.write.GenericWriter;
import io.fd.honeycomb.translate.write.WriterFactory;
import io.fd.honeycomb.translate.write.registry.ModifiableWriterRegistryBuilder;

import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.PLMNId;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.managednfservicegroup.SAP;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.PLMNInfoList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.RRMPolicyRatio;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellrelationgroup.CellIndividualOffset;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrfreqrelationgroup.OffsetMO;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUCPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.NRCellCU;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.NRCellRelation;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.NRFreqRelation;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDU;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.rrmpolicy_group.RRMPolicyMemberList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigData;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Factory producing writers for gnbsim plugin's data.
 */
public final class ModuleWriterFactory implements WriterFactory {

    private static final InstanceIdentifier<RanNetwork> ROOT_CONTAINER_ID = InstanceIdentifier.create(RanNetwork.class);

    /**
     * Injected crud service to be passed to customizers instantiated in this factory.
     */

    // NearRTRIC

    @Inject
    @Named(RANNETWORK_SERVICE_NAME)
    private CrudService<RanNetwork> ranNetworkCrudService;

    @Inject
    @Named(NEARRTRIC_SERVICE_NAME)
    private CrudService<NearRTRIC> nearrtricCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes> nearRtRicAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_ATTR_PLM_SERVICE_NAME)
    private CrudService<PLMNInfoList> nearRtRicAttrpLMNCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_SERVICE_NAME)
    private CrudService<SNSSAIList> nearRtRicAttrpLMNSnssaiCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_CONFIG_SERVICE_NAME)
    private CrudService<ConfigData> nearRtRicConfigDataCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_SLICE_PROFILES_LIST_SERVICE_NAME)
    private CrudService<SliceProfilesList> nearRtRicSliceProfilesListCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_RRMPOLICYRATIO_LIST_SERVICE_NAME)
    private CrudService<RRMPolicyRatio> nearRtRicRRMPolicyRatioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_RRMPOLICYRATIO_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.rrmpolicyratio.Attributes> nearRtRicRRMPolicyRatioAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_RRMPOLICY_MEMBER_LIST_SERVICE_NAME)
    private CrudService<RRMPolicyMemberList> nearRtRicRRMPolicyMemberListCrudService;

    // GNBDUFUNCTION

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_SERVICE_NAME)
    private CrudService<GNBDUFunction> nearRtRicGNBDUFunctionCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFN_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes> gnbduFunctionAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.RRMPolicyRatio> gnbduFunctionAttrrrmpolicyratioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIOATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.rrmpolicyratio.Attributes> gnbduFunctionAttrrrmpolicyratioAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_SAP_SERVICE_NAME)
    private CrudService<SAP> GNBDUFunctionSAPCrudService;

    // NRCELLDU

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_SERVICE_NAME)
    private CrudService<NRCellDU> GNBDUFunctionNRCellDUCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes> nrcellDUAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_PLMNINFOLIST_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.PLMNInfoList> nrcellDUpLMNInfoCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio> nrcellDURrmpolicyratioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIOATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.rrmpolicyratio.Attributes> nrcellDURrmpolicyratioAttrCrudService;

    // GNBCUUPFUNCTION

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUPFUNCTION_SERVICE_NAME)
    private CrudService<GNBCUUPFunction> nearRtRicGNBCUUPFunctionCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUPFN_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes> nearRtRicGNBCUUPFunctionAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUPFN_PLMINFOLIST_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.PLMNInfoList> nearRtRicGNBCUUPFunctionplmnCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.RRMPolicyRatio> GNBCUUPFunctionRrmPolicyRatioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIOATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.rrmpolicyratio.Attributes> GNBCUUPFunctionRrmPolicyRatioAttrCrudService;

    // GNBCUCPFunction

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_SERVICE_NAME)
    private CrudService<GNBCUCPFunction> nearRtRicGNBCUCPFunctionCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes> nearRtRicGNBCUCPFunctionAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_PLMNID_SERVICE_NAME)
    private CrudService<PLMNId> nearRtRicGNBCUCPFunctionAttrPLMNIdCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.RRMPolicyRatio> GNBCUCPFunctionAttrRrmpolicyRatioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIOATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.rrmpolicyratio.Attributes> GNBCUCPFunctionAttrRrmpolicyRatioAttrCrudService;

    // NRCELLCU

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_SERVICE_NAME)
    private CrudService<NRCellCU> nearRtRicGNBCUCPFunctionNRCellCUCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes> nearRtRicGNBCUCPFunctionNRCellCUAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_PLMNINFO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList> nearRtRicGNBCUCPFunctionNRCellCUplmnCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIO_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.RRMPolicyRatio> GNBCUCPFunctionNRCellCURRMPolicyRatioCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIOATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.rrmpolicyratio.Attributes> GNBCUCPFunctionNRCellCURRMPolicyRatioAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_SERVICE_NAME)
    private CrudService<NRCellRelation> nearRtRicGNBCUCPFunctionNRCellCUNRCellCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes> nearRtRicGNBCUCPFunctionNRCellCUNRCellAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_IOFF_SERVICE_NAME)
    private CrudService<CellIndividualOffset> nearRtRicGNBCUCPFunctionNRCellCUNRCellInOffsetCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_SERVICE_NAME)
    private CrudService<NRFreqRelation> nearRtRicGNBCUCPFunctionNRCellCUNRFREQRELCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_ATTR_SERVICE_NAME)
    private CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrfreqrelation.Attributes> nearRtRicGNBCUCPFunctionNRCellCUNRfreqRelAttrCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_OFFSETMO_SERVICE_NAME)
    private CrudService<OffsetMO> nearRtRicGNBCUCPFunctionNRCellCUNRfreqRelOffsetMOCrudService;

    @Override
    public void init(@Nonnull final ModifiableWriterRegistryBuilder registry) {

        // adds writer for child node
        // no need to add writers for empty nodes

        // NearRTRIC

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID, new RanNetworkCustomizer(ranNetworkCrudService)));
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class),
                new NearRTRICCustomizer(nearrtricCrudService)));
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class),
                new NearRTRICAttributesCustomizer(nearRtRicAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(SliceProfilesList.class),
                new NearRTRICSliceProfilesListCustomizer(nearRtRicSliceProfilesListCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.RRMPolicyRatio.class),
                new NearRTRICRRMPolicyRatioCustomizer(nearRtRicRRMPolicyRatioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.rrmpolicyratio.Attributes.class),
                new NearRTRICRRMPolicyRatioAttributesCustomizer(nearRtRicRRMPolicyRatioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(PLMNInfoList.class), new NearRTRICpLMNInfoListCustomizer(nearRtRicAttrpLMNCrudService)));
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(PLMNInfoList.class).child(SNSSAIList.class),
                new NearRTRICpLMNSNSSAICustomizer(nearRtRicAttrpLMNSnssaiCrudService)));
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes.class)
                .child(PLMNInfoList.class).child(SNSSAIList.class).child(ConfigData.class),
                new NearRTRICConfigCustomizer(nearRtRicConfigDataCrudService)));

        // GNBDUFunction

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class),
                new NearRTRICGNBDUFunctionCustomizer(nearRtRicGNBDUFunctionCrudService)));
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes.class),
                new GNBDUFunctionAttributesCustomizer(gnbduFunctionAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.RRMPolicyRatio.class),
                new GNBDUFunctionRRMPolicyRatioCustomizer(gnbduFunctionAttrrrmpolicyratioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.rrmpolicyratio.Attributes.class),
                new GNBDUFunctionRRMPolicyRatioAttrCustomizer(gnbduFunctionAttrrrmpolicyratioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        // NRCellDU

        registry.add(new GenericWriter<>(
                ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(NRCellDU.class),
                new GNBDUFunctionNRCellDUCustomizer(GNBDUFunctionNRCellDUCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class),
                new NRCellDUAttributesCustomizer(nrcellDUAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.PLMNInfoList.class),
                new NRCellDUpLMNInfoListCustomizer(nrcellDUpLMNInfoCrudService)));

        // Fix added for SDN error for ConfigData
        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.PLMNInfoList.class)
                .child(SNSSAIList.class), new NearRTRICpLMNSNSSAICustomizer(nearRtRicAttrpLMNSnssaiCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.PLMNInfoList.class)
                .child(SNSSAIList.class).child(ConfigData.class),
                new NearRTRICConfigCustomizer(nearRtRicConfigDataCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio.class),
                new NRCellDURRMPolicyRatioCustomizer(nrcellDURrmpolicyratioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.rrmpolicyratio.Attributes.class),
                new NRCellDURRMPolicyRatioAttrCustomizer(nrcellDURrmpolicyratioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class)
                .child(NRCellDU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        // GNBCUUPFunction

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class),
                new NearRTRICGNBCUUPFunctionCustomizer(nearRtRicGNBCUUPFunctionCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class),
                new GNBCUUPFunctionAttributesCustomizer(nearRtRicGNBCUUPFunctionAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.PLMNInfoList.class),
                new GNBCUUPFunctionpLMNInfoListCustomizer(nearRtRicGNBCUUPFunctionplmnCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.PLMNInfoList.class)
                .child(SNSSAIList.class), new NearRTRICpLMNSNSSAICustomizer(nearRtRicAttrpLMNSnssaiCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.PLMNInfoList.class)
                .child(SNSSAIList.class).child(ConfigData.class),
                new NearRTRICConfigCustomizer(nearRtRicConfigDataCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.RRMPolicyRatio.class),
                new GNBCUUPFunctionRRMPolicyRatioCustomizer(GNBCUUPFunctionRrmPolicyRatioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.rrmpolicyratio.Attributes.class),
                new GNBCUUPFunctionRRMPolicyRatioAttrCustomizer(GNBCUUPFunctionRrmPolicyRatioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        // GNBCUCPFunction

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class),
                new NearRTRICGNBCUCPFunctionCustomizer(nearRtRicGNBCUCPFunctionCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class),
                new GNBCUCPFunctionAttributesCustomizer(nearRtRicGNBCUCPFunctionAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class)
                .child(PLMNId.class),
                new GNBCUCPFunctionPLMNIdCustomizer(nearRtRicGNBCUCPFunctionAttrPLMNIdCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.RRMPolicyRatio.class),
                new GNBCUCPFunctionRRMPolicyRatioCustomizer(GNBCUCPFunctionAttrRrmpolicyRatioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.rrmpolicyratio.Attributes.class),
                new GNBCUCPFunctionRRMPolicyRatioAttrCustomizer(GNBCUCPFunctionAttrRrmpolicyRatioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(
                org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        // NRCellCU

        registry.add(new GenericWriter<>(
                ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(NRCellCU.class),
                new GNBCUCPFunctionNRCellCUCustomizer(nearRtRicGNBCUCPFunctionNRCellCUCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class),
                new NRCELLCUAttributesCustomizer(nearRtRicGNBCUCPFunctionNRCellCUAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.RRMPolicyRatio.class),
                new NRCellCURRMPolicyRatioCustomizer(GNBCUCPFunctionNRCellCURRMPolicyRatioCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.rrmpolicyratio.Attributes.class),
                new NRCellCURRMPolicyRatioAttrCustomizer(GNBCUCPFunctionNRCellCURRMPolicyRatioAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.RRMPolicyRatio.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.rrmpolicyratio.Attributes.class)
                .child(RRMPolicyMemberList.class),
                new NearRTRICRRMPolicyMemberListCustomizer(nearRtRicRRMPolicyMemberListCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList.class),
                new NRCELLCUpLMNInfoListCustomizer(nearRtRicGNBCUCPFunctionNRCellCUplmnCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList.class)
                .child(SNSSAIList.class), new NearRTRICpLMNSNSSAICustomizer(nearRtRicAttrpLMNSnssaiCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList.class)
                .child(SNSSAIList.class).child(ConfigData.class),
                new NearRTRICConfigCustomizer(nearRtRicConfigDataCrudService)));

        registry.add(new GenericWriter<>(
                ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(NRCellCU.class)
                        .child(NRCellRelation.class),
                new NRCellCUNRCellRelationCustomizer(nearRtRicGNBCUCPFunctionNRCellCUNRCellCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class).child(NRCellRelation.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes.class),
                new NRCellCUNRCellRelationAttrCustomizer(nearRtRicGNBCUCPFunctionNRCellCUNRCellAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class).child(NRCellRelation.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes.class)
                .child(SAP.class), new GNBDUFunctionSAPCustomizer(GNBDUFunctionSAPCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class).child(NRCellRelation.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes.class)
                .child(CellIndividualOffset.class),
                new NRCellRelationCellIndividualOffsetCustomizer(
                        nearRtRicGNBCUCPFunctionNRCellCUNRCellInOffsetCrudService)));

        registry.add(new GenericWriter<>(
                ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class).child(NRCellCU.class)
                        .child(NRFreqRelation.class),
                new NRCellCUNRFreqRelationCustomizer(nearRtRicGNBCUCPFunctionNRCellCUNRFREQRELCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class).child(NRFreqRelation.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrfreqrelation.Attributes.class),
                new NRCellCUNRFreqRelationAttrCustomizer(nearRtRicGNBCUCPFunctionNRCellCUNRfreqRelAttrCrudService)));

        registry.add(new GenericWriter<>(ROOT_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUCPFunction.class)
                .child(NRCellCU.class).child(NRFreqRelation.class)
                .child(org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrfreqrelation.Attributes.class)
                .child(OffsetMO.class),
                new NRFreqRelationOffsetMOCustomizer(nearRtRicGNBCUCPFunctionNRCellCUNRfreqRelOffsetMOCrudService)));

    }
}
