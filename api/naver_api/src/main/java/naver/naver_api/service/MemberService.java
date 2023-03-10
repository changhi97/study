package naver.naver_api.service;

import naver.naver_api.domain.Member;
import naver.naver_api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long save(Member member){
         return memberRepository.save(member);
    }

    //로그인, 가입(중복회원 감지) 등등
}
