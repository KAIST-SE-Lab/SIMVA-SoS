package new_simvasos.model.Enums;

/**
 * Enumeration for CS type definition (last updated: 18-07-19)
 * CS type definition depends on the type of an organization (EnumOrgType: directed ~ virtual)
 *
 * ADMIN: administrator of an organization, very high authority
 * MANAGER: manager, high authority
 * MIDDLE_MANAGER: middle manager, medium authority
 * MEDIATOR: mediator between systems, medium-low authority
 * NORMAL: normal system, low authority
 * PASSIVE: passively operating system, very-low authority
 */
public enum EnumCSType {
    ADMIN,
    MANAGER,
    MIDDLE_MANAGER,
    MEDIATOR,
    NORMAL,
    PASSIVE
}
