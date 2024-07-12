/*
 * Copyright 2014 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.alert;

import azkaban.batch.HoldBatchAlert;
import azkaban.eventnotify.entity.EventNotify;
import azkaban.executor.ExecutableFlow;
import azkaban.executor.ExecutableFlowBase;
import azkaban.executor.ExecutableNode;
import azkaban.executor.ExecutionCycle;
import azkaban.executor.Executor;
import azkaban.executor.ExecutorLoader;
import azkaban.executor.ExecutorManagerException;
import azkaban.history.ExecutionRecover;
import azkaban.project.entity.FlowBusiness;
import azkaban.scheduler.Schedule;
import azkaban.sla.SlaOption;
import azkaban.utils.Props;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;

public interface Alerter {

  void alertOnSuccess(ExecutableFlow exflow) throws Exception;

  void alertOnError(ExecutableFlow exflow, String... extraReasons) throws Exception;

  void alertOnFirstError(ExecutableFlow exflow) throws Exception;

  void alertOnSla(SlaOption slaOption, String slaMessage) throws Exception;

  void alertOnFailedUpdate(Executor executor, List<ExecutableFlow> executions,
      ExecutorManagerException e);
  void sendAlert(List<String> alterList, String subject, String body);

  void alertOnSla(SlaOption slaOption, ExecutableFlow exflow) throws Exception;


  /**
   * 对工程的业务信息进行注册和上报，面向工作流
   * @param exflow
   * @param sharedProps
   * @param logger
   * @param flowBusiness
   * @param props
   * @throws Exception
   */
  void alertOnIMSRegistFlowStart(ExecutableFlow exflow, Map<String, Props> sharedProps, Logger logger,
                                 FlowBusiness flowBusiness, Props props) throws Exception;

  void alertOnIMSRegistNodeStart(ExecutableFlow exflow, org.slf4j.Logger logger,
                                 FlowBusiness flowBusiness, Props props, ExecutableNode node) throws Exception;

  void alertOnIMSRegistStart(ExecutableFlow exflow,Map<String, Props> sharedProps,Logger logger)throws Exception;

  String alertOnIMSRegistStart(String projectName, String flowId, FlowBusiness flowBusiness,
                               Props props);

  void alertOnIMSRegistFinish(ExecutableFlow exflow,Map<String, Props> sharedProps,Logger logger) throws Exception;
  /**
   * 关键路径共工作流直接上报方法
   * @param flowBase
   * @param sharedProps
   * @param logger
   * @param flowBusiness
   * @param node
   * @param props
   * @throws Exception
   */
  void alertOnIMSUploadForFlow(ExecutableFlowBase flowBase, Map<String, Props> sharedProps, Logger logger,
                               FlowBusiness flowBusiness, ExecutableNode node, Props props) throws Exception;

  /**
   * 关键路径节点直接上报
   * @param flowBase
   * @param logger
   * @param flowBusiness
   * @param node
   * @param props
   */
  void alertOnIMSUploadForNode(ExecutableFlowBase flowBase, Logger logger, FlowBusiness flowBusiness,
                               ExecutableNode node, Props props);

  void alertOnIMSRegistError(ExecutableFlow exflow, Map<String, Props> sharedProps, Logger logger)
      throws Exception;

  void alertOnSla(SlaOption slaOption, ExecutableFlow exflow, String alertType) throws Exception;

  void alertOnFinishSla(SlaOption slaOption, ExecutableFlow exflow) throws Exception;

  void alertOnFlowStarted(ExecutableFlow executableFlow, List<EventNotify> eventNotifies)
      throws Exception;

  /**
   * flow失败暂停发送通用告警
   * @param exflow
   * @param nodePath
   * @throws Exception
   */
  void alertOnFlowPaused(ExecutableFlow exflow, String nodePath) throws Exception;

  /**
   * flow 失败暂停发送sla告警
   * @param slaOption
   * @param exflow
   * @throws Exception
   */
  void alertOnFlowPausedSla(SlaOption slaOption, ExecutableFlow exflow, String nodePath) throws Exception;

  void alertOnCycleFlowInterrupt(ExecutableFlow flow, ExecutionCycle cycleFlow, List<String> emails, String alertLevel, String... extraReasons) throws Exception;

  void alertOnHistoryRecoverFinish(ExecutionRecover executionRecover) throws Exception;

  void alertOnHoldBatch(HoldBatchAlert holdBatchAlert, ExecutorLoader executorLoader, boolean isFrequent);

  void doMissSchedulesAlerter(Set<Schedule> scheduleList, Date startTime, Date shutdownTime);

  /**
   *
   * @param jobId
   */
  void alertOnSingnalBacklog(int jobId, Map<String, String> consumeInfo);
}
