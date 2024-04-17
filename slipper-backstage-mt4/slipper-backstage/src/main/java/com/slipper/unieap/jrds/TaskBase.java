package com.slipper.unieap.jrds;

import lombok.Data;

@Data
public class TaskBase {
	//任务参数，根据业务需求多少都行
    private String identifier;
 
    public TaskBase(String identifier) {
        this.identifier = identifier;
    }
}
