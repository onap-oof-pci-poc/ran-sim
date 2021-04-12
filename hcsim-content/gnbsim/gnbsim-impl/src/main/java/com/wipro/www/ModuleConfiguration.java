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
import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.InjectConfig;
import net.jmob.guice.conf.core.Syntax;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class containing static configuration for gnbsim module,<br>
 * either loaded from property file gnbsim.json from classpath.
 * <p/>
 * Further documentation for the configuration injection can be found at:
 * https://github.com/yyvess/gconf
 */

@BindConfig(value = "module", syntax = Syntax.JSON)
public final class ModuleConfiguration {

    @InjectConfig("sample-prop")
    public String sampleProp;



    /**
     * Constant name used to identify gnbsim plugin specific components during dependency injection.
     */
    public static final String RANNETWORK_SERVICE_NAME = "rannetwork-service";
    public static final String NEARRTRIC_SERVICE_NAME = "nearrtric-service";
    public static final String RANNETWORK_NRTRIC_ATTR_SERVICE_NAME = "rannetwork-nearrtric-attr-service";
    public static final String RANNETWORK_NRTRIC_ATTR_PLM_SERVICE_NAME = "rannetwork-nearrtric-attr-plm-service";
    public static final String RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_SERVICE_NAME = "rannetwork-nearrtric-attr-plm-snssai-service";
    public static final String RANNETWORK_NRTRIC_ATTR_PLM_SNSSAI_CONFIG_SERVICE_NAME = "rannetwork-nearrtric-attr-plm-snssai-configdata-service";
   public static final String RANNETWORK_NRTRIC_SLICE_PROFILES_LIST_SERVICE_NAME =
"rannetwork-nearrtric-slice-profile-lists-service";
   public static final String RANNETWORK_NRTRIC_RRMPOLICYRATIO_LIST_SERVICE_NAME = "rannetwork-nearrtric-rrmpolicyratio-service";
   public static final String RANNETWORK_NRTRIC_RRMPOLICYRATIO_ATTR_SERVICE_NAME = "rannetwork-nearrtric-rrmpolicyratio-attr-service";
   public static final String RANNETWORK_NRTRIC_RRMPOLICY_MEMBER_LIST_SERVICE_NAME = "rannetwork-nearrtric-rrmpolicy-member-list-service";

//GNBDUFunction

   public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_SERVICE_NAME = "rannetwork-nearrtric-gnbdufunction-service";
    public static final String RANNETWORK_NRTRIC_GNBDUFN_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-attr-service";
    public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIO_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-attr-rrmpolicyratio-service";
    public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_RRMPOLICYRATIOATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-attr-rrmpolicyratio-attr-service";
    public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_SAP_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-attr-sap-service";

//NRCELLDU

   public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-nrcelldu-service";
   public static final String RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIO_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-nrcelldu-rrmpolicyratio-service";
   public static final String RANNETWORK_NRTRIC_NRCELLDU_RRMPOLICYRATIOATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-nrcelldu-rrmpolicyratioattr-service";
    public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-nrcelldu-attr-service";  
   public static final String RANNETWORK_NRTRIC_GNBDUFUNCTION_NRCELLDU_PLMNINFOLIST_SERVICE_NAME = "rannetwork-nearrtric-gnbdufn-nrcelldu-plminfolist-service";

   //GNBCUUPFunction

   public static final String RANNETWORK_NRTRIC_GNBCUUPFUNCTION_SERVICE_NAME = "rannetwork-nearrtric-gnbcuup-service";
   public static final String RANNETWORK_NRTRIC_GNBCUUPFN_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcuup-attr-service"; 
   public static final String RANNETWORK_NRTRIC_GNBCUUPFN_PLMINFOLIST_SERVICE_NAME = "rannetwork-nearrtric-gnbcuup-plminfolist-service";
   public static final String RANNETWORK_NRTRIC_GNBCUCPFN_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-service";
   public static final String RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIO_SERVICE_NAME = "rannetwork-nearrtric-gnbcuup-rrmpolicyratio-service";
   public static final String RANNETWORK_NRTRIC_GNBCUUP_RRMPOLICYRATIOATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcuup-rrmpolicyratioattr-service";

   // GNBCUCPFunction

   public static final String RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-attr-service";
   public static final String RANNETWORK_NRTRIC_GNBCUCPFN_ATTR_PLMNID_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-attr-plmnid-service";
   public static final String RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIO_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-rrmpolicyratio-service";
   public static final String RANNETWORK_NRTRIC_GNBCUCP_RRMPOLICYRATIOATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-rrmpolicyratioattr-service";

   //NRCELLCU

   public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-service"; 
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-attr-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_PLMNINFO_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-plmninfo-service";
  public static final String RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIO_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-rrmpolicyratio-service";
  public static final String RANNETWORK_NRTRIC_NRCELLCU_RRMPOLICYRATIOATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-rrmpolicyratioattr-service";

  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrcellrelation-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrcellrelation-attr-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRCELL_IOFF_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrcellrelation-cellindividual-offset-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrfreqrelation-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_ATTR_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrfreqrelation-attr-service";
  public static final String RANNETWORK_NRTRIC_GNBCUCPFN_NRCELLCU_NRFREQREL_OFFSETMO_SERVICE_NAME = "rannetwork-nearrtric-gnbcucp-nrcellcu-nrfreqrelation-attr-offsetmo-service";

}
