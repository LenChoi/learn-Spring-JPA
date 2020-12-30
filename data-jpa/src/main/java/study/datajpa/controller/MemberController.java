package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) { //도메인 클래스 컨버터는 조회용으로만 사용해야한다.
        return member.getUsername(); //안쓰는게 좋긴하다.
    }

    @GetMapping("/members") //localhost:8080/members?page=0&size=3&sort=id,desc&sort=username,desc
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {//어노테이션으로도 쿼리파람 가능
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto::new);//method reference 방식  member -> new MemberDto(member)) 같음
    }

    @PostConstruct
    public void init() {
        for (int i =0; i < 100; i++) {
            memberRepository.save(new Member("user"+ i, i));
        }
    }
}
