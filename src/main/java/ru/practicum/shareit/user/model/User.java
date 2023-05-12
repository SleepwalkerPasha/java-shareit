package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @NotNull(groups = BasicInfo.class)
    String name;

    @Email(groups = {BasicInfo.class, AdvanceInfo.class})
    @NotNull(groups = BasicInfo.class)
    String email;

}
