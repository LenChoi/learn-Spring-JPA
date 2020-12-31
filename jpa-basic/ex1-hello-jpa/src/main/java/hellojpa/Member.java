package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING) //EnumType.STRING-> db에 enum 이름을 저
    // EnumType.ORDINAL -> db에 enum의 순서입력 따라서 사용x
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private LocalDate testLocalDate; //날짜를 자동으로 넣어준다(하이버네이트 기능) 과거버전이면 위처
    private LocalDateTime testLocalDateTIme;

    @Lob
    private String description;

    @Transient
    private int temp;


    public Member() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
