from common.result_base import ResultBase
from api.project import project
from common.logger import logger


def get_all_project(page, name):
    """
    获取全部用户信息
    :return: 自定义的关键字返回结果 result
    """
    result = ResultBase()
    res = project.list_all_projects(page=page, name=name)
    result.success = False
    if res.json()["code"] == "999999":
        result.success = True
    else:
        result.error = "接口返回码是 【 {} 】, 返回信息：{} ".format(res.json()["code"], res.json()["msg"])
    result.msg = res.json()["msg"]
    result.response = res
    logger.info("获取项目列表，返回结果 ==>> {}".format(result.response.text))
    return result


def add_project(name, pro_type, version, token, description=""):
    """
    增加新的接口自动化项目
    :param name:  项目名称
    :param pro_type:  项目类型
    :param version:  项目版本号
    :param description:  项目描述
    :param token: token
    return: 自定义的关键字返回结果 result
    """
    result = ResultBase()
    json_data = {
        "name": name,
        "type": pro_type,
        "version": version,
        "description": description
    }
    header = {
        #"Content-Type": "application/x-www-form-urlencoded",
        #"Authorization": "Token b7b680923148883323f920a52bfbc0a379eb8f7b",
        "Authorization": token,
        "Content-Type": "application/json"
    }
    res = project.add(json=json_data, headers=header)
    result.success = False
    if res.json()["code"] == "999999":
        result.success = True
    else:
        result.error = "增加项目接口返回 ===>> code是 【{}】，msg是: {} ".format(res.json()["code"], res.json()["msg"])
    result.msg = res.json()["msg"]
    result.response = res
    logger.info("增加新项目 ===>> 返回结果 ===>> {}".format(result.response.text))
    return result


def del_project(ids):
    """
    增加新的接口自动化项目
    :param ids:  项目id列表
    return: 自定义的关键字返回结果 result
    """
    result = ResultBase()
    json_data = {
        "ids": ids
    }
    logger.info("***********删除项目ids：{}".format(type(json_data["ids"])))
    headers = {
        "Authorization": "Token b7b680923148883323f920a52bfbc0a379eb8f7b",
        "Content-Type": "application/json"
    }
    res = project.delete(json=json_data, headers=headers)
    result.success = False
    if res.json()["code"] == "999999":
        result.success = True
    else:
        result.error = "删除项目接口返回 ===>> code是 【{}】，msg是: {} ".format(res.json()["code"], res.json()["msg"])
    result.msg = res.json()["msg"]
    result.response = res
    logger.info("删除新项目 ===>> 返回结果 ===>> {}".format(result.response.text))
    return result