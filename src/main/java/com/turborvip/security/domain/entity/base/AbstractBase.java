package doctintuc.com.websitedoctintuc.domain.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractBase implements Serializable {

    private static final long serializableUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @CreationTimestamp
    @Column(name = "create_date",  length = 50, updatable = false)
    protected Timestamp createDate;

    @CreatedBy
    @Column(name = "create_by", nullable = true)
    private String createBy;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    protected Timestamp updateDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
