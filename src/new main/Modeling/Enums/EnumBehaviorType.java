package Enums;

/**
 * Enumeration for behavior types (last updated: 18-07-19)
 *
 * VAR_VAL_TO_ZERO: Make the value of a variable 0          -- usage: [VAR_VAL_TO_ZERO] [var1] [] means var1 = 0
 * VAR_VAL_UPDATE: Make the value of a variable N           -- usage: [VAR_VAL_UPDATE] [var2] [0.39] means var2 = 0.39
 * VAR_VAL_INCREASE: Increase the value of a variable by 1  -- usage: [VAR_VAL_INCREASE] [var3] [] means var3 += 1
 * VAR_VAL_DECREASE: Decrease the value of a variable by 1  -- usage: [VAR_VAL_DECREASE] [var4] [] means var4 -= 1
 * SEND_MESSAGE: Send a message
 * RECEIVE_MESSAGE: Receive a message
 */
public enum EnumBehaviorType {
    VAR_VAL_TO_ZERO,
    VAR_VAL_UPDATE,
    VAR_VAL_INCREASE,
    VAR_VAL_DECREASE,
    SEND_MESSAGE,
    RECEIVE_MESSAGE
}
