package naver.naver_api.service;

import lombok.extern.slf4j.Slf4j;
import naver.naver_api.domain.Member;
import naver.naver_api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional  //트랜잭션을 명시적으로 관리하고 제어하기위해 선언
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member) {
        if(DuplicateMemberEmail(member)){
            throw new IllegalStateException("이미 가입된 이메일");
        }else{
            return memberRepository.save(member);
        }
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    //로그인, 가입(중복회원 감지) 등등, email이 pk
    public boolean DuplicateMemberEmail(Member member) {
        Long memberCount = memberRepository.findByEmailCount(member.getEmail());
        if (memberCount > 0) {
            return true;
        }else{
            return false;
        }
    }

    public boolean DuplicateMemberNickName(Member member) {
        Long memberCount = memberRepository.findByNickNameCount(member.getNickName());
        if (memberCount > 0) {
            return true;
        }else{
            return false;
        }
    }
}
