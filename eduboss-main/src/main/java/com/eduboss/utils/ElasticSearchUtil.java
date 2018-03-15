package com.eduboss.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;



/**
 * ElasticSearch工具类
 * Created by xuwen on 2015/3/19.
 */
public class ElasticSearchUtil {

    private static TransportClient transportClient = null;

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取transportClient
     * transportClient仅做数据传输，不会把自身加入集群中
     * @return
     */
    public static Client getClient(){
        if(transportClient == null){
            Settings settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", PropertiesUtils.getStringValue("elasticsearch.cluster.name"))
                    .put("client.transport.sniff", true)
                    .build();
            transportClient = new TransportClient(settings);
            String ipTemp = PropertiesUtils.getStringValue("elasticsearch.address");
            String[] ips = ipTemp.split(",");
            for(String ipstr : ips) {
                String[] address = ipstr.split(":");
                transportClient.addTransportAddress(new InetSocketTransportAddress(address[0],Integer.parseInt(address[1])));
            }
        }
        return transportClient;
    }

    /**
     * 索引数据
     * @param index 索引
     * @param type 类型
     * @param id id
     * @param data 数据
     * @return
     */
    public static IndexResponse index(String index,String type,String id,Object data){
        IndexRequestBuilder indexRequestBuilder = getClient().prepareIndex(index,type);
        if(StringUtils.isNotBlank(id)){
            indexRequestBuilder.setId(id);
        }
        try {
            indexRequestBuilder.setSource(mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return indexRequestBuilder.execute().actionGet();
    }

}
