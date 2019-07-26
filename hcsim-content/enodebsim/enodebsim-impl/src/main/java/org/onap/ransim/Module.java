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

package org.onap.ransim;

import static org.onap.ransim.ModuleConfiguration.CC_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.FS_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.LRC_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.LRNLIULC_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.LRNLIU_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.LRR_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.LR_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.L_SERVICE_NAME;
import static org.onap.ransim.ModuleConfiguration.XL_SERVICE_NAME;
import io.fd.honeycomb.notification.ManagedNotificationProducer;
import io.fd.honeycomb.translate.read.ReaderFactory;
import io.fd.honeycomb.translate.write.WriterFactory;
import net.jmob.guice.conf.core.ConfigurationModule;

import org.onap.ransim.read.CellConfigModuleStateReaderFactory;
import org.onap.ransim.read.FapServiceModuleStateReaderFactory;
import org.onap.ransim.read.LteModuleStateReaderFactory;
import org.onap.ransim.read.LteRanCommonModuleStateReaderFactory;
import org.onap.ransim.read.LteRanModuleStateReaderFactory;
import org.onap.ransim.read.LteRanNeighborListInUseModuleStateReaderFactory;
import org.onap.ransim.read.LteRanRfModuleStateReaderFactory;
import org.onap.ransim.read.X0005b9LteModuleStateReaderFactory;
import org.onap.ransim.write.CellConfigModuleWriterFactory;
import org.onap.ransim.write.FapServiceModuleWriterFactory;
import org.onap.ransim.write.LteModuleWriterFactory;
import org.onap.ransim.write.LteRanCommonModuleWriterFactory;
import org.onap.ransim.write.LteRanModuleWriterFactory;
import org.onap.ransim.write.LteRanNeighborListInUseLteCellModuleWriterFactory;
import org.onap.ransim.write.LteRanNeighborListInUseModuleWriterFactory;
import org.onap.ransim.write.LteRanRfModuleWriterFactory;
import org.onap.ransim.write.X0005b9LteModuleWriterFactory;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.CellConfig;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.X0005b9Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.Lte;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.LteRan;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanCommon;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanNeighborListInUse;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.LteRanRf;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

/**
 * Module class instantiating enodebsim plugin components.
 */
public final class Module extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(Module.class);

    // TODO This initiates all the plugin components, but it still needs to be registered/wired into an integration
    // module producing runnable distributions. There is one such distribution in honeycomb project:
    // vpp-integration/minimal-distribution
    // In order to integrate this plugin with the distribution:
    // 1. Add a dependency on this maven module to the the distribution's pom.xml
    // 2. Add an instance of this module into the distribution in its Main class

    @Override
    protected void configure() {
        LOG.info("RANSIM Module.configure is called");
        ConfigurationModule cfgMod = ConfigurationModule.create();
        // requests injection of properties
        install(cfgMod);
        requestInjection(ModuleConfiguration.class);
        requestInjection(ConfigJsonHandler.class);
        ConfigJsonHandler.getConfigJsonHandler(null);

        // creates binding for interface implementation by name
        bind(new TypeLiteral<CrudService<FapService>>(){})
        .annotatedWith(Names.named(FS_SERVICE_NAME))
        .to(FapServiceCrudService.class);

        // creates reader factory binding
        // can hold multiple binding for separate yang modules
        final Multibinder<ReaderFactory> readerFactoryBinder = Multibinder.newSetBinder(binder(), ReaderFactory.class);
        readerFactoryBinder.addBinding().to(FapServiceModuleStateReaderFactory.class);
        LOG.info("RANSIM Module.configure FapServiceModuleStateReaderFactory registered");

        // create writer factory binding
        // can hold multiple binding for separate yang modules
        final Multibinder<WriterFactory> writerFactoryBinder = Multibinder.newSetBinder(binder(), WriterFactory.class);
        writerFactoryBinder.addBinding().to(FapServiceModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<LteRanCommon>>(){})
        .annotatedWith(Names.named(LRC_SERVICE_NAME))
        .to(LteRanCommonCrudService.class);

        readerFactoryBinder.addBinding().to(LteRanCommonModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(LteRanCommonModuleWriterFactory.class);

        Multibinder.newSetBinder(binder(), ManagedNotificationProducer.class).addBinding().to(NbrListChangeNotifnSender.class);

        bind(new TypeLiteral<CrudService<CellConfig>>(){})
        .annotatedWith(Names.named(CC_SERVICE_NAME))
        .to(CellConfigCrudService.class);

        readerFactoryBinder.addBinding().to(CellConfigModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(CellConfigModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<X0005b9Lte>>(){})
        .annotatedWith(Names.named(XL_SERVICE_NAME))
        .to(X0005b9LteCrudService.class);

        readerFactoryBinder.addBinding().to(X0005b9LteModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(X0005b9LteModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<Lte>>(){})
        .annotatedWith(Names.named(L_SERVICE_NAME))
        .to(LteCrudService.class);

        readerFactoryBinder.addBinding().to(LteModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(LteModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<LteRan>>(){})
        .annotatedWith(Names.named(LR_SERVICE_NAME))
        .to(LteRanCrudService.class);

        readerFactoryBinder.addBinding().to(LteRanModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(LteRanModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<LteRanRf>>(){})
        .annotatedWith(Names.named(LRR_SERVICE_NAME))
        .to(LteRanRfCrudService.class);

        readerFactoryBinder.addBinding().to(LteRanRfModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(LteRanRfModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<LteRanNeighborListInUse>>(){})
        .annotatedWith(Names.named(LRNLIU_SERVICE_NAME))
        .to(LteRanNeighborListInUseCrudService.class);

        readerFactoryBinder.addBinding().to(LteRanNeighborListInUseModuleStateReaderFactory.class);
        writerFactoryBinder.addBinding().to(LteRanNeighborListInUseModuleWriterFactory.class);

        bind(new TypeLiteral<CrudService<LteRanNeighborListInUseLteCell>>(){})
        .annotatedWith(Names.named(LRNLIULC_SERVICE_NAME))
        .to(LteRanNeighborListInUseLteCellCrudService.class);

        //readerFactoryBinder.addBinding().to(LteRanNeighborListInUseLteCellModuleStateReaderFactory.class);
        //LOG.info("RANSIM Module.configure LteRanNeighborListInUseLteCellModuleStateReaderFactory registered");
        writerFactoryBinder.addBinding().to(LteRanNeighborListInUseLteCellModuleWriterFactory.class);
        LOG.info("RANSIM Module.configure LteRanNeighborListInUseLteCellModuleWriterFactory registered");

    }
}

