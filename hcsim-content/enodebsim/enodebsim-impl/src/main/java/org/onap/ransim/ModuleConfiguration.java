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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.InjectConfig;
import net.jmob.guice.conf.core.Syntax;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class containing static configuration for enodebsim module,<br>
 * either loaded from property file enodebsim.json from classpath.
 * <p/>
 * Further documentation for the configuration injection can be found at:
 * https://github.com/yyvess/gconf
 */
@BindConfig(value = "honeycomb", syntax = Syntax.JSON)
public final class ModuleConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ModuleConfiguration.class);

    // TODO change the sample property to real plugin configuration
    // If there is no such configuration, remove this, enodebsim.json resource and its wiring from Module class

    /**
     * Sample property that's injected from external json configuration file.
     */
    @InjectConfig("peristConfigPath")
    public String peristConfigPath;

    @InjectConfig("enodebsimIp")
    public String enodebsimIp;

    @InjectConfig("enodebsimPort")
    public int enodebsimPort;

    @InjectConfig("ransimCtrlrIp")
    public String ransimCtrlrIp;

    @InjectConfig("ransimCtrlrPort")
    public int ransimCtrlrPort;

    @InjectConfig("useNetconfDataChangeNotifn")
    public boolean useNetconfDataChangeNotifn;

    @InjectConfig("vesEventListenerUrl")
    public String vesEventListenerUrl;

    Properties netconfConstants = new Properties();

    /**
     * Constant name used to identify enodebsim plugin specific components during dependency injection.
     */
    //public static final String ELEMENT_SERVICE_NAME = "element-service";
    public static final String FS_SERVICE_NAME = "fap-service";
    public static final String CC_SERVICE_NAME = "cc-service";
    public static final String XL_SERVICE_NAME = "xl-service";
    public static final String L_SERVICE_NAME = "l-service";
    public static final String LR_SERVICE_NAME = "lr-service";
    public static final String LRC_SERVICE_NAME = "lrc-service";
    public static final String LRR_SERVICE_NAME = "lrr-service";
    public static final String LRNLIU_SERVICE_NAME = "lrnliu-service";
    public static final String LRNLIULC_SERVICE_NAME = "lrnliulc-service";
    public void loadConfig() {
        InputStream input = null;
        try {
            input = new FileInputStream("config/ransim.properties");
            netconfConstants.load(input);
            peristConfigPath = netconfConstants.getProperty("peristConfigPath");
            enodebsimIp = netconfConstants.getProperty("enodebsimIp");
            enodebsimPort = Integer.parseInt(netconfConstants.getProperty("enodebsimPort"));
            ransimCtrlrIp = netconfConstants.getProperty("ransimCtrlrIp");
            ransimCtrlrPort = Integer.parseInt(netconfConstants.getProperty("ransimCtrlrPort"));
            useNetconfDataChangeNotifn = Boolean.parseBoolean(netconfConstants.getProperty("useNetconfDataChangeNotifn"));
            vesEventListenerUrl = netconfConstants.getProperty("vesEventListenerUrl");
        } catch (Exception e) {
            LOG.info("Properties file error", e);
        } finally {
            try {
                if(input != null)
                    input.close();
            } catch(Exception ex) {}
        }
    }
}
