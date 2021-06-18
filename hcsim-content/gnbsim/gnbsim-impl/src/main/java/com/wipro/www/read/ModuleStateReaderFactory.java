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

package com.wipro.www.read;

import static com.wipro.www.ModuleConfiguration.NEARRTRIC_SERVICE_NAME;
import static com.wipro.www.ModuleConfiguration.RANNETWORK_NRTRIC_GNBDUFUNCTION_SERVICE_NAME;
import static com.wipro.www.ModuleConfiguration.RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_SERVICE_NAME;
import static com.wipro.www.ModuleConfiguration.RANNETWORK_NRTRIC_GNBCUUPFUNCTION_SERVICE_NAME;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.wipro.www.CrudService;
import io.fd.honeycomb.translate.impl.read.GenericListReader;
import io.fd.honeycomb.translate.read.ReaderFactory;
import io.fd.honeycomb.translate.read.registry.ModifiableReaderRegistryBuilder;
import javax.annotation.Nonnull;

import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetwork;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.RanNetworkBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.NearRTRIC;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunction;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBCUUPFunctionKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.GNBDUFunctionBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.ran.network.rev200806.ran.network.nearrtric.gnbdufunction.NRCellDU;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * Factory producing readers for gnbsim plugin's data.
 */
public final class ModuleStateReaderFactory implements ReaderFactory {

    private static final InstanceIdentifier<RanNetwork> ROOT_STATE_CONTAINER_ID = InstanceIdentifier.create(RanNetwork.class);

    private static final InstanceIdentifier<NearRTRIC> ROOT_NEARRTRIC_CONTAINER_ID = InstanceIdentifier.create(NearRTRIC.class);

    /**
     * Injected crud service to be passed to customizers instantiated in this factory.
     */

    @Inject
    @Named(NEARRTRIC_SERVICE_NAME)
    private CrudService<NearRTRIC> nearrtricCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_SERVICE_NAME)
    private CrudService<GNBDUFunction> nearRtRicGNBDUFunctionCrudService;
  
    @Inject
    @Named(RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_SERVICE_NAME)
    private CrudService<NRCellDU> gNBDUFunctionNRCellDUCrudService;

    @Inject
    @Named(RANNETWORK_NRTRIC_GNBCUUPFUNCTION_SERVICE_NAME)
    private CrudService<GNBCUUPFunction> nearRTRICGNBCUUPFunctionCrudService;

    @Override
    public void init(@Nonnull final ModifiableReaderRegistryBuilder registry) {

        // register reader that only delegate read's to its children
        registry.addStructuralReader(ROOT_STATE_CONTAINER_ID, RanNetworkBuilder.class);

        // just adds reader to the structure
        // use addAfter/addBefore if you want to add specific order to readers on the same level of tree
        // use subtreeAdd if you want to handle multiple nodes in single customizer/subtreeAddAfter/subtreeAddBefore if you also want to add order
        // be aware that instance identifier passes to subtreeAdd/subtreeAddAfter/subtreeAddBefore should define subtree,
        // therefore it should be relative from handled node down - InstanceIdentifier.create(HandledNode), not parent.child(HandledNode.class)

        registry.add(new GenericListReader<>(ROOT_STATE_CONTAINER_ID.child(NearRTRIC.class), new NearRTRICCustomizer(nearrtricCrudService)));
	registry.add(new GenericListReader<>(ROOT_STATE_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class), new NearRTRICGNBDUFunctionCustomizer(nearRtRicGNBDUFunctionCrudService)));
        registry.add(new GenericListReader<>(ROOT_STATE_CONTAINER_ID.child(NearRTRIC.class).child(GNBDUFunction.class).child(NRCellDU.class), new GNBDUFunctionNRCellDUCustomizer(gNBDUFunctionNRCellDUCrudService)));
        registry.add(new GenericListReader<>(ROOT_STATE_CONTAINER_ID.child(NearRTRIC.class).child(GNBCUUPFunction.class), new NearRTRICGNBCUUPFunctionCustomizer(nearRTRICGNBCUUPFunctionCrudService)));
   }
}
