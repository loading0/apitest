import os
from common.req_client import RestClient
from common.read_data import data

BASE_PATH = os.path.dirname(os.path.dirname(os.path.realpath(__file__)))
data_file_path = os.path.join(BASE_PATH, "config", "setting.ini")
api_root_url = data.load_ini(data_file_path)["host"]["api_root_url"]


class Project(RestClient):

    def __init__(self, api_root_url, **kwargs):
        super(Project, self).__init__(api_root_url, **kwargs)

    def list_all_projects(self, page, name, **kwargs):
        if not name:
            return self.get("/project/project_list?page={}&name=".format(page), **kwargs)
        else:
            return self.get("/project/project_list?page={}&name={}".format(page, name), **kwargs)

    def add(self, **kwargs):
        return self.post("/project/add_project", **kwargs)

    def delete(self, **kwargs):
        return self.post("/project/del_project", **kwargs)


project = Project(api_root_url)