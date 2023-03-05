package naver.naver_api.repository;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {
    public static final ConcurrentHashMap map = new ConcurrentHashMap();

}
