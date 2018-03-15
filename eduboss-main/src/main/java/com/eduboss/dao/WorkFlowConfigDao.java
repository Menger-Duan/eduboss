package com.eduboss.dao;

import com.eduboss.domain.WorkFlowConfig;

public interface WorkFlowConfigDao extends GenericDAO<WorkFlowConfig, String> {

    /**
     * 获取第一个流程节点
     * @param type 流程类型
     * @return 该类型流程的第一个节点
     */
    WorkFlowConfig getFirstStep(String type);

    /**
     * 获取下一个流程节点
     * @param workFlowConfig 当前节点
     * @return 该类型流程的下一个节点
     */
    WorkFlowConfig getNextStep(WorkFlowConfig workFlowConfig);

    /**
     * 获取上一个流程节点
     * @param workFlowConfig 当前节点
     * @return 该类型流程的上一个节点
     */
    WorkFlowConfig getPrevStep(WorkFlowConfig workFlowConfig);

}
