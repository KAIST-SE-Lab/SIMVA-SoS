package new_simvasos.model;

import new_simvasos.model.Enums.EnumRoleGrade;
import new_simvasos.model.Enums.EnumRoleType;

import java.util.ArrayList;

public class Organization_Role {
    int roleId;
    String roleName;
    EnumRoleType roleType;
    EnumRoleGrade roleGrade;
    ArrayList<CS> assignedCSs;

    public Organization_Role() {
        roleId = -1;
        roleName = "noName";
        roleType = null;
        roleGrade = null;
        assignedCSs = new ArrayList<CS>();
    }
}
