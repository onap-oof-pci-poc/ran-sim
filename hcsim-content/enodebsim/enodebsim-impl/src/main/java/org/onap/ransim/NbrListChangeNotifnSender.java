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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.NbrlistChangeNotification;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.NbrlistChangeNotificationBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.RadioAccess;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.FapService;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.FapServiceBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.FapServiceKey;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.fap.service.LteRanNeighborListInUseLteCellChanged;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.fap.service.LteRanNeighborListInUseLteCellChangedBuilder;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.nbrlist.change.notification.fap.service.LteRanNeighborListInUseLteCellChangedKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fd.honeycomb.notification.ManagedNotificationProducer;
import io.fd.honeycomb.notification.NotificationCollector;

/**
 * Notification producer for sample plugin
 */
public class NbrListChangeNotifnSender implements ManagedNotificationProducer {

    private static final Logger LOG = LoggerFactory
            .getLogger(NbrListChangeNotifnSender.class);
    private static final InstanceIdentifier<RadioAccess> ROOT_CONTAINER_ID = InstanceIdentifier
            .create(RadioAccess.class);

    private Thread thread;
    static NotificationCollector collector = null;

    public static void sendNotification(UpdateCell updCell) {
        LOG.info("RANSIM NbrListChangeNotifnSender sendNotification called ****** {}"
                , updCell.toString());
        FapServiceKey fsKey = new FapServiceKey(updCell.getOneCell()
                .getCellId());
        List<LteRanNeighborListInUseLteCellChanged> nbrList = new ArrayList<LteRanNeighborListInUseLteCellChanged>();
        for (int i = 0; i < updCell.getOneCell().getNeighborList().size(); i++) {
            Neighbor nbr = updCell.getOneCell().getNeighborList().get(i);
            LteRanNeighborListInUseLteCellChangedKey nbrInUseKey = new LteRanNeighborListInUseLteCellChangedKey(
                    null, null);
            LteRanNeighborListInUseLteCellChanged nbrInUse = new LteRanNeighborListInUseLteCellChangedBuilder()
                    .setPnfName(nbr.getServerId()).setCid(nbr.getNodeId())
                    .setPhyCellId(BigInteger.valueOf(nbr.getPhysicalCellId()))
                    .setPlmnid(nbr.getPlmnId()).build();
            nbrList.add(nbrInUse);
        }
        FapService fsCell = new FapServiceBuilder()
                .setAlias(updCell.getOneCell().getCellId())
                .setCid(updCell.getOneCell().getCellId())
                .setLteRanNeighborListInUseLteCellChanged(nbrList).build();
        BigInteger fapServiceNumberOfEntriesChanged = BigInteger.ONE;
        List<FapService> fsList = new ArrayList<FapService>();
        fsList.add(fsCell);
        final NbrlistChangeNotification notification = new NbrlistChangeNotificationBuilder()
                .setFapServiceNumberOfEntriesChanged(
                        fapServiceNumberOfEntriesChanged).setFapService(fsList)
                .build();
        LOG.info("Emitting notification: {}", notification);
        collector.onNotification(notification);
    }

    @Override
    public void start(@Nonnull final NotificationCollector collector) {

        NbrListChangeNotifnSender.collector = collector;
        LOG.info("Starting notification stream for interfaces");

        // Simulating notification producer
        thread = new Thread(() -> {
                    try { Thread.sleep(25000); } catch(Exception e){}
            int notfnCount = 0;
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                try {
/*        
        	    LOG.info("Sending notification ..." + notfnCount++);
                    List<Neighbor> nbrs = new ArrayList<Neighbor> ();
                    nbrs.add(new Neighbor("jio", "1", 1, "ncserver1001", "ncserver1001", false)); 
                    UpdateCell updCell = new UpdateCell("1", "127.0.0.1", "50000" , new Topology("ncserver1001", 1, "51", nbrs));
                    sendNotification(updCell);
*/
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "NotificationProducer");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Nonnull
    @Override
    public Collection<Class<? extends Notification>> getNotificationTypes() {
        // Producing only this single type of notification
        return Collections.singleton(NbrlistChangeNotification.class);
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
