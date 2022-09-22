import pytest
import allure
from pre_request.project import get_all_project
from testcases.conftest import api_data
from common.logger import logger


@allure.step("步骤1 ===>> 获取所有项目列表")
def step_1():
    logger.info("步骤1 ===>> 获取所有项目列表")


@allure.severity(allure.severity_level.TRIVIAL)
@allure.epic("针对单个接口的测试")
@allure.feature("获取项目列表模块")
class TestGetProjectList():
    """ 获取项目列表模块"""
    @allure.story("Case--获取全部项目信息")
    @allure.description("该用例是针对获取所有项目列表接口的测试")
    @allure.issue("http://47.94.233.53:3000/", name="点击，跳转到对应BUG的链接地址")
    @allure.testcase("http://47.94.233.53:3000/", name="点击，跳转到对应用例的链接地址")
    @pytest.mark.single
    @pytest.mark.parametrize("page, name, except_result, except_code, except_msg",
                             api_data["test_get_project_list"])
    def test_get_project_list(self, page, name, except_result, except_code, except_msg):
        logger.info("*************** 开始执行用例 ***************")
        step_1()
        result = get_all_project(page, name)
        # print(result.__dict__)
        assert result.response.status_code == 200
        assert result.success == except_result, result.error
        logger.info("code ==>> 期望结果：{}， 实际结果：{}".format(except_code, result.response.json().get("code")))
        assert result.response.json().get("code") == except_code
        assert except_msg in result.msg
        logger.info("*************** 结束执行用例 ***************")


if __name__ == '__main__':
    pytest.main(["-q", "-s", "test_02_get_project_list.py"])
