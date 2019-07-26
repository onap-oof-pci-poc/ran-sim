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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.onap.ransim.websocket.model.Neighbor;
import org.onap.ransim.websocket.model.Topology;
import org.onap.ransim.websocket.model.UpdateCell;
import org.opendaylight.yang.gen.v1.org.onap.ccsdk.features.sdnr.northbound.oofpcipoc.rev190308.radio.access.fap.service.cell.config.lte.lte.ran.lte.ran.neighbor.list.in.use.LteRanNeighborListInUseLteCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SendToController extends TimerTask
{
    WriteVesData wvd;

    private static final Logger LOG = LoggerFactory
            .getLogger(SendToController.class);

    public SendToController (WriteVesData wvd) {
        this.wvd = wvd;
    }

    @Override
    public void run() {
        LOG.info("Timer Expired, Sending Cid: {}, List: {} to Controller at {}", wvd.cid,wvd.nbrList,System.currentTimeMillis());

        UpdateCell updateCell = new UpdateCell();
        Topology topology = new Topology();
        topology.setCellId(wvd.cid);

        List<Neighbor> updatedNeighborList = new ArrayList<>();

        for (LteRanNeighborListInUseLteCell neighbr : wvd.nbrList) {
            LOG.info("LteRanNeighborListInUseLteCell  {} ", neighbr);
            Neighbor updatedNbr = new Neighbor();
            updatedNbr.setNodeId(neighbr.getCid());
            if(neighbr.getPhyCellId() != null)
                updatedNbr
                .setPhysicalCellId(neighbr.getPhyCellId().longValue());
            updatedNbr.setPlmnId(neighbr.getPlmnid());
            updatedNbr.setPnfName(neighbr.getPnfName());
            if(neighbr.getBlacklisted() != null)
                updatedNbr.setBlacklisted(Boolean.parseBoolean(neighbr.getBlacklisted()));
            updatedNeighborList.add(updatedNbr);
        }
        topology.setNeighborList(updatedNeighborList);
        updateCell.setOneCell(topology);

        LOG.info("Sending to Ransim Ctrlr.");
        ConfigJsonHandler.getConfigJsonHandler(null).handleUpdateTopology(
                updateCell);

        wvd.nbrList.clear();
        wvd.timer.cancel();
    }
}

class WriteVesData {
    public List<LteRanNeighborListInUseLteCell> nbrList = new ArrayList<LteRanNeighborListInUseLteCell>();
    public Timer timer;
    public SendToController task;
    public String cid;
    public WriteVesData(String cid) {
        this.cid = cid;
    }
    public synchronized void CheckandAdd(LteRanNeighborListInUseLteCell nbr) {
        if(timer == null) {
            nbrList.add(nbr);
            timer = new Timer();
            task = new SendToController(this);
            timer.schedule(task, 2000, 2000);
        } else {
            nbrList.add(nbr);
            timer.cancel();
            task.cancel();
            timer = new Timer();
            task = new SendToController(this);
            timer.schedule(task, 2000, 2000);
        }
    }
}

public class ServerCidNbrListHandler {
    public static HashMap<String, WriteVesData> serverCidNbrList = new HashMap<String, WriteVesData>();
    public synchronized void CheckandAdd(String cid, LteRanNeighborListInUseLteCell nbr) {
        WriteVesData wvd = null;
        if(serverCidNbrList.containsKey(cid)) {
            wvd = serverCidNbrList.get(cid);
        } else {
            wvd = new WriteVesData(cid);
            serverCidNbrList.put(cid, wvd);
        }
        wvd.CheckandAdd(nbr);
    }

}
