package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //@Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") //로딩시점에 오타 오류 나옴
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<Member> findUsernameList();

    @Query( "select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto(); //Dto로 조회할떄는 new로 한다.

    @Query("select m from Member m where m.username in :names") //in으로 컬렉션 타입 검색 가
    List<Member> findByNames(@Param("names") Collection names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    //Page<Member> findByAge(int age, Pageable pageable);
    //Slice<Member> findByAge(int age, Pageable pageable);//3개를 요청하면 4개를 불러와 1개는 더보기 처럼 남겨놓는다


    //CountQuery는 조인이 필요 없는데 따로 나눠놓지 않으면 조인을 하여 성능이 저하된다.
    //따라서 성능의 향상을 위해서 카운터쿼리를 따로 분리하자
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //select가 아니기 때문에 넣자, clear 자동으로 해줌
    @Query("update Member m Set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //fetch join을 하면 연관된 모든 것을 SELECT하여 가져온다. proxy객체가 아닌 진짜 team 객체
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override //위에처럼 쿼리를 하고 Fetch 조인을 해야 하기 떄문에 귀찮아서 이렇게 하면 Fetch조인이 된다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) 
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph(); //이렇게 해도 된다.

    @EntityGraph(attributePaths = ("team")) //findBy를 쓰는데 fetch조인을 쓰고싶으면 이렇게 하면된다
    //@EntityGraph("Member.all")  //@NamedEntityGraph를 쓰려면 이렇게 하면된다
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name= "org.hibernate.readOnly", value="true")) //update체크를 안한다, 성능최적화
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE) //db lock
    List<Member> findLockByUsername(String username);
}
