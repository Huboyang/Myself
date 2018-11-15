package com.hby.myselfproject.config;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class SAPConfig {

  @Bean
  public JCoDestination createJCoDestination(@Value("${jco.host}") String host, @Value("${jco.clientName}") String clientName,
                                             @Value("${jco.userId}") String userId, @Value("${jco.password}") String pwd) throws JCoException, IOException {
    String language = "ZH";
    String system = "00";
    // 设置SAP的基本连接参数
    Properties connectProperties = new Properties();
    connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
    connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, system);
    connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, clientName);
    connectProperties.setProperty(DestinationDataProvider.JCO_USER, userId);
    connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, pwd);
    connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);

    // 连接池配置
    connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "25");
    connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "100");
    connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_TIME, "1000");
    connectProperties.setProperty(DestinationDataProvider.JCO_MAX_GET_TIME, "30000");
    connectProperties.setProperty(DestinationDataProvider.JCO_EXPIRATION_PERIOD, "500");

    final String ABAP_AS = "ABAP_AS_WITH_POOL";
    // 创建DestinationDataProvider，
    File cfg = new File(ABAP_AS + ".jcoDestination");
    try (FileOutputStream fos = new FileOutputStream(cfg, false)) {
      connectProperties.store(fos, "Destination - " + ABAP_AS);
    } catch (IOException e) {
      throw e;
    }

    return JCoDestinationManager.getDestination(ABAP_AS);
  }
}
