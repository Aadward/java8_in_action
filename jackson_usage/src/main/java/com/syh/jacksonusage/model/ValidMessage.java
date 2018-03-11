package com.syh.jacksonusage.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 专门用于测试JSON的合法性
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ValidMessage {

    @NotNull
    long id;

    @NotNull
    String user;

    @Size(max = 10)
    String body;

}
