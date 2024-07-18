package com.movie.rock.member.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 김승준 - 회원
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 회원 고유번호로 확인
    Optional<MemberEntity> findByMemNum(Long memNum);

    // 회원 ID로 확인
    Optional<MemberEntity> findByMemId(String memId);

    // 회원 EMAIL로 확인
    Optional<MemberEntity> findByMemEmail(String memEmail);

    // ID찾기를 위한 EMAIL과 NAME으로 찾기
    Optional<MemberEntity> findByMemEmailAndMemName(String memEmail, String memName);

    // PW찾기를 위한 ID와 EMAIL으로 찾기
    Optional<MemberEntity> findByMemIdAndMemEmail(String memId, String memEmail);

}