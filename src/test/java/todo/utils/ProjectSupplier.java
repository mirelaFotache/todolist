package todo.utils;

import todo.repository.models.Project;

public class ProjectSupplier {

    public static Project supplyProjectForInsert() {
        Project project = new Project();
        project.setLabel("project1");
        return project;
    }

}
