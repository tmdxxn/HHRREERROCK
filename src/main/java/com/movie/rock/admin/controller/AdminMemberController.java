package com.movie.rock.admin.controller;

import com.movie.rock.member.data.MemberEntity;
import com.movie.rock.member.data.MemberListDTO;
import com.movie.rock.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminMemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<Page<MemberListDTO>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("memNum").ascending());
        Page<MemberEntity> membersPage = memberService.getAllMembersPageable(pageable);
        Page<MemberListDTO> memberDTOs = membersPage.map(MemberListDTO::new);
        return ResponseEntity.ok(memberDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members/search")
    public ResponseEntity<List<MemberListDTO>> searchMembers(@RequestParam String term) {
        List<MemberEntity> members = memberService.getAllMembers().stream()
                .filter(member -> member.getMemId().contains(term) ||
                        member.getMemName().contains(term) ||
                        member.getMemEmail().contains(term))
                .collect(Collectors.toList());
        List<MemberListDTO> memberDTOs = members.stream()
                .map(MemberListDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDTOs);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/members/delete")
//    public ResponseEntity<?> deleteMultipleMembers(@RequestBody List<String> memIds, Authentication authentication) {
//        List<String> adminIds = new ArrayList<>();
//        List<String> nonAdminIds = new ArrayList<>();
//
//        for (String memId : memIds) {
//            if (memberService.isAdminMember(memId)) {
//                adminIds.add(memId);
//            } else {
//                nonAdminIds.add(memId);
//            }
//        }
//
//        if (!adminIds.isEmpty()) {
//            String message = "관리자 권한을 가진 회원 " + String.join(", ", adminIds) + "는 삭제할 수 없습니다.";
//            if (!nonAdminIds.isEmpty()) {
//                message += " 나머지 회원들은 삭제되었습니다.";
//            }
//            try {
//                memberService.deleteMultipleMembers(nonAdminIds);
//                return ResponseEntity.ok(message);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(message + " 그러나 일반 회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
//            }
//        } else {
//            try {
//                memberService.deleteMultipleMembers(memIds);
//                return ResponseEntity.ok("선택한 회원들이 성공적으로 삭제되었습니다.");
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("회원 삭제 중 오류 발생: " + e.getMessage());
//            }
//        }
//    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/members/delete")
    public ResponseEntity<?> deleteMultipleMembers(@RequestBody List<String> memIds, Authentication authentication) {
        List<String> adminIds = new ArrayList<>();
        List<String> nonAdminIds = new ArrayList<>();

        for (String memId : memIds) {
            if (memberService.isAdminMember(memId)) {
                adminIds.add(memId);
            } else {
                nonAdminIds.add(memId);
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("failedToDeleteMembers", adminIds);

        if (!nonAdminIds.isEmpty()) {
            try {
                memberService.deleteMultipleMembers(nonAdminIds);
                result.put("deletedMembers", nonAdminIds);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("일반 회원 삭제 중 오류가 발생했습니다: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(result);
    }
}