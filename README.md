
项目采用Python+Requests+Pytest+YAML+Allure。

## 项目说明

项目主要模块分为 HTTP接口封装，请求方法封装、关键字封装、测试用例等模块。

接口封装：利用Python把HTTP接口封装成Python接口，
关键字封装：把Python接口组装成关键字，再把关键字组装成测试用例
用例数据管理：测试用例数据使用YAML格式进行管理，然后通过Pytest来组织执行用例，通过Allure生成测试报告。
jenkins集成：main.py 可以对接口测试进行Jenkins持续集成，通过jenkins 传递参数给到main.py，执行相关案例测试并生成对应的报告。

## 项目使用

### 依赖包安装
下载项目源码， pip 安装 requirements.txt 依赖，：

```
pip3 install -r requirements.txt
```
### 运行main.py
在setting.ini 配置case_path，可以配置api_test,scenario_test路径，然后运行main.py
allure会在report/html目录下生成html报告文件,浏览器可以直接打开查看

## 项目结构

- api ====>> 接口封装层，如封装HTTP接口为Python接口
- common ====>> 读写工具类，请求方法封装等
- config ====>> 配置文件
- testdata ====>> 测试数据文件管理
- pre_request====>> 关键字封装层，如把多个Python接口封装为关键字
- pytest.ini ====>> pytest配置文件
- requirements.txt ====>> 相关依赖包文件
- testcases ====>> 测试用例

## 关键字封装

关键字必须是有意义的，在封装关键字的时候，可以通过调用多个接口来完成。比如测试一个还款接口的时候，还款后还需要调用查询还款计划接口查看剩余还款余额，
判断预期结果和实际结果是否一致：

- 1, 可以把 '还款-查询还款计划' 的操作封装为一个关键字，在这个关键字中依次调用还款和查询还款计划的接口，并可以自定义关键字的返回结果。
- 2, 测试用例编写时，调用关键字测试，拿到关键字返回的结果后，直接对关键字返回结果进行断言。

