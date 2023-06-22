package doctintuc.com.websitedoctintuc.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import doctintuc.com.websitedoctintuc.application.constants.EnumRole;
import doctintuc.com.websitedoctintuc.domain.entity.base.AbstractBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role extends AbstractBase {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private EnumRole roleName;

    @OneToMany(mappedBy = "role" , cascade = CascadeType.MERGE)
    @JsonIgnoreProperties("role")
    private List<User> users;
}
