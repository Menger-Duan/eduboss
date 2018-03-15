package tw.idv.idealist;

import com.opensymphony.workflow.InvalidActionException;
import com.opensymphony.workflow.InvalidEntryStateException;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.InvalidRoleException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.DefaultConfiguration;

/**
 * 請假流程
 * @author steven
 */
public class Leave {
	/**
	 * 送出假單
	 */
	public long send(String employee) {
		Workflow workflow = new BasicWorkflow(employee);
		DefaultConfiguration config = new DefaultConfiguration();
		workflow.setConfiguration(config);
		long workflowId = -1;
		try {
			//leave是workflowx.xml中定的名稱
			workflowId = workflow.initialize("leave", 0, null); 
			workflow.doAction(workflowId, 1, null);
		} catch (InvalidActionException e) {
			e.printStackTrace();
		} catch (InvalidRoleException e) {
			e.printStackTrace();
		} catch (InvalidInputException e) {
			e.printStackTrace();
		} catch (InvalidEntryStateException e) {
			e.printStackTrace();
		} catch (WorkflowException e) {
			e.printStackTrace();
		}

		return workflowId;
	}

	/**
	 * 准假假單
	 * @param workflowId 工作流編號
	 * @param actionId 動作編號, 2 准許, 3 駁回
	 */
	public void allow(long workflowId, int actionId) {
		Workflow workflow = new BasicWorkflow("manager1");
		DefaultConfiguration config = new DefaultConfiguration();
		workflow.setConfiguration(config);
		try {
			workflow.doAction(workflowId, actionId, null);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		} catch (WorkflowException e) {
			e.printStackTrace();
		}
		catch (InvalidActionException e) {
			e.printStackTrace();
		}
	}	
}