# -*- encoding:utf-8 -*-

import os
import sys
import pytest
from common.read_data import data
from common.logger import logger

if __name__ == '__main__':
    BASE_PATH = os.path.dirname(os.path.realpath(__file__))
    data_file_path = os.path.join(BASE_PATH, "config", "setting.ini")
    logger.info("data_file_path:{}".format(data_file_path))

    # 集成到jenkins时/控制台执行时，通过传递参数到main函数，
    if len(sys.argv) >= 2:
        # jenkins自动化执行时，控制台传入报告路径与测试域名
        xml_report_path = os.path.join(sys.argv[1])
        case_path = os.path.join(sys.argv[2])

        # config文件初始化
        data.set_ini(data_file_path, 'case_path', 'case_path', case_path)
    else:
        case_path = data.load_ini(data_file_path)["case_path"]["case_path"]
        if "api_test" in case_path:
            xml_report_path = "./testcases/api_test/report"
            html_report_path = "./testcases/api_test/report/html"
        elif "scenario_test" in case_path:
            xml_report_path = "./testcases/scenario_test/report"
            html_report_path = "./testcases/scenario_test/report/html"
        else:
            xml_report_path = "./report"
            html_report_path = "./report/html"

    case_path = data.load_ini(data_file_path)["case_path"]["case_path"]

    # -s 关闭捕捉，不输出打印信息; -v 详细打印; -n 6 6个进程; -case_path 测试地址
    # -x:出现一条测试用例失败就退出测试。在调试阶段非常有用，当测试用例失败时，应该先调试通过，而不是继续执行测试用例。
    # args = ["-n 2", "-s", "-v", f"--alluredir=./temp", "--clean-alluredir", case_path]
    # os.system("allure generate ./temp -o ./report --clean")

    # pytest.main(["-s", "-v", f"--alluredir={xml_report_path}", "--clean-alluredir", case_path])
    # os.system(f"allure generate {xml_report_path} -o {xml_report_path} --clean")

    pytest.main(["-s", "-v", f"--alluredir={xml_report_path}", "--clean-alluredir", case_path])
    # os.system(f"allure serve {xml_report_path}")
    # 本地调试可以生成html报告，再集成的jenkins时可以不用，使用allure插件浏览。
    os.system(f"allure generate {xml_report_path} -o {html_report_path} --clean")

