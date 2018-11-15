package com.hby.myselfproject.SAP;

import com.alibaba.fastjson.JSONObject;

public class SAPParameterBean implements SAPParameterGetter {
  private String functionName;
  private JSONObject input;
  private JSONObject tables;
  private JSONObject output;

  public String getFunctionName() {
    return functionName;
  }

  public void setFunctionName(String functionName) {
    this.functionName = functionName;
  }

  public JSONObject getInput() {
    return input;
  }

  public void setInput(JSONObject input) {
    this.input = input;
  }

  public JSONObject getTables() {
    return tables;
  }

  public void setTables(JSONObject tables) {
    this.tables = tables;
  }

  public JSONObject getOutput() {
    return output;
  }

  public void setOutput(JSONObject output) {
    this.output = output;
  }
}
