package com.hby.myselfproject.SAP;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctstudio.common.exception.BusinessException;
import com.sap.conn.jco.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * SAP调用辅助类，可使用链式调用，或回调来处理SAP调用
 *
 * @author CT
 */
public class SAPJSONCaller implements SAPParameterGetter {
  private static final Function<String, String> MY_SELF = x -> x;

  private static <K, K2 extends K, V, V2 extends V, M extends Map<K, V>, M2 extends Map<K2, V2>> void putAllIfNotNull(M container, M2 obj) {
    if (container != null && obj != null) {
      container.putAll(obj);
    }
  }

  private static <K, V> void putIfNotNull(Map<K, V> container, K key, V obj) {
    if (container != null && key != null && obj != null) {
      container.put(key, obj);
    }
  }

  private JCoDestination destination;
  private String functionName;
  private JSONObject input;
  private JSONObject tables;

  private JSONObject output;

  private Function<String, String> nameTransfer = MY_SELF;

  private boolean userNameTransfer = true;

  /**
   * 构造函数，须传入SAP连接
   *
   * @param destination SAP连接对象
   */
  public SAPJSONCaller(JCoDestination destination) {
    this.destination = destination;
  }

  public SAPJSONCaller isUserNameTransfer(boolean isUse) {
    this.userNameTransfer = isUse;
    return this;
  }

  /**
   * 添加输入参数
   *
   * @param key 输入参数名
   * @param val 输入参数值
   * @return 本对象
   */
  public SAPJSONCaller addInput(String key, Object val) {
    putIfNotNull(getNotNullInput(), key, val);
    return this;
  }

  /**
   * 批量添加输入参数
   *
   * @param map 输入参数键值对
   * @return 本对象
   */
  public SAPJSONCaller addInputs(Map<? extends String, ?> map) {
    putAllIfNotNull(getNotNullInput(), map);
    return this;
  }

  /**
   * 添加表参数
   *
   * @param tableName 表名
   * @param list      表记录集
   * @return 本对象
   */
  public SAPJSONCaller addTable(String tableName, JSONArray list) {
    putIfNotNull(getNotNullTables(), tableName, list);
    return this;
  }

  /**
   * 添加表参数
   *
   * @param tableName 表名
   * @param list      表记录集
   * @return 本对象
   */
  public <T extends Collection<? extends Map<? extends String, ?>>> SAPJSONCaller addTable(String tableName, T list) {
    putIfNotNull(getNotNullTables(), tableName, list);
    return this;
  }

  /**
   * 添加表参数
   *
   * @param tableName 表名
   * @param record    表记录
   * @return 本对象
   */
  public SAPJSONCaller addTable(String tableName, Map<? extends String, ?> record) {
    if (record != null) {
      JSONObject tables = this.getNotNullTables();
      JSONArray ary = tables.getJSONArray(tableName);
      if (ary == null) {
        ary = new JSONArray();
        tables.put(tableName, ary);
      }
      ary.add(record);
    }
    return this;
  }

  /**
   * 批量添加表参数
   *
   * @param tables 表参数记录集
   * @return 本对象
   */
  public SAPJSONCaller addTables(JSONObject tables) {
    putAllIfNotNull(getNotNullTables(), tables);
    return this;
  }

  /**
   * 批量添加表参数
   *
   * @param tables 表参数记录集
   * @return 本对象
   */
  public SAPJSONCaller addTables(Map<? extends String, List<? extends Map<? extends String, ?>>> tables) {
    putAllIfNotNull(getNotNullTables(), tables);
    return this;
  }

  public SAPJSONCaller setRequestParameter(SAPParameterGetter param) {
    this.setFunction(param.getFunctionName());
    this.addInputs(param.getInput());
    this.addTables(param.getTables());
    return this;
  }

  /**
   * 获取输入参数
   *
   * @return 参数对象
   */
  @Override
  public JSONObject getInput() {
    return input;
  }

  /**
   * 获取输入参数值
   *
   * @param name 参数名
   * @return 参数值
   */
  public String getInput(String name) {
    return input == null ? null : input.getString(name);
  }

  /**
   * 获取输出参数
   *
   * @return 参数对象
   */
  @Override
  public JSONObject getOutput() {
    return output;
  }

  /**
   * 获取输出参数值
   *
   * @param name 参数名
   * @return 参数值
   */
  public String getOutput(String name) {
    return output == null ? null : output.getString(name);
  }

  public JSONObject getOutputJSONObject(String name) {
    return output == null ? null : output.getJSONObject(name);
  }

  public JSONArray getOutputJSONArray(String name) {
    return output == null ? null : output.getJSONArray(name);
  }

  /**
   * 获取表参数
   *
   * @param name 表名
   * @return 表记录集
   */
  public JSONArray getTable(String name) {
    return tables == null ? null : tables.getJSONArray(name);
  }

  /**
   * 获取表的第一条记录
   *
   * @param tableName 表名
   * @return 第一条记录
   */
  public JSONObject getTableFirstRow(String tableName) {
    JSONArray ary = this.getTable(tableName);
    return ary == null || ary.isEmpty() ? null : ary.getJSONObject(0);
  }

  public JSONObject getTableFirstRow() {
    if (this.getTables() != null) {
      for (String tableName : this.getTables().keySet()) {
        return getTableFirstRow(tableName);
      }
    }
    return null;
  }

  public JSONArray getFirstTable() {
    if (this.getTables() != null) {
      for (String tableName : this.getTables().keySet()) {
        return this.getTables().getJSONArray(tableName);
      }
    }
    return null;
  }

  /**
   * 获取表参数全集
   *
   * @return 表参数全集
   */
  @Override
  public JSONObject getTables() {
    return tables;
  }

  public String getErrorMsg() {
    if (output == null) {
      return null;
    }
    JSONObject msg = output.getJSONObject("ES_MESSAGE");
    if (msg == null) {
      return null;
    }
    if ("E".equals(output.getString("MSGTY"))) {
      return output.getString("MSGTX");
    }
    return null;
  }

  /**
   * 设置名称转换回调函数
   *
   * @param transfer 回调函数
   * @return 本对象
   */
  public SAPJSONCaller setNameTransfer(Function<String, String> transfer) {
    if (transfer != null) {
      this.nameTransfer = transfer;
    }
    return this;
  }

  /**
   * 设置所调用的ABAP函数名称
   *
   * @param name 函数名
   * @return 本对象
   */
  public SAPJSONCaller setFunction(String name) {
    this.functionName = name;
    return this;
  }

  /**
   * 执行调用
   *
   * @return 本对象
   */
  public SAPJSONCaller subscribe() throws BusinessException {
    subscribe(null, null, null, null);
    return this;
  }

  public SAPParameterBean toBean() {
    SAPParameterBean bean = new SAPParameterBean();
    bean.setFunctionName(functionName);
    bean.setInput(input);
    bean.setOutput(output);
    bean.setTables(tables);
    return bean;
  }

  /**
   * 执行调用，并通过回调函数接收返回参数及表参数
   *
   * @param output 输出参数处理回调
   * @param tables 表参数处理回调
   */
  public void subscribe(Consumer<JSONObject> output, Consumer<JSONObject> tables) throws BusinessException {
    subscribe(null, null, output, tables);
  }

  /**
   * 执行调用，并通过回调函数接收函数名、入参、返回参数及表参数
   *
   * @param functionName 函数名回调
   * @param input        输入参数回调
   * @param output       输出参数回调
   * @param tables       表参数回调
   */
  public void subscribe(Consumer<String> functionName, Consumer<JSONObject> input, Consumer<JSONObject> output, Consumer<JSONObject> tables)
    throws BusinessException {
    try {
      JCoFunction function = destination.getRepository().getFunction(this.functionName);
      jsonToJCoRecord(this.input, function.getImportParameterList());
      jsonToJCoRecord(this.tables, function.getTableParameterList());
      function.execute(destination);
      this.input = jCoRecordToJSON(function.getImportParameterList());
      this.tables = jCoRecordToJSON(function.getTableParameterList());
      this.output = jCoRecordToJSON(function.getExportParameterList());
      if (functionName != null) {
        functionName.accept(function.getName());
      }
      if (input != null) {
        input.accept(this.input);
      }
      if (output != null) {
        output.accept(this.output);
      }
      if (tables != null) {
        tables.accept(this.tables);
      }
    } catch (JCoException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  private JSONObject getNotNullInput() {
    if (this.input == null) {
      this.input = new JSONObject();
    }
    return this.input;
  }

  private JSONObject getNotNullTables() {
    if (this.tables == null) {
      this.tables = new JSONObject();
    }
    return this.tables;
  }

  /**
   * 获取SAP字段的字符串值，所有类型的特殊处理都写在这里
   *
   * @param field 字段
   * @return 字段值
   */
  private String getValueAsString(JCoField field) {
    switch (field.getType()) {
      case JCoMetaData.TYPE_BCD:
        BigDecimal d = field.getBigDecimal();
        return d.toString();
      default:
        return field.getString();
    }
  }

  /**
   * JCo行转JSONObject
   *
   * @param record 记录
   * @return 记录的数据对象
   */
  private JSONObject jCoRecordToJSON(JCoRecord record) {
    if (record == null) {
      return null;
    }
    JSONObject result = new JSONObject();
    JSON subObj;
    // 遍历字段定义
    for (JCoField field : record) {
      // 如果字段是表参数，则JSON字段按数组遍历处理
      if (field.isTable()) {
        if (null != (subObj = jCoTableToJSON(field.getTable()))) {
          result.put(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName(), subObj);
        }
      } else if (field.isStructure()) {
        // 如果字段是结构，则按子对象处理
        if (null != (subObj = jCoStructureToJSON(field.getStructure()))) {
          result.put(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName(), subObj);
        }
      } else {
        // 如果表参数定义是普通字段，则按直接设值
        result.put(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName(), getValueAsString(field));
      }
    }
    return result;
  }

  private JSONObject jCoStructureToJSON(JCoStructure s) {
    // 递归处理结构的字段
    return jCoRecordToJSON(s);
  }

  private JSONArray jCoTableToJSON(JCoTable t) {
    if (t != null && !t.isEmpty()) {
      JSONArray ary = new JSONArray();
      JSONObject subObj;
      // 递归处理表中的每一行并添加到JSON数组中
      t.firstRow();
      do {
        // 转换为JSONObject并去掉空值
        if (null != (subObj = jCoRecordToJSON(t))) {
          ary.add(subObj);
        }
      } while (t.nextRow());
      return ary;
    }
    return null;
  }

  /**
   * JSONObject转JCo行
   *
   * @param jsonObj 数据对象
   * @param record  记录对象
   */
  private void jsonToJCoRecord(JSONObject jsonObj, JCoRecord record) {
    if (jsonObj == null || record == null) {
      return;
    }
    // 遍历字段定义
    for (JCoField field : record) {
      // 如果字段是表参数，则JSON字段按数组遍历处理
      if (field.isTable()) {
        JSONArray ary = jsonObj.getJSONArray(field.getName());
        if (ary == null) {
          ary = jsonObj.getJSONArray(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName());
        }
        if (ary != null) {
          JCoTable t = field.getTable();
          for (int i = 0; i < ary.size(); i++) {
            JSONObject subObj = ary.getJSONObject(i);
            t.appendRow();
            // 递归处理表中的每一行
            jsonToJCoRecord(subObj, t);
          }
        }
      } else if (field.isStructure()) {
        // 如果字段是结构，则按子对象处理
        JSONObject subObj = jsonObj.getJSONObject(field.getName());
        if (subObj == null) {
          subObj = jsonObj.getJSONObject(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName());
        }
        if (subObj != null) {
          JCoStructure s = field.getStructure();
          // 递归处理结构的字段
          jsonToJCoRecord(subObj, s);
        }
      } else {
        // 如果表参数定义是普通字段，则按直接设值
        Object obj = jsonObj.get(field.getName());
        if (obj == null) {
          obj = jsonObj.get(userNameTransfer ? nameTransfer.apply(field.getName()) : field.getName());
        }
        if (obj != null) {
          if (field.getType() == 4) {
            field.setValue(this.jsonArrytoBytes(obj));
          } else {
            field.setValue(obj.toString());
          }
        }
      }
    }
  }

  private byte[] jsonArrytoBytes(Object obj) {
    if (obj instanceof byte[]) {
      return (byte[]) obj;
    }
    JSONArray array = (JSONArray) obj;
    byte[] byteArr = new byte[array.size()];
    for (int i = 0; i < array.size(); i++) {
      byteArr[i] = array.getByte(i);
    }
    return byteArr;
  }

  @Override
  public String getFunctionName() {
    return this.functionName;
  }
}
