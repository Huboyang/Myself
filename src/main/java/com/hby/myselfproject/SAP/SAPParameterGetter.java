package com.hby.myselfproject.SAP;

import com.alibaba.fastjson.JSONObject;

public interface SAPParameterGetter {
  String getFunctionName();

  JSONObject getInput();

  JSONObject getOutput();

  JSONObject getTables();
}
