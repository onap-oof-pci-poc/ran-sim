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

package com.wipro.www;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.wipro.www.websocket.WebsocketClient;
import com.wipro.www.write.ModuleWriterFactory;
import io.fd.honeycomb.translate.read.ReaderFactory;
import io.fd.honeycomb.translate.write.WriterFactory;
import net.jmob.guice.conf.core.ConfigurationModule;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.Attributes;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.PLMNInfoList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.plmninfo.SNSSAIList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.snssaiconfig.ConfigData;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.SliceProfilesList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.RRMPolicyRatio;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.rrmpolicy_group.RRMPolicyMemberList;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.managednfservicegroup.SAP;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDU;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUCPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.PLMNId;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.NRCellCU;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.NRCellRelation;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellrelationgroup.CellIndividualOffset;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.NRFreqRelation;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrfreqrelationgroup.OffsetMO;

//import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.wipro.www.ModuleConfiguration.*;

/**
 * Module class instantiating gnbsim plugin components.
 */
public final class Module extends AbstractModule {

    // TODO This initiates all the plugin components, but it still needs to be registered/wired into an integration
    // module producing runnable distributions. There is one such distribution in honeycomb project:
    // vpp-integration/minimal-distribution
    // In order to integrate this plugin with the distribution:
    // 1. Add a dependency on this maven module to the the distribution's pom.xml
    // 2. Add an instance of this module into the distribution in its Main class

    private static final Logger LOG = LoggerFactory.getLogger(Module.class);


    @Override
    protected void configure() {
        // requests injection of properties
        ConfigurationModule cfgMod = ConfigurationModule.create();
        install(cfgMod);
        requestInjection(ModuleConfiguration.class);
        Configuration configuration = Configuration.getInstance();

        try{
            byte[] mapData = Files.readAllBytes(Paths.get("config/gnbsim.json"));

            ObjectMapper mapper = new ObjectMapper();
            configuration = mapper.readValue(mapData, Configuration.class);
        } catch(IOException e){
            LOG.error("Configuration not found {]", e);
            System.exit(1);
        }

        //NearRTTIC

        bind(new TypeLiteral<CrudService<RanNetwork>>(){})
                .annotatedWith(Names.named(RANNETWORK_SERVICE_NAME))
                .to(RanNetworkCrudService.class);

        bind(new TypeLiteral<CrudService<NearRTRIC>>(){})
                .annotatedWith(Names.named(NEARRTRIC_SERVICE_NAME))
                .to(NearRTRICCrudService.class);

        bind(new TypeLiteral<CrudService<Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_ATTR_SERVICE_NAME))
                .to(NearRTRICAttributesCrudService.class);

        bind(new TypeLiteral<CrudService<PLMNInfoList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_ATTR_PLM_SERVICE_NAME))
                .to(NearRTRICpLMNInfoListCrudService.class);

        bind(new TypeLiteral<CrudService<SNSSAIList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_SERVICE_NAME))
                .to(NearRTRICpLMNSNSSAICrudService.class);

        bind(new TypeLiteral<CrudService<ConfigData>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_CONFIG_SERVICE_NAME))
                .to(NearRTRICConfigCrudService.class);

        bind(new TypeLiteral<CrudService<SliceProfilesList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_SLICE_PROFILES_LIST_SERVICE_NAME))
                .to(NearRTRICSliceProfilesListCrudService.class);

        bind(new TypeLiteral<CrudService<RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_RRMPOLICYRATIO_LIST_SERVICE_NAME))
                .to(NearRTRICRRMPolicyRatioCrudService.class);
        
        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nearrtricgroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_RRMPOLICYRATIO_ATTR_SERVICE_NAME))
                .to(NearRTRICRRMPolicyRatioAttributesCrudService.class);
        
        
        bind(new TypeLiteral<CrudService<RRMPolicyMemberList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_RRMPOLICY_MEMBER_LIST_SERVICE_NAME))
                .to(NearRTRICRRMPolicyMemberListCrudService.class); 

        //GNBDUFunction

        bind(new TypeLiteral<CrudService<GNBDUFunction>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_SERVICE_NAME))
                .to(NearRTRICGNBDUFunctionCrudService.class);


        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFN_ATTR_SERVICE_NAME))
                .to(GNBDUFunctionAttributesCrudService.class);


        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIO_SERVICE_NAME))
                .to(GNBDUFunctionRRMPolicyRatioCrudService.class);

        
        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbdufunctiongroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIOATTR_SERVICE_NAME))
                .to(GNBDUFunctionRRMPolicyRatioAttrCrudService.class);


         bind(new TypeLiteral<CrudService<SAP>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_SAP_SERVICE_NAME))
                .to(GNBDUFunctionSAPCrudService.class);

        //NRCellDU 

        bind(new TypeLiteral<CrudService<NRCellDU>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_SERVICE_NAME))
                .to(GNBDUFunctionNRCellDUCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.nrcelldu.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_ATTR_SERVICE_NAME))
                .to(NRCellDUAttributesCrudService.class);


        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIO_SERVICE_NAME))
                .to(NRCellDURRMPolicyRatioCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIOATTR_SERVICE_NAME))
                .to(NRCellDURRMPolicyRatioAttrCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcelldugroup.PLMNInfoList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_PLMNINFOLIST_SERVICE_NAME))
                .to(NRCellDUpLMNInfoListCrudService.class);

        //GNBCUUPFunction

        bind(new TypeLiteral<CrudService<GNBCUUPFunction>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUUPFUNCTION_SERVICE_NAME))
                .to(NearRTRICGNBCUUPFunctionCrudService.class);


        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcuupfunction.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUUPFN_ATTR_SERVICE_NAME))
                .to(GNBCUUPFunctionAttributesCrudService.class);

         bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIO_SERVICE_NAME))
                .to(GNBCUUPFunctionRRMPolicyRatioCrudService.class);

         bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIOATTR_SERVICE_NAME))
                .to(GNBCUUPFunctionRRMPolicyRatioAttrCrudService.class);

         bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcuupfunctiongroup.PLMNInfoList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUUPFN_PLMINFOLIST_SERVICE_NAME))
                .to(GNBCUUPFunctionpLMNInfoListCrudService.class);

         //GNBCUCPFunction

         bind(new TypeLiteral<CrudService<GNBCUCPFunction>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_SERVICE_NAME))
                .to(NearRTRICGNBCUCPFunctionCrudService.class);


         bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_SERVICE_NAME))
                .to(GNBCUCPFunctionAttributesCrudService.class);

         bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIO_SERVICE_NAME))
                .to(GNBCUCPFunctionRRMPolicyRatioCrudService.class);
        
        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.gnbcucpfunctiongroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIOATTR_SERVICE_NAME))
                .to(GNBCUCPFunctionRRMPolicyRatioAttrCrudService.class);

        bind(new TypeLiteral<CrudService<PLMNId>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_PLMNID_SERVICE_NAME))
                .to(GNBCUCPPLMNIdFunctionCrudService.class);


         bind(new TypeLiteral<CrudService<NRCellCU>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_SERVICE_NAME))
                .to(GNBCUCPFunctionNRCellCUCrudService.class);

        //NRCELLCU

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_ATTR_SERVICE_NAME))
                .to(NRCELLCUAttributesCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.PLMNInfoList>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_PLMNINFO_SERVICE_NAME))
                .to(NRCELLCUpLMNInfoListCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.RRMPolicyRatio>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIO_SERVICE_NAME))
                .to(NRCellCURRMPolicyRatioCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.nrcellcugroup.rrmpolicyratio.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIOATTR_SERVICE_NAME))
                .to(NRCellCURRMPolicyRatioAttrCrudService.class);

        bind(new TypeLiteral<CrudService<NRCellRelation>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_SERVICE_NAME))
                .to(NRCellCUNRCellRelationCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrcellrelation.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_ATTR_SERVICE_NAME))
                .to(NRCellCUNRCellRelationAttrCrudService.class);

        bind(new TypeLiteral<CrudService<CellIndividualOffset>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_IOFF_SERVICE_NAME))
                .to(NRCellRelationCellIndividualOffsetCrudService.class);

        bind(new TypeLiteral<CrudService<NRFreqRelation>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_SERVICE_NAME))
                .to(NRCellCUNRFreqRelationCrudService.class);

        bind(new TypeLiteral<CrudService<org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbcucpfunction.nrcellcu.nrfreqrelation.Attributes>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_ATTR_SERVICE_NAME))
                .to(NRCellCUNRFreqRelationAttrCrudService.class);

        bind(new TypeLiteral<CrudService<OffsetMO>>(){})
                .annotatedWith(Names.named(RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_OFFSETMO_SERVICE_NAME))
                .to(NRFreqRelationOffsetMOCrudService.class);

        // creates reader factory binding
        // can hold multiple binding for separate yang modules
        /*final Multibinder<ReaderFactory> readerFactoryBinder = Multibinder.newSetBinder(binder(), ReaderFactory.class);
        readerFactoryBinder.addBinding().to(ModuleStateReaderFactory.class); */

        // create writer factory binding
        // can hold multiple binding for separate yang modules
        final Multibinder<WriterFactory> writerFactoryBinder = Multibinder.newSetBinder(binder(), WriterFactory.class);
        writerFactoryBinder.addBinding().to(ModuleWriterFactory.class);

        LOG.info("Starting Websocket client...");

        WebsocketClient websocketClient = new WebsocketClient(configuration.getRansimIp(), configuration.getRansimPort(),
                configuration.getHcIp(), configuration.getHcPort(), configuration.getEndpoint(), configuration.getVesEventListenerUrl());


        websocketClient.initWebsocketClient();

        ConfigurationHandler.getInstance().setWebsocketClient(websocketClient);

        LOG.info("Module configured successfully");

    }
}
