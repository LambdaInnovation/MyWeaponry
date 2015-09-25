/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.runtime;

import java.util.HashMap;
import java.util.Map;

import cn.weaponry.api.ctrl.KeyPhase;
import cn.weaponry.core.Weaponry;

/**
 * @author WeAthFolD
 */
public class StateMachine extends Action {
	
	public static final String ID = "StateMachine";
	
	private Map<String, ControlState> states = new HashMap();
	private ControlState init;
	private ControlState current;
	
	public StateMachine() {
		super(ID);
	}
	
	public void addState(String id, ControlState state) {
		if(states.containsKey(id))
			throw new RuntimeException("State with id " + id + " already exists");
		state.machine = this;
		state.info = getInfo();
		states.put(id, state);
	}
	
	public void setInitState(String id) {
		setInitState(checkState(id));
	}
	
	public void setInitState(ControlState state) {
		checkPresent(state);
		init = state;
	}
	
	public void transitState(String id) {
		transitState(checkState(id));
		Weaponry.log.info("Transit to state " + id);
	}
	
	public void transitState(ControlState state) {
		checkPresent(state);
		if(current != null)
			current.onLeave();
		
		current = state;
		if(current != null)
			current.onEnter();
	}
	
	public void delegateControl(int keyID, KeyPhase phase) {
		if(current != null)
			current.onKey(keyID, phase);
	}
	
	private ControlState checkState(String id) {
		ControlState ret = states.get(id);
		if(ret == null)
			throw new RuntimeException("No state with id " + id + " is present.");
		return ret;
	}
	
	private void checkPresent(ControlState s) {
		if(s == null)
			return;
		if(!states.values().contains(s))
			throw new RuntimeException("State " + s + " not in the state machine.");
	}
	
	@Override
	public void onStart() {
		if(init == null)
			throw new RuntimeException("No init state of this state machine is specified.");
		for(ControlState state : states.values()) {
			state.info = getInfo();
			state.machine = this;
		}
		transitState(init);
	}
	
	@Override
	public void onTick() {
		if(current != null)
			current.onTick();
	}
	
	@Override
	public void onEnd() {
		transitState((ControlState) null);
	}
	
	@Override
	public void onAbort() {
		transitState((ControlState) null);
	}
	
}
