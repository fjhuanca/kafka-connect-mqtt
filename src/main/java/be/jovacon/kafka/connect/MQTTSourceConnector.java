package be.jovacon.kafka.connect;

import be.jovacon.kafka.connect.config.MQTTSourceConnectorConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Implementation of the Kafka Connect Source connector
 */
public class MQTTSourceConnector extends SourceConnector {

    private static final Logger log = LoggerFactory.getLogger(MQTTSourceConnector.class);
    private MQTTSourceConnectorConfig mqttSourceConnectorConfig;
    private Map<String, String> configProps;

    public void start(Map<String, String> map) {
        this.mqttSourceConnectorConfig = new MQTTSourceConnectorConfig(map);
        this.configProps = Collections.unmodifiableMap(map);
    }

    public Class<? extends Task> taskClass() {
        return MQTTSourceTask.class;
    }

    public List<Map<String, String>> taskConfigs(int maxTasks) {
        log.debug("Enter taskconfigs");
        // if (maxTasks > 1) {
        //     log.info("maxTasks is " + maxTasks + ". MaxTasks > 1 is not supported in this connector.");
        // }
        List<Map<String, String>> taskConfigs = new ArrayList<>(maxTasks);
        if (maxTasks == 1){
            Map<String, String> taskConfig = new HashMap<>(configProps);
            taskConfig.put(MQTTSourceConnectorConfig.MQTT_SUBTOPIC, String.valueOf(-1));
            taskConfigs.add(taskConfig);
        }
        else{
            for (int i = 0; i < maxTasks; i++) {
                Map<String, String> taskConfig = new HashMap<>(configProps);
                taskConfig.put(MQTTSourceConnectorConfig.MQTT_SUBTOPIC, String.valueOf(i)); // Modify this line to set the subtopic for each task
                taskConfigs.add(taskConfig);
            }
        }

        log.debug("Taskconfigs: " + taskConfigs);
        return taskConfigs;
    }

    public void stop() {

    }

    public ConfigDef config() {
        return MQTTSourceConnectorConfig.configDef();
    }

    public String version() {
        return Version.getVersion();
    }
}
