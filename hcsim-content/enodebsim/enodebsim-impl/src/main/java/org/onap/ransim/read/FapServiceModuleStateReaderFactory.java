/*
 * Copyright (C) 2018 Wipro Limited.
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

package org.onap.ransim.read;

import static org.onap.ransim.ModuleConfiguration.FS_SERVICE_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.onap.ransim.CrudService;
import io.fd.honeycomb.translate.impl.read.GenericListReader;
import io.fd.honeycomb.translate.read.ReaderFactory;
import io.fd.honeycomb.translate.read.registry.ModifiableReaderRegistryBuilder;
import javax.annotation.Nonnull;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccess;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccessBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;

/**
 * Factory producing readers for enodebsim plugin's data.
 */
public final class FapServiceModuleStateReaderFactory implements ReaderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(FapServiceModuleStateReaderFactory.class);
    public static final InstanceIdentifier<RadioAccess> ROOT_STATE_CONTAINER_ID =
            InstanceIdentifier.create(RadioAccess.class);

    /**
     * Injected crud service to be passed to customizers instantiated in this factory.
     */
    @Inject
    @Named(FS_SERVICE_NAME)
    private CrudService<FapService> crudService;

    @Override
    public void init(@Nonnull final ModifiableReaderRegistryBuilder registry) {
        LOG.info("RANSIM FapServiceModuleStateReaderFactory init called");

        // register reader that only delegate read's to its children
        registry.addStructuralReader(ROOT_STATE_CONTAINER_ID, RadioAccessBuilder.class);

        // just adds reader to the structure
        // use addAfter/addBefore if you want to add specific order to readers on the same level of tree
        // use subtreeAdd if you want to handle multiple nodes in single customizer/subtreeAddAfter/subtreeAddBefore if you also want to add order
        // be aware that instance identifier passes to subtreeAdd/subtreeAddAfter/subtreeAddBefore should define subtree,
        // therefore it should be relative from handled node down - InstanceIdentifier.create(HandledNode), not parent.child(HandledNode.class)
        registry.add(
                new GenericListReader<>(ROOT_STATE_CONTAINER_ID.child(FapService.class),
                        new FapServiceCustomizer(crudService)));
    }
}
