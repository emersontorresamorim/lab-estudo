package br.com.labestudo.api.model.dto;

import br.com.labestudo.api.model.validation.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(groups = {ValidationGroup.Default.class})
	private String name;

    @Email(groups = {ValidationGroup.Insert.class})
    private String email;

    @NotNull(groups = {ValidationGroup.Insert.class})
    private String pass;
}
