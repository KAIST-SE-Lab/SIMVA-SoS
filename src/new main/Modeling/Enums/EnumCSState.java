package Enums;

/**
 * Enumeration for CS's state specification (last updated: 18-07-19)
 *
 * IDLE: idle state, and available for doing something
 * DEACTIVATED: deactivated and inoperable state
 * OCCUPIED: occupied by another system or organization; thus, inoperable state
 */
public enum EnumCSState {
    IDLE,
    DEACTIVATED,
    OCCUPIED
}
